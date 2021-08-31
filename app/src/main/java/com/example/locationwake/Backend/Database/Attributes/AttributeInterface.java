package com.example.locationwake.Backend.Database.Attributes;

import android.content.Context;

public interface AttributeInterface {

    int LOCATION_TYPE = 100;
    int RADIUS_TYPE = 101;
    int SETTING_TYPE = 102;

    int getType();

    /**
     * Method to check whether the current object is valid
     * @return True if the criteria for the attribute has been met, else False
     */
    boolean isValid();
}
