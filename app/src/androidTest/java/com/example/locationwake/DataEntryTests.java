package com.example.locationwake;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.locationwake.Activities.HelperClasses.FormCallBack;
import com.example.locationwake.Backend.Database.Attributes.mDistance;
import com.example.locationwake.Backend.Database.Attributes.mSetting;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Workers.DataEntry.DataEntry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DataEntryTests {
    CountDownLatch latch = new CountDownLatch(1);
    boolean success = false;

    @Test
    public void addDataTest() throws InterruptedException {

        class innerClass implements FormCallBack {

            @Override
            public void onSuccess(int position, String message) {
                success = true;
                latch.countDown();
            }

            @Override
            public void onFailure(int position, String message) {

            }
        }

        innerClass ic = new innerClass();

        DataEntry dataEntry = new DataEntry(ic, new mAttribute(1, 1, new mDistance("500"), new mSetting("SLT")), new mLocation("5", "5", "mauw"), ApplicationProvider.getApplicationContext());
        DataHandler.deleteAll(ApplicationProvider.getApplicationContext());
        dataEntry.run();
        latch.await();
        assertTrue(success);

        int KID = DataHandler.loadLocations(ApplicationProvider.getApplicationContext()).get(0).getKID();

        ArrayList<mAttribute> attributes = DataHandler.loadAttributes(KID, ApplicationProvider.getApplicationContext());
        assertEquals(1, attributes.size());
        assertEquals(attributes.get(0).getDistance().getDistance(), "500");
        assertEquals(attributes.get(0).getSetting().getSetting(), "SLT");
    }
}