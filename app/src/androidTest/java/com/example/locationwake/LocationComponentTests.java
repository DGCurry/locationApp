//package com.example.locationwake;
//
//import android.content.SharedPreferences;
//
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.example.locationwake.Backend.Database.Attributes.mDistance;
//import com.example.locationwake.Backend.Database.mLocation;
//import com.example.locationwake.Backend.Behaviour.Components.LocationComponent;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(AndroidJUnit4.class)
//public class LocationComponentTests {
//
//    @Before
//    public void setUp() {
//        SharedPreferences preferences = ApplicationProvider.getApplicationContext().getSharedPreferences("LOCATION_FILE_NAME", ApplicationProvider.getApplicationContext().MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putFloat("location_lat", (float) 12);
//        editor.putFloat("location_lon", (float) 13);
//        editor.apply();
//    }
//
//    @Test
//    public void testIsActive() {
//        LocationComponent locationComponent = new LocationComponent(ApplicationProvider.getApplicationContext(), new mLocation("10", "10"), new mDistance("395800"));
//        assertEquals(locationComponent.isActive(), true);
//
//        locationComponent = new LocationComponent(ApplicationProvider.getApplicationContext(), new mLocation("10", "10"), new mDistance("390000"));
//        assertEquals(locationComponent.isActive(), false);
//    }
//
//}
