package com.example.locationwake.Backend.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.locationwake.Logger;
import com.example.locationwake.Backend.Database.Attributes.mDistance;
import com.example.locationwake.Backend.Database.Attributes.mSetting;

import java.util.ArrayList;

/**
 * Database for storing the attributes that comes with each location
 */
public class AttributesDbHelper extends SQLiteOpenHelper {

    private String TAG = "AttributesDbHelper";

    // If you change the database schema, you must increment this number below
    public static final int DATABASE_VERSION = 7;

    //information kept in the database
    private static final String DATABASE_NAME = "attributeDatabase";

    //attributes(id, setting, distance)
    private static final String TABLE_ATTRIBUTES = "attributes";
    private static final String LOCATION_ID = "l_id";
    private static final String ATTRIBUTE_ID = "a_id";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String DISTANCE = "distance";
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
        String CREATE_LOCATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ATTRIBUTES + "(" +
                ATTRIBUTE_ID + " INTEGER NOT NULL," +
                LOCATION_ID + " INTEGER NOT NULL," +
                ATTRIBUTE_NAME + " TEXT NOT NULL," +
                DISTANCE + " TEXT NOT NULL," +
                SETTING + " TEXT NOT NULL," +
                "PRIMARY KEY (" + ATTRIBUTE_ID + ", " + LOCATION_ID + ")" +
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
     * Add the attributes in the mAttribute into the database
     * @param
     */
    public String addAttribute(mAttribute attribute) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //attributes(id, setting, distance)
        values.put(LOCATION_ID, attribute.getLID());
        values.put(ATTRIBUTE_NAME, attribute.getName());
        values.put(DISTANCE, attribute.getDistance().getDistance());
        values.put(SETTING, attribute.getSetting().getSetting());
        values.put(ATTRIBUTE_ID, getMax(db) + 1);

        int id = (int)db.insert(TABLE_ATTRIBUTES, null, values);
        db.close();
        return Integer.toString(id);
    }

    /**
     * Method to change attributes in the database
     */
    public void updateAttribute(mAttribute attribute) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //attributes(id, setting, distance)
        values.put(ATTRIBUTE_NAME, attribute.getName());
        values.put(DISTANCE, attribute.getDistance().getDistance());
        values.put(SETTING, attribute.getSetting().getSetting());

        db.update(TABLE_ATTRIBUTES,
                values,
                LOCATION_ID + " = ? AND " + ATTRIBUTE_ID + " = ? ",
                new String[]{attribute.getLID(), attribute.getAID()});
        db.close();
    }

    /**
     * deletes the attribute wiht KID and AID
     * @param LID
     * @param AID
     */
    public void deleteAttribute(String LID, String AID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ATTRIBUTES,
                LOCATION_ID + " = " + LID + " AND " + ATTRIBUTE_ID + " = " + AID,
                null);
        db.close();
    }

    /**
     * gets the max AID, used for autoincrement without using autoincrement :D
     * @param db
     * @return
     */
    public int getMax(SQLiteDatabase db) {
        String selectQuery = "SELECT max(a_id) as id FROM " + TABLE_ATTRIBUTES;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int max = cursor.getInt(cursor.getColumnIndex("id"));
        cursor.close();
        return max;
    }


    /**
     * method to get the attributes of a certain ID out of the database
     * @return String matrix with values stored in the rows
     */
    public ArrayList<mAttribute> getAttributes(String LID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //query for the database to get all the locations
        String query = "SELECT l_id, a_id, name, distance, setting FROM " +
                TABLE_ATTRIBUTES + " WHERE l_id ='" + LID + "'";
        Cursor cursor = db.rawQuery(query, null);

        //list with single entry stored in matrix for all entries
        ArrayList<mAttribute> attributes = new ArrayList<>();
        //put in every entry with the correct data
        while (cursor.moveToNext()) {
            mAttribute attribute = new mAttribute(
                    cursor.getString(cursor.getColumnIndex(LOCATION_ID)),
                    cursor.getString(cursor.getColumnIndex(ATTRIBUTE_ID)),
                    cursor.getString(cursor.getColumnIndex(ATTRIBUTE_NAME)),
                    new mDistance(cursor.getString(cursor.getColumnIndex(DISTANCE))),
                    new mSetting(cursor.getString(cursor.getColumnIndex(SETTING))));
            attributes.add(attribute);
        }

        cursor.close();
        db.close();
        Logger.logV(TAG, "getAttribute(): returned attributes");
        return attributes;
    }


    /**
     * method to get the attributes of a certain KID and AID out of the database
     * @return String matrix with values stored in the rows
     */
    public mAttribute getAttribute(String LID, String AID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {LOCATION_ID, ATTRIBUTE_ID, ATTRIBUTE_NAME, DISTANCE, SETTING};
        String where = "l_id=? AND a_id=?";
        String[] args = {LID, AID};

        Cursor cursor = db.query(TABLE_ATTRIBUTES, columns, where, args, null, null, null);
        //put in every entry with the correct data
        mAttribute attribute = new mAttribute(null, null, null, null, null);
        while (cursor.moveToNext()) {
            attribute.setLID(cursor.getString(0));
            attribute.setAID(cursor.getString(1));
            attribute.setName(cursor.getString(2));
            attribute.setDistance(new mDistance(cursor.getString(3)));
            attribute.setSetting(new mSetting(cursor.getString(4)));
        }
        cursor.close();
        db.close();
        return attribute;
    }


    /**
     * method to get the attributes of a certain ID out of the database
     * @return String matrix with values stored in the rows
     */
    public mSetting getSetting(String LID, String AID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {SETTING};
        String where = "l_id=? AND a_id=?";
        String[] args = {LID, AID};

        Cursor cursor = db.query(TABLE_ATTRIBUTES, columns, where, args, null, null, null);
        //put in every entry with the correct data
        mSetting setting = new mSetting(null);
        while (cursor.moveToNext()) {
            setting.setSetting(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return setting;
    }

    /**
     * Method to delete all data from the database
     */
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ATTRIBUTES, null, null);
        db.close();
    }

}
