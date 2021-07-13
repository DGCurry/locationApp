package com.example.locationwake.Backend.Database.Attributes;

public class mDistance implements AttributeInterface{

    private String distance;

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
        if (distance.equals("")) {
            return false;
        }

        if (distance.trim().length() == 0) {
            return false;
        }

        try {
            Float.parseFloat(distance);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }


}
