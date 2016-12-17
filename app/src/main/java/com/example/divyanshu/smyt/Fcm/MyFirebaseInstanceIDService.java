package com.example.divyanshu.smyt.Fcm;

import android.util.Log;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
public static String token = "";
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        token = refreshedToken;
        //sendRegistrationToServer();
    }
    // [END refresh_token]
    private void sendRegistrationToServer() {
        CallWebService.getInstance(getBaseContext(), false, ApiCodes.FCM_ID).hitJsonObjectRequestAPI(CallWebService.POST, API.UPDATE_FCM_ID, createJsonForSavingFcmID(token), null);
        // TODO: Implement this method to send token to your app server.
    }

    private JSONObject createJsonForSavingFcmID(String id) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getBaseContext());
        try {
            jsonObject.put(Constants.FCM_ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}