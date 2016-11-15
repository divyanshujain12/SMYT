package com.example.divyanshu.smyt.GlobalClasses;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomDateTimePickerHelper;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Interfaces.UpdateUiCallback;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by divyanshu on 5/29/2016.
 */
public class BaseActivity extends AppCompatActivity implements CallWebService.ObjectResponseCallBack, SnackBarCallback, UpdateUiCallback, RecyclerViewClick {

    @Override
    public void onAlertButtonPressed() {

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

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {

    }

    @Override
    public void onFailure(String str, int apiType) {
        CommonFunctions.getInstance().showErrorSnackBar(this, str);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isLogin = MySharedPereference.getInstance().getBoolean(this, Constants.IS_LOGGED_IN);
        if (isLogin && !MySharedPereference.getInstance().getBoolean(this, Constants.LAST_LOGIN)) {
            MySharedPereference.getInstance().setBoolean(this, Constants.LAST_LOGIN, true);
            CallWebService.getInstance(this, false, ApiCodes.UPDATE_LAST_ACTIVE_TIME).hitJsonObjectRequestAPI(CallWebService.POST, API.USER_ACTIVE_STATUS, createJsonForUpdateActiveStatus(1), this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean isLogin = MySharedPereference.getInstance().getBoolean(this, Constants.IS_LOGGED_IN);
        if (isLogin && MySharedPereference.getInstance().getBoolean(this, Constants.LAST_LOGIN)) {
            MySharedPereference.getInstance().setBoolean(this, Constants.LAST_LOGIN, false);
            CallWebService.getInstance(this, false, ApiCodes.UPDATE_LAST_ACTIVE_TIME).hitJsonObjectRequestAPI(CallWebService.POST, API.USER_ACTIVE_STATUS, createJsonForUpdateActiveStatus(0), this);
        }
    }

    private JSONObject createJsonForUpdateActiveStatus(int i) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.LAST_LOGIN,  Utils.getCurrentTime( Utils.CURRENT_DATE_FORMAT));
            jsonObject.put(Constants.AVAILABLE, i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
