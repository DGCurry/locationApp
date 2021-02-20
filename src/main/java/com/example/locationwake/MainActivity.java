package com.example.locationwake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.locationwake.Database.DataHandler;

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
    private EditText distanceEdit;
    private EditText settingEdit;
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
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
    }

    /**
     * Creates the GUI by adding the listeners
     */
    private void createUI() {
        Logger.logV(TAG, "createUI(): getting the UI elements");
        //adding location
        nameEdit = findViewById(R.id.editName);
        longitudeEdit = findViewById(R.id.editLongitude);
        latitudeEdit = findViewById(R.id.editLatitude);
        distanceEdit = findViewById(R.id.editDistance);
        settingEdit = findViewById(R.id.editSetting);

        Logger.logV(TAG, "createUI(): setting onClickListeners on the buttons");

        addButton = findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Log, TAG, method, action
                Logger.logV(TAG, "createUI(): onClick(View v): Clicked on addButton");
                //get the text that has been added
                //TODO: can not be done if there is no text or if the text is already in the database
                DataHandler.addData(nameEdit.getText().toString(),
                        latitudeEdit.getText().toString(),
                        longitudeEdit.getText().toString(),
                        distanceEdit.getText().toString(),
                        settingEdit.getText().toString(),
                        getApplicationContext());
            }
        });

        //activity finding location
        goButton = findViewById(R.id.buttonFind);
        goButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Log, TAG, method, action
                Logger.logV(TAG, "createUI(): onClick(View v): Clicked on goButton");
                //create Intent
                Intent intent = new Intent(MainActivity.this, GetLocations.class);
                startActivity(intent);
            }
        });
    }
}