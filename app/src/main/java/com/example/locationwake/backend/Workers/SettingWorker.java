package com.example.locationwake.backend.Workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.locationwake.Logger;
import com.example.locationwake.backend.Database.DataHandler;
import com.example.locationwake.backend.Database.mAttribute;
import com.example.locationwake.backend.Database.Attributes.mLocation;
import com.example.locationwake.backend.behaviour.Components.LocationComponent;
import com.example.locationwake.backend.behaviour.SettingObject;

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
    @NonNull
    @Override
    public Result doWork() {
        Logger.logD(TAG, "doWork(): started");
        //create notification channel
        //get all from database
        //TODO: for these to work, need to create instances of Component, that all have the correct implementation to check
        // TODO: begin easy, with only location
        //TODO: other options are time, batterylevel etc

        ArrayList<SettingObject> settingObjects = createSettingObjects();

        Integer[] activeKeys = getActiveKeys(settingObjects);
        // in case there has not been found any active settings, set special values
        if (activeKeys == null) {
            saveCurrentSetting(-1, -1, null);
            return Result.success();
        }

        String setting = DataHandler.getSetting(activeKeys[0], activeKeys[1], getApplicationContext());
        setSetting(setting);
        saveCurrentSetting(activeKeys[0], activeKeys[1], setting);
        Logger.logD(TAG, "doWork(): enabled setting " + setting);

        Logger.logD(TAG, "doWork(): finished");
        return Result.success();
    }

    /**
     * returns the setting which should be enabled by the system
     * @param settingObjects all objects that should be checked for active settings
     * @return SLT if silent, SND if sound, VBR if vibrate, None if no setting found
     */
    private Integer[] getActiveKeys(ArrayList<SettingObject> settingObjects) {
        for (SettingObject settingObject:settingObjects) {
            int activeAID = settingObject.isActive();
            if (activeAID != -1) {
                return new Integer[]{settingObject.getKID(), activeAID};
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
            ArrayList<mAttribute> mAttributes = DataHandler.loadAttribute(location.getKID(), mContext);
            for (mAttribute attribute:mAttributes) {
                SettingObject cSettingObject = new SettingObject(attribute.getKID(), attribute.getAID());
                LocationComponent addLocationComponent = new LocationComponent(mContext,
                        location,
                        attribute.getDistance());
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
    private void setSetting(String setting) {

        Logger.logD(TAG, "setSetting(): setting is " + setting);

        AudioManager am = (AudioManager) mContext.getSystemService(mContext.AUDIO_SERVICE);
//        switch(setting) {
//            case "SLT":
//                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//                am.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI);
//            case "VBR":
//                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//                am.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI);
//                //TODO: perhaps let user decide the volume
//            case "SND":
//                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                am.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI);
//        }
    }


    /**
     * saves the location in shared preferences
     */
    private void saveCurrentSetting(int KID, int AID, String setting) {
        SharedPreferences preferences = mContext.getSharedPreferences("SETTING_FILE_NAME", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("KID", KID);
        editor.putInt("AID", AID);
        editor.putString("setting", setting);
        editor.apply();
    }
}
