package com.example.locationwake.Activities.HelperClasses.DataClasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Logger;

public class MainActivityData extends AbstractData {

    //TAG of the class
    static final private String TAG = "MainActivityData";

    private String currentSetting;
    private mLocation location;
    private mAttribute attribute;


    @Override
    public void loadData(Context context) {
        Logger.logD(TAG, "loadData(): loading data...");
        String[] keys = getSavedSetting(context);
        location = !keys[0].equals("None") ? DataHandler.loadLocation(keys[0], context) : null;
        attribute = !keys[1].equals("None") ? DataHandler.loadAttribute(keys[0], keys[1], context) : null;
        currentSetting = keys[2];
        Logger.logD(TAG, "loadData(): finished");
    }

    public mLocation getLocation() {
        return location;
    }

    public mAttribute getAttribute() {
        return attribute;
    }

    public String getCurrentSetting() {
        return currentSetting;
    }

    private String[] getSavedSetting(Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                "SETTING_FILE_NAME", Context.MODE_PRIVATE);

        // if the setting is null, there is no active setting
        return new String[]{
                pref.getString("LID", "None"),
                pref.getString("AID", "None"),
                pref.getString("setting", "None")};
    }
}
