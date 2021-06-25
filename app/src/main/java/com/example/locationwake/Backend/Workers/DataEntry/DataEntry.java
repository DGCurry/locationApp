package com.example.locationwake.Backend.Workers.DataEntry;

import com.example.locationwake.Activities.HelperClasses.FormCallBack;


public class DataEntry implements Runnable {

    /**
     * Tag of the class
     */
    static final private String TAG = "LocationWorker";

    final private FormCallBack callBack;

    public DataEntry(FormCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void run() {
        callBack.callBack(1, "hai");
    }
}
