package com.example.divyanshu.smyt;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = null;
                //if (MySharedPereference.getInstance().getBoolean(SplashActivity.this, Constants.LOGGED_IN)) {
                i = new Intent(SplashActivity.this, LoginActivity.class);

               /* } else {
                    i = new Intent(SplashActivity.this, AppIntroActivity.class);
                }*/
                startActivity(i);
                finish();
            }
        }, 3000);

    }

    Handler handler = new Handler();
}
