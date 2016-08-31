package com.example.divyanshu.smyt.GlobalClasses;


import android.support.v4.app.Fragment;
import android.view.View;

import com.example.divyanshu.smyt.Interfaces.CallBackInterface;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Interfaces.UpdateUiCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by divyanshu on 5/29/2016.
 */
public class BaseFragment extends Fragment implements CallBackInterface, SnackBarCallback, UpdateUiCallback, RecyclerViewClick {
    @Override
    public void onJsonObjectSuccess(JSONObject object) throws JSONException {

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

    @Override
    public void onClickItem(int position, View view) {

    }

}
