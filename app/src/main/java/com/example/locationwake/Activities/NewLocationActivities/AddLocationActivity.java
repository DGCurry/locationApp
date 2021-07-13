package com.example.locationwake.Activities.NewLocationActivities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Activities.HelperClasses.AddLocationRecAdapter;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Backend.Services.DataEntry;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import java.util.ArrayList;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class AddLocationActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "AddLocationActivity";

    //Holds all the data that is in the database
    private ArrayList<String> settings = new ArrayList<>();
    private ArrayList<String> attributes = new ArrayList<>();

    //GUI ELEMENTS
    private RecyclerView recyclerView;
    private AddLocationRecAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button addButton;

    // Callback used by backend when done
    CallBackActivity callBack = this;

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

        //TODO add real attributes
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
                if (mAdapter.getAttributes().getSetting().isValid()
                        && mAdapter.getAttributes().getDistance().isValid()
                        && mAdapter.getLocation().isValid()) {
                    Logger.logD(TAG, "createUI(): " + mAdapter.getAttributes().getSetting().getSetting() + ", "
                            + mAdapter.getAttributes().getDistance().getDistance() + ", " + mAdapter.getLocation().getLng() + ", "
                            + mAdapter.getLocation().getLat() + ", " + mAdapter.getLocation().getName());
                    DataEntry dataEntry = new DataEntry(callBack, mAdapter.getAttributes(), mAdapter.getLocation(), getApplicationContext());
                    dataEntry.run();
                } else {
                    Logger.logD(TAG, "createUI: input is invalid");
                }
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