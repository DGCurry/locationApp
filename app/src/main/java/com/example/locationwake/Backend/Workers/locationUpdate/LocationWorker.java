package com.example.locationwake.Backend.Workers.locationUpdate;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.locationwake.Logger;
import com.example.locationwake.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.CountDownLatch;


public class LocationWorker extends Worker {

    /**
     * Tag of the class
     */
    static final private String TAG = "LocationWorker";

    /**
     * Notification ID and Title for the notification needed for a Foreground Worker
     */
    int NOTIFICATION_ID = 1;
    String CHANNEL_ID = "notification_GPS";
    String NOTIFICATION_TITLE = "GPS worker";
    Context mContext;

    /**
     * Location variables
     */
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10*1000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Constructor of the worker
     * @param context
     * @param workerParams
     */
    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    /**
     * method that is executed when thread is activated
     * @return
     */
    @WorkerThread
    @NonNull
    public Result doWork() {
        Logger.logD(TAG, "doWork(): started");
        //TODO: check whether location is enabled, if not, send notification to turn it on
        //TODO: if no permission, send notification to let the user enable permission(go directly to settings)

        boolean gps_enabled = false;

        try {
            LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            Logger.logE(TAG, "doWork(): " + e);
        }

        if (!gps_enabled) {
            com.example.locationwake.Backend.Managers.NotificationManager notificationManager =
                    new com.example.locationwake.Backend.Managers.NotificationManager(NOTIFICATION_ID
                            , NOTIFICATION_TITLE, "Cannot retrieve location, please enable GPS",
                            getApplicationContext(), 3, CHANNEL_ID);
            setForegroundAsync(notificationManager.createForegroundInfo());
            //TODO: notify the user that location should be enabled
            Logger.logD(TAG, "doWork(): finished");
            return Result.failure();
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&

                ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            Logger.logD(TAG, "doWork(): permission for fine location and background location");

            com.example.locationwake.Backend.Managers.NotificationManager notificationManager =
                    new com.example.locationwake.Backend.Managers.NotificationManager(NOTIFICATION_ID
                            , NOTIFICATION_TITLE, "Retrieving location", getApplicationContext(),
                            3, CHANNEL_ID);
            setForegroundAsync(notificationManager.createForegroundInfo());

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

            CountDownLatch countDownLatch = new CountDownLatch(1);

            mLocationCallback= new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    onNewLocation(locationResult.getLastLocation());
                    Logger.logE(TAG, "doWork(): finished the work, location has been updated");
                    countDownLatch.countDown();
                }
            };

            createLocationRequest();
            requestLocationUpdates();

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Logger.logD(TAG, "doWork(): finished");
            return Result.success();
        } else {
            Logger.logD(TAG, "doWork(): no permission for fine location and background location");
            com.example.locationwake.Backend.Managers.NotificationManager notificationManager =
                    new com.example.locationwake.Backend.Managers.NotificationManager(NOTIFICATION_ID
                            , NOTIFICATION_TITLE, "Permission not granted to retrieve location", getApplicationContext(),
                            3, CHANNEL_ID);
            setForegroundAsync(notificationManager.createForegroundInfo());

        }

        Logger.logD(TAG, "doWork(): finished");
        return Result.failure();
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    private void requestLocationUpdates() {
        Logger.logV(TAG, "requestLocationUpdates(): Requesting location updates");
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.getMainLooper());
        } catch (SecurityException unlikely) {
            Logger.logE(TAG, "requestLocationUpdates(): Lost location permission. " +
                    "Could not request updates. " + unlikely);
        }
    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * function that is called on callback for location.
     * @param location
     */
    private void onNewLocation(Location location) {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        saveLocation(location);
        Logger.logE(TAG, "onNewLocation(Location location): New location: " + location +
                " at " + location.getLatitude() + " " + location.getLongitude());
    }

    /**
     * saves the location in shared preferences
     * @param location
     */
    private void saveLocation(Location location) {
        SharedPreferences preferences = mContext.getSharedPreferences("LOCATION_FILE_NAME", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("location_lat", (float) location.getLatitude());
        editor.putFloat("location_lon", (float) location.getLongitude());
        editor.apply();
    }

}
