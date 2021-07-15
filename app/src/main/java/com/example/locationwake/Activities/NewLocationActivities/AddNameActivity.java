package com.example.locationwake.Activities.NewLocationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Activities.HelperClasses.AddLocationRecAdapter;
import com.example.locationwake.Activities.HelperClasses.NewLocationJSONHelper;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class AddNameActivity extends CallBackActivity {

    //TAG of the class
    static final private String TAG = "AddNameActivity";

    private JSONObject data = new JSONObject();
    /**
     * Method to start activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started AddLocationActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);

        if (getIntent().hasExtra("input")) {
            try {
                data = new JSONObject(getIntent().getStringExtra("input"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
    }

    /**
     * Creates the GUI by adding the listeners
     */
    protected void createUI() {
        Logger.logV(TAG, "createUI(): creating recyclerView and adding elements into it");
        EditText name = findViewById(R.id.editText_ad_name_name);

        Button button = findViewById(R.id.button_ad_name_input);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    data.put("name", name.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), AddLocationActivity.class);
                intent.putExtra("input", data.toString());
                startActivity(intent);
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