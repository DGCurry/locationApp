package com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddLocationActivities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.work.ListenableWorker;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
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
import com.google.android.gms.maps.model.MarkerOptions;

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
    private int locationRequestCode = 1000;
    GoogleMap map;
    float zoom = 13f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location_map);

        getCurrentLocation();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        getCurrentLocation();
//        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        // add markers and circles around saved locations
    }

    private void getCurrentLocation() {
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
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            Logger.logD(TAG, "getCurrentLocation(): creating callback");
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            if (map != null) {
                                Logger.logD(TAG, "getCurrentLocation(): go a location, zooming now");
                                map.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(location.getLatitude(), location.getLongitude()),
                                        zoom));
                            } else {
                                Logger.logE(TAG, "getCurrentLocation(): no map");
                            }
                        } else {
                            Logger.logE(TAG, "getCurrentLocation(): retrieved location is null");
                        }
                    }
                }
            };
        }
    }



    @Override
    public void onCallBack(boolean update, boolean succeeded, boolean failed, char type, String message) {

    }
}
