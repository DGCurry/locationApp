package com.example.locationwake.Backend.Behaviour.Components;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.example.locationwake.Backend.Database.Attributes.mRadius;
import com.example.locationwake.Backend.Database.mLocation;
import com.example.locationwake.Backend.Behaviour.Component;

public class LocationComponent extends Component {

    /**
     * Tag of the class
     */
    static final private String TAG = "LocationComponent";

    private int radius;
    private Context context;
    double savedLatitude, savedLongitude;

    /**
     * constructor
     * @param context
     * @param mLocation
     * @param radius
     */
    public LocationComponent(Context context, mLocation mLocation, mRadius radius) {
        this.context = context;
        this.radius = Integer.parseInt(radius.getRadius());
        this.savedLatitude = Double.parseDouble(mLocation.getLatLng().getLat());
        this.savedLongitude = Double.parseDouble(mLocation.getLatLng().getLng());
    }

    /**
     * Whether the object the component is added to should be activated or not
     * @return True iff the criteria for the component has been met, else False
     */
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
    private double getLatitude() {
        SharedPreferences prfs = context.getSharedPreferences("LOCATION_FILE_NAME", Context.MODE_PRIVATE);
        return Double.parseDouble(prfs.getString("location_lat", "0.0"));
    }

    /**
     * Method to get the location's longitude that has been retrieved by LocationWorker
     * @return
     */
    private double getLongitude() {
        SharedPreferences prfs = context.getSharedPreferences("LOCATION_FILE_NAME", Context.MODE_PRIVATE);
        return Double.parseDouble(prfs.getString("location_lon", "0.0"));
    }

}
