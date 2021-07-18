package com.example.locationwake.Activities.PermissionActivities;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.example.locationwake.Logger;
import com.example.locationwake.R;

public class BackgroundLocationPermissionActivity extends Permission {

    //TAG of the class
    static final private String TAG = "NotificationPermissionActivity";

    /**
     * Method to start activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        permissionList = getIntent().getIntegerArrayListExtra("PERMISSION_CODES");

        int permissionI = BACKGROUNDLOCATION_PERMISSION_CODE;
        String permissionS = getPermissionMapping(permissionI);

        askPermission(permissionS, permissionI);

    }

    /**
     * Method used to start the UI to prompt the user to deny or accept a permission
     * @param permission MANIFEST String that indicates which permission has to be requested
     * @param REQUEST_CODE int to distinguish between permissions, used to see which permission is
     */
    protected void askPermission(String permission, int REQUEST_CODE) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Needed")
                        .setMessage("This permission is needed because of this and that")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Logger.logD(TAG, "askPermission(): permission accepted");
                                ActivityCompat.requestPermissions(BackgroundLocationPermissionActivity.this,
                                        new String[]{permission}, REQUEST_CODE);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            } else {
                Logger.logD(TAG, "askPermission(String permission, int REQUEST_CODE): starting" +
                        "permission interface for " + permission);
                ActivityCompat.requestPermissions(this,
                        new String[]{permission}, REQUEST_CODE);
            }
        } else {
            Logger.logV(TAG, "askPermission(String permission, int REQUEST_CODE): " +
                    "permissions should have been asked when installing");
            this.onFinish();
        }
    }

}