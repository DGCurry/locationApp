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
    private EditText nameEdit = findViewById(R.id.editName);
    private EditText longitudeEdit = findViewById(R.id.editLongitude);
    private EditText latitudeEdit = findViewById(R.id.editLatitude);
    private Button addButton = findViewById(R.id.buttonAdd);

    //activity finding location
    private Button goButton = findViewById(R.id.buttonFind);


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
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Log, TAG, method, action
                Log.d(TAG, "createUI(): Clicked on addButton");
                //get the text that has been added
                //TODO: can not be done if there is no text or if the text is already in the database
                String longitudeString = longitudeEdit.toString();
                String latitudeString = latitudeEdit.toString();
            }
        });

    }
}