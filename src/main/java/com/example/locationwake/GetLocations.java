package com.example.locationwake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.locationwake.Database.AttributesDbHelper;
import com.example.locationwake.Database.DataHandler;
import com.example.locationwake.Database.LocationSettingDbHelper;
import com.example.locationwake.Database.mAttribute;
import com.example.locationwake.Database.mLocation;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class GetLocations extends AppCompatActivity {

    //TAG of the class
    static final private String TAG = "GetLocations";

    //DATA ELEMENTS
    //Holds all the data that is in the database
    private ArrayList<mLocation> locationList = new ArrayList<>();
    private ArrayList<mAttribute> attributeList = new ArrayList<>();

    //GUI ELEMENTS
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
     * Loads data from the databases to an ArrayList
     */
    private void loadData() {
        Logger.logV(TAG, "loadData(): loading the data from the database into dataList");
        locationList = DataHandler.loadLocations(getApplicationContext());

    }


    /**
     * Creates the GUI by adding the listeners
     */
    private void createUI() {
        Logger.logV(TAG, "createUI(): creating recyclerView and adding elements into it");
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new LocationViewAdapter(locationList);
        recyclerView.setAdapter(mAdapter);
    }
}