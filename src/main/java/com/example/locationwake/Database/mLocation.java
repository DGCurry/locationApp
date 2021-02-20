package com.example.locationwake.Database;

/**
 * Holder of the data. Create an instance of this class to save the data to the database
 */
public class mLocation {

    // Data needed for the database
    private String name;
    private String lat, lng;
    private Integer ID;
    /**
     * Constructor to create a data holder from the beginning
     * @param lat latitude the user has given
     * @param lng longitude the user has given
     * @param name name the user has given
     */
    public mLocation(String name, String lat, String lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * Empty constructor
     */
    public mLocation() {
    }

    /**
     * Add the latitude information as String.
     * @param lat latitude the user has given
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * Add the longitude information as String.
     * @param lng longitude the user has given
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     * Add the name information as String.
     * @param name name the user has given
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Add the ID that corresponds to the database entry
     * @param ID ID the database has generated for an entry
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     * Return the latitude information as String
     * @return lat latitude the user has added
     */
    public String getLat() {
        return lat;
    }

    /**
     * Return the longitude information as String
     * @return lng longitude the user has added
     */
    public String getLng() {
        return lng;
    }

    /**
     * Return the name information as String
     * @return name name the user has added
     */
    public String getName() {
        return name;
    }

    /**
     * Return the ID key that corresponds to the location
     * @return ID key
     */
    public Integer getID() {
        return ID;
    }

}
