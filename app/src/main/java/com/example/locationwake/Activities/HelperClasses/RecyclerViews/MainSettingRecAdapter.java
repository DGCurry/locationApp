package com.example.locationwake.Activities.HelperClasses.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Backend.Database.Attributes.mLatLng;
import com.example.locationwake.Backend.Database.Attributes.mRadius;
import com.example.locationwake.Logger;
import com.example.locationwake.R;
import com.example.locationwake.Backend.Database.Attributes.AttributeInterface;
import com.example.locationwake.Backend.Database.mLocation;
import com.example.locationwake.Backend.Database.Attributes.mSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * Recyclerview that is used by the MainActivity to display the location and attributes that is active
 */
public class MainSettingRecAdapter extends RecyclerView.Adapter {

    //TAG of the class
    static final private String TAG = "MainSettingRecAdapter";

    //Holds the different attributes of the location that is in the Recyclerview
    private List<AttributeInterface> attributes;

    //Context of the Activity
    Context context;

    /**
     * Constructor
     * @param list holds all the different attributes of the location
     * @param context context of the application
     */
    public MainSettingRecAdapter(List<AttributeInterface> list, Context context) {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        //Clear the previous Recyclerview(if exists) and add the data
        attributes.clear();
        attributes.addAll(list);
        notifyDataSetChanged();

        this.context = context;
    }

    /**
     * Returns the type of the item contained in the Recyclerview
     * @param position of the item
     * @return the position of the item in the Recyclerview
     */
    @Override
    public int getItemViewType(int position) {
        return attributes.get(position).getType();
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
            case AttributeInterface.RADIUS_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_h_item, parent, false);
                return new RadiusViewHolder(itemView);

            case AttributeInterface.SETTING_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_h_item, parent, false);
                return new SettingViewHolder(itemView);

            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_h_item, parent, false);
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
            case AttributeInterface.RADIUS_TYPE:
                Logger.logV(TAG, "onBindViewHolder(): DISTANCE_TYPE added");
                ((RadiusViewHolder) holder).bindView(position);
                break;

            case AttributeInterface.LOCATION_TYPE:
                Logger.logV(TAG, "onBindViewHolder(): LOCATION_TYPE added");
                ((LocationViewHolder) holder).bindView(position);
                break;

            case AttributeInterface.SETTING_TYPE:
                Logger.logV(TAG, "onBindViewHolder(): SETTING_TYPE added");
                ((SettingViewHolder) holder).bindView(position);
                break;

        }
    }

    /**
     * returns how many attributes are used for the recyclerview
     *
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
    public class SettingViewHolder extends RecyclerView.ViewHolder {

        private final TextView setting;

        /**
         * constructor of the class. Binds the UI elements
         *
         * @param view
         */
        public SettingViewHolder(View view) {
            super(view);
            setting = (TextView) view.findViewById(R.id.textView_h_title);
        }

        /**
         * binds the item to a certain position
         *
         * @param position position of the item
         */
        void bindView(int position) {
            setting.setText(((mSetting) attributes.get(position)).getSetting());
        }
    }

    /**
     * innerclass holds the information in the location item
     */
    public class LocationViewHolder extends RecyclerView.ViewHolder {
        View layout;
        TextView latLng;

        /**
         * constructor of the class. Binds the UI elements
         *
         * @param view
         */
        public LocationViewHolder(View view) {
            super(view);
            layout = view;
            latLng = view.findViewById(R.id.textView_h_title);
        }

        /**
         * binds the item to a certain position
         *
         * @param position position of the item
         */
        void bindView(int position) {
            mLatLng mLatLng = (mLatLng) attributes.get(position);
            latLng.setText(mLatLng.getLat() + ", " + mLatLng.getLng());
            // Store a reference of the ViewHolder object in the layout.
            // Store a reference to the item in the mapView's tag. We use it to get the
            // coordinate of a location, when setting the map location.
        }
    }

    /**
     * innerclass holds the information in the radius item
     */
    public class RadiusViewHolder extends RecyclerView.ViewHolder {

        private final TextView distance;

        /**
         * constructor of the class. Binds the UI elements
         *
         * @param view
         */
        public RadiusViewHolder(View view) {
            super(view);
            // get reference to views
            distance = view.findViewById(R.id.textView_h_title);
        }

        /**
         * binds the item to a certain position
         *
         * @param position position of the item
         */
        void bindView(int position) {
            distance.setText(((mRadius)attributes.get(position)).getRadius());
        }

    }

}

