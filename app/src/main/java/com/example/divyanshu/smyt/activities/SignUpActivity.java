package com.example.divyanshu.smyt.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.CustomAlertDialogs;
import com.example.divyanshu.smyt.Utils.Validation;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.divyanshu.smyt.R.styleable.View;

/**
 * Created by divyanshu on 8/26/2016.
 */
public class SignUpActivity extends BaseActivity {

    @InjectView(R.id.firstnameET)
    EditText firstnameET;
    @InjectView(R.id.lastnameET)
    EditText lastnameET;
    @InjectView(R.id.phoneNumberET)
    EditText phoneNumberET;
    @InjectView(R.id.dobET)
    EditText dobET;
    @InjectView(R.id.emailET)
    EditText emailET;
    @InjectView(R.id.passwordET)
    EditText passwordET;
    @InjectView(R.id.reEnterPasswordET)
    EditText reEnterPasswordET;
    @InjectView(R.id.signUpBT)
    Button signUpBT;
    private Validation validation;
    private HashMap<EditText, String> formValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {
        addValidation();
        dobET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    new DatePickerDialog(SignUpActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                return false;
            }
        });
    }


    @OnClick(R.id.signUpBT)
    public void onClick() {

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
        validation.addValidationField(new ValidationModel(phoneNumberET, Validation.TYPE_PHONE_VALIDATION, getString(R.string.err_phone_number)));
        validation.addValidationField(new ValidationModel(dobET, Validation.TYPE_EMPTY_FIELD_VALIDATION, getString(R.string.err_dob)));
        validation.addValidationField(new ValidationModel(emailET, Validation.TYPE_EMAIL_VALIDATION, getString(R.string.err_email)));
        validation.addValidationField(new ValidationModel(passwordET, Validation.TYPE_PASSWORD_VALIDATION, getString(R.string.err_pass)));
        validation.addValidationField(new ValidationModel(reEnterPasswordET, Validation.TYPE_PASSWORD_VALIDATION, getString(R.string.err_re_enter_pass)));
    }

    private JSONObject createJsonForSignUp() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FIRST_NAME, formValues.get(firstnameET));
            jsonObject.put(Constants.LAST_NAME, formValues.get(lastnameET));
            jsonObject.put(Constants.PHONE_NUMBER, formValues.get(phoneNumberET));
            jsonObject.put(Constants.DATE_OF_BIRTH, formValues.get(dobET));
            jsonObject.put(Constants.EMAIl, formValues.get(emailET));
            jsonObject.put(Constants.PASSWORD, formValues.get(passwordET));


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

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dobET.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth));
            // updateLabel();
        }

    };


    private void updateLabel() {

        String myFormat = "yyyy-mm-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dobET.setText(sdf.format(myCalendar.getTime()));
    }

}
