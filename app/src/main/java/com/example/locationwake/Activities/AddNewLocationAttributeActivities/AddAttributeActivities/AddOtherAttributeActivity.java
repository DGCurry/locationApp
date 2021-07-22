package com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddAttributeActivities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationwake.Logger;
import com.example.locationwake.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class AddOtherAttributeActivity extends AppCompatActivity {

    //TAG of the class
    static final private String TAG = "AddLocationActivity";

    //Holds all the data that is in the database
    private ArrayList<String> settings = new ArrayList<>();
    private ArrayList<String> attributes = new ArrayList<>();

    private JSONObject data = new JSONObject();

    private boolean locationExist = false;

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

        loadData();

        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
    }

    /**
     * Method to load the data send by other activities, and populates the attributes and settings lists
     */
    private void loadData() {
        Logger.logV(TAG, "loadData(): loading the data from the database into dataList");
        settings = new ArrayList<>();
        settings.add("SLT");
        settings.add("VBR");
        settings.add("SND");

        //TODO add real attributes
        attributes = new ArrayList<>();
        attributes.add("SLT");
        attributes.add("VBR");
        attributes.add("SND");

        Logger.logV(TAG, "loadData(): getting data from AddNameActivity");
        if (getIntent().hasExtra("input")) {
            try {
                data = new JSONObject(getIntent().getStringExtra("input"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (getIntent().hasExtra("locationExist")) {
            locationExist = getIntent().getBooleanExtra("locationExist", false);
        }
    }


    /**
     * Creates the GUI by adding the listeners
     */
    protected void createUI() {
//        Logger.logV(TAG, "createUI(): creating recyclerView and adding elements into it");
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_add_location);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        mAdapter = new AddLocationRecAdapter(this.getApplicationContext(), this, settings, attributes);
//        recyclerView.setAdapter(mAdapter);

//        addButton = findViewById(R.id.button_add_entry);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logger.logD(TAG, "createUI(): clicked on Send Button");
//
//                mAttribute addAttribute = mAdapter.getAttributes();
//                mLocation addLocation = mAdapter.getLocation();
//
//                if (!addAttribute.getSetting().isValid()) {
//                    Logger.logE(TAG, "createUI(): onClick(): SETTING is invalid");
//                    Toast.makeText(getApplicationContext(), "The item Setting is invalid", Toast.LENGTH_LONG).show();
//                } else if (!addAttribute.getDistance().isValid()) {
//                    Logger.logE(TAG, "createUI(): onClick(): DISTANCE is invalid");
//                    Toast.makeText(getApplicationContext(), "The item Distance is invalid", Toast.LENGTH_LONG).show();
//                } else if (!addLocation.isValid()) {
//                    Logger.logE(TAG, "createUI(): onClick(): LOCATION is invalid");
//                    Toast.makeText(getApplicationContext(), "The item Location is invalid", Toast.LENGTH_LONG).show();
//                } else {
//                    Logger.logD(TAG, "createUI(): " + mAdapter.getAttributes().getSetting().getSetting() + ", "
//                            + mAdapter.getAttributes().getDistance().getDistance() + ", " + mAdapter.getLocation().getLng() + ", "
//                            + mAdapter.getLocation().getLat() + ", " + mAdapter.getLocation().getName());
//                    DataEntry dataEntry = new DataEntry(callBack, mAdapter.getAttributes(), mAdapter.getLocation(), getApplicationContext());
//                    dataEntry.run();
//                }
//            }
//        });
    }

}