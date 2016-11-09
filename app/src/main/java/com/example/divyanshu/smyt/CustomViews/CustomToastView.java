package com.example.divyanshu.smyt.CustomViews;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by divyanshu.jain on 11/9/2016.
 */

public class CustomToastView extends Toast {
    private boolean showing = false;
    private long currentMilliseconds = 0;
    private long toastDuration = 0;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public CustomToastView(Context context) {
        super(context);
    }

    public boolean isShowing() {
        if ((currentMilliseconds + toastDuration) > getCurrentTime())
            showing = true;
        else
            showing = false;
        return showing;
    }

    private void setShowing(boolean showing) {
        this.showing = showing;
    }

    @Override
    public void show() {
        super.show();
        showing = true;
        currentMilliseconds = getCurrentTime();
    }

    @Override
    public void setDuration(int duration) {
        super.setDuration(duration);
        toastDuration = duration;
    }

    private long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
