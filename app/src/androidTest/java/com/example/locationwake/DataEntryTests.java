package com.example.locationwake;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.locationwake.Backend.Database.Attributes.mRadius;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Backend.Database.mLocation;
import com.example.locationwake.Backend.Services.DataEntry;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DataEntryTests {

    @Test
    public void addDataTest() throws InterruptedException {
        DataHandler.deleteAll(ApplicationProvider.getApplicationContext());

        DataEntry dataEntry = new DataEntry(
                new mAttribute(
                        "1",
                        "2",
                        "Evening",
                        new mRadius("500"),
                        new mSetting("SLT")),
                new mLocation("5", "Home", "5"),
                ApplicationProvider.getApplicationContext());
        dataEntry.run();

        Thread.sleep(2000);

        String LID = DataHandler.loadLocations(ApplicationProvider.getApplicationContext()).get(0).getLID();

        ArrayList<mAttribute> attributes = DataHandler.loadAttributes(LID, ApplicationProvider.getApplicationContext());
        assertEquals(1, attributes.size());
        assertEquals(attributes.get(0).getName(), "Evening");
        assertEquals(attributes.get(0).getRadius().getRadius(), "500");
        assertEquals(attributes.get(0).getSetting().getSetting(), "SLT");
    }
}