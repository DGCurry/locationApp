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
public class GetLocations extends AppCompatActivity {

    //TAG of the class
    static final private String TAG = "GetLocations";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log, TAG, method, action
        Log.d(TAG, "onCreate(Bundle savedInstanceState): started MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log, TAG, method, action
        Log.d(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
//        createUI();
    }

}