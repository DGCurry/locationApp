package com.example.locationwake.Backend.Services;

import android.content.Context;

import com.example.locationwake.Backend.Managers.CallBackManager;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;


public class DataEntry implements Runnable {

    /**
     * Tag of the class
     */
    static final private String TAG = "LocationWorker";

    private mAttribute attribute;
    private mLocation location;

    private final Context context;

    /**
     * Constructor that sets the data to be added to the database
     * @param attribute holds all attributes
     * @param location holds the location and name
     * @param context context of the application
     */
    public DataEntry(mAttribute attribute, mLocation location, Context context) {
        this.attribute = attribute;
        this.location = location;
        this.context = context;
    }

    public DataEntry(mAttribute attribute, Context context) {
        this.attribute = attribute;
        this.context = context;
    }

    public DataEntry(mLocation location, Context context) {
        this.location = location;
        this.context = context;
    }

    /**
     * Called when thread is opened. Checks the input values. If input value is invalid, it calls
     * the onFailure function. If the input is valid, it calls the onSuccess function
     */
    @Override
    public void run() {
        if (attribute == null && location == null) {
            CallBackManager.callBackActivities(false, false, true, 'B',
                    "failed, attribute and location is null");
        } else if (attribute != null) {
            DataHandler.addAttribute(attribute, context);
            CallBackManager.callBackActivities(false, true, false, 'A',
                    "succeeded, added attribute");
        } else if (location != null) {
            DataHandler.addLocation(location, context);
            CallBackManager.callBackActivities(false, true, false, 'L',
                    "succeeded, added location");
        } else {
            DataHandler.addData(attribute, location, context);
            CallBackManager.callBackActivities(false, true, false, 'B',
                    "succeeded, added attribute and location");
        }
    }

}
