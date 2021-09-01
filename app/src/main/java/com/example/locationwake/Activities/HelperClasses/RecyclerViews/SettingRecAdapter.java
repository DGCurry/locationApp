package com.example.locationwake.Activities.HelperClasses.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Activities.EditLocationActivities.ChangeAttributeActivities.ChangeDistanceActivity;
import com.example.locationwake.Activities.EditLocationActivities.ChangeAttributeActivities.ChangeNameAttributeActivity;
import com.example.locationwake.Activities.EditLocationActivities.ChangeAttributeActivities.ChangeSettingActivity;
import com.example.locationwake.Activities.EditLocationActivities.ChangeLocationActivities.ChangeLocationActivity;
import com.example.locationwake.Activities.HelperClasses.JSON.AttributeJSONHelper;
import com.example.locationwake.Activities.HelperClasses.JSON.LocationJSONHelper;
import com.example.locationwake.Backend.Database.Attributes.mRadius;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Logger;
import com.example.locationwake.R;
import com.example.locationwake.Backend.Database.Attributes.AttributeInterface;
import com.example.locationwake.Backend.Database.mLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Helper class for a RecyclerView that displays the setting which is active
 */
public class SettingRecAdapter extends RecyclerView.Adapter{

    //TAG of the class
    static final private String TAG = "SettingRecAdapter";

    //List that holds all the different attributes of the location displayed with the Recyclerview
    private List<mAttribute> attributes;
    private mLocation location;

    //Context of the application
    Context context;

    private int attributeCount = 0;
    private int itemCount = 0;
    private final Stack<AttributeInterface> attributeStack;
    private final Map<Integer, Integer> positionAttributeTypeMapping;

    /**
     * Constructor
     * @param list holds all the different attributes of the location
     * @param context context of the application
     */
    public SettingRecAdapter(List<mAttribute> list, mLocation location, Context context) {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        //Clear the previous Recyclerview(if exists) and add the data
        attributes.clear();
        attributes.addAll(list);
        this.location = location;
        this.context = context;

        attributeStack = new Stack<>();
        positionAttributeTypeMapping = new HashMap<>();

        notifyDataSetChanged();
    }


    /**
     * Returns the type of the item contained in the Recyclerview
     * @param position of the item
     * @return the position of the item in the Recyclerview
     */
    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    /**
     * Converts the position of an item to the position in the mAttribute array
     * @param position position of the item in the Recyclerview
     * @return position of the parent mAttribute in the attributes array
     */
    private int positionConverter(int position) {

        Logger.logD(TAG, "positionConverter(): converting " + position);
        int relativePosition = position - 1;
        int attributeC = 0;
        for (mAttribute attribute:attributes) {
            relativePosition -= attribute.getItemCount();
            if (relativePosition < 0) {
                Logger.logD(TAG, "positionConverter(): found position " + attributeC);
                return attributeC;
            }
            attributeC++;
        } return -1;
    }

    /**
     *
     * @param parent View that holds the Recyclerview items
     * @param viewType type of the item contained in the parent View
     * @return a new View that holds the item that is contained in the parent View
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.location_item_expanded, parent, false);
            return new LocationViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.attribute_item_expanded, parent, false);
            return new AttributeViewHolder(itemView);
        }
    }

    /**
     * when the item in bind, this determines what function should be called to bind the position
     * @param holder holder of the item
     * @param position position in the recyclerview
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            Logger.logV(TAG, "onBindViewHolder(): DISTANCE_TYPE added" );
            ((LocationViewHolder) holder).bindView(position);
        } else {
            Logger.logV(TAG, "onBindViewHolder(): ATTRIBUTE_TYPE added" );
            ((AttributeViewHolder) holder).bindView(position);
        }
    }

    /**
     * returns how many attributes are used for the recyclerview
     * @return attributes size if not null
     */
    @Override
    public int getItemCount() {
        return location == null ? 0 : attributes.size() + 1;
    }

    /**
     * innerclass holds the information for the header item
     */
    public class AttributeViewHolder extends RecyclerView.ViewHolder {

        private View v;

        /**
         * constructor of the class. Binds the UI elements
         * @param view
         */
        public AttributeViewHolder(View view) {
            super(view);
            v = view;
        }

        /**
         * binds the item to a certain position
         * @param position position of the item
         */
        void bindView(int position) {
            TextView header = v.findViewById(R.id.page_header);
            header.setText("Attribute");

            TextView title = v.findViewById(R.id.textView_header_title);
            title.setText(attributes.get(position - 1).getName());

            TextView setting = v.findViewById(R.id.subheader_setting);
            setting.setText(attributes.get(position - 1).getSetting().getSetting());

            TextView radius = v.findViewById(R.id.subheader_radius);
            radius.setText(attributes.get(position - 1).getRadius().getRadius());
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
            mapView = (MapView) layout.findViewById(R.id.map_main);

            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                mapView.getMapAsync(this);
            }

        }


        /**
         * binds the item to a certain position
         * @param position position of the item
         */
        void bindView(int position) {
            TextView name = layout.findViewById(R.id.textView_header_title);
            name.setText(location.getName());

            TextView lat = layout.findViewById(R.id.textView_subheader_double_one);
            TextView lng = layout.findViewById(R.id.textView_subheader_double_two);

            lat.setText(location.getLatLng().getLat());
            lng.setText(location.getLatLng().getLng());
        }


        /**
         * Adds a marker and centers the camera on the NamedLocation with the normal map type.
         */
        private void setMapLocation() {
            if (map == null) return;
            // Set the map type back to normal.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(5, 5), 13f));
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            map = googleMap;
            setMapLocation();
        }
//
    }


}
