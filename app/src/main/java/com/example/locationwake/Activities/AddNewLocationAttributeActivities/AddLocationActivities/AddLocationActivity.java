package com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddLocationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddAttributeActivities.AddNameAttributeActivity;
import com.example.locationwake.Backend.Behaviour.Components.LocationComponent;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                //check the input data
                if (!new mLocation(null, null, latitudeInput.getText().toString(),
                        longitudeInput.getText().toString()).isValid()) {
                    Logger.logE(TAG, "createUI(): onClick(): LOCATION is invalid");
                    Toast.makeText(getApplicationContext(), "The item Location is invalid", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    data.put("latitude", latitudeInput.getText().toString());
                    data.put("longitude", longitudeInput.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Runnable checkLocationEntry = new CheckLocationEntry(
                        getApplicationContext(),
                        latitudeInput.getText().toString(),
                        longitudeInput.getText().toString(),
                        new mRadius("500"));
                checkLocationEntry.run();
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