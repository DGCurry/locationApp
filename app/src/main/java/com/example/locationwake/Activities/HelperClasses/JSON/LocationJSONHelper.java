package com.example.locationwake.Activities.HelperClasses.JSON;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper class to communicate location and attributes between activities
 */
public class LocationJSONHelper {
    //JSONObject that holds all the data(location and attributes) to be communicated
    private JSONObject data;

    /**
     * Constructor that sets all the data
     * @param latitude latitude of the location
     * @param longitude longitude of the location
     */
    public LocationJSONHelper(String LID, String locationName, String latitude, String longitude) {
        data = new JSONObject();
        try {
            data.put("LID", LID);
            data.put("locationName", locationName);
            data.put("latitude", latitude);
            data.put("longitude", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * method that returns the JSON object that has been created by the constructor
     */
    public JSONObject build() {
        return data;
    }
}
