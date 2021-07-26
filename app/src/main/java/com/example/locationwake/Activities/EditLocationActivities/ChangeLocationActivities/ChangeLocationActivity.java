package com.example.locationwake.Activities.EditLocationActivities.ChangeLocationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Activities.EditLocationActivities.ChangeAttributeActivities.ChangeNameAttributeActivity;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Services.ChangeLocationEntry;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity to add a location to the database
 */
public class ChangeLocationActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "ChangeLocationActivity";

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

        Runnable runnableCallBack = new Runnable() {
            @Override
            public void run() {
                addCallBack();
            }
        };
        runnableCallBack.run();

        loadData();

        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
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

        TextView title = findViewById(R.id.textView_location_title_main);
        try {
            title.setText(data.get("locationName").toString());
            latitudeInput.setText(data.get("latitude").toString());
            longitudeInput.setText(data.get("longitude").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                Intent intent = new Intent(getApplicationContext(), ChangeNameAttributeActivity.class);
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
                //check the input data
                if (!new mLocation(null, null, latitudeInput.getText().toString(),
                        longitudeInput.getText().toString()).isValid()) {
                    Logger.logE(TAG, "createUI(): onClick(): LOCATION is invalid");
                    Toast.makeText(getApplicationContext(), "The item Location is invalid", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    ChangeLocationEntry locationEntry = new ChangeLocationEntry(
                            new mLocation(data.get("LID").toString(),
                                    data.get("locationName").toString(),
                                    latitudeInput.getText().toString(),
                                    longitudeInput.getText().toString()), getApplicationContext());
                    locationEntry.run();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * If the data entry has succeeded, it will call the callback method, which will be catched here
     * @param update if the Activity should update components of itself, this is true
     * @param succeeded if an action called by the Activity has succeeded, this is true
     * @param failed if an action called by the Activity has failed, this is true
     * @param type to distinguish between more CallBacks with the same boolean values, a Char can be added
     * @param message to give the user or developer feedback, a message can be added
     */
    @Override
    public void onCallBack(boolean update, boolean succeeded, boolean failed, char type, String message) {
        if (update) {
            switch(type) {
                // D for data
                case 'L':
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    Logger.logD(TAG, message);
                    finish();
            }
        } else if (failed) {
            switch(type) {
                case 'L':
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    Logger.logD(TAG, message);
            }
        }
    }
}
