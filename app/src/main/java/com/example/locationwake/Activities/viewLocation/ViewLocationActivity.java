package com.example.locationwake.Activities.viewLocation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Activities.HelperClasses.SettingRecAdapter;
import com.example.locationwake.Backend.Database.Attributes.AttributeInterface;
import com.example.locationwake.Backend.Database.Attributes.mDistance;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class ViewLocationActivity extends AppCompatActivity {

    //TAG of the class
    static final private String TAG = "settingactivity";

    //DATA ELEMENTS
    //Holds all the data that is in the database
    private List<List<AttributeInterface>> attributes = new ArrayList<>();
    private mLocation location;
    List<String> names;

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
        setContentView(R.layout.activity_view_location);
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

        attributes = new ArrayList<>();
        location = new mLocation("1", "HOME", "1", "2");
        attributes.add();
    }


    /**
     * Creates the GUI by adding the listeners
     */
    private void createUI() {
        Logger.logV(TAG, "createUI(): creating recyclerView and adding elements into it");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_view_location);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SettingRecAdapter(attributes, location, getApplicationContext());
        recyclerView.setAdapter(mAdapter);
    }
}