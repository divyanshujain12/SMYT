package com.example.divyanshu.smyt.ServicesAndNotifications;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by divyanshu.jain on 12/7/2016.
 */

public class OtherUserAvailabilityService extends Service implements CallWebService.ObjectResponseCallBack {
    ScheduledExecutorService scheduler;
    static UserAvailabilityInterface mUserAvailabilityInterface;
    public static OtherUserAvailabilityService otherUserAvailabilityService = new OtherUserAvailabilityService();
    String customerVIdeoID;

    public static OtherUserAvailabilityService getInstance() {
        return otherUserAvailabilityService;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        customerVIdeoID = intent.getStringExtra(Constants.CUSTOMERS_VIDEO_ID);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(runnable, 5, 5, TimeUnit.SECONDS);

        return START_STICKY;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            CallWebService.getInstance(getBaseContext(), false, ApiCodes.OTHER_USER_VIDEO_URL).hitJsonObjectRequestAPI(CallWebService.POST, API.OTHER_CUSTOMER_VIDEO_URL, createJsonForGetUserAvailability(customerVIdeoID), OtherUserAvailabilityService.this);
        }
    };

    private JSONObject createJsonForGetUserAvailability(String customerVideoID) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getBaseContext());
        try {
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, customerVideoID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        if (mUserAvailabilityInterface != null && !response.get(Constants.VIDEO_URL).equals(""))
            mUserAvailabilityInterface.onAvailable(response.getString(Constants.VIDEO_URL));
    }

    @Override
    public void onFailure(String str, int apiType) {

    }

    public interface UserAvailabilityInterface {
        void onAvailable(String videoUrl);
    }

    public void setUserAvailabilityInterface(UserAvailabilityInterface userAvailabilityInterface) {
        mUserAvailabilityInterface = userAvailabilityInterface;
    }

    @Override
    public boolean stopService(Intent name) {
        // TODO Auto-generated method stub
        stopScheduler();
        return super.stopService(name);

    }

    private void stopScheduler() {
        mUserAvailabilityInterface = null;
        scheduler.shutdown();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopScheduler();
    }
}
