package com.example.divyanshu.smyt.activities;

import android.os.Bundle;

import com.example.divyanshu.smyt.CustomViews.ToolbarWithBackButton;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {

    }

    @OnClick(R.id.signUpBT)
    public void onClick() {
        onBackPressed();
    }
}
