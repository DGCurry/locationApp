//package com.example.locationwake.Activities;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import com.example.locationwake.Logger;
//import com.example.locationwake.R;
//
//public class ChangeSetting extends AppCompatActivity {
//
//    //TAG of the class
//    static final private String TAG = "ChangeSetting";
//
//    AudioManager am;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_change_setting);
//        am = (AudioManager)getSystemService(AUDIO_SERVICE);
//        createUI();
//    }
//
//    private void createUI() {
//        Button silentButton = findViewById(R.id.button_silent);
//        Button vibrateButton = findViewById(R.id.button_vibrate);
//        Button soundButton = findViewById(R.id.button_sound);
//
//        silentButton.setOnClickListener(new View.OnClickListener() {
//        // TODO: lower API for AudioManager.ADJUST_MUTE
//            @Override
//            public void onClick(View v) {
//                if (!am.isVolumeFixed()) {
//                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//                    am.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI);
//                    Logger.logD(TAG, "createUI(): onClick(View v): put into SILENT mode " + am.getRingerMode());
//
//                } else {
//                    Logger.logE(TAG, "createUI(): onClick(View v): cannot set SILENT mode");
//                }
//            }
//        });
//
//        vibrateButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (!am.isVolumeFixed()) {
//                    am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//                    am.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI);
//                    Logger.logD(TAG, "createUI(): onClick(View v): put into VIBRATE mode " + am.getMode());
//
//                } else {
//                    Logger.logE(TAG, "createUI(): onClick(View v): cannot set SILENT mode");
//                }
//            }
//        });
//
//        soundButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (!am.isVolumeFixed()) {
//                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                    am.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI);
//                    Logger.logD(TAG, "createUI(): onClick(View v): put into SOUND mode " + am.getRingerMode());
//
//                } else {
//                    Logger.logE(TAG, "createUI(): onClick(View v): cannot set SILENT mode");
//                }
//            }
//        });
//    }
//}