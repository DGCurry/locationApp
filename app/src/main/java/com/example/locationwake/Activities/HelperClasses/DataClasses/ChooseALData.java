package com.example.locationwake.Activities.HelperClasses.DataClasses;

import android.content.Context;

import com.example.locationwake.Backend.Database.mLocation;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Logger;

import java.util.ArrayList;

public class ChooseALData extends AbstractData {

    //TAG of the class
    static final private String TAG = "ChooseALData";

    private ArrayList<mLocation> locations;

    @Override
    public void loadData(Context context) {
        Logger.logD(TAG, "loadData(): loading data...");
        locations = DataHandler.loadLocations(context);
        Logger.logD(TAG, "loadData(): finished");

    }

    public ArrayList<mLocation> getLocations() {
        return locations;
    }
}
