package com.example.locationwake.Activities.HelperClasses.DataClasses;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.locationwake.Backend.Managers.CallBackManager;
import com.example.locationwake.Logger;

public abstract class AbstractData implements Runnable {

    //TAG of the class
    static final private String TAG = "AbstractData";

    protected Context context;

    public void setContext(@NonNull Context context) {
        this.context = context;
    }

    protected abstract void loadData(@NonNull Context context);

    protected void updateActivity() {
        Logger.logD(TAG, "updateActivity(): calling back activity");
        CallBackManager.callBackActivities(true, false, false, 'D', "Data has been loaded in, update the activity");
    }

    @Override
    public void run() {
        loadData(context);
        updateActivity();
    }
}
