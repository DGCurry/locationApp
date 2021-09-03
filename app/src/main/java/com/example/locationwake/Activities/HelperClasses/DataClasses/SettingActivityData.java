package com.example.locationwake.Activities.HelperClasses.DataClasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mLocation;
import com.example.locationwake.Logger;

import java.util.ArrayList;

public class SettingActivityData extends AbstractData {

    //TAG of the class
    static final private String TAG = "SettingActivityData";

    private ArrayList<mLocation> locations;

    @Override
    public void loadData(Context context) {
        Logger.logD(TAG, "loadData(): loading data...");
        locations = DataHandler.loadLocations(context);
        Logger.logD(TAG, "loadData(): finished");

    }

    public void setServiceMinutes(int minutes) {
        SharedPreferences pref = context.getSharedPreferences(
                "SERVICE_MINUTES", Context.MODE_PRIVATE);
        SharedPreferences.Editor minutesEditor = pref.edit();

    }
}
