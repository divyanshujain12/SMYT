package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.Utils.VideoSetupUtils;

import org.json.JSONException;
import org.json.JSONObject;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.PlayVideoInterface;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerStandardTwo;

/**
 * Created by divyanshu.jain on 1/12/2017.
 */

public class TwoVideoPlayerCustomView extends LinearLayout implements View.OnClickListener, PlayVideoInterface {
    private JCVideoPlayerStandard firstVideoPlayer;
    private JCVideoPlayerStandardTwo secondVideoPlayer;
    private FrameLayout fullscreenFL;
    private ImageView fullscreenIV;
    private ImageView playVideosIV;
    private String customerVideoID;


    public TwoVideoPlayerCustomView(Context context) {
        super(context);
    }

    public TwoVideoPlayerCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setUp(String firstVideoUrl, String secondVideoUrl, String firstThumbUrl, String secondThumbUrl, String customerVideoID) {
        this.customerVideoID = customerVideoID;
        LayoutInflater.from(getContext()).inflate(R.layout.battle_video_inside_view, this);
        firstVideoPlayer = (JCVideoPlayerStandard) findViewById(R.id.firstVideoPlayer);
        secondVideoPlayer = (JCVideoPlayerStandardTwo) findViewById(R.id.secondVideoPlayer);
        fullscreenFL = (FrameLayout) findViewById(R.id.fullscreenFL);
        fullscreenIV = (ImageView) findViewById(R.id.fullscreenIV);
        fullscreenIV.setOnClickListener(this);
        playVideosIV = (ImageView) findViewById(R.id.playVideosIV);
        playVideosIV.setOnClickListener(this);

        firstVideoPlayer.setPlayVideoInterface(this);
        //showPlayersStartButtons(false);
        resetPlayers();
        setUpVideoPlayers(getContext(), firstVideoUrl, secondVideoUrl, firstThumbUrl, secondThumbUrl);
    }


    private void setUpVideoPlayers(Context context, String firstVideoUrl, String secondVideoUrl, String firstThumbUrl, String secondThumbUrl) {
        VideoSetupUtils.getInstance(context).setUpFirstVideoPlayer(firstVideoPlayer, firstVideoUrl, firstThumbUrl);
        VideoSetupUtils.getInstance(context).setUpSecondVideoPlayer(secondVideoPlayer, secondVideoUrl, secondThumbUrl);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playVideosIV:
                onPlayButtonClicked();
                break;
            case R.id.fullscreenIV:
                onFullScreenClicked();
                break;
        }
    }

    private void onPlayButtonClicked() {
        showPlayersStartButtons(true);
        VideoSetupUtils.getInstance(getContext()).playMusic(firstVideoPlayer, secondVideoPlayer);
        CallWebService.getInstance(getContext(), false, ApiCodes.UPDATE_VIDEO_VIEW_COUNT).hitJsonObjectRequestAPI(CallWebService.POST, API.UPDATE_VIDEO_VIEWS_COUNT, createJsonForUpdateViewsCount(customerVideoID), null);
        fullscreenFL.setVisibility(VISIBLE);
        playVideosIV.setVisibility(GONE);
    }

    private void onFullScreenClicked() {
        VideoSetupUtils.getInstance(getContext()).playMusic(firstVideoPlayer, secondVideoPlayer);
        firstVideoPlayer.startWindowTiny(Gravity.TOP);
        secondVideoPlayer.startWindowTiny(Gravity.BOTTOM);
    }

    private JSONObject createJsonForUpdateViewsCount(String customerVideoID) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, customerVideoID);
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JCVideoPlayerStandard getFirstVideoPlayer() {
        return firstVideoPlayer;
    }

    public JCVideoPlayerStandardTwo getSecondVideoPlayer() {
        return secondVideoPlayer;
    }

    public FrameLayout getFullscreenFL() {
        return fullscreenFL;
    }

    public ImageView getFullscreenIV() {
        return fullscreenIV;
    }

    public ImageView getPlayVideosIV() {
        return playVideosIV;
    }

    public void resetPlayers() {
        showPlayersStartButtons(false);
        fullscreenFL.setVisibility(GONE);
        playVideosIV.setVisibility(VISIBLE);
    }

    private void showPlayersStartButtons(boolean show) {
        firstVideoPlayer.showPlayButton(show);
        secondVideoPlayer.showPlayButton(show);
    }

    @Override
    public void onVideoPlay() {
        CallWebService.getInstance(getContext(), false, ApiCodes.UPDATE_VIDEO_VIEW_COUNT).hitJsonObjectRequestAPI(CallWebService.POST, API.UPDATE_VIDEO_VIEWS_COUNT, createJsonForUpdateViewsCount(customerVideoID), null);
    }

    @Override
    public void onVideoStopped() {
        resetPlayers();
    }
}
