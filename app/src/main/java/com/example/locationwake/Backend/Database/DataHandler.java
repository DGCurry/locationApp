package com.example.locationwake.Backend.Database;

import android.content.Context;

import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Logger;
import com.example.locationwake.Backend.Database.Attributes.mSetting;

import java.util.ArrayList;

/**
 * Class to handle all the data going into the database and getting from the database
 */
public class DataHandler {

    //TODO: refactor the shit out of this, together with the database

    //TAG of the class
    static final private String TAG = "DataHandler";

    /**
     * method to add data to the databases
     * @param context context of the application
     * @return ID of the entry in the LocationSettingDbHelper
     */
    public static String addData(mAttribute attribute, mLocation location, Context context) {

        //Adding data to database
        LocationSettingDbHelper locationSettingDbHelper = new LocationSettingDbHelper(context);
        attribute.setLID(locationSettingDbHelper.addLocation(location));

        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        return attributesDbHelper.addAttribute(attribute);
    }

    /**
     * Adding an attribute to the database linked to an entry in the location database, with key KID
     * @param context
     */
    public static void addAttribute(mAttribute attribute, Context context) {
        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        attributesDbHelper.addAttribute(attribute);
    }


    /**
     * Adding an location to the database
     * @param context
     */
    public static void addLocation(mLocation location, Context context) {
        LocationSettingDbHelper locationSettingDbHelper = new LocationSettingDbHelper(context);
        locationSettingDbHelper.addLocation(location);
    }


    /**
     * Deleting an attribute from the database with KID and AID as key
     * @param LID key of the location in the location database
     * @param AID key of the attribute
     * @param context
     */
    public static void deleteAttribute(String LID, String AID, Context context) {
        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        attributesDbHelper.deleteAttribute(LID, AID);
    }

    /**
     * loads the locations from the LocationSettingDbHelper
     * @param context context of the application
     * @return ArrayList of all locations put in a mLocation instance
     */
    public static ArrayList<mLocation> loadLocations(Context context) {
        //get the database
        LocationSettingDbHelper locationSettingDbHelper = new LocationSettingDbHelper(context);
        //set the data we have in the correct lists
        return locationSettingDbHelper.getLocations();
    }

    /**
     * loads the attributes that correspond with the ID from the AttributesDbHelper
     * @param context context of the application
     * @return ArrayList of all attributes that correspond with the location ID
     */
    public static ArrayList<mAttribute> loadAttributes(String LID, Context context) {
        Logger.logV(TAG, "loadData(): loading the data from the database into dataList");
        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        return attributesDbHelper.getAttributes(LID);
    }


    /**
     * loads the attributes that correspond with the ID from the AttributesDbHelper
     * @param context context of the application
     * @return ArrayList of all attributes that correspond with the location ID
     */
    public static mAttribute loadAttribute(String LID, String AID, Context context) {
        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        return attributesDbHelper.getAttribute(LID, AID);
    }

    /**
     * Deletes all entries in the location and attribute database
     * @param context
     */
    public static void deleteAll(Context context) {
        LocationSettingDbHelper locationSettingDbHelper = new LocationSettingDbHelper(context);
        locationSettingDbHelper.deleteAll();
        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        attributesDbHelper.deleteAll();
    }

    /**
     * Returns the setting linked to the KID and AID
     * @param context
     * @param LID key of the location in the location database
     * @param AID key of the attribute
     * @return
     */
    public static mSetting getSetting(String LID, String AID, Context context) {
        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        return attributesDbHelper.getSetting(LID, AID);
    }

    /**
     * Returns the mLocation object that is found with the KID
     * @param KID
     * @param context
     * @return
     */
    public static mLocation loadLocation(String LID, Context context) {
        LocationSettingDbHelper attributesDbHelper = new LocationSettingDbHelper(context);
        return attributesDbHelper.getLocation(LID);
    }
}
