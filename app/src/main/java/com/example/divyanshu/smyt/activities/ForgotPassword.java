package com.example.divyanshu.smyt.activities;

import android.os.Bundle;
import android.view.View;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.Utils.Validation;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ForgotPassword extends BaseActivity {

    @InjectView(R.id.emailET)
    EditText emailET;
    @InjectView(R.id.signInBT)
    Button signInBT;
    private Validation validation;
    private HashMap<View, String> validationMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {
        validation = new Validation();
        validation.addValidationField(new ValidationModel(emailET, Validation.TYPE_EMAIL_VALIDATION, getString(R.string.err_email)));

    }

    private JSONObject createJsonObjectForForgotPassword() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.EMAIl, validationMap.get(emailET));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @OnClick(R.id.signInBT)
    public void onClick() {
        validationMap = validation.validate(this);
        if (validationMap != null) {
            CallWebService.getInstance(this, true, ApiCodes.FORGOT_PASSWORD).hitJsonObjectRequestAPI(CallWebService.POST, API.FORGOT_PASSWORD, createJsonObjectForForgotPassword(), this);
        }

    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);

        CustomAlertDialogs.showAlertDialog(this, "ALERT", response.getString(Constants.MESSAGE), this);
    }

    @Override
    public void doAction() {
        super.doAction();
        finish();
    }
}
