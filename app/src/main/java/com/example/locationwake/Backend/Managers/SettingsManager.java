package com.example.locationwake.Backend.Managers;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    private final Context context;

    public SettingsManager(Context context) {
        this.context = context;
    }

    public int getLocationUpdateDelay() {
        SharedPreferences preferences = context.getSharedPreferences(
                "LOCATION_UPDATE_DELAY",
                Context.MODE_PRIVATE);

        return preferences.getInt("update_delay", 15);
    }

    public void setLocationUpdateDelay(int minutes) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "LOCATION_UPDATE_DELAY",
                Context.MODE_PRIVATE).edit();

        editor.putInt("update_delay", minutes);
        editor.apply();
    }

    public boolean getNotificationEnabled() {
        SharedPreferences preferences = context.getSharedPreferences(
                "NOTIFICATION_ENABLED",
                Context.MODE_PRIVATE);

        return preferences.getBoolean("enabled", false);
    }

    public void setNotificationsEnabled(boolean enabled) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "NOTIFICATION_ENABLED",
                Context.MODE_PRIVATE).edit();

        editor.putBoolean("enabled", enabled);
        editor.apply();
    }
}
