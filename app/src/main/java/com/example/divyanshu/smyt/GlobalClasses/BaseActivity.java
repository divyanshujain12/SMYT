package com.example.divyanshu.smyt.GlobalClasses;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.divyanshu.smyt.Interfaces.CallBackInterface;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Interfaces.UpdateUiCallback;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by divyanshu on 5/29/2016.
 */
public class BaseActivity extends AppCompatActivity implements CallBackInterface, SnackBarCallback, UpdateUiCallback,RecyclerViewClick {
    @Override
    public void onJsonObjectSuccess(JSONObject object) {

    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {

    }

    @Override
    public void doAction() {

    }

    @Override
    public void updateUi(String string) {

    }

    public void showDialogFragment(DialogFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment.show(fragmentManager, fragment.getClass().getName());
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level > TRIM_MEMORY_MODERATE) {
            // Restart app so data is reloaded
            android.os.Process.killProcess(android.os.Process.myPid());

        }

    }

    @Override
    public void onClickItem(int position, View view) {

    }
}
