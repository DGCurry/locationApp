package com.example.locationwake.Activities.EditLocationActivities.ChangeAttributeActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Backend.Database.Attributes.mDistance;
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
public class ChangeDistanceActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "ChangeDistanceActivity";

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
        setContentView(R.layout.activity_add_distance);

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
        Logger.logV(TAG, "loadData(): loading the data from the database into dataList");

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
        Logger.logV(TAG, "createUI(): creating recyclerView and adding elements into it");

        TextView radiusValue = findViewById(R.id.textView_ad_attribute_distance_value);

        SeekBar radius = findViewById(R.id.seekBar_ad_attribute_radius);
        radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // updated continuously as the user slides the thumb
                radiusValue.setText(progress + " meters");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // called after the user finishes moving the SeekBar
            }
        });

        TextView title = findViewById(R.id.textView_location_title_main);
        TextView subTitle = findViewById(R.id.textView_setting_title_main);
        subTitle.setVisibility(View.VISIBLE);

        try {
            title.setText(data.get("locationName").toString());
            subTitle.setText(data.get("attributeName").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addNavigation(radius);
    }

    /**
     * Method to add navigation behaviour to the bottom navigation bar
     * @param radius SeekBar that holds the preferred radius by the user
     */
    private void addNavigation(SeekBar radius) {

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
                if (!new mDistance(Integer.toString(radius.getProgress())).isValid()) {
                    Logger.logE(TAG, "createUI(): onClick(): RADIUS is invalid");
                    Toast.makeText(getApplicationContext(), "The item RADIUS is invalid", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    ChangeAttributeEntry attributeEntry = new ChangeAttributeEntry(
                            new mAttribute(data.get("LID").toString(),
                                    data.get("AID").toString(),
                                    data.get("attributeName").toString(),
                                    new mDistance(Integer.toString(radius.getProgress())),
                                    new mSetting(data.get("setting").toString())),
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