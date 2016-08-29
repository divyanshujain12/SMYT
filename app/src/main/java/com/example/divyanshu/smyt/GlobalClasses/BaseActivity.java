package com.example.divyanshu.smyt.GlobalClasses;

import android.support.v7.app.AppCompatActivity;
import com.example.divyanshu.smyt.Interfaces.CallBackInterface;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Interfaces.UpdateUiCallback;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by divyanshu on 5/29/2016.
 */
public class BaseActivity extends AppCompatActivity implements CallBackInterface,SnackBarCallback,UpdateUiCallback {
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
}
