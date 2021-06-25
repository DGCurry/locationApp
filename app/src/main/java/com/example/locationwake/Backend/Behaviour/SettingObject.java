package com.example.locationwake.Backend.Behaviour;

import java.util.ArrayList;

/**
 * Encapsulates a setting
 * For instance, can add the behaviour that only when the phone is charging, or certain time, or certain days etc to a location
 */
public class SettingObject {
    private int KID, AID;
    //important components are not attributes, they are derived from attributes
    //important this setting object represents one attribute
    private ArrayList<Component> behaviourList;

    /**
     * constructor, takes over the Key ID
     * @param KID
     */
    public SettingObject(int KID, int AID) {
        this.KID = KID;
        this.AID = AID;
        behaviourList = new ArrayList<>();
    }

    /**
     * adds a component to this settingobject
     * @param component
     */
    public void addComponent(Component component) {
        behaviourList.add(component);
    }

    /**
     * checks if every component in the Setting Object is active
     * @return AID if one is active, else -1
     */
    public int isActive() {
        for (Component c:behaviourList) {
            if (!c.isActive()) return -1;
        }
        return AID;
    }

    /**
     * gets the KID of this settingobject
     * @return KID
     */
    public int getKID() {
        return KID;
    }
}
