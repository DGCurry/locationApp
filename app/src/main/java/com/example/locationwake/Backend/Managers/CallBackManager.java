package com.example.locationwake.Backend.Managers;

import androidx.lifecycle.Lifecycle;

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

    private static CallBackActivity callBackActivity;

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
    public void setActivity(CallBackActivity activity) {
        callBackActivity = activity;
    }

    public void removeActivity() {
        callBackActivity = null;
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
        if (callBackActivity == null) {
            Logger.logE(TAG, "callBackActivities(): callBackActivity is null");
            return;
        }
        if (callBackActivity.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED) ||
                callBackActivity.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            Logger.logD(TAG, "callBackActivities(): calling back activity " + callBackActivity.getClass().toString());
            callBackActivity.onCallBack(update, succeeded, failed, type, message);
        } else {
            Logger.logE(TAG, "callBackActivities(): no active activity to call back");
        }
    }
}
