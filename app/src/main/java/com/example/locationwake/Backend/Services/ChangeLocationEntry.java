package com.example.locationwake.Backend.Services;

import android.content.Context;

import com.example.locationwake.Backend.Database.mLocation;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Managers.CallBackManager;
import com.example.locationwake.Logger;


public class ChangeLocationEntry implements Runnable {

    /**
     * Tag of the class
     */
    static final private String TAG = "ChangeLocationEntry";

    private final mLocation location;

    private final Context context;

    /**
     * Constructor that sets the data to be added to the database
     * @param location holds the location and name
     * @param context context of the application
     */
    public ChangeLocationEntry(mLocation location, Context context) {
        this.location = location;
        this.context = context;
    }

    /**
     * Called when thread is opened. Checks the input values. If input value is invalid, it calls
     * the onFailure function. If the input is valid, it calls the onSuccess function
     */
    @Override
    public void run() {
        if (location != null) {
            Logger.logD(TAG, "run(): Succeeded to change data, location object is not null");
            DataHandler.updateLocation(location, context);
            CallBackManager.callBackActivities(true, false, false, 'L',
                    "succeeded, updated location");
        } else {
            Logger.logE(TAG, "run(): Failed to change data, location object is null");
            CallBackManager.callBackActivities(false, false, true, 'L',
                    "failed, did not update location");
        }

    }

}
