package com.example.locationwake.Backend.Database;

import com.example.locationwake.Backend.Database.Attributes.mDistance;
import com.example.locationwake.Backend.Database.Attributes.mSetting;

/**
 * Holder of the data. Create an instance of this class to save the data to the database
 */
public class mAttribute {

    // Data needed for the database
    private Integer KID;
    private Integer AID;

    private com.example.locationwake.Backend.Database.Attributes.mDistance mDistance;
    private com.example.locationwake.Backend.Database.Attributes.mSetting mSetting;

    /**
     * Constructor to create a data holder from the beginning
     * @param KID key id to link to LocationSettingDbHelper entry
     * @param AID key id to link to LocationSettingDbHelper entry
     * All objects here are the least an attribute should have. Any other must be set by getters and setters.
     */
    public mAttribute(Integer KID, Integer AID, mDistance mDistance, mSetting mSetting) {
        this.KID = KID;
        this.AID = AID;
        this.mDistance = mDistance;
        this.mSetting = mSetting;
    }

    /**
     * Add the ID information as Integer.
     * @param KID key id to link to LocationSettingDbHelper entry
     */
    public void setKID(Integer KID) {
        this.KID = KID;
    }


    /**
     * Add the ID information as Integer.
     * @param A_ID key id to link to LocationSettingDbHelper entry
     */
    public void setAID(Integer A_ID) {
        this.AID = A_ID;
    }

    /**
     * Return the KEY ID information as Integer
     * @return KEY ID key id to link to LocationSettingDbHelper entry
     */
    public Integer getKID() {
        return KID;
    }

    /**
     * Return the ATTRIBUTE ID information as Integer
     * @return ATTRIBUTE ID id to link to LocationSettingDbHelper entry
     */
    public Integer getAID() {
        return AID;
    }

    public com.example.locationwake.Backend.Database.Attributes.mDistance getDistance() {
        return mDistance;
    }

    public void setDistance(com.example.locationwake.Backend.Database.Attributes.mDistance mDistance) {
        this.mDistance = mDistance;
    }

    public com.example.locationwake.Backend.Database.Attributes.mSetting getSetting() {
        return mSetting;
    }

    public void setSetting(com.example.locationwake.Backend.Database.Attributes.mSetting mSetting) {
        this.mSetting = mSetting;
    }
}
