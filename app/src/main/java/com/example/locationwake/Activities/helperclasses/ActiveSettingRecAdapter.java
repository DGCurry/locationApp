package com.example.locationwake.Activities.helperclasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Logger;
import com.example.locationwake.R;
import com.example.locationwake.backend.Database.Attributes.AttributeInterface;
import com.example.locationwake.backend.Database.Attributes.mDistance;
import com.example.locationwake.backend.Database.Attributes.mLocation;
import com.example.locationwake.backend.Database.Attributes.mSetting;
import com.example.locationwake.backend.Database.mAttribute;

import java.util.ArrayList;
import java.util.List;

public class ActiveSettingRecAdapter extends RecyclerView.Adapter{

    //TAG of the class
    static final private String TAG = "MainActivity";

    private List<AttributeInterface> attributes;


    public ActiveSettingRecAdapter(List<AttributeInterface> list) {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }

        attributes.clear();
        attributes.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return attributes.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case AttributeInterface.DISTANCE_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.distance_item, parent, false);
                return new DistanceViewHolder(itemView);

            case AttributeInterface.SETTING_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.setting_item, parent, false);
                return new SettingViewHolder(itemView);

            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.location_item, parent, false);
                return new LocationViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Logger.logD(TAG, "onBindViewHolder(): " + holder.getClass());
        switch (getItemViewType(position)) {
            case AttributeInterface.DISTANCE_TYPE:
                Logger.logD(TAG, "onBindViewHolder(): distance" );
                ((DistanceViewHolder) holder).bindView(position);
                break;

            case AttributeInterface.LOCATION_TYPE:
                Logger.logD(TAG, "onBindViewHolder(): location" );
                ((LocationViewHolder) holder).bindView(position);
                break;

            case AttributeInterface.SETTING_TYPE:
                Logger.logD(TAG, "onBindViewHolder(): setting" );
                ((SettingViewHolder) holder).bindView(position);
                break;

        }
    }

    @Override
    public int getItemCount() {
        if (attributes == null) {
            return 0;
        }
        return attributes.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView latitude;
        private final TextView longitude;

        //LAYOUT
        public View layout;


        public LocationViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            layout = view;
            title = (TextView) view.findViewById(R.id.item_title);
            latitude = (TextView) view.findViewById(R.id.sub_item_lat);
            longitude = (TextView) view.findViewById(R.id.sub_item_lon);
        }

        void bindView(int position) {
            title.setText("Location");
            latitude.setText(((mLocation)attributes.get(position)).getLat());
            longitude.setText(((mLocation)attributes.get(position)).getLng());
        }
    }

    public class SettingViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView setting;

        //LAYOUT
        public View layout;


        public SettingViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            layout = view;
            title = (TextView) view.findViewById(R.id.item_title);
            setting = (TextView) view.findViewById(R.id.sub_item_setting);
        }

        void bindView(int position) {
            title.setText("Setting");
            setting.setText(((mSetting)attributes.get(position)).getSetting());
        }
    }

    public class DistanceViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView distance;

        //LAYOUT
        public View layout;

        public DistanceViewHolder(View view) {
            super(view);
            // get reference to views
            layout = view;
            title = (TextView) view.findViewById(R.id.item_title);
            distance = (TextView) view.findViewById(R.id.sub_item_dis);        }

        void bindView(int position) {
            title.setText("Distance");
            distance.setText(((mDistance)attributes.get(position)).getDistance());
        }
    }

}
