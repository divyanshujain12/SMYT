package com.example.divyanshu.smyt.Fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.Models.NewChallengeNotiModel;
import com.example.divyanshu.smyt.Models.UpcomingRoundInfoModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.ServicesAndNotifications.NotificationUtils;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.activities.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String NEW_CHALLENGE = "New Challenge";
    private static final String UPCOMING_ROUND = "Upcoming Round";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


        if (remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage.getData().toString());
        }       // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        try {
            JSONObject jsonObject = new JSONObject(messageBody);
            JSONObject data = jsonObject.getJSONObject(Constants.DATA);
            String notificationType = data.getString(Constants.TITLE);
            JSONObject pushData = data.getJSONObject(Constants.PUSH_DATA);

            switch (notificationType) {
                case NEW_CHALLENGE:
                    generateNewChallengeNotification(pushData);
                    break;
                case UPCOMING_ROUND:
                    generateUpcomingRoundNotification(pushData);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void generateNewChallengeNotification(JSONObject pushData) {
        NewChallengeNotiModel categoryModel = UniversalParser.getInstance().parseJsonObject(pushData, NewChallengeNotiModel.class);
        NotificationUtils.getInstance(getBaseContext()).sendNewChallengeNotification(getApplicationContext(), "New Challenges posted in " + categoryModel.getCategory_name(), categoryModel);
    }

    private void generateUpcomingRoundNotification(JSONObject pushData) {
        UpcomingRoundInfoModel upcomingRoundInfoModel = UniversalParser.getInstance().parseJsonObject(pushData, UpcomingRoundInfoModel.class);
        NotificationUtils.getInstance(getBaseContext()).sendUpcomingRoundNotification(getBaseContext(), "You have an upcoming round in" + Utils.getChallengeTimeDifference(Long.parseLong(upcomingRoundInfoModel.getEdate())) + "! Click here for more info!", upcomingRoundInfoModel);
    }
}