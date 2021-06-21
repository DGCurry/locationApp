package com.example.locationwake.backend.Database.Attributes;

/**
 * Holder of the data. Create an instance of this class to save the data to the database
 */
public class mLocation implements AttributeInterface{


    // Data needed for the database
    private String lat, lng;
    private String name;
    private Integer KID;

    /**
     * Constructor to create a data holder from the beginning
     * @param lat latitude the user has given
     * @param lng longitude the user has given
     * @param name name the user has given
     */
    public mLocation(String lat, String lng, String name) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    /**
     * Constructor to create a data holder from the beginning
     * @param lat latitude the user has given
     * @param lng longitude the user has given
     */
    public mLocation(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
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
     * Add the ID that corresponds to the database entry
     * @param KID ID the database has generated for an entry
     */
    public void setKID(Integer KID) {
        this.KID = KID;
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
     * Return the ID key that corresponds to the location
     * @return ID key
     */
    public Integer getKID() {
        return KID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return AttributeInterface.LOCATION_TYPE;
    }
}
