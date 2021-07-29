package com.example.locationwake.Backend.Database;

import com.example.locationwake.Backend.Database.Attributes.mRadius;
import com.example.locationwake.Backend.Database.Attributes.mSetting;

/**
 * Holder of the data. Create an instance of this class to save the data to the database
 */
public class mAttribute {

    /**
     * Tag of the class
     */
    static final private String TAG = "mAttribute";

    // Data needed for the database
    private String LID;
    private String AID;

    private mRadius radius;
    private mSetting mSetting;

    private String name;

    /**
     * Constructor to create a data holder from the beginning
     * @param LID key id to link to LocationSettingDbHelper entry
     * @param AID key id to link to LocationSettingDbHelper entry
     * All objects here are the least an attribute should have. Any other must be set by getters and setters.
     */
    public mAttribute(String LID, String AID, String name, mRadius radius, mSetting mSetting) {
        this.LID = LID;
        this.AID = AID;
        this.name = name;
        this.radius = radius;
        this.mSetting = mSetting;
    }

    /**
     * Add the ID information as Integer.
     * @param LID key id to link to LocationSettingDbHelper entry
     */
    public void setLID(String LID) {
        this.LID = LID;
    }


    /**
     * Add the ID information as Integer.
     * @param A_ID key id to link to LocationSettingDbHelper entry
     */
    public void setAID(String A_ID) {
        this.AID = A_ID;
    }

    /**
     * Return the KEY ID information as Integer
     * @return KEY ID key id to link to LocationSettingDbHelper entry
     */
    public String getLID() {
        return LID;
    }

    /**
     * Return the ATTRIBUTE ID information as Integer
     * @return ATTRIBUTE ID id to link to LocationSettingDbHelper entry
     */
    public String getAID() {
        return AID;
    }

    /**
     * method to retrieve the attribute mDistance
     * @return mDistance
     */
    public mRadius getRadius() {
        return radius;
    }

    /**
     * sets the attribute mDistance
     * @param radius
     */
    public void setRadius(mRadius radius) {
        this.radius = radius;
    }

    /**
     * method to retrieve the attribute mSetting
     * @return mSetting
     */
    public com.example.locationwake.Backend.Database.Attributes.mSetting getSetting() {
        return mSetting;
    }

    /**
     * sets the attribute mSetting
     * @param mSetting
     */
    public void setSetting(com.example.locationwake.Backend.Database.Attributes.mSetting mSetting) {
        this.mSetting = mSetting;
    }

    /**
     * method to retrieve the name of the attribute
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * method to set the name of the attribute
     * @param name of the attribute
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Special method to be called if the set items (name, distance, setting etc) needs to be retrieved
     * @return if an item is not null, +1
     */
    public int getItemCount() {
        //TODO: if there is an attribute that is optional(not applicable now) actually count
        return 3;
    }
}
