package com.example.locationwake;

import android.util.Log;

public class Logger {
    public static boolean logD = true;
    public static boolean logV = true;
    public static boolean logE = true;

    public static void logD(String TAG, String message) {
        if (logD) Log.d(TAG, message);
    }

    public static void logV(String TAG, String message) {
        if (logV) Log.v(TAG, message);
    }

    public static void logE(String TAG, String message) {
        if (logE) Log.e(TAG, message);
    }
}
