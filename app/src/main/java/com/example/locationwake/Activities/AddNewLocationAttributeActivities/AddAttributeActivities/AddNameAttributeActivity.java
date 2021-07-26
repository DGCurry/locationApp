package com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddAttributeActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationwake.Logger;
import com.example.locationwake.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a test class to test all func. The GUI will be added later on.
 */
public class AddNameAttributeActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_add_name_location);

        loadData();

        //Log, TAG, method, action
        Logger.logV(TAG, "onCreate(Bundle savedInstanceState): started createUI()");
        createUI();
    }

    /**
     * Method to load the data send by other activities
     */
    private void loadData() {
        Logger.logV(TAG, "loadData(): getting data from AddNameActivity");
        if (getIntent().hasExtra("data")) {
            try {
                data = new JSONObject(getIntent().getStringExtra("data"));
            } catch (JSONException e) {
                Logger.logE(TAG, "loadData(): could not load the data");
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates the GUI by adding the listeners
     */
    protected void createUI() {
        Logger.logV(TAG, "createUI(): creating recyclerView and adding elements into it");
        TextView title = findViewById(R.id.textView_location_title_main);
        TextView subTitle = findViewById(R.id.textView_setting_title_main);
        subTitle.setVisibility(View.VISIBLE);

        EditText name = findViewById(R.id.editText_ad_name_name);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                subTitle.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        try {
            title.setText(data.get("locationName").toString());
        } catch (JSONException e) {
            title.setText("None");
            e.printStackTrace();
        }

        addNavigation(name);
    }

    /**
     * Method to add navigation behaviour to the bottom navigation bar
     * @param name EditText that holds the input given by the user
     */
    private void addNavigation(EditText name) {
        // Navigation
        Button back = findViewById(R.id.button_navigation_left);
        Button stop = findViewById(R.id.button_navigation_middle);
        Button send = findViewById(R.id.button_navigation_right);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the back button");
                finish();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the stop button");
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.logD(TAG, "onClick(): clicked on the send button");
                try {
                    data.put("attributeName", name.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), AddSettingActivity.class);
                intent.putExtra("data", data.toString());
                startActivity(intent);
                finish();
            }
        });
    }

}