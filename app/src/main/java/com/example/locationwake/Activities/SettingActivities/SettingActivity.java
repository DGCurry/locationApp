package com.example.locationwake.Activities.SettingActivities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Activities.AddNewLocationAttributeActivities.ChooseAttributeOrLocation;
import com.example.locationwake.Activities.HelperClasses.RecyclerViews.MainSettingRecAdapter;
import com.example.locationwake.Activities.PermissionActivities.BackgroundLocationPermissionActivity;
import com.example.locationwake.Activities.PermissionActivities.LocationPermissionActivity;
import com.example.locationwake.Activities.PermissionActivities.NotificationPermissionActivity;
import com.example.locationwake.Activities.PermissionActivities.Permission;
import com.example.locationwake.Activities.ViewLocation.LocationListActivity;
import com.example.locationwake.Backend.Database.Attributes.AttributeInterface;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class SettingActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "MainActivity";

    //GUI ELEMENTS
    private View activeSettingStub;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


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
        setContentView(R.layout.activity_main);

        // ask permissions -> create worker if needed -> gather data needed -> create GUI
        // only if sdk is greater than 23, otherwise permissions will be granted at install
        if (Build.VERSION.SDK_INT >= 23) {
            Logger.logV(TAG, "onCreate(): asking permissions");
            askPermissions();
        }
        Logger.logV(TAG, "onCreate() started startWorker()");

        Runnable runnableWorker = this::startWorker;
        runnableWorker.run();
    }

    @Override
    protected void onResume() {
        Logger.logV(TAG, "onResume(): resuming activity");
        super.onResume();
        Logger.logV(TAG, "onStart() started createUI()");
        createUI();
    }

    /**
     * Creates the GUI
     */
    protected void createUI() {
        Logger.logV(TAG, "createUI(): getting the UI elements");
        createSettingUI();
        addNavigation();
    }

    /**
     * Method to add navigation behaviour to the bottom navigation bar
     */
    private void addNavigation() {

        // Navigation
        Button list = findViewById(R.id.button_navigation_left);
        Button add = findViewById(R.id.button_navigation_middle);
        Button setting = findViewById(R.id.button_navigation_right);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the list button");
                Intent intent = new Intent(getApplicationContext(), LocationListActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the add button");
                Intent intent = new Intent(getApplicationContext(), ChooseAttributeOrLocation.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the setting button");
            }
        });
    }

    /**
     * Creates the GUI for the Setting that is currently active, or not
     */
    private void createSettingUI() {

        activeSettingStub = findViewById(R.id.include_main_active_setting);

        Logger.logD(TAG, "createSettingUI(): retrieving saved setting");
        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                "SETTING_FILE_NAME", Context.MODE_PRIVATE);

        String savedSetting = pref.getString("setting", null);

        // if the setting is null, there is no active setting
        if (savedSetting == null) {
            updateUINoSettingFound();
        } else {
            updateUISettingFound(pref.getString("LID", "None"), pref.getString("AID", "None"));
        }

    }

    /**
     * Method to ask the user permissions for services used by the application
     */
    private void askPermissions() {
        Logger.logD(TAG, "askPermissions(): gathering permissions");
        ArrayList<Integer> permissionList = checkPermissions();

        if (permissionList.size() != 0) {
            Intent intent;

            switch (permissionList.get(0)) {

                case Permission.NOTIFICATION_PERMISSION_CODE:
                    Logger.logD(TAG, "askPermissions(): starting NotificationPermissionActivity");
                    intent = new Intent(getApplicationContext(), NotificationPermissionActivity.class);
                    permissionList.remove(0);
                    intent.putExtra("PERMISSION_CODES", permissionList);
                    startActivity(intent);
                    break;

                case Permission.LOCATION_PERMISSION_CODE:
                    Logger.logD(TAG, "askPermissions(): starting LocationPermissionActivity");
                    intent = new Intent(getApplicationContext(), LocationPermissionActivity.class);
                    permissionList.remove(0);
                    intent.putExtra("PERMISSION_CODES", permissionList);
                    startActivity(intent);
                    break;

                case Permission.BACKGROUNDLOCATION_PERMISSION_CODE:
                    Logger.logD(TAG, "askPermissions(): starting BackgroundLocationPermissionActivity");
                    intent = new Intent(getApplicationContext(), BackgroundLocationPermissionActivity.class);
                    permissionList.remove(0);
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
            Logger.logD(TAG, "checkPermissions(): adding NOTIFICATION_PERMISSION_CODE to list");
            permissionList.add(Permission.NOTIFICATION_PERMISSION_CODE);
        }

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Logger.logD(TAG, "checkPermissions(): adding LOCATION_PERMISSION_CODE to list");
            permissionList.add(Permission.LOCATION_PERMISSION_CODE);
        }

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Logger.logD(TAG, "checkPermissions(): adding BACKGROUNDLOCATION_PERMISSION_CODE to list");
            permissionList.add(Permission.BACKGROUNDLOCATION_PERMISSION_CODE);
        }

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Logger.logD(TAG, "checkPermissions(): adding INTERNET_PERMISSION_CODE to list");
            permissionList.add(Permission.INTERNET_PERMISSION_CODE);
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
                com.example.locationwake.Backend.Managers.WorkManager.class,
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
    }

    /**
     * method called when the backend has found no setting to be enabled.
     */
    private void updateUINoSettingFound() {
        Logger.logD(TAG, "updateUISettingFound(): savedSetting is null, no location found");

        TextView title = findViewById(R.id.textView_location_title_main);
        TextView subtitle = findViewById(R.id.textView_setting_title_main);

        title.setText("No location found");
        subtitle.setText("Add location or wait 10 minutes");

        subtitle.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(), "There is no location found, UI updated", Toast.LENGTH_SHORT).show();
    }

    /**
     * method called when the backend has found a setting to be enabled. The UI is updated
     */
    private void updateUISettingFound(String LID, String AID) {
        Logger.logD(TAG, "createUI(): savedSetting is not null, location found");
        //retrieve all the data and inflate one of the thingies
        activeSettingStub.setVisibility(View.VISIBLE);

        TextView title = findViewById(R.id.textView_location_title_main);

        String name = DataHandler.loadLocation(LID, getApplicationContext()).getName();

        title.setText(name);

        mLocation mLocation = DataHandler.loadLocation(LID, getApplicationContext());
        mAttribute attribute = DataHandler.loadAttribute(LID, AID, getApplicationContext());
        //Holds all the data that is in the database
        ArrayList<AttributeInterface> list = new ArrayList<>();
        list.add(attribute.getSetting());
        list.add(attribute.getRadius());
        list.add(mLocation);

        Logger.logD(TAG, "createUI(): populating recyclerview");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_main_active_setting);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MainSettingRecAdapter(list, getApplicationContext());
        recyclerView.setAdapter(mAdapter);

        Toast.makeText(getApplicationContext(), "There is a location found, UI updated", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to handle the callback from the backend
     * @param update if the Activity should update components of itself, this is true
     * @param succeeded if an action called by the Activity has succeeded, this is true
     * @param failed if an action called by the Activity has failed, this is true
     * @param type to distinguish between more CallBacks with the same boolean values, a Char can be added
     * @param message to give the user or developer feedback, a message can be added
     */
    @Override
    public void onCallBack(boolean update, boolean succeeded, boolean failed, char type, String message) {
        if (update) {
            runOnUiThread(this::createUI);
        }
    }
}
