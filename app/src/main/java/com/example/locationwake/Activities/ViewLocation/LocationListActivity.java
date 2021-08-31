package com.example.locationwake.Activities.ViewLocation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Activities.HelperClasses.RecyclerViews.AddAttributeLocationListRecAdapter;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mLocation;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import java.util.ArrayList;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class LocationListActivity extends AppCompatActivity {

    //TAG of the class
    static final private String TAG = "LocationListActivity";

    //DATA ELEMENTS
    //Holds all the data that is in the database
    private ArrayList<mLocation> list = new ArrayList<>();

    //GUI ELEMENTS
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Method to start activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
    }

    @Override
    protected void onResume() {
        Logger.logV(TAG, "onResume(): resuming activity");
        super.onResume();
        loadData();
        createUI();
    }
    /**
     * Loads data from the databases to an ArrayList
     */
    private void loadData() {
        Logger.logV(TAG, "loadData(): loading the data from the database into dataList");
        list = new ArrayList<>();
        ArrayList<mLocation> locations = DataHandler.loadLocations(getApplicationContext());
        list.addAll(locations);
    }


    /**
     * Creates the GUI by adding the listeners
     */
    private void createUI() {
        Logger.logV(TAG, "createUI(): creating recyclerView and adding elements into it");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_list_location);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AddAttributeLocationListRecAdapter(list);
        recyclerView.setAdapter(mAdapter);
    }
}