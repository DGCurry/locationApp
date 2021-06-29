package com.example.locationwake.Activities.NewLocationActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Activities.HelperClasses.AddLocationRecAdapter;
import com.example.locationwake.Activities.HelperClasses.FormCallBack;
import com.example.locationwake.Backend.Database.Attributes.mDistance;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Backend.Workers.DataEntry.DataEntry;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import java.util.ArrayList;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class AddLocationActivity extends AppCompatActivity implements FormCallBack {

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

    FormCallBack callBack = this;

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
        recyclerView = (RecyclerView) findViewById(R.id.add_location_recyclerview);
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
    public void onSuccess(int position, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Logger.logD(TAG, message);
    }

    @Override
    public void onFailure(int position, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Logger.logD(TAG, message);
    }
}