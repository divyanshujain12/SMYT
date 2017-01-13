package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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

/**
 * Created by divyanshu.jain on 1/12/2017.
 */

public class SingleVideoPlayerCustomView extends LinearLayout implements View.OnClickListener, PlayVideoInterface {
    private JCVideoPlayerStandard firstVideoPlayer;
    private ImageView playVideoIV;
    private String customerVideoID;


    public SingleVideoPlayerCustomView(Context context) {
        super(context);
    }

    public SingleVideoPlayerCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setUp(String firstVideoUrl, String firstThumbUrl, String customerVideoID) {
        this.customerVideoID = customerVideoID;
        LayoutInflater.from(getContext()).inflate(R.layout.single_video_inside_view, this);
        firstVideoPlayer = (JCVideoPlayerStandard) findViewById(R.id.firstVideoPlayer);
        playVideoIV = (ImageView) findViewById(R.id.playVideoIV);
        setUpVideoPlayers(getContext(), firstVideoUrl, firstThumbUrl);
        playVideoIV.setOnClickListener(this);
        resetPlayers();
        firstVideoPlayer.setPlayVideoInterface(this);
    }

    private void setUpVideoPlayers(Context context, String firstVideoUrl, String firstThumbUrl) {
        VideoSetupUtils.getInstance(context).setUpFirstVideoPlayer(firstVideoPlayer, firstVideoUrl, firstThumbUrl);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playVideoIV:
                onPlayButtonClicked();
                break;
        }
    }

    private void onPlayButtonClicked() {
        firstVideoPlayer.startPlayLogic();
        if (customerVideoID.length() > 0)
            CallWebService.getInstance(getContext(), false, ApiCodes.UPDATE_VIDEO_VIEW_COUNT).hitJsonObjectRequestAPI(CallWebService.POST, API.UPDATE_VIDEO_VIEWS_COUNT, createJsonForUpdateViewsCount(customerVideoID), null);
        playVideoIV.setVisibility(GONE);

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

    public ImageView getPlayVideoIV() {
        return playVideoIV;
    }

    public void resetPlayers() {
        playVideoIV.setVisibility(VISIBLE);
        firstVideoPlayer.showPlayButton(false);
    }

    @Override
    public void onVideoPlay(View view) {
        // CallWebService.getInstance(getContext(), false, ApiCodes.UPDATE_VIDEO_VIEW_COUNT).hitJsonObjectRequestAPI(CallWebService.POST, API.UPDATE_VIDEO_VIEWS_COUNT, createJsonForUpdateViewsCount(customerVideoID), null);
    }

    @Override
    public void onVideoStopped() {
        resetPlayers();
    }
}
