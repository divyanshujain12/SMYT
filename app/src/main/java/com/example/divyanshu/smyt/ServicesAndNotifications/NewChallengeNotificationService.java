package com.example.divyanshu.smyt.ServicesAndNotifications;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Models.CategoryModel;
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

/**
 * Created by divyanshu on 17/12/16.
 */

public class NewChallengeNotificationService extends Service implements CallWebService.ObjectResponseCallBack {

    private static NewChallengeNotificationService newChallengeService = new NewChallengeNotificationService();
    ScheduledExecutorService scheduler;

    public NewChallengeNotificationService() {
    }

    public static NewChallengeNotificationService getInstance() {
        return newChallengeService;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        CallWebService.getInstance(getBaseContext(), false, ApiCodes.NEW_CHALLENGE_NOTIFICATION).hitJsonObjectRequestAPI(CallWebService.POST, API.NEW_CHALLENGE_NOTIFICATION, createJsonForGetNewChallenge(), NewChallengeNotificationService.this);
                    }
                }, 0, 30, TimeUnit.SECONDS);

        return START_STICKY;
    }

    private JSONObject createJsonForGetNewChallenge() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getBaseContext());
        try {
            long currentTime = Utils.getCurrentTimeInMillisecond();
            jsonObject.put(Constants.E_DATE, currentTime);
            jsonObject.put(Constants.E_DATE_1, Utils.getPreviousOneMinuteInMS(currentTime));
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


    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        if (response.has(Constants.DATA)) {
            ArrayList<CategoryModel> categoryModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONArray(Constants.DATA), CategoryModel.class);

            for (CategoryModel categoryModel : categoryModels)
                NotificationUtils.getInstance(getBaseContext()).sendNewChallengeNotification(getBaseContext(), "New Challenges posted in " + categoryModel.getcategory_name(), categoryModel);

        }

    }

    @Override
    public void onFailure(String str, int apiType) {

    }
}


