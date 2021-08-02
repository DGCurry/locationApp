package com.example.locationwake.Backend.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.example.locationwake.Backend.Behaviour.Component;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.Attributes.mRadius;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Managers.CallBackManager;
import com.example.locationwake.Logger;

import java.util.ArrayList;

public class CheckLocationEntry implements Runnable {

    /**
     * Tag of the class
     */
    static final private String TAG = "CheckLocationEntry";

    private final int radius;
    private final Context context;
    Double savedLatitude, savedLongitude;

    /**
     * constructor
     * @param context
     * @param radius
     */
    public CheckLocationEntry(Context context, String latitude, String longitude, mRadius radius) {
        this.context = context;
        this.radius = Integer.parseInt(radius.getRadius());
        this.savedLatitude = Double.parseDouble(latitude);
        this.savedLongitude = Double.parseDouble(longitude);
    }

    @Override
    public void run() {
        Logger.logD(TAG, "run(): started to check location ");
        ArrayList<mLocation> locations = DataHandler.loadLocations(context);

        Location currentLocation = new Location("");
        currentLocation.setLongitude(savedLongitude);
        currentLocation.setLatitude(savedLatitude);

        //for each location
        for (mLocation savedLocation:locations) {
            Location loadedLocation = new Location("");
            loadedLocation.setLongitude(Double.parseDouble(savedLocation.getLng()));
            loadedLocation.setLatitude(Double.parseDouble(savedLocation.getLat()));

            Logger.logD(TAG, "run(): Calculating distance for \n" +
                    "\t LID: " + savedLocation.getLID() + "\n" +
                    "\t latitude: " + savedLocation.getLat() + "\n" +
                    "\t longitude: " + savedLocation.getLng() + "\n" +
                    "\n And new location \n" +
                    "\t latitude: " + savedLatitude + "\n" +
                    "\t longitude: " + savedLongitude);

            float distance = currentLocation.distanceTo(loadedLocation);
            Logger.logD(TAG, "run(): Calculated distance for is " + distance);
            if (distance <= radius) {
                Logger.logD(TAG, "run(): Too close to another location");
                CallBackManager.callBackActivities(false, false, true, 'L', savedLocation.getLID());
                return;
            }
        }
        Logger.logD(TAG, "run(): Location is valid");
        CallBackManager.callBackActivities(false, true, false, 'L', "Everything is okay");
    }
}
