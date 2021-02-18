package com.example.locationwake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log, TAG, method, action
        Log.d(TAG, "onCreate(Bundle savedInstanceState): started MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log, TAG, method, action
        Log.d(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
    }

    /**
     * Creates the GUI by adding the listeners
     */
    private void createUI() {
        //adding location
        nameEdit = findViewById(R.id.editName);
        longitudeEdit = findViewById(R.id.editLongitude);
        latitudeEdit = findViewById(R.id.editLatitude);
        addButton = findViewById(R.id.buttonAdd);

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Log, TAG, method, action
                Log.d(TAG, "createUI(): Clicked on addButton");
                //get the text that has been added
                //TODO: can not be done if there is no text or if the text is already in the database
                String longitudeString = longitudeEdit.getText().toString();
                String latitudeString = latitudeEdit.getText().toString();

                Log.d(TAG, "createUI(): Got Strings longitudeString = " + longitudeString);
                Log.d(TAG, "createUI(): Got Strings latitudeString = " + latitudeString);
            }
        });
        //activity finding location
        goButton = findViewById(R.id.buttonFind);
    }
}