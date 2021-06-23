package com.example.locationwake.Activities.PermissionActivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.locationwake.Logger;

import java.util.ArrayList;

public class Permission extends AppCompatActivity {

    static final private String TAG = "Permission";

    protected ArrayList<Integer> permissionList;

    protected static final int NOTIFICATION_PERMISSION_CODE = 100;
    protected static final int LOCATION_PERMISSION_CODE = 101;
    protected static final int BACKGROUNDLOCATION_PERMISSION_CODE = 102;
    protected static final int INTERNET_PERMISSION_CODE = 103;


    protected String getPermissionMapping(int permission) {
        switch (permission) {
            case NOTIFICATION_PERMISSION_CODE:
                if (Build.VERSION.SDK_INT >= 23) {
                    return Manifest.permission.ACCESS_NOTIFICATION_POLICY;
                } else {
                    return null;
                }

            case LOCATION_PERMISSION_CODE:
                return Manifest.permission.ACCESS_FINE_LOCATION;

            case BACKGROUNDLOCATION_PERMISSION_CODE:
                if (Build.VERSION.SDK_INT >= 29) {
                    return Manifest.permission.ACCESS_BACKGROUND_LOCATION;
                } else {
                    return null;
                }

            case INTERNET_PERMISSION_CODE:
                return Manifest.permission.INTERNET;

            default:
                return null;
        }
    }

    public void onFinish() {
        Logger.logE(TAG, "onFinish(): the permission list is: ");
        for (Integer permission:permissionList) {
            Logger.logE(TAG, permission + " " + getPermissionMapping(permission));
        }

        if (!permissionList.isEmpty()) {
            int nextPermission = permissionList.get(0);
            permissionList.remove(0);
            Intent intent;
            switch (nextPermission) {

                case BACKGROUNDLOCATION_PERMISSION_CODE:
                    Logger.logD(TAG, "askPermissions(): starting BackgroundLocationPermissionActivity");
                    intent = new Intent(getApplicationContext(), BackgroundLocationPermissionActivity.class);
                    intent.putExtra("PERMISSION_CODES", permissionList);
                    startActivity(intent);
                    break;

                case NOTIFICATION_PERMISSION_CODE:
                    Logger.logD(TAG, "askPermissions(): starting NotificationPermissionActivity");
                    intent = new Intent(getApplicationContext(), NotificationPermissionActivity.class);
                    intent.putExtra("PERMISSION_CODES", permissionList);
                    startActivity(intent);
                    break;

                case LOCATION_PERMISSION_CODE:
                    Logger.logD(TAG, "askPermissions(): starting LocationPermissionActivity");
                    intent = new Intent(getApplicationContext(), LocationPermissionActivity.class);
                    intent.putExtra("PERMISSION_CODES", permissionList);
                    startActivity(intent);
                    break;

                case INTERNET_PERMISSION_CODE:
                    Logger.logD(TAG, "askPermissions(): starting InternetPermissionActivity");
                    intent = new Intent(getApplicationContext(), InternetPermissionActivity.class);
                    intent.putExtra("PERMISSION_CODES", permissionList);
                    startActivity(intent);
                    break;

            }
        }
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Logger.logE(TAG, requestCode + " " + permissions.length + " " + grantResults[0]);
        switch (requestCode) {
            case NOTIFICATION_PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Logger.logE(TAG, "permission for not granted");
                } else {
                    Logger.logE(TAG, "permission for not denied");
                }
                break;

            case LOCATION_PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Logger.logE(TAG, "permission for loc granted");

                } else {
                    Logger.logE(TAG, "permission for loc denied");
                }
                break;

            case BACKGROUNDLOCATION_PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Logger.logE(TAG, "permission for backloc granted");
                } else {
                    Logger.logE(TAG, "permission for backloc denied");
                }

            case INTERNET_PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Logger.logE(TAG, "permission for internet granted");
                } else {
                    Logger.logE(TAG, "permission for internet denied");
                }
        }
        onFinish();
    }

}
