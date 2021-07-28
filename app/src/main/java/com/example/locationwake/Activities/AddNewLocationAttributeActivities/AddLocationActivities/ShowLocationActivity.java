package com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddLocationActivities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Logger;
import com.example.locationwake.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ShowLocationActivity extends CallBackActivity implements OnMapReadyCallback {

    /**
     * Tag of the class
     */
    static final private String TAG = "ShowLocationActivity";

    private Location currentLocation;

    /**
     * Location variables
     */
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private final int locationRequestCode = 1000;
    GoogleMap map;
    float zoom = 13f;

    private Marker currentPositionMarker;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location_map);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createLocationCallBack();

        createLocationRequest();
        requestLocationUpdates();

    }

    @Override
    public void finish() {
        super.finish();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.map = map;

    }

    private void addSavedLocationsMarkers() {
        ArrayList<mLocation> locations = DataHandler.loadLocations(getApplicationContext());

    }

    private void createLocationCallBack() {
        Logger.logD(TAG, "getCurrentLocation(): retrieving location");
        boolean gps_enabled = false;
        try {
            LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            Logger.logE(TAG, "getCurrentLocation(): " + e);
        }

        if (!gps_enabled) {
            Logger.logE(TAG, "getCurrentLocation(): gps is not enabled");
            //mhhh
            return;
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Logger.logD(TAG, "getCurrentLocation(): creating callback");

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    onNewLocation(locationResult.getLastLocation());
                }
            };
        }
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
    private void  onNewLocation(Location location) {
        currentLocation = location;
        if (currentPositionMarker != null) {
            currentPositionMarker.remove();
        }

        final MarkerOptions positionMarker = new MarkerOptions().position(
                new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("Current Location");
        currentPositionMarker = map.addMarker(positionMarker);

        map.animateCamera(CameraUpdateFactory.newLatLng(
                new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));

        Logger.logE(TAG, "onNewLocation(Location location): New location: " + location +
                " at " + location.getLatitude() + " " + location.getLongitude());
    }





    @Override
    public void onCallBack(boolean update, boolean succeeded, boolean failed, char type, String message) {

    }
}
