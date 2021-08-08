package com.example.locationwake.Activities.ActivityExtension;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
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
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class BasicMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    /**
     * Tag of the class
     */
    static protected String TAG = "BasicMapActivity";

    protected LatLng currentLocation;

    /**
     * Location variables
     */
    protected LocationRequest mLocationRequest;
    protected FusedLocationProviderClient mFusedLocationClient;
    protected LocationCallback mLocationCallback;
    protected GoogleMap map;
    protected boolean isZoomed = false;

    protected Marker currentPositionMarker;
    protected Map<Circle, mAttribute> circleToAttributeMapping = new HashMap<>();

    protected TextView latitude, longitude;


    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    protected static final long UPDATE_INTERVAL_IN_MILLISECONDS = 60*1000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    protected static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS;


    @Override
    protected void onStart() {
        super.onStart();

        latitude = findViewById(R.id.textView_maps_latitude);
        longitude = findViewById(R.id.textView_maps_longitude);

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
        addSavedLocationsMarkers();
        //Here we are going to get the location with a long click
    }

    protected void addSavedLocationsMarkers() {
        ArrayList<mLocation> locations = DataHandler.loadLocations(getApplicationContext());
        for (mLocation location:locations) {
            LatLng locationCenter = new LatLng(Double.parseDouble(location.getLat()), Double.parseDouble(location.getLng()));

            map.addMarker(new MarkerOptions().position(locationCenter).title(location.getName()));
            ArrayList<mAttribute> attributes = DataHandler.loadAttributes(location.getLID(), getApplicationContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                attributes.sort(new Comparator<mAttribute>() {
                    @Override
                    public int compare(mAttribute o1, mAttribute o2) {
                        int f1 = Integer.parseInt(o1.getRadius().getRadius());
                        int f2 = Integer.parseInt(o2.getRadius().getRadius());
                        if (f1 == f2) return 0;
                        return f1 < f2 ? 1 : -1;
                    }
                });
            } else {
                Collections.sort(attributes, new Comparator<mAttribute>() {
                    @Override
                    public int compare(mAttribute o1, mAttribute o2) {
                        int f1 = Integer.parseInt(o1.getRadius().getRadius());
                        int f2 = Integer.parseInt(o2.getRadius().getRadius());
                        if (f1 == f2) return 0;
                        return f1 < f2 ? 1 : -1;
                    }
                });
            }

            int zIndex = 0;

            for (mAttribute attribute:attributes) {
                CircleOptions locationRadius = new CircleOptions();
                locationRadius.center(locationCenter);
                locationRadius.radius(Double.parseDouble(attribute.getRadius().getRadius()));
                Circle circle = map.addCircle(locationRadius);

                circleToAttributeMapping.put(circle, attribute);

                circle.setClickable(true);
                circle.setZIndex(zIndex);
                //alpha -> 255 is less transparency
                circle.setFillColor(Color.argb(30, 200, 50, 50));
                circle.setStrokeWidth(1);
                zIndex++;
            }
            map.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                @Override
                public void onCircleClick(@NonNull Circle circle) {
                    Logger.logD(TAG, "addSavedLocationsMarkers(): onCircleClick(): clicked mf on circle " + circle.getId());

                }
            });
        }
    }

    protected void createLocationCallBack() {
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
    protected void requestLocationUpdates() {
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
    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * function that is called on callback for location.
     * @param location
     */
    protected void  onNewLocation(Location location) {
        Logger.logD(TAG, "onNewLocation(): current location is " + location.getLatitude() + " " + location.getLongitude());
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (currentPositionMarker != null) {
            currentPositionMarker.remove();
        }

        final MarkerOptions positionMarker = new MarkerOptions().position(
                currentLocation).title("Current Location");
        currentPositionMarker = map.addMarker(positionMarker);
        if (!isZoomed) {
            map.animateCamera(CameraUpdateFactory.newLatLng(
                  currentLocation));
            isZoomed = true;

        }
        Logger.logE(TAG, "onNewLocation(Location location): New location: " + location +
                " at " + location.getLatitude() + " " + location.getLongitude());
    }

}
