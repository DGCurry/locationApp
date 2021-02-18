package com.example.locationwake.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Database for storing the location, with an ID, name and setting
 */
public class LocationSettingDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment this number below
    public static final int DATABASE_VERSION = 1;

    //information kept in the database
    private static final String DATABASE_NAME = "lookupDatabase";

    //location(id, name, latitude, longitude, distance, setting)
    private static final String TABLE_LOCATION = "location";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LON = "longitude";
    private static final String KEY_DISTANCE = "distance";
    private static final String SETTING = "setting";

    /**
     * Constructor for the database
     * @param context the app context
     * @param name name of the database
     * @param factory -
     * @param version version id of the database, for later updates
     * @param errorHandler -
     */
    public LocationSettingDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION, errorHandler);
    }

    /**
     * Create the table
     * @param db database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " TEXT," + KEY_LAT + " TEXT," + KEY_LON + " TEXT," + KEY_DISTANCE + " TEXT," + SETTING + " TEXT" + ")";
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

        //location(id, name, latitude, longitude, distance, setting)
        values.put(KEY_NAME, location.getName());
        values.put(KEY_LAT, location.getLat());
        values.put(KEY_LON, location.getLng());
        values.put(KEY_DISTANCE, location.getDistance());
        values.put(SETTING, location.getSetting());

        db.insert(TABLE_LOCATION, null, values);
        db.close();
    }

    /**
     * method to get the locations out of the database
     * @return String matrix with values stored in the rows
     */
    public String[][] getLocation() {
        SQLiteDatabase db = this.getWritableDatabase();
        //query for the database to get all the locations
        String query = "SELECT id, name, latitude, longitude, distance, setting FROM " + TABLE_LOCATION;
        Cursor cursor = db.rawQuery(query, null);

        //list with single entry stored in matrix for all entries
        String[][] locationList = new String[cursor.getCount()][6];
        int i = 0;

        //put in every entry the correct data
        while (cursor.moveToNext()) {
            //put everything in the list
            locationList[i][0] = (cursor.getString(cursor.getColumnIndex(KEY_ID)));
            locationList[i][1] = (cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            locationList[i][2] = (cursor.getString(cursor.getColumnIndex(KEY_LAT)));
            locationList[i][3] = (cursor.getString(cursor.getColumnIndex(KEY_LON)));
            locationList[i][4] = (cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
            locationList[i][5] = (cursor.getString(cursor.getColumnIndex(SETTING)));
        }

        db.close();
        return locationList;
    }


}
