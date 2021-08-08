package com.example.locationwake.Activities.AddNewLocationAttributeActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddLocationActivities.AddNameLocationActivity;
import com.example.locationwake.Activities.HelperClasses.DataClasses.ChooseALData;
import com.example.locationwake.Activities.HelperClasses.DataClasses.MainActivityData;
import com.example.locationwake.Activities.HelperClasses.RecyclerViews.AddAttributeLocationListRecAdapter;
import com.example.locationwake.Activities.HelperClasses.RecyclerViews.MainSettingRecAdapter;
import com.example.locationwake.Activities.ViewLocation.LocationListActivity;
import com.example.locationwake.Backend.Database.Attributes.AttributeInterface;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Logger;
import com.example.locationwake.R;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class ChooseAttributeOrLocation extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "ChooseAttributeOrLocation";

    //DATA ELEMENTS
    private ChooseALData dataHolder;
    private TextView addAttribute, addLocation;
    private Button addLocationB;


    /**
     * Method to start activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): starting activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_att_loc);
    }

    @Override
    protected void onStart() {
        Logger.logV(TAG, "onStart(): starting activity");
        super.onStart();

        addCallBack();

        Logger.logV(TAG, "onStart(): initializing UI ");
        initializeUI();
    }


    @Override
    protected void onResume() {
        Logger.logV(TAG, "onResume(): resuming activity");
        super.onResume();

        Logger.logV(TAG, "onResume(): loading data ");
        dataHolder = new ChooseALData();
        dataHolder.setContext(getApplicationContext());
        dataHolder.run();

        Logger.logV(TAG, "onResume(): started createUI()");
        createUI();
    }

    /**
     * Method to add navigation behaviour to the bottom navigation bar
     */
    private void addNavigation() {

        // Navigation
        Button list = findViewById(R.id.button_navigation_left);
        Button add = findViewById(R.id.button_navigation_middle);
        Button setting = findViewById(R.id.button_navigation_right);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the list button");
                Intent intent = new Intent(getApplicationContext(), LocationListActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the add button");
                Intent intent = new Intent(getApplicationContext(), ChooseAttributeOrLocation.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the setting button");
            }
        });
    }

    /**
     * Creates the GUI by adding the listeners
     */
    private void createUI() {
        Logger.logV(TAG, "createUI(): ");
        addLocationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNameLocationActivity.class);
                startActivity(intent);
            }
        });
        addLocation.setText("Add a new Location");
        addAttribute.setText("Add a new Attribute");
    }

    /**
     * Updates and creates the GUI for the Setting that is currently active, or not
     */
    private void updateUI() {
        if (dataHolder.getLocations().size() > 0) {
            Logger.logD(TAG, "updateUI(): creating RC");
            findViewById(R.id.add_attribute).setVisibility(View.VISIBLE);
            findViewById(R.id.add_attribute).animate();
            populateRC(dataHolder.getLocations());
        }
    }

    private void initializeUI() {
        Logger.logD(TAG, "initializeUI(): initializing");

        addLocation = findViewById(R.id.add_location).findViewById(R.id.textView_header_title);
        addAttribute = findViewById(R.id.add_attribute).findViewById(R.id.textView_header_title);
        addLocationB = findViewById(R.id.button_header_icon);
        addLocationB.setVisibility(View.VISIBLE);
    }

    /**
     * method called when the backend has found a setting to be enabled. The UI is updated
     */
    private void populateRC(ArrayList<mLocation> locations) {
        //GUI ELEMENTS
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_list_location);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new AddAttributeLocationListRecAdapter(locations, getApplicationContext());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCallBack(boolean update, boolean succeeded, boolean failed, char type, String message) {
        Logger.logD(TAG, "mUAW");
        if (update) {
            runOnUiThread(this::updateUI);
        }
    }

}