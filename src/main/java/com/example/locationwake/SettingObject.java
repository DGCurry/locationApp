package com.example.locationwake;

import java.util.ArrayList;

/**
 * Encapsulates a setting
 * For instance, can add the behaviour that only when the phone is charging, or certain time, or certain days etc to a location
 */
public class SettingObject {

    private ArrayList<Component> behaviourList;

    public void addComponent(Component component) {
        behaviourList.add(component);
    }

}
