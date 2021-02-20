package com.example.locationwake;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.locationwake.Database.mLocation;

import java.util.ArrayList;

public class LocationViewAdapter extends RecyclerView.Adapter<LocationViewAdapter.ViewHolder> {

    //TAG of the class
    private final String TAG = "LocationViewAdapter";

    //DATA ELEMENTS
    ArrayList<mLocation> mLocations;

    /**
     * Inner class that holds a ViewHolder and the layout of the text files in a recyclerview
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //GUI elements
        private final TextView nameView;
        private final TextView idView;

        //LAYOUT
        public View layout;

        /**
         * represents the layout and the text in one row in the recylcerview
         * @param view -
         */
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            layout = view;
            nameView = (TextView) view.findViewById(R.id.location_row_name);
            idView = (TextView) view.findViewById(R.id.location_row_ID);
        }

        /**
         * returns the name text TextView
         * @return nameView
         */
        public TextView getNameView() {
            return nameView;
        }

        /**
         * returns the ID text TextView
         * @return idView
         */
        TextView getIdView() {
            return idView;
        }
    }

    /**
     * Constructor
     * @param mLocations data the recyclerview is going to show
     */
    public LocationViewAdapter(ArrayList<mLocation> mLocations) {
        this.mLocations = mLocations;
    }

    /**
     * - TODO: add comment when sober
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recyclerviewlocations_row, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * TODO: add comment when sober
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Logger.logD(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getNameView().setText(mLocations.get(position).getName());
        viewHolder.getIdView().setText(mLocations.get(position).getID().toString());
    }

    /**
     * TODO: add comment when sober
     * @return
     */
    @Override
    public int getItemCount() {
        return mLocations.size();
    }
}
