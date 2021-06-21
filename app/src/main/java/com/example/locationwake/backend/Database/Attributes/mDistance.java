package com.example.locationwake.backend.Database.Attributes;

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
}
