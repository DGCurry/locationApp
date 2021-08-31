package com.example.locationwake.Backend.Services;

import android.content.Context;

import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Backend.Managers.CallBackManager;
import com.example.locationwake.Logger;


public class ChangeAttributeEntry implements Runnable {

    /**
     * Tag of the class
     */
    static final private String TAG = "ChangeAttributeEntry";

    private final mAttribute attribute;

    private final Context context;

    public ChangeAttributeEntry(mAttribute attribute, Context context) {
        this.attribute = attribute;
        this.context = context;
    }

    /**
     * Called when thread is opened. Checks the input values. If input value is invalid, it calls
     * the onFailure function. If the input is valid, it calls the onSuccess function
     */
    @Override
    public void run() {
        if (attribute != null) {
            Logger.logD(TAG, "run(): Succeeded to change data, attribute object is not null");
            DataHandler.updateAttribute(attribute, context);
            CallBackManager.callBackActivities(true, false, false, 'A',
                    "succeeded, updated attribute");
        } else {
            Logger.logE(TAG, "run(): Failed to change data, attribute object is null");
            CallBackManager.callBackActivities(false, false, true, 'A',
                    "failed, did not update attribute");
        }
    }

}
