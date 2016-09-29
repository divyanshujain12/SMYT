package com.example.divyanshu.smyt.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu.jain on 9/29/2016.
 */

public class UserSettingActivity extends BaseActivity {


    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.userIV)
    ImageView userIV;
    @InjectView(R.id.changeUserImageIV)
    ImageView changeUserImageIV;
    @InjectView(R.id.firstNameET)
    EditText firstNameET;
    @InjectView(R.id.lastNameET)
    EditText lastNameET;
    @InjectView(R.id.mobileET)
    EditText mobileET;
    @InjectView(R.id.statusET)
    EditText statusET;
    @InjectView(R.id.aboutUsTV)
    TextView aboutUsTV;
    @InjectView(R.id.contactUsTV)
    TextView contactUsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        ButterKnife.inject(this);


        InitViews();
    }

    private void InitViews() {
        Utils.configureToolbarWithBackButton(this, toolbarView, getString(R.string.setting));
        String firstName = MySharedPereference.getInstance().getString(this, Constants.FIRST_NAME);
        String lastName = MySharedPereference.getInstance().getString(this, Constants.LAST_NAME);
        String phoneNumber = MySharedPereference.getInstance().getString(this, Constants.PHONE_NUMBER);
        String status = MySharedPereference.getInstance().getString(this, Constants.STATUS);

        firstNameET.setText(firstName);
        lastNameET.setText(lastName);
        mobileET.setText(phoneNumber);
        statusET.setText(status);
    }


    @OnClick({R.id.aboutUsTV, R.id.contactUsTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aboutUsTV:
                break;
            case R.id.contactUsTV:
                break;
        }
    }
}
