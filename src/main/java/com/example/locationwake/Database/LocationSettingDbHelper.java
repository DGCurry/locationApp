package com.example.locationwake.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.locationwake.Logger;

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
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " TEXT," + KEY_LAT + " TEXT," + KEY_LON + " TEXT" + ")";
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
    public void addLocation(mLocation location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //location(id, name, latitude, longitude)
        values.put(KEY_NAME, location.getName());
        values.put(KEY_LAT, location.getLat());
        values.put(KEY_LON, location.getLng());

        db.insert(TABLE_LOCATION, null, values);
        Logger.logD(TAG, "addLocation(mLocation location): added location " + TABLE_LOCATION + "\n"
                + location.getName() + "\n"
                + location.getLat() + "\n"
                + location.getLng());

        db.close();
    }


    /**
     * method to get the locations out of the database
     * @return String matrix with values stored in the rows
     */
    public ArrayList<String[]> getLocation() {
        SQLiteDatabase db = this.getWritableDatabase();
        //query for the database to get all the locations
        String query = "SELECT id, name, latitude, longitude FROM " + TABLE_LOCATION;
        Cursor cursor = db.rawQuery(query, null);

        //list with single entry stored in matrix for all entries
        ArrayList<String[]> locationList = new ArrayList<>();
        //put in every entry with the correct data
        while (cursor.moveToNext()) {
            String[] dataEntry = new String[4];
            dataEntry[0] = cursor.getString(cursor.getColumnIndex(KEY_ID));
            dataEntry[1] = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            dataEntry[2] = cursor.getString(cursor.getColumnIndex(KEY_LAT));
            dataEntry[3] = cursor.getString(cursor.getColumnIndex(KEY_LON));

            locationList.add(dataEntry);
        }
        Logger.logD(TAG, "getLocation(): size of the dataEntry array is " + Integer.toString(locationList.size()));
        for (String[] entry:locationList) {
            Logger.logD(TAG, "getLocation(): got location from table " + TABLE_LOCATION + "\n"
                    + entry[0] + "\n"
                    + entry[1] + "\n"
                    + entry[2] + "\n"
                    + entry[3]);

        }

        Logger.logV(TAG, "getLocation(): returned locations");

        cursor.close();
        db.close();
        return locationList;
    }
}
