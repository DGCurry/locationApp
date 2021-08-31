package com.example.locationwake.Backend.Database.Attributes;

import com.example.locationwake.Logger;

public class mLatLng implements AttributeInterface {

    static final private String TAG = "mLatLng";

    private String lng;
    private String lat;

    public mLatLng(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public int getType() {
        return LOCATION_TYPE;
    }

    /**
     * Method to check whether the current object is valid
     * @return True if the criteria for the attribute has been met, else False
     */
    @Override
    public boolean isValid() {
        Logger.logD(TAG,  "isValid(): checking validity of " + lat + " " + lng);
        if (lat.trim().equals("") || lng.trim().equals("")) {
            Logger.logD(TAG, "isValid(): INVALID LOCATION, no input");
            return false;
        }

        if (lat.trim().length() == 0 || lng.trim().length() == 0) {
            Logger.logD(TAG, "isValid(): INVALID LOCATION, no input");
            return false;
        }

        if (lat == null || lng == null) {
            Logger.logD(TAG, "isValid(): INVALID LOCATION, latitude or longitude is null");
            return false;
        }

        try {
            Double.parseDouble(this.getLat());
            Double.parseDouble(this.getLng());
        } catch (Exception e) {
            Logger.logD(TAG, "isValid(): INVALID LOCATION, no double");
            e.printStackTrace();
            return false;
        }

        if (Double.parseDouble(lat) < -90 || Double.parseDouble(lat) > 90) {
            Logger.logD(TAG, "isValid(): INVALID LOCATION, latitude < -90 || lat > 90");
            return false;
        } else if (Double.parseDouble(lng) < -180 && Double.parseDouble(lng) > 180) {
            Logger.logD(TAG, "isValid(): INVALID LOCATION, longitude < -180 || longitude > 180");
            return false;
        }
        Logger.logD(TAG, "isValid(): VALID LOCATION");
        return true;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }


    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
