package com.example.locationwake.Activities.HelperClasses;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Backend.Database.Attributes.mDistance;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import java.util.ArrayList;

public class AddLocationRecAdapter extends RecyclerView.Adapter{

    //TAG of the class
    static final private String TAG = "ActiveSettingRecAdapter";

    Context context;

    private Object parent;

    ArrayList<String> settings, attributes;

    private String setting, attribute;
    private EditText editName, editLatitude, editLongitude, editRadius;

    public AddLocationRecAdapter(Context context, Object parent, ArrayList<String> settings, ArrayList<String> attributes) {
        this.context = context;
        this.parent = parent;
        this.settings = settings;
        this.attributes = attributes;
    }

    @Override
    public int getItemViewType(int position) {
        return position;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ad_name, parent, false);
                return new nameViewHolder(itemView);

            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ad_setting, parent, false);
                return new settingViewHolder(itemView);

            case 2:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ad_location, parent, false);
                return new locationViewHolder(itemView);

            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ad_attribute, parent, false);
                return new attributeViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                Logger.logV(TAG, "onBindViewHolder(): DISTANCE_TYPE added" );
                ((nameViewHolder) holder).bindView(position);
                break;

            case 1:
                Logger.logV(TAG, "onBindViewHolder(): LOCATION_TYPE added" );
                ((settingViewHolder) holder).bindView(position);
                break;

            case 2:
                Logger.logV(TAG, "onBindViewHolder(): SETTING_TYPE added" );
                ((locationViewHolder) holder).bindView(position);
                break;

            case 3:
                Logger.logV(TAG, "onBindViewHolder(): SETTING_TYPE added" );
                ((attributeViewHolder) holder).bindView(position);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class nameViewHolder extends RecyclerView.ViewHolder {

        public nameViewHolder(View view) {
            super(view);
            editName = (EditText) view.findViewById(R.id.editText_ad_name_name);
        }

        void bindView(int position) {

        }
    }

    public mAttribute getAttributes() {
        return new mAttribute(null, null, new mDistance(editRadius.getText().toString()), new mSetting(setting));
    }

    public mLocation getLocation() {
        return new mLocation(editLatitude.getText().toString(), editLongitude.getText().toString(), editName.getText().toString());
    }

    public class settingViewHolder extends RecyclerView.ViewHolder {

        private final Spinner spinnerSetting;

        public settingViewHolder(View view) {
            super(view);
            spinnerSetting = (Spinner) view.findViewById(R.id.spinner_ad_setting_setting);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, settings);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSetting.setAdapter(arrayAdapter);
            spinnerSetting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setting = parent.getItemAtPosition(position).toString();
                }
                @Override
                public void onNothingSelected(AdapterView <?> parent) {
                    setting = settings.get(0);
                }
            });
        }

        void bindView(int position) {
        }
    }

    public class locationViewHolder extends RecyclerView.ViewHolder {

        public locationViewHolder(View view) {
            super(view);
            editLatitude = (EditText) view.findViewById(R.id.editText_ad_location_latitude);
            editLongitude = (EditText) view.findViewById(R.id.editText_ad_location_longitude);
            editRadius = (EditText) view.findViewById(R.id.editText_ad_location_radius);
        }

        void bindView(int position) {
        }
    }

    public class attributeViewHolder extends RecyclerView.ViewHolder {

        private final Spinner spinnerAttributes;

        public attributeViewHolder(View view) {
            super(view);
            spinnerAttributes = (Spinner) view.findViewById(R.id.spinner_ad_attribute_setting);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, attributes);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAttributes.setAdapter(arrayAdapter);
            spinnerAttributes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    attribute = parent.getItemAtPosition(position).toString();
                }
                @Override
                public void onNothingSelected(AdapterView <?> parent) {
                    attribute = attributes.get(0);
                }
            });
        }

        void bindView(int position) {
        }
    }
}
