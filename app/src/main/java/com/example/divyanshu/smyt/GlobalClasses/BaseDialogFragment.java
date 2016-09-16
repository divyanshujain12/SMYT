package com.example.divyanshu.smyt.GlobalClasses;


import android.support.v4.app.DialogFragment;
import android.view.View;

import com.example.divyanshu.smyt.Interfaces.CallBackInterface;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Interfaces.UpdateUiCallback;
import com.example.divyanshu.smyt.Utils.CallWebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by divyanshu on 9/3/2016.
 */
public class BaseDialogFragment extends DialogFragment implements CallWebService.ObjectResponseCallBack, SnackBarCallback, UpdateUiCallback, RecyclerViewClick {

    @Override
    public void doAction() {

    }

    @Override
    public void updateUi(String string) {

    }

    @Override
    public void onClickItem(int position, View view) {

    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {

    }

    @Override
    public void onFailure(String str, int apiType) {

    }
}
