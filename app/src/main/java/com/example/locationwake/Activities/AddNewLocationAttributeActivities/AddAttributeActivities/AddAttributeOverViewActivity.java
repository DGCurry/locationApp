package com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddAttributeActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Backend.Database.Attributes.mRadius;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Backend.Services.DataEntry;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class AddAttributeOverViewActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "AddAttributeOverViewActivity";

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
        setContentView(R.layout.activity_add_overview_attribute);

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
        TextView locationName, attributeName, radius, setting;
        locationName = findViewById(R.id.textView_location_title_main);
        attributeName = findViewById(R.id.textView_setting_title_main);
        radius = findViewById(R.id.textView_ad_overview_radius);
        setting = findViewById(R.id.textView_ad_overview_setting);

        final String locationNameString, attributeNameEntry, radiusEntry, settingEntry;
        try {
            locationNameString= data.get("locationName").toString();
            attributeNameEntry = data.get("attributeName").toString();
            radiusEntry = data.get("radius").toString();
            settingEntry= data.get("setting").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        locationName.setText(locationNameString);
        attributeName.setText(attributeNameEntry);
        radius.setText(radiusEntry);
        setting.setText(settingEntry);

        addNavigation(attributeNameEntry, radiusEntry, settingEntry);
    }

    /**
     * Method to add navigation behaviour to the bottom navigation bar
     * @param radiusEntry String that holds the given radius by the user
     * @param settingEntry String that holds the given setting by the user
     */
    private void addNavigation(String attributeNameEntry,
                               String radiusEntry, String settingEntry) {

        // Navigation
        Button back = findViewById(R.id.button_navigation_left);
        Button stop = findViewById(R.id.button_navigation_middle);
        Button send = findViewById(R.id.button_navigation_right);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the back button");
                Intent intent = new Intent(getApplicationContext(), AddSettingActivity.class);
                intent.putExtra("input", data.toString());
                startActivity(intent);
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
                Logger.logD(TAG, "onClick(): clicked on the send button");
                DataEntry dataEntry = null;
                try {
                    dataEntry = new DataEntry(new mAttribute(data.get("LID").toString(), null,
                            attributeNameEntry, new mRadius(radiusEntry), new mSetting(settingEntry)),
                            getApplicationContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dataEntry.run();
                finish();
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
        if (succeeded) {
            switch(type) {
                // D for data
                case 'D':
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    Logger.logD(TAG, message);
                    finish();

            }
        } else if (failed) {
            switch(type) {
                case 'D':
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    Logger.logD(TAG, message);
            }
        }
    }
}