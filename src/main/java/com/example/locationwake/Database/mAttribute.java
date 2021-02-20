package com.example.locationwake.Database;

/**
 * Holder of the data. Create an instance of this class to save the data to the database
 */
public class mAttribute {

    // Data needed for the database
    //TODO: add day/timeStep
    private String distance;
    private String setting;
    private Integer ID;

    /**
     * Constructor to create a data holder from the beginning
     * @param ID key id to link to LocationSettingDbHelper entry
     * @param distance distance the user has given
     * @param setting setting the user has added
     *                SLT: silent
     *                VBR: vibrate
     *                SND: sound
     */
    public mAttribute(int ID, String distance, String setting) {
        this.ID = ID;
        this.distance = distance;
        this.setting = setting;
    }

    /**
     * Empty constructor
     */
    public mAttribute() {
    }

    /**
     * Add the ID information as Integer.
     * @param ID key id to link to LocationSettingDbHelper entry
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     * Add the distance information as String.
     * @param distance distance the user has given
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

    /**
     * Add the setting information as String
     * @return setting setting the user has added
     *                SLT: silent
     *                VBR: vibrate
     *                SND: sound
     */
    public void setSetting(String setting) {
        this.setting = setting;
    }

    /**
     * Return the ID information as Integer
     * @return ID key id to link to LocationSettingDbHelper entry
     */
    public Integer getID() {
        return ID;
    }

    /**
     * Return the distance information as String
     * @return distance distance the user has added
     */
    public String getDistance() {
        return distance;
    }

    /**
     * Return the setting information as String
     * @return setting setting the user has added
     *                SLT: silent
     *                VBR: vibrate
     *                SND: sound
     */
    public String getSetting() {
        return setting;
    }

}
