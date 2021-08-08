package com.example.locationwake.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Activities.AddNewLocationAttributeActivities.ChooseAttributeOrLocation;
import com.example.locationwake.Activities.HelperClasses.DataClasses.MainActivityData;
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
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class MainActivity extends CallBackActivity implements OnMapReadyCallback {

    //TAG of the class
    static final private String TAG = "MainActivity";

    //GUI ELEMENTS
    private View activeSettingStub;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private TextView locationName;
    private RecyclerView attributesRC;
    private RecyclerView.Adapter attributesAd;

    private MainActivityData dataHolder;


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

        Logger.logV(TAG, "onCreate(): adding callback");
        Runnable runnableCallBack = this::addCallBack;
        runnableCallBack.run();
    }

    @Override
    protected void onStart() {
        Logger.logV(TAG, "onStart(): starting activity");
        super.onStart();

        Logger.logV(TAG, "onStart(): loading data ");
        dataHolder = new MainActivityData();
        dataHolder.setContext(getApplicationContext());
        dataHolder.run();

        Logger.logV(TAG, "onStart(): initializing UI ");
        initializeUI();
    }

    @Override
    protected void onResume() {
        Logger.logV(TAG, "onResume(): resuming activity");
        super.onResume();

        Logger.logV(TAG, "onResume(): started createUI()");
        createUI();
    }

    /**
     * Creates the GUI
     */
    protected void createUI() {
        Logger.logV(TAG, "createUI(): adding navigation to the activity");
        addNavigation();

        Logger.logV(TAG, "createUI(): getting the UI elements");
        updateUI();
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
     * method called when the backend has found a setting to be enabled. The UI is updated
     */
    private void populateRC(mLocation location, mAttribute attribute) {
        //retrieve all the data and inflate one of the thingies
        //Holds all the data that is in the database
        ArrayList<AttributeInterface> list = new ArrayList<>();
        list.add(attribute.getSetting());
        list.add(attribute.getRadius());
        list.add(location);

        attributesRC.setVisibility(View.VISIBLE);

        attributesRC.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        attributesRC.setLayoutManager(layoutManager);
        RecyclerView.Adapter attributesAd = new MainSettingRecAdapter(list, getApplicationContext());
        attributesRC.setAdapter(attributesAd);

        Toast.makeText(getApplicationContext(), "There is a location found, UI updated", Toast.LENGTH_SHORT).show();
    }

    private void initializeUI() {
        Logger.logD(TAG, "initializeUI(): initializing");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_main);
        attributesRC = (RecyclerView) findViewById(R.id.recyclerView_main_active_setting);
        locationName = findViewById(R.id.textView_header_title);
    }

    /**
     * Updates and creates the GUI for the Setting that is currently active, or not
     */
    private void updateUI() {
        String locationNameText = dataHolder.getLocation() != null ? dataHolder.getLocation().getName() : "No location has been found";

        locationName.setText(locationNameText);
        if (dataHolder.getLocation() != null && dataHolder.getAttribute() != null) {
            mapFragment.getMapAsync(this);
            findViewById(R.id.include_add_location).setVisibility(View.INVISIBLE);

            Logger.logD(TAG, "updateUI(): found location, updating RC");
            populateRC(dataHolder.getLocation(), dataHolder.getAttribute());
            View switchLocation = findViewById(R.id.cardView_turn_on_off);
            switchLocation.setVisibility(View.VISIBLE);

        } else {
            Logger.logD(TAG, "updateUI(): found no location, displaying add location button");
            findViewById(R.id.include_add_location).setVisibility(View.INVISIBLE);

            View addLocation = findViewById(R.id.include_add_location);
            addLocation.setVisibility(View.VISIBLE);
            TextView addLocationT = addLocation.findViewById(R.id.textView_header_title);
            addLocationT.setText("Add a new location");
            addLocation.findViewById(R.id.button_header_icon).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.getUiSettings().setScrollGesturesEnabled(false);

        LatLng location = new LatLng(Double.parseDouble(dataHolder.getLocation().getLat()), Double.parseDouble(dataHolder.getLocation().getLng()));
        googleMap.addMarker(new MarkerOptions().position(location));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
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
            runOnUiThread(this::updateUI);
        }
    }

}
