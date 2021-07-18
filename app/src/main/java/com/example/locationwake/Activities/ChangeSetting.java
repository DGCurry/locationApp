package com.example.locationwake.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.locationwake.Logger;
import com.example.locationwake.R;

public class ChangeSetting extends AppCompatActivity {

    //TAG of the class
    static final private String TAG = "ChangeSetting";

    /**
     * Method to start activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createUI();
    }

    /**
     * Creates the GUI
     */
    private void createUI() {
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);

        Button silentButton = findViewById(R.id.button_navigation_left);
        Button vibrateButton = findViewById(R.id.button_navigation_middle);
        Button soundButton = findViewById(R.id.button_navigation_right);

        silentButton.setOnClickListener(new View.OnClickListener() {
        // TODO: lower API for AudioManager.ADJUST_MUTE
            @Override
            public void onClick(View v) {
                if (!am.isVolumeFixed()) {
                    am.setRingerMode(0);
                    am.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI);
                    Logger.logD(TAG, "createUI(): onClick(View v): put into SILENT mode " + am.getRingerMode());

                } else {
                    Logger.logE(TAG, "createUI(): onClick(View v): cannot set SILENT mode");
                }
            }
        });

        vibrateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!am.isVolumeFixed()) {
                    am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    am.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI);
                    Logger.logD(TAG, "createUI(): onClick(View v): put into VIBRATE mode " + am.getMode());

                } else {
                    Logger.logE(TAG, "createUI(): onClick(View v): cannot set SILENT mode");
                }
            }
        });

        soundButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!am.isVolumeFixed()) {
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    am.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI);
                    Logger.logD(TAG, "createUI(): onClick(View v): put into SOUND mode " + am.getRingerMode());

                } else {
                    Logger.logE(TAG, "createUI(): onClick(View v): cannot set SILENT mode");
                }
            }
        });
    }
}