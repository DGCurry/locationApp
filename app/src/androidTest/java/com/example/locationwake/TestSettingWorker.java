//package com.example.locationwake;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.rule.GrantPermissionRule;
//import androidx.work.ListenableWorker;
//import androidx.work.testing.TestWorkerBuilder;
//
//import com.example.locationwake.backend.Database.Attributes.mDistance;
//import com.example.locationwake.backend.Database.DataHandler;
//import com.example.locationwake.backend.Database.Attributes.mLocation;
//import com.example.locationwake.backend.Workers.SettingWorker;
//import com.example.locationwake.backend.behaviour.Components.LocationComponent;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.ArrayList;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
//import static org.junit.Assert.assertTrue;
//
//@RunWith(AndroidJUnit4.class)
//public class TestSettingWorker {
//
//    @Rule
//    public GrantPermissionRule notification_policy = GrantPermissionRule.grant(Manifest.permission.ACCESS_NOTIFICATION_POLICY);
//
//    Context mContext;
//
//    @Before
//    public void setUp() {
//        mContext = ApplicationProvider.getApplicationContext();
//
//
//        SharedPreferences preferences = ApplicationProvider.getApplicationContext().getSharedPreferences("LOCATION_FILE_NAME", ApplicationProvider.getApplicationContext().MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putFloat("location_lat", (float) 12);
//        editor.putFloat("location_lon", (float) 13);
//        editor.apply();
//    }
//
//    @Test
//    public void testSuccess() {
//        putSetting("SLT", "10", "10", "395800");
//        SettingWorker worker =
//                (SettingWorker) TestWorkerBuilder.from(mContext,
//                        SettingWorker.class)
//                        .build();
//
//        ListenableWorker.Result result = worker.doWork();
//        assertEquals(result, ListenableWorker.Result.success());
//    }
//
//    @Test
//    public void testIsActiveSND() {
//        LocationComponent locationComponent = new LocationComponent(ApplicationProvider.getApplicationContext(), new mLocation("10", "10"), new mDistance("395800"));
//        assertTrue(locationComponent.isActive());
//        String setting = "SND";
//        putSetting(setting, "10", "10", "395800");
//        SettingWorker worker =
//                (SettingWorker) TestWorkerBuilder.from(mContext,
//                        SettingWorker.class)
//                        .build();
//        ListenableWorker.Result result = worker.doWork();
//
//        SharedPreferences pref = ApplicationProvider.getApplicationContext().getSharedPreferences(
//                "SETTING_FILE_NAME", Context.MODE_PRIVATE);
//
//        Integer KID = pref.getInt("KID", -1);
//        Integer AID = pref.getInt("AID", -1);
//        String savedSetting = pref.getString("setting", null);
//        assertNotEquals(-1, (int) KID);
//        assertNotEquals(-1, (int) AID);
//        assertEquals(savedSetting, setting);
//    }
//
//    @Test
//    public void testIsActiveSLT() {
//        LocationComponent locationComponent = new LocationComponent(ApplicationProvider.getApplicationContext(), new mLocation("10", "10"), new mDistance("395800"));
//        assertTrue(locationComponent.isActive());
//        String setting = "SLT";
//        putSetting(setting, "10", "10", "395800");
//        SettingWorker worker =
//                (SettingWorker) TestWorkerBuilder.from(mContext,
//                        SettingWorker.class)
//                        .build();
//        ListenableWorker.Result result = worker.doWork();
//
//        SharedPreferences pref = ApplicationProvider.getApplicationContext().getSharedPreferences(
//                "SETTING_FILE_NAME", Context.MODE_PRIVATE);
//
//        Integer KID = pref.getInt("KID", -1);
//        Integer AID = pref.getInt("AID", -1);
//        String savedSetting = pref.getString("setting", null);
//        assertNotEquals(-1, (int) KID);
//        assertNotEquals(-1, (int) AID);
//        assertEquals(savedSetting, setting);
//    }
//
//    @Test
//    public void testIsActiveVBR() {
//        LocationComponent locationComponent = new LocationComponent(ApplicationProvider.getApplicationContext(), new mLocation("10", "10"), new mDistance("395800"));
//        assertTrue(locationComponent.isActive());
//        String setting = "VBR";
//        putSetting(setting, "10", "10", "395800");
//        SettingWorker worker =
//                (SettingWorker) TestWorkerBuilder.from(mContext,
//                        SettingWorker.class)
//                        .build();
//        ListenableWorker.Result result = worker.doWork();
//
//        SharedPreferences pref = ApplicationProvider.getApplicationContext().getSharedPreferences(
//                "SETTING_FILE_NAME", Context.MODE_PRIVATE);
//
//
//        Integer KID = pref.getInt("KID", -1);
//        Integer AID = pref.getInt("AID", -1);
//        String savedSetting = pref.getString("setting", null);
//        assertNotEquals(-1, (int) KID);
//        assertNotEquals(-1, (int) AID);
//        assertEquals(savedSetting, setting);
//    }
//
//    @Test
//    public void testIsNotActive() {
//        LocationComponent locationComponent = new LocationComponent(ApplicationProvider.getApplicationContext(), new mLocation("10", "10"), new mDistance("39000"));
//        assertTrue(!locationComponent.isActive());
//        String setting = "SLT";
//        putSetting(setting, "10", "10", "39000");
//        SettingWorker worker =
//                (SettingWorker) TestWorkerBuilder.from(mContext,
//                        SettingWorker.class)
//                        .build();
//        ListenableWorker.Result result = worker.doWork();
//
//        SharedPreferences pref = ApplicationProvider.getApplicationContext().getSharedPreferences(
//                "SETTING_FILE_NAME", Context.MODE_PRIVATE);
//
//        Integer KID = pref.getInt("KID", -1);
//        Integer AID = pref.getInt("AID", -1);
//        String savedSetting = pref.getString("setting", null);
//        assertEquals(-1, (int) KID);
//        assertEquals(-1, (int) AID);
//        assertEquals(savedSetting, null);
//        assertNotEquals(savedSetting, setting);
//    }
//
//    private void putSetting(String setting, String latitude, String longitude, String distance) {
//        DataHandler.deleteAll(ApplicationProvider.getApplicationContext());
//        int KID = DataHandler.addData("JeMoeder", latitude, longitude, distance, setting, ApplicationProvider.getApplicationContext());
//
//        ArrayList<mLocation> location =  DataHandler.loadLocations(ApplicationProvider.getApplicationContext());
//        assertEquals(location.get(0).getName(), "JeMoeder");
//        assertTrue(location.get(0).getKID() == KID);
//    }
//
//}