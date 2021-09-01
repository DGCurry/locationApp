package com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddLocationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddAttributeActivities.AddNameAttributeActivity;
import com.example.locationwake.Backend.Database.Attributes.mLatLng;
import com.example.locationwake.Backend.Database.mLocation;
import com.example.locationwake.Backend.Database.Attributes.mRadius;
import com.example.locationwake.Backend.Services.CheckLocationEntry;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity to add a location to the database
 */
public class AddLocationActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "AddLocationActivity";

    // JSON object used to communicate between activities
    private JSONObject data = new JSONObject();

    /**
     * Method to start activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started AddLocationActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        createUI();
    }

    @Override
    protected void onStart() {
        Logger.logV(TAG, "onStart(): starting activity");
        super.onStart();


        Logger.logV(TAG, "onStart(): adding callback");
        Runnable runnableCallBack = this::addCallBack;
        runnableCallBack.run();

    }

    /**
     * Method to load the data send by other activities
     */
    private void loadData() {
        Logger.logV(TAG, "loadData(): getting data from AddNameActivity");
        if (getIntent().hasExtra("data")) {
            try {
                data = new JSONObject(getIntent().getStringExtra("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Creates the GUI by adding the listeners
     */
    protected void createUI() {
        Logger.logV(TAG, "createUI(): creating UI and assigning listeners");

        EditText latitudeInput = findViewById(R.id.editText_ad_location_latitude);
        EditText longitudeInput = findViewById(R.id.editText_ad_location_longitude);

        Button mapButton = findViewById(R.id.button_ad_maps);
        mapButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ShowLocationActivity.class);
            intent.putExtra("data", data.toString());
            startActivity(intent);
            finish();
        });


        addNavigation(latitudeInput, longitudeInput);
    }

    /**
     * Method to add navigation behaviour to the bottom navigation bar
     * @param latitudeInput EditText for user to enter the latitude of the location
     * @param longitudeInput EditText for user to enter the longitude of the location
     */
    private void addNavigation(EditText latitudeInput, EditText longitudeInput) {

        // Navigation
        Button back = findViewById(R.id.button_navigation_left);
        Button stop = findViewById(R.id.button_navigation_middle);
        Button send = findViewById(R.id.button_navigation_right);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the back button");
                Intent intent = new Intent(getApplicationContext(), AddNameAttributeActivity.class);
                intent.putExtra("data", data.toString());
                startActivity(intent);
                finish();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the stop button");
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "createUI(): clicked on send Button");

                try {
                    String latitude = data.get("latitude").toString();
                    String longitude = data.get("longitude").toString();

                    if (new mLatLng(latitude, longitude).isValid()) {
                        Runnable checkLocationEntry = new CheckLocationEntry(
                                getApplicationContext(),
                                latitude,
                                longitude,
                                new mRadius("100"));
                        checkLocationEntry.run();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCallBack(boolean update, boolean succeeded, boolean failed, char type, String message) {
        if (succeeded) {
            //go to the next activity
            Intent intent = new Intent(getApplicationContext(), AddLocationOverViewActivity.class);
            intent.putExtra("data", data.toString());
            startActivity(intent);
            finish();
        } else if (failed) {
            Logger.logD(TAG, "onCallBack(): location " + message + " is too close");
        }
    }
}