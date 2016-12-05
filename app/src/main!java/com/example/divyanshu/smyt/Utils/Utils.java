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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class Utils {
    static String DAYS = "days";
    static String HOURS = "hrs";
    static String MINUTES = "mins";
    static String SECONDS = "secs";
    static String AGO = "ago";

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "hh:mm aa";
    public static final String DEFAULT_DATE = "1940-01-01";
    public static final String POST_CHALLENGE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;
    public static final String CURRENT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CURRENT_DATE_FORMAT);
        try {
            givenDate = simpleDateFormat.parse(time);
            long differenceInMillisecond = givenDate.getTime() - currentDate.getTime();
            timeDifference = getTimeDifference(differenceInMillisecond);
            return timeDifference;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDifference;
    }

    public static String getUploadedTimeDifference(String time) {
        String timeDifference = "";
        Date currentDate = Calendar.getInstance().getTime();
        Date givenDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CURRENT_DATE_FORMAT);
        try {
            givenDate = simpleDateFormat.parse(time);
            long differenceInMillisecond = currentDate.getTime() - givenDate.getTime();
            timeDifference = getTimeDifference(differenceInMillisecond) + " " + AGO;

            return timeDifference;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDifference;
    }

    public static String formatDateAndTime(long date, String format) {
        Date dt = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(dt);
    }

    public static Date getCurrentSelectedDate(String selectedDate, String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        try {
            return format.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateFromDateTmeString(String dateValue, String timeValue) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(POST_CHALLENGE_TIME_FORMAT, Locale.ENGLISH);
        dateValue = dateValue + " " + timeValue;
        return simpleDateFormat.parse(dateValue);
    }

    public static String getDateInTwentyFourHoursFormat(String selectedDate, String time) {
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        try {
            Date date = parseFormat.parse(time);
            String formattedTime = displayFormat.format(date);
            return selectedDate + " " + formattedTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentTime(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat displayFormat = new SimpleDateFormat(format, Locale.getDefault());
        return displayFormat.format(calendar.getTime());
    }


    public static String getTimeDifference(long different) {
        System.out.println("different : " + different);
        long currentTimeDifference = 0;
        currentTimeDifference = getCurrentTimeInMillisecond() - different;
        if (currentTimeDifference < 0)
            currentTimeDifference = different - getCurrentTimeInMillisecond();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = currentTimeDifference / daysInMilli;
        currentTimeDifference = currentTimeDifference % daysInMilli;

        long elapsedHours = currentTimeDifference / hoursInMilli;
        currentTimeDifference = currentTimeDifference % hoursInMilli;
        if (elapsedDays != 0) {
            return String.format(Locale.getDefault(), "%d days, %d hr", elapsedDays, elapsedHours);
        }
        long elapsedMinutes = currentTimeDifference / minutesInMilli;
        currentTimeDifference = currentTimeDifference % minutesInMilli;
        if (elapsedHours != 0) {
            return String.format(Locale.getDefault(), "%d hr,%d min", elapsedHours, elapsedMinutes);
        }
        long elapsedSeconds = currentTimeDifference / secondsInMilli;
        return String.format(Locale.getDefault(), "%d min,%d sec", elapsedMinutes, elapsedSeconds);
    }


    public static long getCurrentTimeInMillisecond() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static long getNextTwentyFourHoursInMS(long milliseconds) {
        milliseconds = milliseconds + 86400000;
        return milliseconds;
    }
}