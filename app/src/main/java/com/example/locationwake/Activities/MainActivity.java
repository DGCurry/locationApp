package com.example.locationwake.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.locationwake.Activities.PermissionActivities.BackgroundLocationPermissionActivity;
import com.example.locationwake.Activities.PermissionActivities.LocationPermissionActivity;
import com.example.locationwake.Activities.PermissionActivities.NotificationPermissionActivity;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class MainActivity extends AppCompatActivity {

    //TAG of the class
    static final private String TAG = "MainActivity";



    //GUI ELEMENTS
    //adding location
    private EditText nameEdit;
    private EditText longitudeEdit;
    private EditText latitudeEdit;
    private EditText distanceEdit;
    private EditText settingEdit;
    private Button addButton;
    private Button soundButton;

    //activity finding location
    private Button goButton;

    protected static final int NOTIFICATION_PERMISSION_CODE = 100;
    protected static final int LOCATION_PERMISSION_CODE = 101;
    protected static final int BACKGROUNDLOCATION_PERMISSION_CODE = 102;
    protected static final int INTERNET_PERMISSION_CODE = 103;

    /**
     * Method to start activity
     *
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.logV(TAG, "onCreate(): created MainActivity");
        super.onCreate(savedInstanceState);

        // ask permissions -> create worker if needed -> gather data needed -> create GUI
        // only if sdk is greater than 23, otherwise permissions will be granted at install
        if (Build.VERSION.SDK_INT >= 23) {
            Logger.logV(TAG, "onCreate(): asking permissions");
            askPermissions();
        }
        setContentView(R.layout.activity_main);
        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(): created MainActivity");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        Logger.logV(TAG, "onStart(): started MainActivity");
        super.onStart();

        //Log, TAG, method, action
        Logger.logV(TAG, "onStart() started createUI()");
        createUI();

        Logger.logV(TAG, "onStart() started startWorker()");
        startWorker();
        Intent intent = new Intent(MainActivity.this, settingactivity.class);
        startActivity(intent);
    }



    /**
     * Creates the GUI by adding the listeners
     */
    private void createUI() {
        Logger.logV(TAG, "createUI(): getting the UI elements");
//        //adding location
//        nameEdit = findViewById(R.id.editName);
//        longitudeEdit = findViewById(R.id.editLongitude);
//        latitudeEdit = findViewById(R.id.editLatitude);
//        distanceEdit = findViewById(R.id.editDistance);
//        settingEdit = findViewById(R.id.editSetting);
//
//        Logger.logV(TAG, "createUI(): setting onClickListeners on the buttons");
//
//        addButton = findViewById(R.id.buttonAdd);
//        addButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //Log, TAG, method, action
//                Logger.logV(TAG, "createUI(): onClick(View v): Clicked on addButton");
//                //get the text that has been added
//                //TODO: can not be done if there is no text or if the text is already in the database
//                DataHandler.addData(nameEdit.getText().toString(),
//                        latitudeEdit.getText().toString(),
//                        longitudeEdit.getText().toString(),
//                        distanceEdit.getText().toString(),
//                        settingEdit.getText().toString(),
//                        getApplicationContext());
//            }
//        });
//
//        //activity finding location
//        goButton = findViewById(R.id.buttonFind);
//        goButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //Log, TAG, method, action
//                Logger.logV(TAG, "createUI(): onClick(View v): Clicked on goButton");
//                //create Intent
//                Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        //activity finding location
//        soundButton = findViewById(R.id.buttonSound);
//        soundButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //Log, TAG, method, action
//                Logger.logV(TAG, "createUI(): onClick(View v): Clicked on soundButton");
//                //create Intent
//                Intent intent = new Intent(MainActivity.this, ChangeSetting.class);
//                startActivity(intent);
//            }
//        });
    }

    /**
     * Method to ask the user permissions for services used by the application
     */
    private void askPermissions() {
        Logger.logD(TAG, "askPermissions(): gathering permissions");
        ArrayList<Integer> permissionList = checkPermissions();

        if (permissionList.size() != 0) {
            permissionList.remove(0);
            Intent intent;

            switch (permissionList.get(0)) {

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

                case BACKGROUNDLOCATION_PERMISSION_CODE:
                    Logger.logD(TAG, "askPermissions(): starting BackgroundLocationPermissionActivity");
                    intent = new Intent(getApplicationContext(), BackgroundLocationPermissionActivity.class);
                    intent.putExtra("PERMISSION_CODES", permissionList);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * method to check which permissions need to be asked by the application
     * @return list of integers responding to the permissions
     */
    private ArrayList<Integer> checkPermissions() {
        Logger.logV(TAG, "checkPermissions(): checking permissions to be asked");
        ArrayList<Integer> permissionList = new ArrayList<>();
        // Always ask for the notification permission, to change notification schema
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_NOTIFICATION_POLICY) ==
                PackageManager.PERMISSION_DENIED) {
            Logger.logD(TAG, "askPermissions(): adding NOTIFICATION_PERMISSION_CODE to list");
            permissionList.add(NOTIFICATION_PERMISSION_CODE);
        }

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Logger.logD(TAG, "askPermissions(): adding LOCATION_PERMISSION_CODE to list");
            permissionList.add(LOCATION_PERMISSION_CODE);
        }

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Logger.logD(TAG, "askPermissions(): adding BACKGROUNDLOCATION_PERMISSION_CODE to list");
            permissionList.add(BACKGROUNDLOCATION_PERMISSION_CODE);
        }

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Logger.logD(TAG, "askPermissions(): adding INTERNET_PERMISSION_CODE to list");
            permissionList.add(INTERNET_PERMISSION_CODE);
        }

        return permissionList;
    }

    /**
     * method to start all worker background threads
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startWorker() {
        Logger.logD(TAG, "startWorker(): starting workmanager");
        PeriodicWorkRequest  work = new PeriodicWorkRequest.Builder(
                com.example.locationwake.backend.Workers.WorkManager.class,
                15,
                TimeUnit.MINUTES)
                .addTag("WorkManager")
                .setBackoffCriteria(BackoffPolicy.LINEAR, 60*1000, TimeUnit.MILLISECONDS)
                .build();

        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        workManager.enqueueUniquePeriodicWork(
                "WorkManager",
                ExistingPeriodicWorkPolicy.KEEP,
                work
        );
        Logger.logD(TAG, "startWorker(): started workmanager");
    }
}
