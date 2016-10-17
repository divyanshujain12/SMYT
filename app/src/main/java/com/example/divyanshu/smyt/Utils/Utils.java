package com.example.divyanshu.smyt.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class Utils {
    static String DAYS = "days";
    static String HOURS = "hrs";
    static String MINUTES = "mins";
    static String SECONDS = "secs";

    public static Bitmap getRoundedCornerBitmap(Context context, int resource) {
        Bitmap mbitmap = ((BitmapDrawable) context.getResources().getDrawable(resource)).getBitmap();
        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas = new Canvas(imageRounded);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 100, 100, mpaint);

        return imageRounded;
    }

    public static void configureToolbarWithBackButton(final AppCompatActivity appCompatActivity, Toolbar toolbar, String name) {
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        actionBar.setTitle(name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appCompatActivity.onBackPressed();
            }
        });
    }

    public static void configureToolbarWithOutBackButton(final AppCompatActivity appCompatActivity, Toolbar toolbar, String name) {
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        actionBar.setTitle(name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appCompatActivity.onBackPressed();
            }
        });
    }

    public static String getTimeDifference(String time) {
        String timeDifference = "";
        Date currentDate = Calendar.getInstance().getTime();
        Date givenDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            givenDate = simpleDateFormat.parse(time);
            long differenceInMillisecond = givenDate.getTime() - currentDate.getTime();
            timeDifference = getActiveTime(differenceInMillisecond);
            return timeDifference;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDifference;
    }

    public static String getActiveTime(long active_before) {
        String timePostFix = DAYS;
        long tempMillis = active_before;
        active_before = TimeUnit.MILLISECONDS.toDays(tempMillis);
        if (active_before <= 0) {
            active_before = TimeUnit.MILLISECONDS.toHours(tempMillis);
            timePostFix = HOURS;
        }
        if (active_before <= 0) {
            active_before = TimeUnit.MILLISECONDS.toMinutes(tempMillis);
            timePostFix = MINUTES;
        }
        if (active_before <= 0) {
            active_before = TimeUnit.MILLISECONDS.toSeconds(tempMillis);
            timePostFix = SECONDS;
        }
        return String.valueOf(active_before) + " " + timePostFix;
    }
}
