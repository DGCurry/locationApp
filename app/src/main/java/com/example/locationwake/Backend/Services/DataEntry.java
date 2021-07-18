package com.example.locationwake.Backend.Services;

import android.content.Context;

import com.example.locationwake.Backend.Managers.CallBackManager;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Logger;


public class DataEntry implements Runnable {

    /**
     * Tag of the class
     */
    static final private String TAG = "LocationWorker";

    private final mAttribute mAttribute;
    private final mLocation mLocation;

    private final Context context;

    /**
     * Constructor that sets the data to be added to the database
     * @param mAttribute holds all attributes
     * @param mLocation holds the location and name
     * @param context context of the application
     */
    public DataEntry(mAttribute mAttribute, mLocation mLocation, Context context) {
        this.mAttribute = mAttribute;
        Logger.logE(TAG, "DataEntry(): mLocation " + mLocation.getLat() + " " + mLocation.getLng());
        this.mLocation = mLocation;

        this.context = context;
    }

    /**
     * Called when thread is opened. Checks the input values. If input value is invalid, it calls
     * the onFailure function. If the input is valid, it calls the onSuccess function
     */
    @Override
    public void run() {
        DataHandler.addData(mLocation.getName(), mLocation.getLat(), mLocation.getLng(),
                mAttribute.getDistance().getDistance(), mAttribute.getSetting().getSetting(), context);
        CallBackManager.callBackActivities(false, true, false, 'D',  "succeeded");

    }

    
}
