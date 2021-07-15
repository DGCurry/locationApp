package com.example.locationwake.Activities.NewLocationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class AddSettingActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "AddSettingActivity";

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

        Logger.logV(TAG, "onCreate(): getting data from AddNameActivity");

        if (getIntent().hasExtra("input")) {
            try {
                data = new JSONObject(getIntent().getStringExtra("input"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        loadData();

        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
    }

    private void loadData() {
        settings = new ArrayList<>();
        settings.add("SLT");
        settings.add("VBR");
        settings.add("SND");

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

        Button button = findViewById(R.id.button_ad_setting_input);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!new mSetting(setting[0]).isValid()) {
                    Logger.logE(TAG, "createUI(): onClick(): SETTING is invalid");
                    Toast.makeText(getApplicationContext(), "The item Setting is invalid", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    data.put("setting", setting[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //go to the next activity
                Intent intent = new Intent(getApplicationContext(), AddLocationOverViewActivity.class);
                intent.putExtra("input", data.toString());
                startActivity(intent);
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