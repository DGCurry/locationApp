//package com.example.locationwake;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.SharedPreferences;
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
//import com.example.locationwake.backend.Workers.WorkManager;
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
//public class TestWorkManager {
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
//        WorkManager worker =
//                (WorkManager) TestWorkerBuilder.from(context,
//                        WorkManager.class)
//                        .build();
//
//        ListenableWorker.Result result = worker.doWork();
//        assertEquals(result, ListenableWorker.Result.success());
//    }
//}