package com.example.divyanshu.smyt.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.MySharedPereference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = null;
                if (!MySharedPereference.getInstance().getBoolean(SplashActivity.this, Constants.IS_LOGGED_IN)) {
                    i = new Intent(SplashActivity.this, LoginActivity.class);

                } else {
                    i = new Intent(SplashActivity.this, CategoriesActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, 3000);

    }

    Handler handler = new Handler();
}
