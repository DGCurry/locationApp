package com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddAttributeActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationwake.Backend.Database.Attributes.mDistance;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class AddDistanceActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_add_distance);

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
                Intent intent = new Intent(getApplicationContext(), AddNameActivity.class);
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
                Logger.logD(TAG, "onClick(): clicked on the send button");
                if (!new mDistance(Integer.toString(radius.getProgress())).isValid()) {
                    Logger.logE(TAG, "createUI(): onClick(): RADIUS is invalid");
                    Toast.makeText(getApplicationContext(), "The item RADIUS is invalid", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    data.put("radius", Integer.toString(radius.getProgress()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //go to the next activity
                Intent intent = new Intent(getApplicationContext(), AddAttributeOverViewActivity.class);
                intent.putExtra("data", data.toString());
                startActivity(intent);
                finish();
            }
        });
    }

}