package com.example.locationwake.Activities.NewLocationActiivties;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.locationwake.Activities.HelperClasses.FormCallBack;
import com.example.locationwake.R;

public class AddLocationActivity extends AppCompatActivity implements FormCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
    }

    @Override
    public void callBack(int position, String message) {
    }
}