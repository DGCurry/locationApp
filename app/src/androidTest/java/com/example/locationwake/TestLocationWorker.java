//package com.example.locationwake;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.location.Location;
//import android.preference.PreferenceManager;
//
//import androidx.test.rule.GrantPermissionRule;
//
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.platform.app.InstrumentationRegistry;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.work.ListenableWorker;
//import androidx.work.Worker;
//import androidx.work.testing.TestWorkerBuilder;
//
//import com.example.locationwake.backend.Workers.LocationWorker;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//import static org.junit.Assert.*;
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(AndroidJUnit4.class)
//public class TestLocationWorker {
//
//    @Rule
//    public GrantPermissionRule location_access = GrantPermissionRule.grant(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//
//    @Rule
//    public GrantPermissionRule location_access_fine = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
//
//    private Context context;
//
//    @Before
//    public void setUp() {
//        context = ApplicationProvider.getApplicationContext();
//    }
//
//    @Test
//    public void workerSuccess() {
//        LocationWorker worker =
//                (LocationWorker) TestWorkerBuilder.from(context,
//                        LocationWorker.class)
//                        .build();
//
//        ListenableWorker.Result result = worker.doWork();
//        assertEquals(result, ListenableWorker.Result.success());
//    }
//
//    @Test
//    public void saveLocationSuccess() {
//        //TODO get location, save it and retrieve it
//        Location location = new Location("");//provider name is unnecessary
//        location.setLatitude(100f);
//        location.setLongitude(100f);
//    }
//
//    @Test
//    public void workerLocationRetrieve() throws InterruptedException {
//        //TODO: fix
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putFloat("location_lat", 100f);
//        editor.putFloat("location_lon", -100f);
//        editor.apply();
//
//        LocationWorker worker =
//                (LocationWorker) TestWorkerBuilder.from(context,
//                        LocationWorker.class)
//                        .build();
//
//        ListenableWorker.Result result = worker.doWork();
//        if (result.equals(ListenableWorker.Result.success())) {
//
//            SharedPreferences pref = context.getSharedPreferences(
//                    "LOCATION_FILE_NAME", Context.MODE_PRIVATE);
//
//            Float location_lat = pref.getFloat("location_lat", 0f);
//            Float location_lon = pref.getFloat("location_lon", 0f);
//
//            assertNotEquals(location_lat, 100f);
//            assertNotEquals(location_lon, -100f);
//
//        }
//    }
//
//}