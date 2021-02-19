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
 * Database for storing the attributes that comes with each location
 */
public class AttributesDbHelper extends SQLiteOpenHelper {

    private String TAG = "AttributesDbHelper";

    // If you change the database schema, you must increment this number below
    public static final int DATABASE_VERSION = 1;

    //information kept in the database
    private static final String DATABASE_NAME = "attributeDatabase";

    //attributes(id, setting, distance)
    private static final String TABLE_ATTRIBUTES = "attributes";
    private static final String KEY_ID = "id";
    private static final String KEY_DISTANCE = "distance";
    private static final String SETTING = "setting";

    /**
     * Constructor for the database
     * @param context the app context
     */
    public AttributesDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create the table
     * @param db database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Logger.logV(TAG, "onCreate(SQLiteDatabase db): opened database");
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_ATTRIBUTES + "(" + KEY_ID + " INTEGER," +
                KEY_DISTANCE + " TEXT," + SETTING + " TEXT" + ")";
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
     * Add the attributes in the mAttribute into the database
     * @param attribute instance of the data holder
     */
    public void addAttribute(mAttribute attribute) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //attributes(id, setting, distance)
        values.put(KEY_ID, attribute.getID());
        values.put(KEY_DISTANCE, attribute.getDistance());
        values.put(SETTING, attribute.getSetting());

        db.insert(TABLE_ATTRIBUTES, null, values);
        Logger.logD(TAG, "addAttribute(mAttribute attribute): added attribute to ID: " + attribute.getID() + " to " + TABLE_ATTRIBUTES + "\n"
                + attribute.getID() + "\n"
                + attribute.getDistance() + "\n"
                + attribute.getSetting());
        db.close();
    }


    /**
     * method to get the attributes of a certain ID out of the database
     * @return String matrix with values stored in the rows
     */
    public ArrayList<String[]> getAttribute(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //query for the database to get all the locations
        String query = "SELECT id, distance, setting FROM " + TABLE_ATTRIBUTES + " WHERE id ='" + ID + "'";
        Cursor cursor = db.rawQuery(query, null);

        //list with single entry stored in matrix for all entries
        ArrayList<String[]> attributeList = new ArrayList<>();
        //put in every entry with the correct data
        while (cursor.moveToNext()) {
            String[] dataEntry = new String[3];
            dataEntry[0] = cursor.getString(cursor.getColumnIndex(KEY_ID));
            dataEntry[1] = cursor.getString(cursor.getColumnIndex(KEY_DISTANCE));
            dataEntry[2] = cursor.getString(cursor.getColumnIndex(KEY_DISTANCE));

            attributeList.add(dataEntry);
        }

        Logger.logD(TAG, "getAttribute(): size of the dataEntry array is " + attributeList.size());

        for (String[] entry:attributeList) {
            Logger.logD(TAG, "getLocation(): got attributes from table " + TABLE_ATTRIBUTES + "\n"
                    + entry[0] + "\n"
                    + entry[1] + "\n"
                    + entry[2]);
        }

        Logger.logV(TAG, "getAttribute(): returned attributes");

        cursor.close();
        db.close();
        return attributeList;
    }
}
