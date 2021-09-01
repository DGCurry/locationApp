package com.example.locationwake.Backend.Database;

import androidx.annotation.NonNull;

import com.example.locationwake.Backend.Database.Attributes.AttributeInterface;
import com.example.locationwake.Backend.Database.Attributes.mLatLng;
import com.example.locationwake.Logger;

/**
 * Holder of the data. Create an instance of this class to save the data to the database
 */
public class mLocation {

    /**
     * Tag of the class
     */
    static final private String TAG = "mLocation";

    // Data needed for the database
    private String name;
    private String LID;
    private mLatLng latLng;

    /**
     * Constructor to create a data holder from the beginning
     * @param name name the user has given
     * @param latLng latitude the user has given
     */
    public mLocation(String LID, String name, @NonNull mLatLng latLng) {
        this.LID = LID;
        this.name = name;
        this.latLng = latLng;
    }

    /**
     * Add the latitude information as String.
     * @param latLng latitude, longitude the user has given
     */
    public void setLatLng(@NonNull mLatLng latLng) {
        this.latLng = latLng;
    }

    public mLatLng getLatLng() {
        return latLng;
    }

    /**
     * Add the ID that corresponds to the database entry
     * @param LID ID the database has generated for an entry
     */
    public void setLID(String LID) {
        this.LID = LID;
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

}
