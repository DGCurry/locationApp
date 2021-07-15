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
import com.example.locationwake.Activities.HelperClasses.NewLocationJSONHelper;
import com.example.locationwake.Backend.Database.Attributes.mDistance;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class AddLocationActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "AddLocationActivity";

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

        Logger.logV(TAG, "onCreate(): getting data from AddNameActivity");
        Intent intent = getIntent();

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

        EditText latitudeInput = findViewById(R.id.editText_ad_location_latitude);
        EditText longitudeInput = findViewById(R.id.editText_ad_location_longitude);

        TextView radiusValue = findViewById(R.id.textView_ad_location_distance_value);

        SeekBar radius = findViewById(R.id.seekBar_ad_location_radius);
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

        Button send = findViewById(R.id.button_ad_location_input);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "createUI(): clicked on Send Button");
                //check the input data
                if (!new mLocation(latitudeInput.getText().toString(), longitudeInput.getText().toString()).isValid()) {
                    Logger.logE(TAG, "createUI(): onClick(): LOCATION is invalid");
                    Toast.makeText(getApplicationContext(), "The item Location is invalid", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!new mDistance(Integer.toString(
                        radius.getProgress())).isValid(getApplicationContext(),
                        latitudeInput.getText().toString(),
                        longitudeInput.getText().toString())) {
                    Logger.logE(TAG, "createUI(): onClick(): RADIUS is invalid");
                    Toast.makeText(getApplicationContext(), "The item Radius is invalid", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    data.put("latitude", latitudeInput.getText().toString());
                    data.put("longitude", longitudeInput.getText().toString());
                    data.put("radius", Integer.toString(radius.getProgress()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //go to the next activity
                Intent intent = new Intent(getApplicationContext(), AddSettingActivity.class);
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