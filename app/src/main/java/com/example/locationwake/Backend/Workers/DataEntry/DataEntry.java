package com.example.locationwake.Backend.Workers.DataEntry;

import android.content.Context;
import android.location.Location;
import android.telecom.Call;

import com.example.locationwake.Activities.ExtendedActivities.CallBackActivity;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;

import java.util.ArrayList;


public class DataEntry implements Runnable {

    /**
     * Tag of the class
     */
    static final private String TAG = "LocationWorker";

    final private CallBackActivity callBack;

    private final mAttribute mAttribute;
    private final mLocation mLocation;

    private final Context context;

    public DataEntry(CallBackActivity callBack, mAttribute mAttribute, mLocation mLocation, Context context) {
        this.callBack = callBack;

        this.mAttribute = mAttribute;
        this.mLocation = mLocation;

        this.context = context;
    }

    /**
     * Called when thread is opened. Checks the input values. If input value is invalid, it calls
     * the onFailure function. If the input is valid, it calls the onSuccess function
     */
    @Override
    public void run() {
        if (!checkLocation(Float.parseFloat(mLocation.getLng()),
                Float.parseFloat(mLocation.getLat()),
                Integer.parseInt(mAttribute.getDistance().getDistance()))) {
            callBack.onCallBack(false, false, true, 'D', "location failed");
            return;
        }

        if (!checkSetting(mAttribute.getSetting().getSetting())) {
            callBack.onCallBack(false, false, true, 'D', "setting failed");
            return;
        }

        if (!checkName(mLocation.getName())) {
            callBack.onCallBack(false, false, true, 'D', "name failed");
            return;
        }

        DataHandler.addData(mLocation.getName(), mLocation.getLat(), mLocation.getLng(),
                mAttribute.getDistance().getDistance(), mAttribute.getSetting().getSetting(), context);

        callBack.onCallBack(false, true, false, 'D', "succeeded");

    }

    /**
     *
     * @param name
     * @return
     */
    private boolean checkName(String name) {
        return true;
    }

    private boolean checkSetting(String setting) {
        return setting.equals("VBR") || setting.equals("SLT") || setting.equals("SND");
    }

    private boolean checkLocation(float longitude, float latitude, int radius) {
        if (latitude < -90 || latitude > 90) {
            return false;
        } else if (longitude < -180 || longitude > 180) {
            return false;
        }

        // check other locations in database and see if it is close to another location
        ArrayList<mLocation> locations = DataHandler.loadLocations(context);

        for (mLocation location:locations) {
            ArrayList<mAttribute> attributes = DataHandler.loadAttributes(location.getKID(), context);
            for (mAttribute attribute:attributes) {
                // get distance from new location to old location
                // check if this distance is smaller than one of the radius'

                Location newLocation = new Location("");
                newLocation.setLongitude(longitude);
                newLocation.setLatitude(latitude);

                Location savedLocation = new Location("");
                savedLocation.setLongitude(Float.parseFloat(location.getLng()));
                savedLocation.setLatitude(Float.parseFloat(location.getLat()));

                float distance = newLocation.distanceTo(savedLocation);

                if (distance <= radius
                        || distance <= Integer.parseInt(attribute.getDistance().getDistance())) {
                    return false;
                }
            }
        }
        return true;
    }

    
}
