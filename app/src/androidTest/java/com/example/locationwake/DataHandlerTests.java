package com.example.locationwake;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Backend.Database.Attributes.mLocation;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DataHandlerTests {

    @Test
    public void addDataTest() {
        DataHandler.deleteAll(ApplicationProvider.getApplicationContext());
        DataHandler.addData("JeMoeder", "10", "11", "10", "SLT", ApplicationProvider.getApplicationContext());
        ArrayList<mLocation> location =  DataHandler.loadLocations(ApplicationProvider.getApplicationContext());
        assertEquals(location.get(0).getLat(), "10");
        assertEquals(location.get(0).getLng(), "11");
        assertEquals(location.get(0).getName(), "JeMoeder");
    }

    @Test
    public void loadDataTest() {
        DataHandler.deleteAll(ApplicationProvider.getApplicationContext());
        String name = "jeMoeder";
        String latitude = "10.00";
        String longitude = "10.00";
        String distance = "100.067";
        String setting = "SLT";

        DataHandler.addData(name, latitude, longitude, distance, setting, ApplicationProvider.getApplicationContext());
        ArrayList<mLocation> locations =  DataHandler.loadLocations(ApplicationProvider.getApplicationContext());
        assertEquals(1, locations.size());
        assertEquals(locations.get(0).getName(), name);
        assertEquals(locations.get(0).getLat(), latitude);
        assertEquals(locations.get(0).getLng(), longitude);
        ArrayList<mAttribute> attributes = DataHandler.loadAttribute(locations.get(0).getKID(), ApplicationProvider.getApplicationContext());
        assertEquals(attributes.get(0).getDistance().getDistance(), distance);
        assertEquals(attributes.get(0).getSetting().getSetting(), setting);
    }

    @Test
    public void addAttributeTest() {
        DataHandler.deleteAll(ApplicationProvider.getApplicationContext());
        String name = "jeMoeder";
        String latitude = "10.00";
        String longitude = "10.00";
        String distance = "100.067";
        String setting = "SLT";
        DataHandler.addData(name, latitude, longitude, distance, setting, ApplicationProvider.getApplicationContext());

        String newDistance = "1";
        String newSetting = "SLT";

        int KID = DataHandler.loadLocations(ApplicationProvider.getApplicationContext()).get(0).getKID();
        DataHandler.addAttribute(KID, newDistance, newSetting, ApplicationProvider.getApplicationContext());

        ArrayList<mAttribute> attributes = DataHandler.loadAttribute(KID, ApplicationProvider.getApplicationContext());
        assertEquals(2, attributes.size());
        assertEquals(attributes.get(1).getDistance().getDistance(), newDistance);
        assertEquals(attributes.get(1).getSetting().getSetting(), newSetting);
    }

    @Test
    public void deleteAttributeTest() {
        addAttributeTest();
        int KID = DataHandler.loadLocations(ApplicationProvider.getApplicationContext()).get(0).getKID();
        ArrayList<mAttribute> attributes = DataHandler.loadAttribute(KID, ApplicationProvider.getApplicationContext());
        int AID = attributes.get(1).getAID();
        assertEquals(DataHandler.loadAttribute(KID, ApplicationProvider.getApplicationContext()).size(), 2);
        DataHandler.deleteAttribute(KID, AID, ApplicationProvider.getApplicationContext());
        assertEquals(DataHandler.loadAttribute(KID, ApplicationProvider.getApplicationContext()).size(), 1);
    }

    @Test
    public void deleteAllTest() {
        DataHandler.deleteAll(ApplicationProvider.getApplicationContext());
        ArrayList<mLocation> locations =  DataHandler.loadLocations(ApplicationProvider.getApplicationContext());

        assertEquals(locations.size(), 0);
    }

    @Test
    public void getSettingTest() {
        DataHandler.deleteAll(ApplicationProvider.getApplicationContext());
        String name = "jeMoeder";
        String latitude = "10.00";
        String longitude = "10.00";
        String distance = "100.067";
        String setting1 = "SLT";
        String setting2 = "SND";
        DataHandler.addData(name, latitude, longitude, distance, setting1, ApplicationProvider.getApplicationContext());
        DataHandler.addData(name, latitude, longitude, distance, setting2, ApplicationProvider.getApplicationContext());
        int KID1 = DataHandler.loadLocations(ApplicationProvider.getApplicationContext()).get(0).getKID();
        int KID2 = DataHandler.loadLocations(ApplicationProvider.getApplicationContext()).get(1).getKID();
        ArrayList<mAttribute> attributes1 = DataHandler.loadAttribute(KID1, ApplicationProvider.getApplicationContext());
        ArrayList<mAttribute> attributes2 = DataHandler.loadAttribute(KID2, ApplicationProvider.getApplicationContext());

        assertEquals("SLT", DataHandler.getSetting(KID1, attributes1.get(0).getAID(), ApplicationProvider.getApplicationContext()));
    }
}