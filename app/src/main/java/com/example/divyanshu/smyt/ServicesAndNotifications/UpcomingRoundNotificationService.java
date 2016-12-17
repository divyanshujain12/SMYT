package com.example.divyanshu.smyt.ServicesAndNotifications;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Models.UpcomingRoundInfoModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UpcomingRoundNotificationService extends Service implements CallWebService.ObjectResponseCallBack {

    ScheduledExecutorService scheduler;
    public static UpcomingRoundNotificationService notificationService = new UpcomingRoundNotificationService();
    private static Handler mHandler = null;

    public UpcomingRoundNotificationService() {
    }

    public static UpcomingRoundNotificationService getInstance() {
        return notificationService;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        CallWebService.getInstance(getBaseContext(), false, ApiCodes.UPCOMING_EVENTS).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_UPCOMING_ROUND_INFO, createJsonForGetUpcomingEvents(), UpcomingRoundNotificationService.this);
                    }
                }, 1, 5, TimeUnit.MINUTES);

        return START_STICKY;
    }

    private JSONObject createJsonForGetUpcomingEvents() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getBaseContext());
        try {
            long currentTime = Utils.getCurrentTimeInMillisecond();
            jsonObject.put(Constants.E_DATE, currentTime);
            jsonObject.put(Constants.E_DATE_1, Utils.getNextTenMinuteInMS(currentTime));
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
        if (response.has(Constants.DATA)) {
            ArrayList<UpcomingRoundInfoModel> upcomingRoundInfoModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONArray(Constants.DATA), UpcomingRoundInfoModel.class);

            for (UpcomingRoundInfoModel upcomingRoundInfoModel : upcomingRoundInfoModels)
                NotificationUtils.getInstance(getBaseContext()).sendUpcomingRoundNotification(getBaseContext(), "You have an upcoming round! Click here for more info!", upcomingRoundInfoModel.getChallenge_id());
        }

    }

    @Override
    public void onFailure(String str, int apiType) {

    }
}
