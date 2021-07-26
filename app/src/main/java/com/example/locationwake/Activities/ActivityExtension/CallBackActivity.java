package com.example.locationwake.Activities.ActivityExtension;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationwake.Backend.Managers.CallBackManager;

/**
 * Class to be extended. Gives activities the means to listen to CallBacks.
 */
public abstract class CallBackActivity extends AppCompatActivity {

    @Override
    public void finish() {
        super.finish();
        CallBackManager callBackManager = CallBackManager.getInstance();
        callBackManager.removeActivity(this);
    }

    /**
     * Creates a signature for Activities to handle CallBacks.
     * @param update if the Activity should update components of itself, this is true
     * @param succeeded if an action called by the Activity has succeeded, this is true
     * @param failed if an action called by the Activity has failed, this is true
     * @param type to distinguish between more CallBacks with the same boolean values, a Char can be added
     * @param message to give the user or developer feedback, a message can be added
     */
    public abstract void onCallBack(boolean update, boolean succeeded, boolean failed, char type, String message);

    /**
     * Method to add the Activity to the CallBack activity list
     */
    protected void addCallBack() {
        CallBackManager callBackManager = CallBackManager.getInstance();
        callBackManager.addActivity(this);
    }
}
