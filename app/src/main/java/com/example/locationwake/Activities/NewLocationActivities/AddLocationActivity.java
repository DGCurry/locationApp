package com.example.locationwake.Activities.NewLocationActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Activities.ExtendedActivities.CallBackActivity;
import com.example.locationwake.Activities.HelperClasses.AddLocationRecAdapter;
import com.example.locationwake.Backend.Workers.DataEntry.DataEntry;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import java.util.ArrayList;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class AddLocationActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "settingactivity";

    //DATA ELEMENTS
    //Holds all the data that is in the database
    private ArrayList<String> settings = new ArrayList<>();
    private ArrayList<String> attributes = new ArrayList<>();

    //GUI ELEMENTS
    private RecyclerView recyclerView;
    private AddLocationRecAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    CallBackActivity callBack = this;

    private Button addButton;

    /**
     * Method to start activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started loadData()");
        loadData();
        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
    }

    /**
     * Loads data from the databases to an ArrayList
     */
    private void loadData() {
        Logger.logV(TAG, "loadData(): loading the data from the database into dataList");
        settings = new ArrayList<>();
        settings.add("SLT");
        settings.add("VBR");
        settings.add("SND");

        attributes = new ArrayList<>();
        attributes.add("SLT");
        attributes.add("VBR");
        attributes.add("SND");
    }


    /**
     * Creates the GUI by adding the listeners
     */
    private void createUI() {
        Logger.logV(TAG, "createUI(): creating recyclerView and adding elements into it");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_add_location);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AddLocationRecAdapter(this.getApplicationContext(), this, settings, attributes);
        recyclerView.setAdapter(mAdapter);

        addButton = findViewById(R.id.button_add_entry);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "createUI(): clicked on Send Button");
                DataEntry dataEntry = new DataEntry(callBack, mAdapter.getAttributes(), mAdapter.getLocation(), getApplicationContext());
                dataEntry.run();
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