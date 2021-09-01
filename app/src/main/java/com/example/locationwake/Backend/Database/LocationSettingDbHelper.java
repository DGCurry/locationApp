package com.example.locationwake.Backend.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.locationwake.Backend.Database.Attributes.mLatLng;
import com.example.locationwake.Logger;

import java.util.ArrayList;

/**
 * Database for storing the location, with an ID, name and setting
 */
public class LocationSettingDbHelper extends SQLiteOpenHelper {

    private String TAG = "LocationSettingDbHelper";

    // If you change the database schema, you must increment this number below
    public static final int DATABASE_VERSION = 2;

    //information kept in the database
    private static final String DATABASE_NAME = "lookupDatabase";

    //location(id, name, latitude, longitude)
    private static final String TABLE_LOCATION = "location";
    private static final String LOCATION_ID = "l_id";
    private static final String LOCATION_NAME = "name";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    /**
     * Constructor for the database
     * @param context the app context
     */
    public LocationSettingDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create the table
     * @param db database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Logger.logV(TAG, "onCreate(SQLiteDatabase db): opened database");
        String CREATE_LOCATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOCATION + "(" +
                LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LOCATION_NAME + " TEXT NOT NULL," +
                LATITUDE + " TEXT NOT NULL," +
                LONGITUDE + " TEXT NOT NULL" +
                ")";
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    /**
     * for later updates to the database, add behaviour
     * @param db database instance
     * @param oldVersion integer of the version before
     * @param newVersion integer of the new version, newVersion > oldVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: add behaviour for updates
    }

    /**
     * Add the values in the mLocation into the database
     * @param location instance of the data holder
     */
    public String addLocation(mLocation location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(LATITUDE, location.getLatLng().getLat());
        values.put(LONGITUDE, location.getLatLng().getLng());
        values.put(LOCATION_NAME, location.getName());

        int id = (int)db.insert(TABLE_LOCATION, null, values);

        db.close();
        return Integer.toString(id);
    }


    /**
     * update the values in the mLocation into the database
     * @param location instance of the data holder
     */
    public void updateLocation(mLocation location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(LATITUDE, location.getLatLng().getLat());
        values.put(LONGITUDE, location.getLatLng().getLng());
        values.put(LOCATION_NAME, location.getName());

        db.update(TABLE_LOCATION, values, LOCATION_ID + " = ?", new String[]{location.getLID()});
        db.close();
    }


    /**
     * method to get the locations out of the database
     * @return String matrix with values stored in the rows
     *              row 0: ID
     *              row 1: latitude
     *              row 2: longitude
     */
    public ArrayList<mLocation> getLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        //query for the database to get all the locations
        String query = "SELECT l_id, name, latitude, longitude FROM " + TABLE_LOCATION;
        Cursor cursor = db.rawQuery(query, null);

        //list with single entry stored in matrix for all entries
        ArrayList<mLocation> locations = new ArrayList<>();
        //put in every entry with the correct data
        while (cursor.moveToNext()) {
            mLocation location = new mLocation(
                    cursor.getString(cursor.getColumnIndex(LOCATION_ID)),
                    cursor.getString(cursor.getColumnIndex(LOCATION_NAME)),
                    new mLatLng(
                            cursor.getString(cursor.getColumnIndex(LATITUDE)),
                            cursor.getString(cursor.getColumnIndex(LONGITUDE)))
            );

            locations.add(location);
        }

        cursor.close();
        db.close();
        Logger.logV(TAG, "getLocation(): returned locations");
        return locations;
    }


    /**
     * method to get the attributes of a certain ID out of the database
     * @return String matrix with values stored in the rows
     */
    public mLocation getLocation(String LID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //query for the database to get all the locations
        String query = "SELECT l_id, name, latitude, " +
                "longitude FROM " + TABLE_LOCATION + " WHERE l_id ='" + LID + "'";
        Cursor cursor = db.rawQuery(query, null);

        //put in every entry with the correct data
        mLocation location = new mLocation(null, null, null);
        while (cursor.moveToNext()) {
            location = new mLocation(
                    cursor.getString(cursor.getColumnIndex(LOCATION_ID)),
                    cursor.getString(cursor.getColumnIndex(LOCATION_NAME)),
                    new mLatLng(
                            cursor.getString(cursor.getColumnIndex(LATITUDE)),
                            cursor.getString(cursor.getColumnIndex(LONGITUDE)))
            );
        }

        cursor.close();
        db.close();
        return location;
    }

    /**
     * method to get the locations out of the database
     * @return String matrix with values stored in the rows
     *              row 0: ID
     *              row 1: name
     *              row 2: latitude
     *              row 3: longitude
     */
    public String getName(String LID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //query for the database to get all the locations
        String query = "SELECT name FROM " + TABLE_LOCATION + " WHERE l_id ='" + LID + "'";
        Cursor cursor = db.rawQuery(query, null);

        //list with single entry stored in matrix for all entries
        String name = "None";
        //put in every entry with the correct data
        try {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(LOCATION_NAME));
        } catch (Exception e) {
            Logger.logE(TAG, "getName(): no name found");
        }

        cursor.close();
        db.close();
        Logger.logV(TAG, "getLocation(): returned name");
        return name;
    }

    /**
     * Method to delete all data from the database
     */
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATION, null, null);
    }

}
