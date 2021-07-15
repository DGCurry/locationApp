package com.example.locationwake.Activities.HelperClasses;

import org.json.JSONException;
import org.json.JSONObject;

public class NewLocationJSONHelper {
    private JSONObject data;

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

    public JSONObject build() {
        return data;
    }
}
