package com.example.divyanshu.smyt.ServicesAndNotifications;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.DialogActivities.OngoingChallengeDescriptionActivity;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.Models.NewChallengeNotiModel;
import com.example.divyanshu.smyt.Models.UpcomingRoundInfoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.activities.HomeActivity;
import com.example.divyanshu.smyt.activities.RecordChallengeVideoActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Ravi on 31/03/15.
 */
public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();

    private static Context mContext;
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final int NEW_CHALLENGE_NOTIFICATION_ID = 1111;
    public static final int UPCOMING_ROUND_NOTIFICATION_ID = 1112;

    private static NotificationUtils notificationUtils = new NotificationUtils();

    public static NotificationUtils getInstance(Context context) {
        mContext = context;
        return notificationUtils;
    }

    private NotificationUtils() {

    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
        showNotificationMessage(title, message, timeStamp, intent, null);
    }

    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;


        // notification icon
        final int icon = R.mipmap.ic_launcher;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final Notification.Builder mBuilder = new Notification.Builder(
                mContext);

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/notification");

        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                } else {
                    showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                }
            }
        } else {
            showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
            playNotificationSound();
        }
    }


    public void showSmallNotification(Notification.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

        Notification.InboxStyle inboxStyle = new Notification.InboxStyle();

        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void showBigNotification(Bitmap bitmap, Notification.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUpcomingRoundNotification(Context context, String messageBody, UpcomingRoundInfoModel upcomingRoundInfoModel) {
        Intent intent;
        // if (upcomingRoundInfoModel.getMinutes() == 1)
        //  intent = new Intent(context, RecordChallengeVideoActivity.class);
        // else
        intent = new Intent(context, OngoingChallengeDescriptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, upcomingRoundInfoModel.getCustomers_videos_id());
        intent.putExtra(Constants.ROUND_TIME, upcomingRoundInfoModel.getEdate());
        intent.putExtra(Constants.CHALLENGE_ID, upcomingRoundInfoModel.getChallenge_id());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final Notification.Builder builder = new Notification.Builder(context);
        builder.setStyle(new Notification.BigTextStyle(builder)
                .bigText(messageBody)
                .setBigContentTitle("Upcoming Round")
                .setSummaryText(messageBody))
                .setAutoCancel(true)
                .setContentTitle("New Challenge")
                .setContentText(messageBody)
                .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);
        builder.setSound(defaultSoundUri);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(UPCOMING_ROUND_NOTIFICATION_ID, builder.build());
    }

    public void sendNewChallengeNotification(final Context context, String messageBody, NewChallengeNotiModel categoryModel) {
        final Intent intent = new Intent(context, OngoingChallengeDescriptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent.putExtra(Constants.DATA, categoryModel);
        intent.putExtra(Constants.CHALLENGE_ID, categoryModel.getChallenge_id());
        intent.putExtra(Constants.ACCEPT_STATUS, 0);
        intent.putExtra(Constants.FROM_NOTIFICATION, true);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final Notification.Builder builder = new Notification.Builder(context);
        builder.setStyle(new Notification.BigTextStyle(builder)
                .bigText(messageBody)
                .setBigContentTitle("New Challenge")
                .setSummaryText("summary"))
                .setAutoCancel(true)
                .setContentTitle("New Challenge")
                .setContentText("Summary")
                .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setSound(defaultSoundUri);
        if (!isAppIsInBackground(context)) {
            builder.setPriority(Notification.PRIORITY_MAX);
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NEW_CHALLENGE_NOTIFICATION_ID, builder.build());
    }

    public void sendDeletedChallengeNotification(final Context context, String messageBody, String title) {
        final Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final Notification.Builder builder = new Notification.Builder(context);
        builder.setStyle(new Notification.BigTextStyle(builder)
                .bigText(messageBody)
                .setBigContentTitle("Challenge Deleted")
                .setSummaryText(""))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText("Challenge Deleted")
                .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setSound(defaultSoundUri);
        if (!isAppIsInBackground(context)) {
            builder.setPriority(Notification.PRIORITY_MAX);
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) Utils.getCurrentTimeInMillisecond(), builder.build());
    }


    private void showAlertForNotification(final Context context, final Intent intent) {
        CustomAlertDialogs.showNewNotificationAlert(context, new SnackBarCallback() {
            @Override
            public void doAction() {
                context.startActivity(intent);
            }
        });
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}