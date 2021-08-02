package com.example.locationwake.Backend.Workers.locationUpdate;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.locationwake.Backend.Managers.CallBackManager;
import com.example.locationwake.Logger;
import com.example.locationwake.Backend.Database.DataHandler;
import com.example.locationwake.Backend.Database.mAttribute;
import com.example.locationwake.Backend.Database.Attributes.mLocation;
import com.example.locationwake.Backend.Behaviour.Components.LocationComponent;
import com.example.locationwake.Backend.Behaviour.SettingObject;

import java.util.ArrayList;

/**
 * Class that will use the location to get information from the database, and then check for
 * possible settings
 */
public class SettingWorker extends Worker {

    /**
     * Tag of the class
     */
    static final private String TAG = "SettingWorker";

    /**
     * Notification ID and Title for the notification needed for a Foreground Worker
     */
    int NOTIFICATION_ID = 1;
    String CHANNEL_ID = "notification_SETTING";
    String NOTIFICATION_TITLE = "SETTING worker";

    Context mContext;

    /**
     * Constructor of the worker
     * @param context context of the application
     * @param workerParams ..
     */
    public SettingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }


    /**
     * method that starts the worker. creates components from saved data, and checks if a component
     * is active. If there are no component, it does nothing. Else it changes the setting of the phone
     * also saves values to shared preferences.
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Result doWork() {
        Logger.logD(TAG, "doWork(): started");

        //create notification channel
        com.example.locationwake.Backend.Managers.NotificationManager notificationManager =
                new com.example.locationwake.Backend.Managers.NotificationManager(NOTIFICATION_ID
                        , NOTIFICATION_TITLE, "Retrieving and checking settings for your system", getApplicationContext(),
                        3, CHANNEL_ID);
        setForegroundAsync(notificationManager.createForegroundInfo());

        //get all from database
        //TODO: for these to work, need to create instances of Component, that all have the correct implementation to check
        // TODO: begin easy, with only location
        //TODO: other options are time, batterylevel etc

        ArrayList<SettingObject> settingObjects = createSettingObjects();

        String[] activeKeys = getActiveKeys(settingObjects);

        // in case there has not been found any active settings, set special values
        if (activeKeys == null) {
            saveCurrentSetting("None", "None", null);
            Logger.logD(TAG, "doWork(): calling callBack on activities for UI update");
            CallBackManager.callBackActivities(false, false, true, 's', "setting has been updated, none found");
            return Result.success();
        }

        String setting = DataHandler.getSetting(activeKeys[0], activeKeys[1], getApplicationContext()).getSetting();
        setSetting(setting);

        saveCurrentSetting(activeKeys[0], activeKeys[1], setting);
        Logger.logD(TAG, "doWork(): enabled setting " + setting);

        Logger.logD(TAG, "doWork(): calling callBack on activities for UI update");
        CallBackManager.callBackActivities(true, false, false, 's', "setting has been updated");

        Logger.logD(TAG, "doWork(): finished");
        return Result.success();
    }

    /**
     * returns the setting which should be enabled by the system
     * @param settingObjects all objects that should be checked for active settings
     * @return SLT if silent, SND if sound, VBR if vibrate, None if no setting found
     */
    private String[] getActiveKeys(ArrayList<SettingObject> settingObjects) {
        for (SettingObject settingObject:settingObjects) {
            String activeAID = settingObject.isActive();
            if (!activeAID.equals("None")) {
                return new String[]{settingObject.getLID(), activeAID};
            }
        }
        return null;
    }

    /**
     * creates list of settingobjects, with the attributes loaded in.
     * @return
     */
    private ArrayList<SettingObject> createSettingObjects() {
        /*important a settingobject represents one attribute. Components are loaded in to check
         * if all components are active, then the AID of the active attribute will be returned
         */
        ArrayList<SettingObject> settingObjects = new ArrayList<>();
        ArrayList<mLocation> locations =  DataHandler.loadLocations(mContext);
        // going through each location, get the ID, create a SettingObject with this.
        for (mLocation location:locations) {
            ArrayList<mAttribute> mAttributes = DataHandler.loadAttributes(location.getLID(), mContext);
            for (mAttribute attribute:mAttributes) {
                SettingObject cSettingObject = new SettingObject(attribute.getLID(), attribute.getAID());
                LocationComponent addLocationComponent = new LocationComponent(mContext,
                        location,
                        attribute.getRadius());
                // important need to add more components when more attributes kinds are added
                cSettingObject.addComponent(addLocationComponent);
                settingObjects.add(cSettingObject);
            }
        }
        return settingObjects;
    }

    /**
     * method to set the sound setting as requested and found
     * @param setting SND, SLT, VBR
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setSetting(String setting) {
        Logger.logD(TAG, "setSetting(): setting is " + setting);

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED) {

            Logger.logD(TAG, "setSetting(): allowed to change setting");

            AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            switch(setting) {
                case "SLT":
                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    am.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI);
                case "VBR":
                    am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    am.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI);
                    //TODO: perhaps let user decide the volume
                case "SND":
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    am.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI);
            }
        }

    }


    /**
     * saves the location in shared preferences
     */
    private void saveCurrentSetting(String LID, String AID, String setting) {
        SharedPreferences preferences = mContext.getSharedPreferences("SETTING_FILE_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LID", LID);
        editor.putString("AID", AID);
        editor.putString("setting", setting);
        editor.apply();
    }
}
