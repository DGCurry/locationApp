package com.example.locationwake.Backend.Database;

import android.content.Context;

import com.example.locationwake.Logger;
import com.example.locationwake.Backend.Database.Attributes.mDistance;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.Attributes.mSetting;

import java.util.ArrayList;

/**
 * Class to handle all the data going into the database and getting from the database
 */
public class DataHandler {

    //TAG of the class
    static final private String TAG = "DataHandler";

    /**
     * method to add data to the databases
     * @param nameString name the user added to the entry
     * @param latitudeString latitude of the entry
     * @param longitudeString longitude of the entry
     * @param distanceString distance of the entry
     * @param settingString setting of the entry, see mAttribute
     * @param context context of the application
     * @return ID of the entry in the LocationSettingDbHelper
     */
    public static int addData(String nameString, String latitudeString, String longitudeString,
                              String distanceString, String settingString, Context context) {

        //Adding data to database

        mLocation mLocation = new mLocation(latitudeString, longitudeString, nameString);

        LocationSettingDbHelper locationSettingDbHelper = new LocationSettingDbHelper(context);
        int KID = locationSettingDbHelper.addLocation(mLocation);

        mDistance mDistance = new mDistance(distanceString);
        mSetting mSetting = new mSetting(settingString);

        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        attributesDbHelper.addAttribute(Integer.toString(KID), mDistance, mSetting);

        //debug
        Logger.logD(TAG, "addData(String nameString, String latitudeString, String longitudeString, \n" +
                "String distanceString, String settingString, Context context): " +
                "Added data \n " +
                KID + "\n" +
                nameString + "\n" +
                latitudeString + "\n" +
                longitudeString + "\n" +
                distanceString + "\n" +
                settingString);

        return KID;
    }

    /**
     * Adding an attribute to the database linked to an entry in the location database, with key KID
     * @param KID key of the location in the location database
     * @param distanceString string with distance in meters, needs to be an Integer when converted
     * @param settingString Silent SLT, Vibrate VBR, Sound SND
     * @param context
     */
    public static void addAttribute(int KID, String distanceString, String settingString, Context context) {
        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        mDistance mDistance = new mDistance(distanceString);
        mSetting mSetting = new mSetting(settingString);
        attributesDbHelper.addAttribute(Integer.toString(KID), mDistance, mSetting);
    }

    /**
     * Deleting an attribute from the database with KID and AID as key
     * @param KID key of the location in the location database
     * @param AID key of the attribute
     * @param context
     */
    public static void deleteAttribute(int KID, int AID, Context context) {
        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        attributesDbHelper.deleteAttribute(KID, AID);
    }

    /**
     * loads the locations from the LocationSettingDbHelper
     * @param context context of the application
     * @return ArrayList of all locations put in a mLocation instance
     */
    public static ArrayList<mLocation> loadLocations(Context context) {
        ArrayList<mLocation> returnList = new ArrayList<>();
        //get the database
        LocationSettingDbHelper settingDbHelper = new LocationSettingDbHelper(context);
        //set the data we have in the correct lists
        ArrayList<String[]> locationList = settingDbHelper.getLocation();

        for (String[] location:locationList) {
            mLocation mLocation = new mLocation(location[1], location[2]);
            mLocation.setName(settingDbHelper.getName(location[0]));
            mLocation.setKID((Integer.valueOf(location[0])));

            //debug
            Logger.logD(TAG, "loadLocations(Context context): Got location \n " +
                    mLocation.getKID() + "\n" +
                    mLocation.getLat() + "\n" +
                    mLocation.getLng());

            returnList.add(mLocation);
        }
        return returnList;
    }

    /**
     * loads the attributes that correspond with the ID from the AttributesDbHelper
     * @param context context of the application
     * @return ArrayList of all attributes that correspond with the location ID
     */
    public static ArrayList<mAttribute> loadAttributes(int KID, Context context) {
        ArrayList<mAttribute> returnList = new ArrayList<>();
        Logger.logV(TAG, "loadData(): loading the data from the database into dataList");
        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        ArrayList<String[]> attributeList = attributesDbHelper.getAttributes(KID);

        for (String[] attribute:attributeList) {
            mAttribute mAttribute  = new mAttribute(
                    Integer.parseInt(attribute[0]),
                    Integer.parseInt(attribute[1]),
                    new mDistance(attribute[2]),
                    new mSetting(attribute[3]));

            //debug
            Logger.logD(TAG, "loadLocations(Context context): Got location \n " +
                    KID + "\n" +
                    mAttribute.getAID() + "\n" +
                    mAttribute.getDistance() + "\n" +
                    mAttribute.getSetting());

            returnList.add(mAttribute);
        }
        return returnList;
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
     * @param KID key of the location in the location database
     * @param AID key of the attribute
     * @return
     */
    public static String getSetting(int KID, int AID, Context context) {
        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        return attributesDbHelper.getSetting(KID, AID);
    }
}
