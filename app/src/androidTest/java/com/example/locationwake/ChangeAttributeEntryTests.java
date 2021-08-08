package com.example.locationwake;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Backend.Services.ChangeAttributeEntry;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ChangeAttributeEntryTests {

    @Test
    public void changeNameTest() throws InterruptedException {
        //ensure the test is running, and adds data
        DataEntryTests dataEntryTests = new DataEntryTests();
        dataEntryTests.addDataTest();
        mLocation location = DataHandler.loadLocations(ApplicationProvider.getApplicationContext()).get(0);
        mAttribute loadedAttribute = DataHandler.loadAttributes(location.getLID(), ApplicationProvider.getApplicationContext()).get(0);

        ChangeAttributeEntry dataEntry = new ChangeAttributeEntry(
                new mAttribute(
                        loadedAttribute.getLID(),
                        loadedAttribute.getAID(),
                        "Morning",
                        loadedAttribute.getRadius(),
                        loadedAttribute.getSetting()),
                ApplicationProvider.getApplicationContext());
        dataEntry.run();

        Thread.sleep(2000);

        String LID = DataHandler.loadLocations(ApplicationProvider.getApplicationContext()).get(0).getLID();

        ArrayList<mAttribute> attributes = DataHandler.loadAttributes(LID, ApplicationProvider.getApplicationContext());
        assertEquals(1, attributes.size());
        assertEquals(attributes.get(0).getName(), "Morning");
        assertEquals(attributes.get(0).getRadius().getRadius(), "500");
        assertEquals(attributes.get(0).getSetting().getSetting(), "SLT");
    }
}