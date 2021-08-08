package com.example.locationwake.Activities.HelperClasses.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddAttributeActivities.AddNameAttributeActivity;
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
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
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
    private Stack<AttributeInterface> attributeStack;
    private Map<Integer, Integer> positionAttributeTypeMapping;

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
        setPositionAttributeTypeMapping();

        notifyDataSetChanged();
    }

    private void setPositionAttributeTypeMapping() {
        if (attributes.isEmpty()) {
            positionAttributeTypeMapping.put(0, AttributeInterface.LOCATION_TYPE);
            return;
        }

        int maxPosition = getItemCount();
        //Load in the attributes
        attributeStack.push(attributes.get(attributeCount).getRadius());
        attributeStack.push(attributes.get(attributeCount).getSetting());

        for (int position = 0; position < maxPosition; position++) {
            Logger.logE(TAG, "\n Going into the loop with values: \n" +
                    "attributeCount: " + attributeCount + "\n" +
                    "itemCount: " + itemCount + "\n" +
                    "position: " + position + "\n");

            if (position == 0) {
                //this is the location of the list
                Logger.logD(TAG, "setPositionAttributeTypeMapping(): returning with values: \n" +
                        "attributeCount: " + attributeCount + "\n" +
                        "itemCount: " + itemCount + "\n" +
                        "position: " + position);

                Logger.logD(TAG, "setPositionAttributeTypeMapping(): attribute location type");
                positionAttributeTypeMapping.put(position, AttributeInterface.LOCATION_TYPE);
                Logger.logE(TAG, "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
                continue;
            }

            //Check if all items have been dealt with for the attributes list
            if (attributes.get(attributeCount).getItemCount() == itemCount) {
                Logger.logD(TAG, "setPositionAttributeTypeMapping(): all attributes for the list has been dealt " +
                        "with, going to the next attributes list");
                itemCount = 0;
                attributeCount++;

                //Load in the attributes
                attributeStack.push(attributes.get(attributeCount).getRadius());
                attributeStack.push(attributes.get(attributeCount).getSetting());
            }

            if (itemCount == 0) {
                //this is the name of the attribute
                Logger.logD(TAG, "setPositionAttributeTypeMapping(): returning with values: \n" +
                        "attributeCount: " + attributeCount + "\n" +
                        "itemCount: " + itemCount + "\n" +
                        "position: " + position);
                Logger.logD(TAG, "setPositionAttributeTypeMapping(): attribute name type");
                positionAttributeTypeMapping.put(position, AttributeInterface.NAME_TYPE);
                Logger.logE(TAG, "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
                itemCount++;
                continue;
            }

            int returnValue = attributeStack.pop().getType();
            Logger.logD(TAG, "setPositionAttributeTypeMapping(): returning with values: \n" +
                    "attributeCount: " + attributeCount + "\n" +
                    "itemCount: " + itemCount + "\n" +
                    "position: " + position);
            Logger.logD(TAG, "setPositionAttributeTypeMapping(): attribute " + returnValue + " type");
            positionAttributeTypeMapping.put(position, returnValue);
            itemCount++;
            Logger.logE(TAG, "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        }
    }

    /**
     * Returns the type of the item contained in the Recyclerview
     * @param position of the item
     * @return the position of the item in the Recyclerview
     */
    @Override
    public int getItemViewType(int position) {
        return positionAttributeTypeMapping.get(position);
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
        switch (viewType) {
            case AttributeInterface.DISTANCE_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.distance_item, parent, false);
                return new DistanceViewHolder(itemView);

            case AttributeInterface.SETTING_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header_icon_item_nt, parent, false);
                return new SettingViewHolder(itemView);

            case AttributeInterface.LOCATION_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.location_item_expanded, parent, false);
                return new LocationViewHolder(itemView);

            //name
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header_icon_item_nt, parent, false);
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
        for (mAttribute attribute:attributes) {
            c += attribute.getItemCount();
        }
        Logger.logD(TAG, "getItemCount(): found " + c + " items");
        return c;
    }

    /**
     * innerclass holds the information for the header item
     */
    public class SettingViewHolder extends RecyclerView.ViewHolder {

        private final TextView setting;
        private View v;

        /**
         * constructor of the class. Binds the UI elements
         * @param view
         */
        public SettingViewHolder(View view) {
            super(view);
            v = view;
            setting = (TextView) view.findViewById(R.id.textView_header_subtitle);
            setting.setVisibility(View.VISIBLE);
        }

        /**
         * binds the item to a certain position
         * @param position position of the item
         */
        void bindView(int position) {
            int attributeArrayPosition = positionConverter(position);
            mSetting settingObject = attributes.get(attributeArrayPosition).getSetting();
            setting.setText(settingObject.getSetting());

            Button button = v.findViewById(R.id.button_header_icon);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChangeSettingActivity.class);
                    intent.putExtra("data", new AttributeJSONHelper(
                            location.getName(),
                            location.getLID(),
                            attributes.get(attributeArrayPosition).getName(),
                            attributes.get(attributeArrayPosition).getAID(),
                            attributes.get(attributeArrayPosition).getRadius().getRadius(),
                            attributes.get(attributeArrayPosition).getSetting().getSetting()).build().toString());
                    v.getContext().startActivity(intent);
                }
            });
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
            TextView latitude = layout.findViewById(R.id.textView_main_location_latitude);
            TextView longitude = layout.findViewById(R.id.textView_main_location_longitude);
            name.setText(location.getName());
            latitude.setText(location.getLat());
            longitude.setText(location.getLng());

            Button changeLocation = layout.findViewById(R.id.button_header_icon);
            changeLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChangeLocationActivity.class);
                    intent.putExtra("data", new LocationJSONHelper(location.getLID(),
                            location.getName(), location.getLat(), location.getLng()).build().toString());
                    v.getContext().startActivity(intent);
                }
            });

            Button addAttribute = layout.findViewById(R.id.button_subheader);
            addAttribute.setVisibility(View.VISIBLE);
            addAttribute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.logD(TAG, "LocationViewHolder.bindView(): sending name " + location.getName());
                    Intent intent = new Intent(v.getContext(), AddNameAttributeActivity.class);
                    intent.putExtra("data", new AttributeJSONHelper(
                            location.getName(),
                            location.getLID(),
                            null,
                            null,
                            null,
                            null).build().toString());
                    v.getContext().startActivity(intent);
                }
            });
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

    /**
     * innerclass holds the information in the radius item
     */
    public class DistanceViewHolder extends RecyclerView.ViewHolder {

        private final TextView distance;
        View v;

        /**
         * constructor of the class. Binds the UI elements
         * @param view
         */
        public DistanceViewHolder(View view) {
            super(view);
            v = view;
            // get reference to views
            distance = (TextView) view.findViewById(R.id.textView_distance_radius);

        }

        /**
         * binds the item to a certain position
         * @param position position of the item
         */
        void bindView(int position) {
            int attributeArrayPosition = positionConverter(position);
            mRadius distanceObject = attributes.get(attributeArrayPosition).getRadius();
            distance.setText(distanceObject.getRadius());

            Button button = v.findViewById(R.id.button_header_icon);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChangeDistanceActivity.class);
                    intent.putExtra("data", new AttributeJSONHelper(
                            location.getName(),
                            location.getLID(),
                            attributes.get(attributeArrayPosition).getName(),
                            attributes.get(attributeArrayPosition).getAID(),
                            attributes.get(attributeArrayPosition).getRadius().getRadius(),
                            attributes.get(attributeArrayPosition).getSetting().getSetting()).build().toString());
                    v.getContext().startActivity(intent);
                }
            });
        }

    }

    /**
     * innerclass holds the information in the radius item
     */
    public class NameViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private View v;
        /**
         * constructor of the class. Binds the UI elements
         * @param view
         */
        public NameViewHolder(View view) {
            super(view);
            v = view;
            Logger.logD(TAG, "NameViewHolder(): changing params");
            setLayout();

            // get reference to views
            title = (TextView) view.findViewById(R.id.textView_header_title);
        }

        private void setLayout() {
            v.setBackgroundColor(context.getResources().getColor(R.color.fulvous));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );

            Resources r = context.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    30,
                    r.getDisplayMetrics()
            );

            params.setMargins(0, px, 0, 0);
            v.setLayoutParams(params);
        }

        /**
         * binds the item to a certain position
         * @param position position of the item
         */
        void bindView(int position) {
            int attributeArrayPosition = positionConverter(position);
            String name = attributes.get(attributeArrayPosition).getName();

            title.setText(name);

            Button button = v.findViewById(R.id.button_header_icon);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChangeNameAttributeActivity.class);
                    intent.putExtra("data", new AttributeJSONHelper(
                            location.getName(),
                            location.getLID(),
                            attributes.get(attributeArrayPosition).getName(),
                            attributes.get(attributeArrayPosition).getAID(),
                            attributes.get(attributeArrayPosition).getRadius().getRadius(),
                            attributes.get(attributeArrayPosition).getSetting().getSetting()).build().toString());
                    v.getContext().startActivity(intent);
                }
            });
        }

    }

}
