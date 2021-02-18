package com.example.locationwake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class GetLocations extends AppCompatActivity {

    //TAG of the class
    static final private String TAG = "GetLocations";


    //GUI ELEMENTS
    //adding location
    private TextView nameView;
    private TextView longitudeView;
    private TextView latitudeView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log, TAG, method, action
        Log.d(TAG, "onCreate(Bundle savedInstanceState): started MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        //Log, TAG, method, action
        Log.d(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
    }

    /**
     * Creates the GUI by adding the listeners
     */
    private void createUI() {
        //adding location
        nameView = findViewById(R.id.textViewName);
        longitudeView = findViewById(R.id.textViewLongitude);
        latitudeView = findViewById(R.id.textViewLatitude);
        backButton = findViewById(R.id.buttonBack);

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}