package com.example.locationwake.Activities.ViewLocation;

import android.os.Bundle;
import android.widget.HorizontalScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Activities.HelperClasses.RecyclerViews.CustomItemDecoration.LocationListDividerLine;
import com.example.locationwake.Activities.HelperClasses.RecyclerViews.SettingRecAdapter;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class ViewLocationActivity extends AppCompatActivity {

    //TAG of the class
    static final private String TAG = "ViewLocationActivity";

    //DATA ELEMENTS
    //Holds all the data that is in the database
    private List<mAttribute> attributes = new ArrayList<>();
    private mLocation location;

    JSONObject data;

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

        if (getIntent().hasExtra("data")) {
            try {
                data = new JSONObject(getIntent().getStringExtra("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        attributes = new ArrayList<>();

        try {
            String LID = data.get("LID").toString();
            List<mAttribute> attributesList = DataHandler.loadAttributes(LID, getApplicationContext());
            attributes.addAll(attributesList);

            location = DataHandler.loadLocation(LID, getApplicationContext());

            Logger.logD(TAG, "loadData(): attributes have been loaded in, location is assigned");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (attributes.isEmpty() || location == null) {
            Logger.logE(TAG, "loadData(): nothing has been found");
        }

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

        recyclerView.addItemDecoration(new LocationListDividerLine(getApplicationContext(), R.drawable.drawable_divider));
        mAdapter = new SettingRecAdapter(attributes, location, getApplicationContext());
        recyclerView.setAdapter(mAdapter);
    }
}