package com.example.locationwake.Activities.SettingActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Activities.AddNewLocationAttributeActivities.ChooseAttributeOrLocation;
import com.example.locationwake.Activities.ViewLocation.LocationListActivity;
import com.example.locationwake.Logger;
import com.example.locationwake.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.concurrent.TimeUnit;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class SettingActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "SettingActivity";

    //GUI ELEMENTS
    SwitchMaterial notificationSwitch, notificationAfterUpdateSwitch, uiOSSwitch, uiDarkSwitch, uiLightSwitch;
    SeekBar serviceMinutes;
    Button acceptServiceMinutes;
    TextView serviceMinutesTv;


    /**
     * Method to start activity
     *
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.logV(TAG, "onCreate(): created SettingActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onStart() {
        Logger.logV(TAG, "onStart(): starting activity");
        super.onStart();


        Logger.logV(TAG, "onStart(): adding callback");
        Runnable runnableCallBack = this::addCallBack;
        runnableCallBack.run();

        Logger.logV(TAG, "onStart(): initializing UI ");
        initializeUI();
    }

    @Override
    protected void onResume() {
        Logger.logV(TAG, "onResume(): resuming activity");
        super.onResume();

        Logger.logV(TAG, "onResume(): loading data ");
//        dataHolder = new MainActivityData();
//        dataHolder.setContext(getApplicationContext());
//        dataHolder.run();

        Logger.logV(TAG, "onStart() started createUI()");
        createUI();
        Logger.logV(TAG, "onStart() started updateUI()");
        updateUI();
    }

    private void initializeUI() {
        Logger.logD(TAG, "initializeUI(): initializing");

        notificationSwitch = findViewById(R.id.switch_not_update);
        notificationAfterUpdateSwitch = findViewById(R.id.switch_not_after_update);
        uiOSSwitch = findViewById(R.id.switch_use_OS);
        uiDarkSwitch = findViewById(R.id.switch_ui_dark);
        uiLightSwitch = findViewById(R.id.switch_ui_light);
        serviceMinutes = findViewById(R.id.seekBar_service_minutes);
        acceptServiceMinutes = findViewById(R.id.button_agree);
        serviceMinutesTv = findViewById(R.id.textView_service_minutes);

        initializeThemeUI();
        initializeNotificationUI();
        initializeServiceMinutesUI();
    }

    private void initializeThemeUI() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("THEME_FILE_NAME", Context.MODE_PRIVATE);
        int themeMode = preferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        switch(themeMode) {
            case AppCompatDelegate.MODE_NIGHT_YES:
                uiDarkSwitch.setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                uiOSSwitch.setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                uiLightSwitch.setChecked(true);
        }

        //TODO: switches can still be turned off when they are enabled, leaving everything disabled
        // adding listeners to the switches for themes
        uiOSSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if it is checked, other theme switches must be turned off
                if (isChecked) {
                    uiDarkSwitch.setChecked(false);
                    uiLightSwitch.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    preferences.edit().putInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM).apply();
                }
            }
        });

        uiDarkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if it is checked, other theme switches must be turned off
                if (isChecked) {
                    uiOSSwitch.setChecked(false);
                    uiLightSwitch.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    preferences.edit().putInt("theme_mode", AppCompatDelegate.MODE_NIGHT_YES).apply();
                }
            }
        });

        uiLightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if it is checked, other theme switches must be turned off
                if (isChecked) {
                    uiDarkSwitch.setChecked(false);
                    uiOSSwitch.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    preferences.edit().putInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO).apply();
                }
            }
        });
    }

    private void initializeNotificationUI() {

        //adding listener to switches to control behaviour of notifications
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if it is checked, settings should changed in backend
                SharedPreferences pref = getApplicationContext().getSharedPreferences(
                        "NOTIFICATIONS_UPDATE_FILE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("notification_during_update", isChecked);
                editor.apply();
            }
        });

        notificationAfterUpdateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if it is checked, settings should changed in backend
                SharedPreferences pref = getApplicationContext().getSharedPreferences(
                        "NOTIFICATIONS_UPDATE_FILE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("notification_after_update", isChecked);
                editor.apply();
            }
        });
    }

    private void initializeServiceMinutesUI() {

        serviceMinutes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                serviceMinutesTv.setText((progress + 15) + " minutes");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        acceptServiceMinutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked acceptServiceMinutes, setting delay between" +
                        "worker at least " + serviceMinutes.getProgress() + 15);
                int minutes = serviceMinutes.getProgress() + 15;
                SharedPreferences pref = getApplicationContext().getSharedPreferences(
                        "SERVICE_MINUTES_FILE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("service_minutes", minutes);
                editor.apply();

                restartWorker();

                Toast.makeText(getBaseContext(), "Worker has been " +
                                "restarted with the new interval of " + minutes + " minutes",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUI() {
        serviceMinutesTv.setText("15 Minutes");
    }

    /**
     * Creates the GUI
     */
    protected void createUI() {
        Logger.logV(TAG, "createUI(): getting the UI elements");
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

    private void restartWorker() {
        Logger.logD(TAG, "startWorker(): starting workmanager");

        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                "SERVICE_MINUTES_FILE_NAME", Context.MODE_PRIVATE);
        int repeatInterval = pref.getInt("service_minutes", 15);

        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(
                com.example.locationwake.Backend.Managers.WorkManager.class,
                repeatInterval,
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
}
