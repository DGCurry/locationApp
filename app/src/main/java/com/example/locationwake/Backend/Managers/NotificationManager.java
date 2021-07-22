package com.example.locationwake.Backend.Managers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.os.Build;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

    private final int PRIORITY_INT;
    private final int NOTIFICATION_ID;
    private final String CHANNEL_ID;
    private String NOTIFICATION_TITLE;
    private String message;
    private final Context context;

    public NotificationManager(int NOTIFICATION_ID, String NOTIFICATION_TITLE, String message,
                               Context context, int PRIORITY_INT, String CHANNEL_ID) {
        this.NOTIFICATION_ID = NOTIFICATION_ID;
        this.NOTIFICATION_TITLE = NOTIFICATION_TITLE;
        this.message = message;
        this.context = context;
        this.PRIORITY_INT = PRIORITY_INT;
        this.CHANNEL_ID = CHANNEL_ID;
    }

    /**
     * method that creates the foreground notification information
     * @return
     */
    @NonNull
    public ForegroundInfo createForegroundInfo() {
        //TODO: check how to change notification information
        //TODO: make notificationID a variable
        return new ForegroundInfo(NOTIFICATION_ID, createNotification());
    }

    /**
     * Method that creates a notification from the front-end
     */
    public void createNotificationFromFrontEnd() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, createNotification());
    }


    /**
     * method that creates the notification
     * @return
     */
    private Notification createNotification() {
        //TODO: change information into resource

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(CHANNEL_ID, NOTIFICATION_TITLE, message);
        }

        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText("Much longer text that cannot fit one line... TODO")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setPriority(PRIORITY_INT)
                .build();
    }

    /**
     * channel for notification
     * @param CHANNEL_ID
     * @param name
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel(String CHANNEL_ID, String name, String description) {
        //TODO: get string from resources, get requiresApi working
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, PRIORITY_INT);
        channel.setDescription(description);
        android.app.NotificationManager notificationManager = context.getSystemService(android.app.NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
