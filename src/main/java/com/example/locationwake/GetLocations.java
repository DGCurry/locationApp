package com.example.locationwake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.locationwake.Database.LocationSettingDbHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class GetLocations extends AppCompatActivity {

    //TAG of the class
    static final private String TAG = "GetLocations";

    //Holds all the data that is in the database
    private ArrayList<String[]> dataList = new ArrayList<>();

    //GUI ELEMENTS
    //viewing location
    private TextView IDView;
    private TextView nameView;
    private TextView latitudeView;
    private TextView longitudeView;
    private Button previousButton;
    private Button nextButton;

    //going back to previous activity
    private Button backButton;

    /**
     * Method to start activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started loadData()");
        loadData();
        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
    }

    /**
     * Loads data from the database to a arraylist
     */
    private void loadData() {
        Logger.logV(TAG, "loadData(): loading the data from the database into dataList");
        //get the database
        LocationSettingDbHelper settingDbHelper = new LocationSettingDbHelper(getApplicationContext());
        //set the data we have in the correct lists
        dataList = settingDbHelper.getLocation();
    }


    /**
     * Creates the GUI by adding the listeners
     */
    private void createUI() {
        //adding location
        Logger.logV(TAG, "createUI(): getting the UI elements");
        IDView = findViewById(R.id.textViewID);
        nameView = findViewById(R.id.textViewName);
        latitudeView = findViewById(R.id.textViewLatitude);
        longitudeView = findViewById(R.id.textViewLongitude);
        backButton = findViewById(R.id.buttonBack);
        previousButton = findViewById(R.id.buttonPrevious);
        nextButton = findViewById(R.id.buttonNext);

        //Iterator for debugger
        ListIterator<String[]> iterator = dataList.listIterator();

        try {
            for (String[] dataEntry:dataList) {
                Logger.logV(TAG, "createUI(): Got data\n " +
                        dataEntry[0] + "\n" +
                        dataEntry[1] + "\n" +
                        dataEntry[2] + "\n" +
                        dataEntry[3]
                );
            }

            IDView.setText(dataList.get(0)[0]);
            nameView.setText(dataList.get(0)[1]);
            latitudeView.setText(dataList.get(0)[2]);
            longitudeView.setText(dataList.get(0)[3]);
        } catch(Exception e) {
            Logger.logE(TAG, "createUI(): Oh no... an error occurred; \n" + e);
        }

        Logger.logV(TAG, "createUI(): setting onClickListeners on the buttons");
        //going back to previous activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logV(TAG, "createUI(): onClick(View v): clicked the back button, going back to the previous activity");
                finish();
            }
        });

        //Getting previous location with information, if possible
        previousButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Logger.logV(TAG, "createUI(): onClick(View v): clicked the previous button, getting the previous data entry");
                if (!iterator.hasPrevious()) {
                    Logger.logD(TAG, "createUI(): onClick(View v): no previous element in data iterator");
                    return;
                }
                String[] dataEntry = iterator.previous();
                IDView.setText(dataEntry[0]);
                nameView.setText(dataEntry[1]);
                latitudeView.setText(dataEntry[2]);
                longitudeView.setText(dataEntry[3]);
            }
        });

        //Getting next location with information, if possible
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Logger.logV(TAG, "createUI(): onClick(View v): clicked the next button, getting the next data entry");
                if (!iterator.hasNext()) {
                    Logger.logD(TAG, "createUI(): onClick(View v): no next element in data iterator");
                    return;
                }
                String[] dataEntry = iterator.next();
                IDView.setText(dataEntry[0]);
                nameView.setText(dataEntry[1]);
                latitudeView.setText(dataEntry[2]);
                longitudeView.setText(dataEntry[3]);
            }
        });
    }


}