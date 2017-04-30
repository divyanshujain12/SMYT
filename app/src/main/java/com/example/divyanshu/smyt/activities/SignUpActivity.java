package com.example.divyanshu.smyt.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.CustomViews.CustomDateTimePickerHelper;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.Utils.Validation;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.divyanshu.smyt.Utils.Utils.DATE_FORMAT;
import static com.example.divyanshu.smyt.Utils.Utils.DEFAULT_DATE;

/**
 * Created by divyanshu on 8/26/2016.
 */
public class SignUpActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.firstnameET)
    EditText firstnameET;
    @InjectView(R.id.lastnameET)
    EditText lastnameET;
    @InjectView(R.id.phoneNumberET)
    EditText phoneNumberET;
    @InjectView(R.id.dobTV)
    TextView dobTV;
    @InjectView(R.id.emailET)
    EditText emailET;
    @InjectView(R.id.passwordET)
    EditText passwordET;
    @InjectView(R.id.reEnterPasswordET)
    EditText reEnterPasswordET;
    @InjectView(R.id.signUpBT)
    Button signUpBT;
    @InjectView(R.id.userNameET)
    EditText userNameET;
    @InjectView(R.id.radioM)
    RadioButton radioM;
    @InjectView(R.id.radioF)
    RadioButton radioF;
    @InjectView(R.id.radioGrp)
    RadioGroup radioGrp;
    private Validation validation;
    private HashMap<View, String> formValues;
    private String genderStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {

        genderStr = getString(R.string.gender_male);
        addValidation();
        radioGrp.setOnCheckedChangeListener(this);
    }


    @OnClick({R.id.signUpBT, R.id.dobTV})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBT:
                hitWebService();
                break;
            case R.id.dobTV:
                CustomDateTimePickerHelper.getInstance().showDateDialog(this, dobTV, DATE_FORMAT, DEFAULT_DATE,0);
                break;
        }
    }

    private void hitWebService() {
        formValues = validation.validate(this);
        if (formValues != null) {
            if (!formValues.get(passwordET).equals(formValues.get(reEnterPasswordET))) {
                CommonFunctions.getInstance().getErrorSnackbar(this).setText(R.string.err_password_mismatch).show();
                return;
            }

            CallWebService.getInstance(this, true, ApiCodes.SIGN_UP).hitJsonObjectRequestAPI(CallWebService.POST, API.REGISTRATION, createJsonForSignUp(), this);
        }
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        CustomAlertDialogs.showAlertDialog(this, getString(R.string.congratulation), response.getString(Constants.MESSAGE), this);

    }

    private void addValidation() {
        validation = new Validation();
        validation.addValidationField(new ValidationModel(firstnameET, Validation.TYPE_NAME_VALIDATION, getString(R.string.err_first_name)));
        validation.addValidationField(new ValidationModel(lastnameET, Validation.TYPE_NAME_VALIDATION, getString(R.string.err_last_name)));
        validation.addValidationField(new ValidationModel(userNameET, Validation.TYPE_NAME_VALIDATION, getString(R.string.err_user_name)));
        //validation.addValidationField(new ValidationModel(phoneNumberET, Validation.TYPE_PHONE_VALIDATION, getString(R.string.err_phone_number)));
        validation.addValidationField(new ValidationModel(dobTV, Validation.TYPE_EMPTY_FIELD_VALIDATION, getString(R.string.err_dob)));
        validation.addValidationField(new ValidationModel(emailET, Validation.TYPE_EMAIL_VALIDATION, getString(R.string.err_email)));
        validation.addValidationField(new ValidationModel(passwordET, Validation.TYPE_PASSWORD_VALIDATION, getString(R.string.err_pass)));
        validation.addValidationField(new ValidationModel(reEnterPasswordET, Validation.TYPE_PASSWORD_VALIDATION, getString(R.string.err_re_enter_pass)));
    }

    private JSONObject createJsonForSignUp() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FIRST_NAME, formValues.get(firstnameET));
            jsonObject.put(Constants.LAST_NAME, formValues.get(lastnameET));
            jsonObject.put(Constants.USER_NAME, formValues.get(userNameET));
            //jsonObject.put(Constants.PHONE_NUMBER, formValues.get(phoneNumberET));
            jsonObject.put(Constants.DATE_OF_BIRTH, formValues.get(dobTV));
            jsonObject.put(Constants.GENDER,genderStr);
            jsonObject.put(Constants.EMAIl, formValues.get(emailET));
            jsonObject.put(Constants.PASSWORD, formValues.get(passwordET));
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void doAction() {
        super.doAction();
        onBackPressed();
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioM:
                genderStr = getString(R.string.gender_male);
                break;
            case R.id.radioF:
                genderStr = getString(R.string.gender_female);
                break;
        }
    }
}
