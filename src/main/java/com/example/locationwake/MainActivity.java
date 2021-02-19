package com.example.locationwake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.locationwake.Database.LocationSettingDbHelper;
import com.example.locationwake.Database.mLocation;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class MainActivity extends AppCompatActivity {

    //TAG of the class
    static final private String TAG = "MainActivity";

    //GUI ELEMENTS
    //adding location
    private EditText nameEdit;
    private EditText longitudeEdit;
    private EditText latitudeEdit;
    private Button addButton;

    //activity finding location
    private Button goButton;


    /**
     * Method to start activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log, TAG, method, action
        Log.v(TAG, "onCreate(Bundle savedInstanceState): started MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log, TAG, method, action
        Log.v(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
    }

    /**
     * Creates the GUI by adding the listeners
     */
    private void createUI() {
        Log.v(TAG, "createUI(): getting the UI elements");
        //adding location
        nameEdit = findViewById(R.id.editName);
        longitudeEdit = findViewById(R.id.editLongitude);
        latitudeEdit = findViewById(R.id.editLatitude);

        Log.v(TAG, "createUI(): setting onClickListeners on the buttons");

        addButton = findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Log, TAG, method, action
                Log.d(TAG, "createUI(): onClick(View v): Clicked on addButton");
                //get the text that has been added
                //TODO: can not be done if there is no text or if the text is already in the database
                String nameString = nameEdit.getText().toString();
                String latitudeString = latitudeEdit.getText().toString();
                String longitudeString = longitudeEdit.getText().toString();

                Log.d(TAG, "createUI(): onClick(View v): Got Strings latitudeString = " + latitudeString);
                Log.d(TAG, "createUI(): onClick(View v): Got Strings longitudeString = " + longitudeString);

                Log.v(TAG, "createUI(): onClick(View v): Adding entry to database");

                //Adding data to database
                mLocation locationEntry = new mLocation();
                locationEntry.setName(nameString);
                locationEntry.setLat(latitudeString);
                locationEntry.setLng(longitudeString);
                locationEntry.setDistance(null);
                locationEntry.setSetting(null);
                LocationSettingDbHelper locationSettingDbHelper = new LocationSettingDbHelper(getApplicationContext());
                locationSettingDbHelper.addLocation(locationEntry);
            }
        });

        //activity finding location
        goButton = findViewById(R.id.buttonFind);
        goButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Log, TAG, method, action
                Log.v(TAG, "createUI(): onClick(View v): Clicked on goButton");
                //create Intent
                Intent intent = new Intent(MainActivity.this, GetLocations.class);
                startActivity(intent);
            }
        });
    }
}