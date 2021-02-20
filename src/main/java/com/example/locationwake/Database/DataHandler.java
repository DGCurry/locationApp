package com.example.locationwake.Database;

import android.content.Context;

import com.example.locationwake.Logger;

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
        mLocation locationEntry = new mLocation();
        locationEntry.setName(nameString);
        locationEntry.setLat(latitudeString);
        locationEntry.setLng(longitudeString);
        LocationSettingDbHelper locationSettingDbHelper = new LocationSettingDbHelper(context);
        int id = locationSettingDbHelper.addLocation(locationEntry);

        //Adding attribute to database
        mAttribute attributeEntry = new mAttribute();
        attributeEntry.setID(id);
        attributeEntry.setDistance(distanceString);
        attributeEntry.setSetting(settingString);
        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        attributesDbHelper.addAttribute(attributeEntry);

        //debug
        Logger.logD(TAG, "addData(String nameString, String latitudeString, String longitudeString, \n" +
                "String distanceString, String settingString, Context context): " +
                "Added data \n " +
                id + "\n" +
                nameString + "\n" +
                latitudeString + "\n" +
                longitudeString + "\n" +
                distanceString + "\n" +
                settingString);

        return id;
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
            mLocation mLocation = new mLocation(location[1], location[2], location[3]);
            mLocation.setID((Integer.valueOf(location[0])));

            //debug
            Logger.logD(TAG, "loadLocations(Context context): Got location \n " +
                    mLocation.getID() + "\n" +
                    mLocation.getName() + "\n" +
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
    public static ArrayList<mAttribute> loadAttribute(int ID, Context context) {
        ArrayList<mAttribute> returnList = new ArrayList<>();
        Logger.logV(TAG, "loadData(): loading the data from the database into dataList");
        AttributesDbHelper attributesDbHelper = new AttributesDbHelper(context);
        ArrayList<String[]> attributeList = attributesDbHelper.getAttribute(ID);

        for (String[] attribute:attributeList) {
            mAttribute mAttribute  = new mAttribute(ID, attribute[1], attribute[2]);

            //debug
            Logger.logD(TAG, "loadLocations(Context context): Got location \n " +
                    ID + "\n" +
                    mAttribute.getDistance() + "\n" +
                    mAttribute.getSetting());

            returnList.add(mAttribute);
        }
        return returnList;
    }
}
