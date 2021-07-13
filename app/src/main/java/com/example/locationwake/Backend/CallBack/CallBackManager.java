package com.example.locationwake.Backend.CallBack;

import android.app.Activity;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Logger;

import java.util.ArrayList;

public class CallBackManager {

    /**
     * Tag of the class
     */
    static final private String TAG = "CallBackManager";

    private static final CallBackManager instance = new CallBackManager();

    private static final ArrayList<CallBackActivity> callBackActivityList = new ArrayList<>();

    public static CallBackManager getInstance() {
        return instance;
    }

    public void addActivity(CallBackActivity activity) {
        callBackActivityList.add(activity);
    }

    public static void callBackActivities(boolean update,
                                          boolean succeeded,
                                          boolean failed,
                                          char type,
                                          String message ) {
        for (CallBackActivity activity:callBackActivityList) {
            if (!activity.isFinishing() && !activity.isDestroyed()) {
                Logger.logD(TAG, "callBackActivities(): calling back activity " + activity.getClass().toString());
                activity.onCallBack(update, succeeded, failed, type, message);
            }
        }
    }
}
