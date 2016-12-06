package com.example.divyanshu.smyt.ServicesAndNotifications;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationService extends Service implements CallWebService.ObjectResponseCallBack {

    ScheduledExecutorService scheduler;
    public static NotificationService notificationService = new NotificationService();
    private static Handler mHandler = null;

    public NotificationService() {
    }

    public static NotificationService getInstance() {
        return notificationService;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        CallWebService.getInstance(getBaseContext(), false, ApiCodes.UPCOMING_EVENTS).hitJsonObjectRequestAPI(CallWebService.POST, API.UPCOMING_EVENTS, createJsonForGetUpcomingEvents(), NotificationService.this);
                    }
                }, 1, 1, TimeUnit.MINUTES);

        return START_STICKY;
    }

    private JSONObject createJsonForGetUpcomingEvents() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getBaseContext());
        try {
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
        if (mHandler != null)
            mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        if(!NotificationUtils.isAppIsInBackground(getBaseContext())){
            CustomAlertDialogs.showAlertDialogWithCallBack(getBaseContext(), "Upcoming Challenge", "You have a upcoming challenge within 1 minute! Would you like to go for it", new SnackBarCallback() {
                @Override
                public void doAction() {

                }
            });
        }
    }

    @Override
    public void onFailure(String str, int apiType) {

    }
}
