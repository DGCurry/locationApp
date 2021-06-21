package com.example.locationwake.backend.behaviour.Components;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.example.locationwake.backend.Database.Attributes.mDistance;
import com.example.locationwake.backend.Database.Attributes.mLocation;
import com.example.locationwake.backend.behaviour.Component;

public class LocationComponent extends Component {

    /**
     * Tag of the class
     */
    static final private String TAG = "LocationComponent";

    private int radius;
    private Context context;
    float savedLatitude, savedLongitude;

    /**
     * constructor
     * @param context
     * @param mLocation
     * @param mDistance
     */
    public LocationComponent(Context context, mLocation mLocation, mDistance mDistance) {
        this.context = context;
        this.radius = Integer.parseInt(mDistance.getDistance());
        this.savedLatitude = Integer.parseInt(mLocation.getLat());
        this.savedLongitude = Integer.parseInt(mLocation.getLng());
    }

    @Override
    public boolean isActive() {
        Location currentLocation = new Location("");
        currentLocation.setLongitude(getLongitude());
        currentLocation.setLatitude(getLatitude());

        Location savedLocation = new Location("");
        savedLocation.setLongitude(savedLongitude);
        savedLocation.setLatitude(savedLatitude);

        float distance = currentLocation.distanceTo(savedLocation);
        return distance <= radius;
    }

    /**
     * Method to get the location's latitude that has been retrieved by LocationWorker
     * @return
     */
    private float getLatitude() {
        SharedPreferences prfs = context.getSharedPreferences("LOCATION_FILE_NAME", Context.MODE_PRIVATE);
        return prfs.getFloat("location_lat", 0);
    }

    /**
     * Method to get the location's longitude that has been retrieved by LocationWorker
     * @return
     */
    private float getLongitude() {
        SharedPreferences prfs = context.getSharedPreferences("LOCATION_FILE_NAME", Context.MODE_PRIVATE);
        return prfs.getFloat("location_lon", 0);
    }

}
