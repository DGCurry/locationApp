package com.example.locationwake.Activities.HelperClasses;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper class to communicate location and attributes between activities
 */
public class NewLocationJSONHelper {
    //JSONObject that holds all the data(location and attributes) to be communicated
    private JSONObject data;

    /**
     * Constructor that sets all the data
     * @param name name of the location
     * @param latitude latitude of the location
     * @param longitude longitude of the location
     * @param radius distance of the location
     * @param setting setting(SLT, SND, VBR) of the location
     */
    public NewLocationJSONHelper(String name, String latitude, String longitude, String radius, String setting) {
        data = new JSONObject();
        try {
            data.put("name", name);
            data.put("latitude", latitude);
            data.put("longitude", longitude);
            data.put("radius", radius);
            data.put("setting", setting);
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
