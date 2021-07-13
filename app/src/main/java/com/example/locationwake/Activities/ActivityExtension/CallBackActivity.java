package com.example.locationwake.Activities.ActivityExtension;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.example.locationwake.Backend.CallBack.CallBackManager;

public abstract class CallBackActivity extends AppCompatActivity {

    public abstract void onCallBack(boolean update, boolean succeeded, boolean failed, char type, String message);

    protected void addCallBack() {
        CallBackManager callBackManager = CallBackManager.getInstance();
        callBackManager.addActivity(this);
    }
}
