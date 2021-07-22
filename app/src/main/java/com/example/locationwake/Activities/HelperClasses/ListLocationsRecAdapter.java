//package com.example.locationwake.Activities.HelperClasses;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityOptionsCompat;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.locationwake.Backend.Database.Attributes.mLocation;
//import com.example.locationwake.Logger;
//import com.example.locationwake.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Helper class for a RecyclerView that displays the setting which is active
// */
//public class ListLocationsRecAdapter extends RecyclerView.Adapter{
//
//    //TAG of the class
//    static final private String TAG = "ListSettingsRecAdapter";
//
//    //List that holds all the different attributes of the location displayed with the Recyclerview
//    private List<mLocation> locations;
//
//    //Context of the application
//    Context context;
//
//    /**
//     * Constructor
//     * @param context context of the application
//     */
//    public ListLocationsRecAdapter(List<mLocation> locations, Context context) {
//        if (locations == null) {
//            locations = new ArrayList<>();
//        }
//
//        //Clear the previous Recyclerview(if exists) and add the data
//        this.locations.clear();
//        this.locations.addAll(locations);
//
//        notifyDataSetChanged();
//
//        this.context = context;
//    }
//
//    /**
//     *
//     * @param parent View that holds the Recyclerview items
//     * @param viewType type of the item contained in the parent View
//     * @return a new View that holds the item that is contained in the parent View
//     */
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView;
//        itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.header_icon, parent, false);
//
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ViewLocationActivity.class);
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation((Activity) context, itemView, "header");
//                itemView.getContext().startActivity(intent, options.toBundle());
//            }
//        });
//
//        return new headerViewHolder(itemView);
//
//    }
//
//    /**
//     * when the item in bind, this determines what function should be called to bind the position
//     * @param holder holder of the item
//     * @param position position in the recyclerview
//     */
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Logger.logV(TAG, "onBindViewHolder(): SETTING_TYPE added" );
//        ((headerViewHolder) holder).bindView(position);
//    }
//
//    /**
//     * returns how many attributes are used for the recyclerview
//     * @return attributes size if not null
//     */
//    @Override
//    public int getItemCount() {
//        if (locations == null) {
//            return 0;
//        }
//        return locations.size();
//    }
//
//    /**
//     * innerclass holds the information for the header item
//     */
//    public class headerViewHolder extends RecyclerView.ViewHolder {
//
//        private final TextView title;
//
//        /**
//         * constructor of the class. Binds the UI elements
//         * @param view
//         */
//        public headerViewHolder(View view) {
//            super(view);
//            title = (TextView) view.findViewById(R.id.textView_location_title_main);
//        }
//
//        /**
//         * binds the item to a certain position
//         * @param position position of the item
//         */
//        void bindView(int position) {
//            title.setText(locations.get(position).getName());
//        }
//    }
//}
