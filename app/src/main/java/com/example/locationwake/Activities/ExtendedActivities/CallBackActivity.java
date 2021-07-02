package com.example.locationwake.Activities.ExtendedActivities;

import androidx.appcompat.app.AppCompatActivity;

public abstract class CallBackActivity extends AppCompatActivity {

    public abstract void onCallBack(boolean update, boolean succeeded, boolean failed, char type, String message);
}
