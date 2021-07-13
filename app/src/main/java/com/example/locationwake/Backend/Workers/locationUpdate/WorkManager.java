package com.example.locationwake.Backend.Workers.locationUpdate;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.locationwake.Logger;

public class WorkManager extends Worker {

    static final private String TAG = "WorkManager";

    private final String LocationTag = "LOCATIONWORKER";
    private final String SettingTag = "SETTINGWORKER";

    /**
     * constructor
     * @param context
     * @param workerParams
     */
    public WorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * creates all the individual workers
     * @return success
     */
    @NonNull
    @Override
    public Result doWork() {
        Logger.logD(TAG, "doWork(): started");
        OneTimeWorkRequest locationWorkRequest =
                new OneTimeWorkRequest.Builder(LocationWorker.class)
                        .addTag(LocationTag)
                        .build();

        OneTimeWorkRequest settingWorkRequest =
                new OneTimeWorkRequest.Builder(SettingWorker.class)
                        .addTag(SettingTag)
                        .build();

        androidx.work.WorkManager
                .getInstance(getApplicationContext())
                .beginWith(locationWorkRequest)
                .then(settingWorkRequest)
                .enqueue();

        Logger.logD(TAG, "doWork(): finished");
        return Result.success();
    }
}