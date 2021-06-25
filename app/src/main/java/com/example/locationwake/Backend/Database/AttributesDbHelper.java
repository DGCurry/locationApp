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
    public static final int DATABASE_VERSION = 5;

    //information kept in the database
    private static final String DATABASE_NAME = "attributeDatabase";

    //attributes(id, setting, distance)
    private static final String TABLE_ATTRIBUTES = "attributes";
    private static final String KEY_ID = "k_id";
    private static final String ATTRIBUTE_ID = "a_id";
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
                KEY_ID + " INTEGER NOT NULL," +
                DISTANCE + " TEXT NOT NULL," +
                SETTING + " TEXT NOT NULL," +
                "PRIMARY KEY (" + ATTRIBUTE_ID + ", " + KEY_ID + ")" +
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
    public int addAttribute(String KID, mDistance mDistance, mSetting mSetting) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //attributes(id, setting, distance)
        values.put(KEY_ID, KID);
        values.put(DISTANCE, mDistance.getDistance());
        values.put(SETTING, mSetting.getSetting());
        values.put(ATTRIBUTE_ID, getMax(db) + 1);

        int id = (int)db.insert(TABLE_ATTRIBUTES, null, values);
        db.close();
        return id;
    }

    public void updateAttribute(String KID, String AID, mDistance mDistance, mSetting mSetting) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //attributes(id, setting, distance)
        values.put(DISTANCE, mDistance.getDistance());
        values.put(SETTING, mSetting.getSetting());

        db.update(TABLE_ATTRIBUTES, values, KEY_ID + " = ? AND " + ATTRIBUTE_ID + " = ? ", new String[]{KID, AID});
        db.close();
    }

    /**
     * deletes the attribute wiht KID and AID
     * @param KID
     * @param AID
     */
    public void deleteAttribute(int KID, int AID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ATTRIBUTES, KEY_ID + " = " + KID + " AND " + ATTRIBUTE_ID + " = " + AID, null);
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
    public ArrayList<String[]> getAttributes(int KID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //query for the database to get all the locations
        String query = "SELECT k_id, a_id, distance, setting FROM " + TABLE_ATTRIBUTES + " WHERE k_id ='" + KID + "'";
        Cursor cursor = db.rawQuery(query, null);

        //list with single entry stored in matrix for all entries
        ArrayList<String[]> attributeList = new ArrayList<>();
        //put in every entry with the correct data
        while (cursor.moveToNext()) {
            String[] dataEntry = new String[4];
            dataEntry[0] = cursor.getString(cursor.getColumnIndex(KEY_ID));
            dataEntry[1] = cursor.getString(cursor.getColumnIndex(ATTRIBUTE_ID));
            dataEntry[2] = cursor.getString(cursor.getColumnIndex(DISTANCE));
            dataEntry[3] = cursor.getString(cursor.getColumnIndex(SETTING));

            attributeList.add(dataEntry);
        }

        cursor.close();
        db.close();
        Logger.logV(TAG, "getAttribute(): returned attributes");
        return attributeList;
    }


    /**
     * method to get the attributes of a certain ID out of the database
     * @return String matrix with values stored in the rows
     */
    public String getSetting(int KID, int AID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {SETTING};
        String where = "k_id=? AND a_id=?";
        String[] args = {Integer.toString(KID), Integer.toString(AID)};

        Cursor cursor = db.query(TABLE_ATTRIBUTES, columns, where, args, null, null, null);
        //put in every entry with the correct data
        String dataEntry = null;
        while (cursor.moveToNext()) {
            dataEntry = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return dataEntry;
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
