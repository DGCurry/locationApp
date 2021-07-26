package com.example.locationwake.Activities.HelperClasses.JSON;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper class to communicate location and attributes between activities
 */
public class AttributeJSONHelper {
    //JSONObject that holds all the data(location and attributes) to be communicated
    private JSONObject data;

    /**
     * Constructor that sets all the data
     * @param radius distance of the location
     * @param setting setting(SLT, SND, VBR) of the location
     */
    public AttributeJSONHelper(String locationName, String LID, String attributeName, String AID, String radius, String setting) {
        data = new JSONObject();
        try {
            data.put("locationName", locationName);
            data.put("LID", LID);
            data.put("attributeName", attributeName);
            data.put("AID", AID);
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
