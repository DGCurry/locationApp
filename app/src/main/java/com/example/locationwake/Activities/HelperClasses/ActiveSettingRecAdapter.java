package com.example.locationwake.Activities.HelperClasses;

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
import com.example.locationwake.Backend.Database.Attributes.AttributeInterface;
import com.example.locationwake.Backend.Database.Attributes.mDistance;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ActiveSettingRecAdapter extends RecyclerView.Adapter{

    //TAG of the class
    static final private String TAG = "ActiveSettingRecAdapter";

    private List<AttributeInterface> attributes;
    private String name;

    Context context;

    /**
     * Constructor
     * @param list
     * @param context
     * @param name
     */
    public ActiveSettingRecAdapter(List<AttributeInterface> list, Context context, String name) {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }

        attributes.clear();
        attributes.addAll(list);
        notifyDataSetChanged();

        this.context = context;

        this.name = name;
    }

    /**
     * Returns the type of the item
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return attributes.get(position).getType();
    }

    /**
     * 
     * @param parent
     * @param viewType
     * @return
     */
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

    /**
     * when the item in bind, this determines what function should be called to bind the position
     * @param holder holder of the item
     * @param position position in the recyclerview
     */
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

    /**
     * returns how many attributes are used for the recyclerview
     * @return attributes size if not null
     */
    @Override
    public int getItemCount() {
        if (attributes == null) {
            return 0;
        }
        return attributes.size();
    }

    /**
     * innerclass holds the information for the header item
     */
    public class headerViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView setting;
        private final Button changeButton;

        /**
         * constructor of the class. Binds the UI elements
         * @param view
         */
        public headerViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView_header_name);
            setting = (TextView) view.findViewById(R.id.textView_header_setting);
            changeButton = (Button) view.findViewById(R.id.button_header_change);
        }

        /**
         * binds the item to a certain position
         * @param position position of the item
         */
        void bindView(int position) {
            title.setText(name);
            setting.setText(((mSetting)attributes.get(position)).getSetting());
        }
    }

    /**
     * innerclass holds the information in the location item
     */
    public class LocationViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        MapView mapView;
        GoogleMap map;
        View layout;

        /**
         * constructor of the class. Binds the UI elements
         * @param view
         */
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

        /**
         * binds the item to a certain position
         * @param position position of the item
         */
        void bindView(int position) {
            mLocation location = (mLocation)attributes.get(position);
            // Store a reference of the ViewHolder object in the layout.
            layout.setTag(this);
            // Store a reference to the item in the mapView's tag. We use it to get the
            // coordinate of a location, when setting the map location.
            mapView.setTag(location);
            setMapLocation();
        }

        /**
         * callback function called when the map is ready to be displayed
         * @param googleMap
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            map = googleMap;
            setMapLocation();
        }

        /**
         * function used to set the map to a certain location, and to add a marker.
         */
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

    /**
     * innerclass holds the information in the radius item
     */
    public class DistanceViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView distance;

        /**
         * constructor of the class. Binds the UI elements
         * @param view
         */
        public DistanceViewHolder(View view) {
            super(view);
            // get reference to views
            title = (TextView) view.findViewById(R.id.textView_distance_title);
            distance = (TextView) view.findViewById(R.id.textView_distance_radius);
        }

        /**
         * binds the item to a certain position
         * @param position position of the item
         */
        void bindView(int position) {
        }

    }

}
