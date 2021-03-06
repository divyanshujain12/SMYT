package com.example.divyanshu.smyt.broadcastreceivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Models.AllVideoModel;

import static com.example.divyanshu.smyt.Constants.ApiCodes.ALL_DATA;
import static com.example.divyanshu.smyt.Constants.ApiCodes.BANNER_VIDEOS;
import static com.example.divyanshu.smyt.Constants.ApiCodes.DELETE;
import static com.example.divyanshu.smyt.Constants.Constants.COMMENT_COUNT;
import static com.example.divyanshu.smyt.Constants.Constants.FAVORITE_STATUS;
import static com.example.divyanshu.smyt.Constants.Constants.LIKE_COUNT;
import static com.example.divyanshu.smyt.Constants.Constants.VOTE_COUNT_INT;

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

    public void sendDeleteVideoBroadcast(Context context) {
        Intent intent = new Intent();
        intent.putExtra(Constants.TYPE, DELETE);
        intent.setAction(Constants.USER_FRAGMENT_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.ALL_VIDEO_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void sendCommentCountBroadcast(Context context, String customer_video_id, int commentCount) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, customer_video_id);
        intent.putExtra(Constants.COUNT, commentCount);
        intent.putExtra(Constants.TYPE, COMMENT_COUNT);
        intent.setAction(Constants.USER_FRAGMENT_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.ALL_VIDEO_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.USER_MUSIC_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.CATEGORY_MUSIC_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public void sendLikesCountBroadcast(Context context, String customer_video_id, String likesCount) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, customer_video_id);
        intent.putExtra(Constants.COUNT, likesCount);
        intent.putExtra(Constants.TYPE, LIKE_COUNT);
        intent.setAction(Constants.USER_FRAGMENT_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.ALL_VIDEO_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.USER_MUSIC_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.CATEGORY_MUSIC_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void sendChallengeCommentCountBroadcast(Context context, String customer_video_id, int commentCount) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, customer_video_id);
        intent.putExtra(Constants.COUNT, commentCount);
        intent.putExtra(Constants.TYPE, COMMENT_COUNT);
        intent.setAction(Constants.ALL_VIDEO_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void sendCommentCountBroadcastToLiveTab(Context context, String challengeID, int commentCount) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CHALLENGE_ID, challengeID);
        intent.putExtra(Constants.COUNT, commentCount);
        intent.putExtra(Constants.TYPE, COMMENT_COUNT);
        intent.setAction(Constants.LIVE_CHALLENGES_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void sendVoteCountBroadcastToLiveTab(Context context, String challengeID, String voteCount, int userNumber) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CHALLENGE_ID, challengeID);
        intent.putExtra(Constants.TYPE, VOTE_COUNT_INT);
        intent.setAction(Constants.LIVE_CHALLENGES_TAB_UI);
        intent.putExtra(Constants.VOTE_COUNT, voteCount);
        intent.putExtra(Constants.USER_NUMBER, userNumber);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void sendBannerVideoAddedBroadcast(Context context) {
        Intent intent = new Intent();
        intent.putExtra(Constants.TYPE, BANNER_VIDEOS);
        intent.setAction(Constants.ALL_VIDEO_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void sendVideoFavoriteBroadcast(Context context, AllVideoModel allVideoModel, int status) {
        Intent intent = new Intent();
        intent.putExtra(Constants.TYPE, FAVORITE_STATUS);
        intent.putExtra(Constants.DATA, allVideoModel);
        intent.putExtra(Constants.STATUS, status);
        intent.setAction(Constants.USER_FAV_FRAGMENT_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.USER_FRAGMENT_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.ALL_VIDEO_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.USER_MUSIC_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.CATEGORY_MUSIC_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void reloadAllVideoData(Context context) {
        Intent intent = new Intent();
        intent.putExtra(Constants.TYPE, ALL_DATA);
        intent.setAction(Constants.ALL_VIDEO_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.LIVE_CHALLENGES_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.USER_FRAGMENT_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.CATEGORY_MUSIC_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
