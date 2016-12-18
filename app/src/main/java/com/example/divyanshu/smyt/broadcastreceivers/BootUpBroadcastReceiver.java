package com.example.divyanshu.smyt.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.ServicesAndNotifications.NewChallengeNotificationService;
import com.example.divyanshu.smyt.ServicesAndNotifications.UpcomingRoundNotificationService;
import com.example.divyanshu.smyt.Utils.MySharedPereference;

/**
 * Created by divyanshu on 18/12/16.
 */

public class BootUpBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (MySharedPereference.getInstance().getBoolean(context, Constants.IS_LOGGED_IN))
            startServices(context);
        //Toast.makeText(context,"received!",Toast.LENGTH_LONG).show();
    }

    private void startServices(Context context) {
        Intent intent = new Intent(context, UpcomingRoundNotificationService.class);
        context.startService(intent);
        Intent intent1 = new Intent(context, NewChallengeNotificationService.class);
        context.startService(intent1);
    }
}
