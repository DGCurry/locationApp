package com.example.locationwake;

import android.util.Log;

public class Logger {

    public static void logD(String TAG, String message) {
        if (BuildConfig.logD) Log.d(TAG, message);
    }

    public static void logV(String TAG, String message) {
        if (BuildConfig.logV) Log.v(TAG, message);
    }

    public static void logE(String TAG, String message) {
        if (BuildConfig.logE) Log.e(TAG, message);
    }


}
