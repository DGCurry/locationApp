package com.example.locationwake.Backend.Database.Attributes;

import android.content.Context;

public interface AttributeInterface {
    int DISTANCE_TYPE = 100;
    int SETTING_TYPE = 101;
    int LOCATION_TYPE = 102;

    int getType();

    boolean isValid();
}
