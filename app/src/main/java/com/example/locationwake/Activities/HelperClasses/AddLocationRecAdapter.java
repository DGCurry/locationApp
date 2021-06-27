package com.example.locationwake.Activities.HelperClasses;

import android.content.Context;
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

import com.example.locationwake.Logger;
import com.example.locationwake.R;

import java.util.ArrayList;

public class AddLocationRecAdapter extends RecyclerView.Adapter{

    //TAG of the class
    static final private String TAG = "ActiveSettingRecAdapter";

    Context context;

    private Object parent;

    ArrayList<String> settings;

    private String name, setting, latitude, longitude, radius;
    private EditText editName, editLatitude, editLongitude;

    public AddLocationRecAdapter(Context context, Object parent, ArrayList<String> settings) {
        this.context = context;
        this.parent = parent;
        this.settings = settings;
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
        return 3;
    }

    public class nameViewHolder extends RecyclerView.ViewHolder {

        public nameViewHolder(View view) {
            super(view);
            editName = (EditText) view.findViewById(R.id.editText_ad_name_name);
        }

        void bindView(int position) {

        }
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
        }

        void bindView(int position) {
        }
    }

    public class attributeViewHolder extends RecyclerView.ViewHolder {

        public attributeViewHolder(View view) {
            super(view);
        }

        void bindView(int position) {
        }
    }


}
