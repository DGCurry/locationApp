package com.example.locationwake.Activities.AddNewLocationAttributeActivities.AddLocationActivities;

import android.content.Intent;
import android.icu.number.Precision;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import com.example.locationwake.Activities.ActivityExtension.BasicMapActivity;
import com.example.locationwake.Logger;
import com.example.locationwake.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ShowLocationActivity extends BasicMapActivity {

    private LatLng focussedLocation;
    private Marker focussedLocationMarker;

    SearchView searchView;

    // JSON object used to communicate between activities
    private JSONObject data = new JSONObject();

    /**
     * Tag of the class
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "ShowLocationActivity";

        setContentView(R.layout.activity_choose_location_map);

        searchView = findViewById(R.id.idSearchView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();

        setSearchQueries();
        setOkayButton();
    }


    /**
     * Method to load the data send by other activities
     */
    private void loadData() {
        Logger.logV(TAG, "loadData(): getting data from AddNameActivity");
        if (getIntent().hasExtra("data")) {
            try {
                data = new JSONObject(getIntent().getStringExtra("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.map = map;
        addSavedLocationsMarkers();
        //Here we are going to get the location with a long click
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                // get location, zoom in, go to location, set marker
                focussedLocation = latLng;

                if (focussedLocationMarker != null) {
                    focussedLocationMarker.remove();
                }
                MarkerOptions positionMarker = new MarkerOptions().position(
                        latLng).title("Focussed Location");

                focussedLocationMarker = map.addMarker(positionMarker);
                map.animateCamera(CameraUpdateFactory.newLatLng(
                        latLng));

                latitude.setText(String.valueOf(latLng.latitude));
                longitude.setText(String.valueOf(latLng.longitude));
            }
        });
    }

    private void setOkayButton() {
        Button agreeButton = findViewById(R.id.addLocationButton);
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (focussedLocation != null) {
                    Intent intent = new Intent(getApplicationContext(), AddLocationActivity.class);
                    try {
                        data.put("latitude", String.valueOf(focussedLocation.latitude));
                        data.put("longitude", String.valueOf(focussedLocation.longitude));

                        intent.putExtra("data", data.toString());
                        startActivity(intent);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (currentLocation != null) {
                    Intent intent = new Intent(getApplicationContext(), AddLocationActivity.class);
                    try {
                        data.put("latitude", String.valueOf(currentLocation.latitude));
                        data.put("longitude", String.valueOf(currentLocation.longitude));

                        intent.putExtra("data", data.toString());
                        startActivity(intent);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void setSearchQueries() {
        SearchView searchView = findViewById(R.id.idSearchView);
        // adding on query listener for our search view.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (!(addressList == null || addressList.size() == 0)) {
                        // on below line we are getting the location
                        // from our list a first position.
                        Address address = addressList.get(0);

                        // on below line we are creating a variable for our location
                        // where we will add our locations latitude and longitude.
                        focussedLocation = new LatLng(address.getLatitude(), address.getLongitude());
                        if (focussedLocationMarker != null) {
                            focussedLocationMarker.remove();
                        }

                        // on below line we are adding marker to that position.
                        focussedLocationMarker = map.addMarker(new MarkerOptions().position(focussedLocation).title(location));

                        // below line is to animate camera to that position.
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(focussedLocation, 10));
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "No location found", Toast.LENGTH_LONG);
                        toast.show();
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}
