package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu on 8/26/2016.
 */
public class LoginActivity extends BaseActivity {

    @InjectView(R.id.usernameET)
    EditText usernameET;
    @InjectView(R.id.passwordET)
    EditText passwordET;
    @InjectView(R.id.signInBT)
    Button signInBT;
    @InjectView(R.id.signUpTV)
    TextView signUpTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.signInBT, R.id.signUpTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signInBT:
                Intent categoryIntent = new Intent(this, CategoriesActivity.class);
                startActivity(categoryIntent);
                break;
            case R.id.signUpTV:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }
}
