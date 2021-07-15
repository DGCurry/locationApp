package com.example.locationwake.Backend.Database.Attributes;

import android.content.Context;
import android.location.Location;

import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;

import java.util.ArrayList;

public class mDistance implements AttributeInterface{

    private String distance;

    //used to determine isValid
    private mLocation location;

    public mDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public int getType() {
        return AttributeInterface.DISTANCE_TYPE;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    public boolean isValid(Context context, String latitude, String longitude) {
        if (distance.equals("")) {
            return false;
        }

        if (distance.trim().length() == 0) {
            return false;
        }

        try {
            Integer.parseInt(distance);
        } catch(NumberFormatException e) {
            return false;
        }


        // check other locations in database and see if it is close to another location
        ArrayList<mLocation> locations = DataHandler.loadLocations(context);

        for (mLocation location : locations) {
            ArrayList<mAttribute> attributes = DataHandler.loadAttributes(location.getKID(), context);
            for (mAttribute attribute : attributes) {
                // get distance from new location to old location
                // check if this distance is smaller than one of the radius'

                Location newLocation = new Location("");
                newLocation.setLongitude(Double.parseDouble(longitude));
                newLocation.setLatitude(Double.parseDouble(latitude));

                Location savedLocation = new Location("");
                savedLocation.setLongitude(Float.parseFloat(location.getLng()));
                savedLocation.setLatitude(Float.parseFloat(location.getLat()));

                float newDistance = newLocation.distanceTo(savedLocation);

                if (newDistance <= Float.parseFloat(this.distance)
                        || newDistance <= Integer.parseInt(attribute.getDistance().getDistance())) {
                    return false;
                }
            }
        }
        return true;
    }



}
