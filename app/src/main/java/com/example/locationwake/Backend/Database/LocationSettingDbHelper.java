package com.example.locationwake.Backend.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.locationwake.Logger;
import com.example.locationwake.Backend.Database.Attributes.mLocation;

import java.util.ArrayList;

/**
 * Database for storing the location, with an ID, name and setting
 */
public class LocationSettingDbHelper extends SQLiteOpenHelper {

    private String TAG = "LocationSettingDbHelper";

    // If you change the database schema, you must increment this number below
    public static final int DATABASE_VERSION = 1;

    //information kept in the database
    private static final String DATABASE_NAME = "lookupDatabase";

    //location(id, name, latitude, longitude)
    private static final String TABLE_LOCATION = "location";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LON = "longitude";

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
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " TEXT NOT NULL," +
                KEY_LAT + " TEXT NOT NULL," +
                KEY_LON + " TEXT NOT NULL" +
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
    public int addLocation(mLocation location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_LAT, location.getLat());
        values.put(KEY_LON, location.getLng());
        values.put(KEY_NAME, location.getName());

        int id = (int)db.insert(TABLE_LOCATION, null, values);

        db.close();
        return id;
    }


    /**
     * update the values in the mLocation into the database
     * @param location instance of the data holder
     */
    public void updateLocation(String id, mLocation location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_LAT, location.getLat());
        values.put(KEY_LON, location.getLng());
        values.put(KEY_NAME, location.getName());

        db.update(TABLE_LOCATION, values, KEY_ID + " = ?", new String[]{id});
    }


    /**
     * method to get the locations out of the database
     * @return String matrix with values stored in the rows
     *              row 0: ID
     *              row 1: latitude
     *              row 2: longitude
     */
    public ArrayList<String[]> getLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        //query for the database to get all the locations
        String query = "SELECT id, latitude, longitude FROM " + TABLE_LOCATION;
        Cursor cursor = db.rawQuery(query, null);

        //list with single entry stored in matrix for all entries
        ArrayList<String[]> locationList = new ArrayList<>();
        //put in every entry with the correct data
        while (cursor.moveToNext()) {
            String[] dataEntry = new String[3];
            dataEntry[0] = cursor.getString(cursor.getColumnIndex(KEY_ID));
            dataEntry[1] = cursor.getString(cursor.getColumnIndex(KEY_LAT));
            dataEntry[2] = cursor.getString(cursor.getColumnIndex(KEY_LON));

            locationList.add(dataEntry);
        }

        cursor.close();
        db.close();
        Logger.logV(TAG, "getLocation(): returned locations");
        return locationList;
    }


    /**
     * method to get the attributes of a certain ID out of the database
     * @return String matrix with values stored in the rows
     */
    public String[] getLocation(String KID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //query for the database to get all the locations
        String query = "SELECT latitude, longitude FROM " + TABLE_LOCATION + " WHERE id ='" + KID + "'";
        Cursor cursor = db.rawQuery(query, null);

        //put in every entry with the correct data
        String[] dataEntry = new String[2];
        while (cursor.moveToNext()) {
            dataEntry[0] = cursor.getString(0);
            dataEntry[1] = cursor.getString(1);
        }

        cursor.close();
        db.close();
        return dataEntry;
    }



    /**
     * method to get the locations out of the database
     * @return String matrix with values stored in the rows
     *              row 0: ID
     *              row 1: name
     *              row 2: latitude
     *              row 3: longitude
     */
    public String getName(String KID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //query for the database to get all the locations
        String query = "SELECT name FROM " + TABLE_LOCATION + " WHERE id ='" + KID + "'";
        Cursor cursor = db.rawQuery(query, null);

        //list with single entry stored in matrix for all entries
        String name = "None";
        //put in every entry with the correct data
        try {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
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
