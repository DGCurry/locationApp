package com.example.locationwake.Backend.Managers;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Logger;

import java.util.ArrayList;

/**
 * Class that handles the callbacks from the backend to the frontend
 */
public class CallBackManager {

    /**
     * Tag of the class
     */
    static final private String TAG = "CallBackManager";

    // Singleton instance of the class
    private static final CallBackManager instance = new CallBackManager();

    // List of all activities that have subscribed for the callback
    private static final ArrayList<CallBackActivity> callBackActivityList = new ArrayList<>();

    /**
     * Returns the Singleton instance of CallBackManager
     * @return
     */
    public static CallBackManager getInstance() {
        return instance;
    }

    /**
     * adds an activity to the list of callbacks
     * @param activity target activity for the callback
     */
    public void addActivity(CallBackActivity activity) {
        callBackActivityList.add(activity);
    }

    public void removeActivity(CallBackActivity activity) {
        callBackActivityList.remove(activity);
    }

    /**
     * Goes through all classes/activities that has subscribed for a callback. Checks first if the
     * activity is still active, and if so, calls the callback
     * @param update if the Activity should update components of itself, this is true
     * @param succeeded if an action called by the Activity has succeeded, this is true
     * @param failed if an action called by the Activity has failed, this is true
     * @param type to distinguish between more CallBacks with the same boolean values, a Char can be added
     * @param message to give the user or developer feedback, a message can be added
     */
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
