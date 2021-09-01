package com.example.locationwake.Activities.EditLocationActivities.ChangeAttributeActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Backend.Database.Attributes.mRadius;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Backend.Services.ChangeAttributeEntry;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class ChangeSettingActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "ChangeSettingActivity";

    ArrayList<String> settings;

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
        setContentView(R.layout.activity_add_setting);

        loadData();

        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
    }

    /**
     * Method to load the data send by other activities
     */
    private void loadData() {
        Logger.logV(TAG, "loadData(): loading the data from the database into dataList");
        settings = new ArrayList<>();
        settings.add("SLT");
        settings.add("VBR");
        settings.add("SND");

        Logger.logV(TAG, "loadData(): getting data from AddNameActivity");
        if (getIntent().hasExtra("data")) {
            try {
                data = new JSONObject(getIntent().getStringExtra("data"));
            } catch (JSONException e) {
                Logger.logE(TAG, "loadData(): could not load the data");
                e.printStackTrace();
            }
        }
    }


    /**
     * Creates the GUI by adding the listeners
     */
    protected void createUI() {
        Logger.logV(TAG, "createUI(): creating recyclerView and adding elements into it");
        Spinner spinnerSetting = (Spinner) findViewById(R.id.spinner_ad_setting);
        final String[] setting = new String[1];

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, settings);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSetting.setAdapter(arrayAdapter);
        spinnerSetting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setting[0] = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                setting[0] = settings.get(0);
            }
        });

        TextView title = findViewById(R.id.textView_header_title);
        TextView subTitle = findViewById(R.id.textView_header_subtitle);
        subTitle.setVisibility(View.VISIBLE);

        try {
            title.setText(data.get("locationName").toString());
            subTitle.setText(data.get("attributeName").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addNavigation(setting);
    }

    /**
     * Method to add navigation behaviour to the bottom navigation bar
     * @param setting String that holds the setting given by the user
     */
    private void addNavigation(String[] setting) {

        // Navigation
        Button back = findViewById(R.id.button_navigation_left);
        Button stop = findViewById(R.id.button_navigation_middle);
        Button send = findViewById(R.id.button_navigation_right);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the back button");
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
                Logger.logD(TAG, "onClick(): clicked on the send button");
                Logger.logE(TAG, "onClick(): setting is " + setting[0]);
                if (!new mSetting(setting[0]).isValid()) {
                    Logger.logE(TAG, "createUI(): onClick(): SETTING is invalid");
                    Toast.makeText(getApplicationContext(), "The item Setting is invalid", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    ChangeAttributeEntry attributeEntry = new ChangeAttributeEntry(
                            new mAttribute(data.get("LID").toString(),
                                    data.get("AID").toString(),
                                    data.get("attributeName").toString(),
                                    new mRadius(data.get("radius").toString()),
                                    new mSetting(setting[0])),
                            getApplicationContext());
                    attributeEntry.run();
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
                case 'A':
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    Logger.logD(TAG, message);
                    finish();
            }
        } else if (failed) {
            switch(type) {
                case 'A':
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    Logger.logD(TAG, message);
            }
        }
    }

}