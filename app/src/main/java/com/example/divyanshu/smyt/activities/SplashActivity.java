package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.MySharedPereference;

import org.jsoup.Jsoup;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //checkNewVersion();
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

    private void checkNewVersion() {
        new GetLatestVersion().execute();

    }

    private class GetLatestVersion extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String newVersion = Jsoup
                        .connect(
                                "https://play.google.com/store/apps/details?id="
                                        + getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent(
                                "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com").get()
                        .select("div[itemprop=softwareVersion]").first()
                        .ownText();
                return newVersion;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
