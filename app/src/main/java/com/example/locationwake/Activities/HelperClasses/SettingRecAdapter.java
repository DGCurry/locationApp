package com.example.locationwake.Activities.HelperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Backend.Database.Attributes.mDistance;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Logger;
import com.example.locationwake.R;
import com.example.locationwake.Backend.Database.Attributes.AttributeInterface;
import com.example.locationwake.Backend.Database.Attributes.mLocation;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Helper class for a RecyclerView that displays the setting which is active
 */
public class SettingRecAdapter extends RecyclerView.Adapter{

    //TAG of the class
    static final private String TAG = "SettingRecAdapter";

    //List that holds all the different attributes of the location displayed with the Recyclerview
    private List<List<AttributeInterface>> attributes;
    private List<String> names;
    private mLocation location;

    //Context of the application
    Context context;

    private int attributeCount = 0;
    private int itemCount = 0;

    /**
     * Constructor
     * @param list holds all the different attributes of the location
     * @param context context of the application
     */
    public SettingRecAdapter(List<List<AttributeInterface>> list, mLocation location, Context context) {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }

        //Clear the previous Recyclerview(if exists) and add the data
        attributes.clear();
        attributes.addAll(list);
        this.location = location;
        this.context = context;

        notifyDataSetChanged();
    }

    /**
     * Returns the type of the item contained in the Recyclerview
     * @param position of the item
     * @return the position of the item in the Recyclerview
     */
    @Override
    public int getItemViewType(int position) {
        Logger.logD(TAG, "getItemViewType(): position is " + position);
        if (position == 0) {
            Logger.logD(TAG, "getItemViewType(): first position, is the Location");
            return AttributeInterface.LOCATION_TYPE;
        }

        if (attributeCount == attributes.size()) {
            Logger.logE(TAG, "getItemViewType(): Something is wrong");
            return -1;
        }

        //Check if all items have been dealt with for the attributes list
        if (attributes.get(attributeCount).size() == itemCount) {
            Logger.logD(TAG, "getItemViewType(): all attributes for the list has been dealt " +
                    "with, going to the next attributes list");
            itemCount = 0;
            attributeCount++;
        }

        Logger.logD(TAG, "getItemViewType(): attributeCount " + attributeCount);

        if (itemCount == 0) {
            Logger.logD(TAG, "getItemViewType(): attribute name type");
            itemCount++;
            return AttributeInterface.NAME_TYPE;
        }

        // Keep popping the stack for new types
        itemCount++;
        Logger.logD(TAG, "getItemViewType(): type is " + attributes.get(attributeCount).get(itemCount).getType());
        return attributes.get(attributeCount).get(itemCount).getType();
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
        for (List<AttributeInterface> attributesList:attributes) {
            relativePosition -= attributesList.size();
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
        switch (viewType) {
            case AttributeInterface.DISTANCE_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.distance_item, parent, false);
                return new DistanceViewHolder(itemView);

            case AttributeInterface.SETTING_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.setting_item, parent, false);
                return new SettingViewHolder(itemView);

            case AttributeInterface.LOCATION_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.location_item, parent, false);
                return new LocationViewHolder(itemView);

            //name
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header_icon, parent, false);
                return new NameViewHolder(itemView);


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
                ((SettingViewHolder) holder).bindView(position);
                break;

            //name
            default:
                Logger.logV(TAG, "onBindViewHolder(): NAME_TYPE added" );
                ((NameViewHolder) holder).bindView(position);
                break;

        }
    }

    /**
     * returns how many attributes are used for the recyclerview
     * @return attributes size if not null
     */
    @Override
    public int getItemCount() {
        if (location == null) {
            Logger.logD(TAG, "getItemCount(): found " + 0 + " items");
            return 0;
        }
        // The first item is for mLocation, c = 1
        int c = 1;
        if (attributes == null) {
            Logger.logD(TAG, "getItemCount(): found " + c + " items");
            return c;
        }
        // get the amount of attributes per mAttribute that is not null or set
        for (List<AttributeInterface> attributeList:attributes) {
            c += attributeList.size();
        }
        Logger.logD(TAG, "getItemCount(): found " + c + " items");
        return c;
    }

    /**
     * innerclass holds the information for the header item
     */
    public class SettingViewHolder extends RecyclerView.ViewHolder {

        private final TextView setting;

        /**
         * constructor of the class. Binds the UI elements
         * @param view
         */
        public SettingViewHolder(View view) {
            super(view);
            setting = (TextView) view.findViewById(R.id.textView_header_setting);
        }

        /**
         * binds the item to a certain position
         * @param position position of the item
         */
        void bindView(int position) {
            int attributeArrayPosition = positionConverter(position);
            mSetting settingObject = (mSetting)attributes.get(attributeArrayPosition).get(1);
            setting.setText(settingObject.getSetting());
        }
    }

    /**
     * innerclass holds the information in the location item
     */
    public class LocationViewHolder extends RecyclerView.ViewHolder {

//        MapView mapView;
//        GoogleMap map;
        View layout;

        /**
         * constructor of the class. Binds the UI elements
         * @param view
         */
        public LocationViewHolder(View view) {
            super(view);
            layout = view;
//            mapView = layout.findViewById(R.id.lite_listrow_map);
//            if (mapView != null) {
                // Initialise the MapView
//                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
//                mapView.getMapAsync(this);
            }

        /**
         * binds the item to a certain position
         * @param position position of the item
         */
        void bindView(int position) {
            TextView latitude = layout.findViewById(R.id.textView_main_location_latitude);
            TextView longitude = layout.findViewById(R.id.textView_main_location_longitude);
            latitude.setText(location.getLat());
            longitude.setText(location.getLng());

            // Store a reference of the ViewHolder object in the layout.
//            layout.setTag(this);
            // Store a reference to the item in the mapView's tag. We use it to get the
            // coordinate of a location, when setting the map location.
//            mapView.setTag(location);
//            setMapLocation();
        }
//
//        /**
//         * callback function called when the map is ready to be displayed
//         * @param googleMap
//         */
//        @Override
//        public void onMapReady(@NonNull GoogleMap googleMap) {
//            MapsInitializer.initialize(context);
//            map = googleMap;
//            setMapLocation();
//        }
//
//        /**
//         * function used to set the map to a certain location, and to add a marker.
//         */
//        private void setMapLocation() {
//            if (map == null) return;
//            mLocation data = (mLocation) mapView.getTag();
//            if (data == null) return;
//
//            LatLng latlng = new LatLng(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLng()));
//
//
//            // Add a marker for this item and set the camera
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13f));
//            map.addMarker(new MarkerOptions().position(latlng));
//
//            // Set the map type back to normal.
//            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        }
    }

    /**
     * innerclass holds the information in the radius item
     */
    public class DistanceViewHolder extends RecyclerView.ViewHolder {

        private final TextView distance;

        /**
         * constructor of the class. Binds the UI elements
         * @param view
         */
        public DistanceViewHolder(View view) {
            super(view);
            // get reference to views
            distance = (TextView) view.findViewById(R.id.textView_distance_radius);

        }

        /**
         * binds the item to a certain position
         * @param position position of the item
         */
        void bindView(int position) {
            int attributeArrayPosition = positionConverter(position);
            mDistance distanceObject = (mDistance)attributes.get(attributeArrayPosition).get(2);
            distance.setText(distanceObject.getDistance());
        }

    }

    /**
     * innerclass holds the information in the radius item
     */
    public class NameViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;

        /**
         * constructor of the class. Binds the UI elements
         * @param view
         */
        public NameViewHolder(View view) {
            super(view);
            // get reference to views
            title = (TextView) view.findViewById(R.id.textView_location_title_main);
        }

        /**
         * binds the item to a certain position
         * @param position position of the item
         */
        void bindView(int position) {
            int attributeArrayPosition = positionConverter(position);
            title.setText(names.get(attributeArrayPosition));
        }

    }

}
