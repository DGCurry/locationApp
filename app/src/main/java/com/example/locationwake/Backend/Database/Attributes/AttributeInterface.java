package com.example.locationwake.Backend.Database.Attributes;

import android.content.Context;

public interface AttributeInterface {

    //Types of the Attributes, used by the front-end
    int DISTANCE_TYPE = 100;
    int SETTING_TYPE = 101;
    int LOCATION_TYPE = 102;

    /**
     * Method used to return the type
     * @return DISTANCE_TYPE, SETTING_TYPE or LOCATION_TYPE
     */
    int getType();

    /**
     * Method to check whether the current object is valid
     * @return True if the criteria for the attribute has been met, else False
     */
    boolean isValid();
}
