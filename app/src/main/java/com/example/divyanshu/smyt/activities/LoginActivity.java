package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.ServicesAndNotifications.NewChallengeNotificationService;
import com.example.divyanshu.smyt.ServicesAndNotifications.UpcomingRoundNotificationService;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.Utils.Validation;
import com.google.firebase.iid.FirebaseInstanceId;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu on 8/26/2016.
 */
public class LoginActivity extends BaseActivity {

    @InjectView(R.id.emailET)
    EditText emailET;
    @InjectView(R.id.passwordET)
    EditText passwordET;
    @InjectView(R.id.signInBT)
    Button signInBT;
    @InjectView(R.id.forgotPassTV)
    TextView forgotPassTV;
    @InjectView(R.id.signUpBT)
    Button signUpBT;

    private Validation validation;
    private HashMap<View, String> validationMap = new HashMap<>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.inject(this);
        addValidation();

    }

    private void addValidation() {
        validation = new Validation();
        validation.addValidationField(new ValidationModel(emailET, Validation.TYPE_NAME_VALIDATION, getString(R.string.err_user_name)));
        validation.addValidationField(new ValidationModel(passwordET, Validation.TYPE_PASSWORD_VALIDATION, getString(R.string.err_pass)));
        //emailET.setText("divyanshujain12@hotmail.com");
        //passwordET.setText("divyanshu");
    }


    @OnClick({R.id.signInBT,  R.id.forgotPassTV, R.id.signUpBT})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signInBT:
                validateFields();

                break;
            case R.id.signUpBT:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.forgotPassTV:
                Intent forgotPasswordIntent = new Intent(this, ForgotPassword.class);
                startActivity(forgotPasswordIntent);
                break;
        }
    }

    private void validateFields() {

        validationMap = validation.validate(this);
        if (validationMap != null) {
            CallWebService.getInstance(this, true, ApiCodes.SIGN_IN).hitJsonObjectRequestAPI(CallWebService.POST, API.LOGIN, createJsonForSignIn(), this);
        }
    }

    private JSONObject createJsonForSignIn() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.EMAIl, validationMap.get(emailET));
            jsonObject.put(Constants.PASSWORD, validationMap.get(passwordET));
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);

        UserModel userModel = UniversalParser.getInstance().parseJsonObject(response.getJSONObject(Constants.DATA), UserModel.class);
        MySharedPereference.getInstance().setString(this, Constants.FIRST_NAME, userModel.getFirst_name());
        MySharedPereference.getInstance().setString(this, Constants.LAST_NAME, userModel.getLast_name());
        MySharedPereference.getInstance().setString(this, Constants.PHONE_NUMBER, userModel.getPhonenumber());
        MySharedPereference.getInstance().setString(this, Constants.EMAIl, userModel.getEmail());
        MySharedPereference.getInstance().setString(this, Constants.DATE_OF_BIRTH, userModel.getDate_of_birth());
        MySharedPereference.getInstance().setString(this, Constants.PASSWORD, validationMap.get(passwordET));
        MySharedPereference.getInstance().setString(this, Constants.CUSTOMER_ID, userModel.getCustomer_id());
        MySharedPereference.getInstance().setBoolean(this, Constants.IS_LOGGED_IN, true);

        sendRegistrationToServer();
        startServices();

        Intent categoryIntent = new Intent(this, CategoriesActivity.class);
        startActivity(categoryIntent);
        finish();
    }

    private void sendRegistrationToServer() {
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null && !token.equals(""))
            CallWebService.getInstance(getBaseContext(), false, ApiCodes.FCM_ID).hitJsonObjectRequestAPI(CallWebService.POST, API.UPDATE_FCM_ID, createJsonForSavingFcmID(token), null);
    }

    private void startServices() {
        Intent intent = new Intent(this, UpcomingRoundNotificationService.class);
        startService(intent);
        intent = new Intent(this, NewChallengeNotificationService.class);
        startService(intent);
    }

    private JSONObject createJsonForSavingFcmID(String id) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getBaseContext());
        try {
            jsonObject.put(Constants.FCM_ID, id);
            jsonObject.put(Constants.MOBILE_TYPE, getString(R.string.android));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
