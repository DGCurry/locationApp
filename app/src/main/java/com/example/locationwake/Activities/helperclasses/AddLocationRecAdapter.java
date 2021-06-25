package com.example.locationwake.Activities.helperclasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Logger;
import com.example.locationwake.R;
import com.example.locationwake.backend.Database.Attributes.AttributeInterface;
import com.example.locationwake.backend.Database.Attributes.mDistance;
import com.example.locationwake.backend.Database.Attributes.mLocation;
import com.example.locationwake.backend.Database.Attributes.mSetting;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class AddLocationRecAdapter extends RecyclerView.Adapter{

    //TAG of the class
    static final private String TAG = "ActiveSettingRecAdapter";

    private List<AttributeInterface> attributes;
    private String name;

    Context context;

    public AddLocationRecAdapter(List<AttributeInterface> list, Context context, String name) {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }

        attributes.clear();
        attributes.addAll(list);
        notifyDataSetChanged();

        this.context = context;

        this.name = name;
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
                        .inflate(R.layout.as_distance_item, parent, false);
                return new DistanceViewHolder(itemView);

            case AttributeInterface.SETTING_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.as_setting_header, parent, false);
                return new headerViewHolder(itemView);

            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.as_location_item, parent, false);
                return new LocationViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case AttributeInterface.DISTANCE_TYPE:
                Logger.logV(TAG, "onBindViewHolder(): DISTANCE_TYPE added" );
                ((DistanceViewHolder) holder).bindView(position);
                break;

            case AttributeInterface.LOCATION_TYPE:
                Logger.logV(TAG, "onBindViewHolder(): LOCATION_TYPE added" );
                ((LocationViewHolder) holder).bindView(position);
                break;

            case AttributeInterface.SETTING_TYPE:
                Logger.logV(TAG, "onBindViewHolder(): SETTING_TYPE added" );
                ((headerViewHolder) holder).bindView(position);
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

    public class headerViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView setting;
        private final Button changeButton;

        public headerViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView_header_name);
            setting = (TextView) view.findViewById(R.id.textView_header_setting);
            changeButton = (Button) view.findViewById(R.id.button_header_change);
        }

        void bindView(int position) {
            title.setText(name);
            setting.setText(((mSetting)attributes.get(position)).getSetting());
        }
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        MapView mapView;
        GoogleMap map;
        View layout;

        public LocationViewHolder(View view) {
            super(view);
            layout = view;
            mapView = layout.findViewById(R.id.lite_listrow_map);
            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);
            }

        }

        void bindView(int position) {
            mLocation location = (mLocation)attributes.get(position);
            // Store a reference of the ViewHolder object in the layout.
            layout.setTag(this);
            // Store a reference to the item in the mapView's tag. We use it to get the
            // coordinate of a location, when setting the map location.
            mapView.setTag(location);
            setMapLocation();
        }

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            map = googleMap;
            setMapLocation();
        }

        private void setMapLocation() {
            if (map == null) return;
            mLocation data = (mLocation) mapView.getTag();
            if (data == null) return;

            LatLng latlng = new LatLng(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLng()));


            // Add a marker for this item and set the camera
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13f));
            map.addMarker(new MarkerOptions().position(latlng));

            // Set the map type back to normal.
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

    }

    public class DistanceViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView distance;

        public DistanceViewHolder(View view) {
            super(view);
            // get reference to views
            title = (TextView) view.findViewById(R.id.textView_distance_title);
            distance = (TextView) view.findViewById(R.id.textView_distance_radius);
        }

        void bindView(int position) {
            title.setText("Distance");
            distance.setText(((mDistance)attributes.get(position)).getDistance() + " meters");
        }

    }

}