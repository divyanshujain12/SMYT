package com.example.divyanshu.smyt.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.divyanshu.smyt.Constants.Constants;

import static com.example.divyanshu.smyt.Constants.ApiCodes.BANNER_VIDEOS;
import static com.example.divyanshu.smyt.Constants.ApiCodes.DELETE_VIDEO;
import static com.example.divyanshu.smyt.Constants.Constants.COMMENT_COUNT;

/**
 * Created by divyanshu on 12/12/16.
 */
public class BroadcastSenderClass {
    private static BroadcastSenderClass ourInstance = new BroadcastSenderClass();

    public static BroadcastSenderClass getInstance() {
        return ourInstance;
    }

    private BroadcastSenderClass() {
    }

    public void sendDeleteCommentBroadcast(Context context) {
        Intent intent = new Intent();
        intent.putExtra(Constants.TYPE,DELETE_VIDEO);
        intent.setAction(Constants.UPDATE_UI_VIDEO_FRAGMENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.ALL_VIDEO_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void sendCommentCountBroadcast(Context context,String customer_video_id, int commentCount){
        Intent intent = new Intent();
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, customer_video_id);
        intent.putExtra(Constants.COUNT, commentCount);
        intent.putExtra(Constants.TYPE,COMMENT_COUNT);
        intent.setAction(Constants.UPDATE_UI_VIDEO_FRAGMENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.ALL_VIDEO_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public void sendBannerVideoAddedBroadcast(Context context){
        Intent intent = new Intent();
        intent.putExtra(Constants.TYPE,BANNER_VIDEOS);
        intent.setAction(Constants.ALL_VIDEO_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
