package com.example.locationwake.Activities.NewLocationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Activities.MainActivity;
import com.example.locationwake.Backend.Database.Attributes.mDistance;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
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
public class AddLocationOverViewActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "AddLocationOverViewActivity";

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
        setContentView(R.layout.activity_add_overview);

        Runnable runnableCallBack = new Runnable() {
            @Override
            public void run() {
                addCallBack();
            }
        };
        runnableCallBack.run();

        Logger.logV(TAG, "onCreate(): getting data from AddNameActivity");
        if (getIntent().hasExtra("input")) {
            try {
                data = new JSONObject(getIntent().getStringExtra("input"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
    }


    /**
     * Creates the GUI by adding the listeners
     */
    protected void createUI() {
        Logger.logV(TAG, "createUI(): creating UI and assigning listeners");
        TextView name, latitude, longitude, radius, setting;
        name = findViewById(R.id.textView_ad_overview_name);
        latitude = findViewById(R.id.textView_ad_overview_latitude);
        longitude = findViewById(R.id.textView_ad_overview_longitude);
        radius = findViewById(R.id.textView_ad_overview_radius);
        setting = findViewById(R.id.textView_ad_overview_setting);

        final String nameEntry, latitudeEntry, longitudeEntry, radiusEntry, settingEntry;
        try {
            nameEntry = data.get("name").toString();
            latitudeEntry = data.get("latitude").toString();
            longitudeEntry = data.get("longitude").toString();
            radiusEntry = data.get("radius").toString();
            settingEntry = data.get("setting").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        name.setText(nameEntry);
        latitude.setText(latitudeEntry);
        longitude.setText(longitudeEntry);
        radius.setText(radiusEntry);
        setting.setText(settingEntry);


        Button sendButton = findViewById(R.id.button_ad_overview_input);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataEntry dataEntry = new DataEntry(new mAttribute(null, null, new mDistance(radiusEntry), new mSetting(settingEntry)),
                        new mLocation(latitudeEntry, longitudeEntry, nameEntry),
                        getApplicationContext());
                dataEntry.run();
            }
        });
    }

    @Override
    public void onCallBack(boolean update, boolean succeeded, boolean failed, char type, String message) {
        if (succeeded) {
            switch(type) {
                // D for data
                case 'D':
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    Logger.logD(TAG, message);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
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