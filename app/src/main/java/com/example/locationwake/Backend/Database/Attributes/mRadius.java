package com.example.locationwake.Backend.Database.Attributes;

import android.content.Context;
import android.location.Location;

import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;

import java.util.ArrayList;

public class mRadius implements AttributeInterface{

    private String radius;

    /**
     * Constructor, sets the radius
     * @param radius String which holds the radius in meter
     */
    public mRadius(String radius) {
        this.radius = radius;
    }

    /**
     *
     * @return
     */
    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    @Override
    public int getType() {
        return AttributeInterface.DISTANCE_TYPE;
    }

    /**
     * Method to check whether the current object is valid
     * @return True if the criteria for the attribute has been met, else False
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * Method to check whether the current object is valid
     * @return True if the criteria for the attribute has been met, else False
     */
    public boolean isValid(Context context, String latitude, String longitude) {
        if (radius.equals("")) {
            return false;
        }

        if (radius.trim().length() == 0) {
            return false;
        }

        try {
            Integer.parseInt(radius);
        } catch(NumberFormatException e) {
            return false;
        }


        // check other locations in database and see if it is close to another location
        ArrayList<mLocation> locations = DataHandler.loadLocations(context);

        for (mLocation location : locations) {
            ArrayList<mAttribute> attributes = DataHandler.loadAttributes(location.getLID(), context);
            for (mAttribute attribute : attributes) {
                // get distance from new location to old location
                // check if this distance is smaller than one of the radius'

                Location newLocation = new Location("");
                newLocation.setLongitude(Double.parseDouble(longitude));
                newLocation.setLatitude(Double.parseDouble(latitude));

                Location savedLocation = new Location("");
                savedLocation.setLongitude(Double.parseDouble(location.getLng()));
                savedLocation.setLatitude(Double.parseDouble(location.getLat()));

                float newDistance = newLocation.distanceTo(savedLocation);

                if (newDistance <= Float.parseFloat(this.radius)
                        || newDistance <= Integer.parseInt(attribute.getRadius().getRadius())) {
                    return false;
                }
            }
        }
        return true;
    }



}
