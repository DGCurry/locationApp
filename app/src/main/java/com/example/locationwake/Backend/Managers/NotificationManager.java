package com.example.locationwake.Backend.Managers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.os.Build;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;

import com.example.locationwake.Activities.ActivityExtension.CallBackActivity;
import com.example.locationwake.Logger;
import com.example.locationwake.R;

import java.util.ArrayList;

public class NotificationManager {

    /**
     * Tag of the class
     */
    static final private String TAG = "NotificationManager";

    private final String NOTIFICATION_ID;
    private String NOTIFICATION_TITLE;
    private String message;
    private final Context context;

    public NotificationManager(String NOTIFICATION_ID, String NOTIFICATION_TITLE, String message, Context context) {
        this.NOTIFICATION_ID = NOTIFICATION_ID;
        this.NOTIFICATION_TITLE = NOTIFICATION_TITLE;
        this.message = message;
        this.context = context;
    }

    /**
     * method that creates the foreground notification information
     * @return
     */
    @NonNull
    public ForegroundInfo createForegroundInfo() {
        //TODO: check how to change notification information
        //TODO: make notificationID a variable
        return new ForegroundInfo(1, createNotification());
    }

    /**
     * method that creates the notification to ensure the thread is foreground
     * @return
     */
    private Notification createNotification() {
        //TODO: change information into resource

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(NOTIFICATION_ID, NOTIFICATION_TITLE);
        }

        return new NotificationCompat.Builder(context, NOTIFICATION_ID)
                .setContentTitle(NOTIFICATION_TITLE)
                .setTicker(NOTIFICATION_TITLE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .build();
    }

    /**
     * channel for notification
     * @param channelID
     * @param name
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel(String channelID, String name) {
        //TODO: get string from resources, get requiresApi working
        String description = "mah";
        NotificationChannel channel = new NotificationChannel(channelID, name, android.app.NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(description);
        android.app.NotificationManager notificationManager = context.getSystemService(android.app.NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
