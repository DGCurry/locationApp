package com.example.locationwake.Backend.Database.Attributes;

import com.example.locationwake.Backend.Database.Attributes.AttributeInterface;
import com.example.locationwake.Logger;

/**
 * Holder of the data. Create an instance of this class to save the data to the database
 */
public class mLocation implements AttributeInterface {

    /**
     * Tag of the class
     */
    static final private String TAG = "mLocation";

    // Data needed for the database
    private String lat, lng;
    private String name;
    private String LID;

    /**
     * Constructor to create a data holder from the beginning
     * @param lat latitude the user has given
     * @param lng longitude the user has given
     * @param name name the user has given
     */
    public mLocation(String LID, String name, String lat, String lng) {
        this.LID = LID;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    /**
     * Add the latitude information as String.
     * @param lat latitude the user has given
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * Add the longitude information as String.
     * @param lng longitude the user has given
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     * Add the ID that corresponds to the database entry
     * @param LID ID the database has generated for an entry
     */
    public void setLID(String LID) {
        this.LID = LID;
    }

    /**
     * Return the latitude information as String
     * @return lat latitude the user has added
     */
    public String getLat() {
        return lat;
    }

    /**
     * Return the longitude information as String
     * @return lng longitude the user has added
     */
    public String getLng() {
        return lng;
    }

    /**
     * Return the ID key that corresponds to the location
     * @return ID key
     */
    public String getLID() {
        return LID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return AttributeInterface.LOCATION_TYPE;
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

}
