package com.example.divyanshu.smyt.Utils;

import android.content.Context;
import android.view.View;

import com.example.divyanshu.smyt.Adapters.UploadedAllVideoAdapter;
import com.example.divyanshu.smyt.Models.VideoDetailModel;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerStandardTwo;

/**
 * Created by divyanshu.jain on 9/27/2016.
 */
public class VideoSetupUtils {
    private static ImageLoading imageLoading;

    static VideoSetupUtils videoSetupUtils = null;

    private VideoSetupUtils() {
    }

    public static VideoSetupUtils getInstance(Context context) {
        if (videoSetupUtils == null)
            videoSetupUtils = new VideoSetupUtils();
        imageLoading = new ImageLoading(context);
        return videoSetupUtils;
    }


    public void setUpFirstVideoPlayer(JCVideoPlayerStandard videoPlayer, String videoUrl, String imageUrl) {
        boolean firstVideoSetup = videoPlayer.setUp(videoUrl, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
        if (firstVideoSetup)
            imageLoading.LoadImage(imageUrl, videoPlayer.thumbImageView, null);
    }

    public void setUpSecondVideoPlayer(JCVideoPlayerStandardTwo videoPlayer, String videoUrl, String imageUrl) {
        boolean firstVideoSetup = videoPlayer.setUp(videoUrl, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
        if (firstVideoSetup)
            imageLoading.LoadImage(imageUrl, videoPlayer.thumbImageView, null);
    }

    public void playMusic(JCVideoPlayerStandard firstVideoPlayer, JCVideoPlayerStandardTwo secondVideoPlayer) {
        if (firstVideoPlayer.currentState != firstVideoPlayer.CURRENT_STATE_PLAYING && firstVideoPlayer.currentState != firstVideoPlayer.CURRENT_STATE_PLAYING_BUFFERING_START && firstVideoPlayer.currentState != firstVideoPlayer.CURRENT_STATE_PAUSE)
            firstVideoPlayer.startPlayLogic();
        if (secondVideoPlayer.currentState != secondVideoPlayer.CURRENT_STATE_PLAYING && secondVideoPlayer.currentState != secondVideoPlayer.CURRENT_STATE_PLAYING_BUFFERING_START && secondVideoPlayer.currentState != secondVideoPlayer.CURRENT_STATE_PAUSE)
            secondVideoPlayer.startPlayLogic();
    }

    public void onBackButtonClick(final JCVideoPlayerStandard firstVideoPlayer, final JCVideoPlayerStandardTwo secondVideoPlayer) {
        firstVideoPlayer.fullscreenButton.setVisibility(View.GONE);
        secondVideoPlayer.fullscreenButton.setVisibility(View.GONE);
        firstVideoPlayer.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstVideoPlayer.backPress();
                secondVideoPlayer.backPress();
            }
        });

        secondVideoPlayer.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstVideoPlayer.backPress();
                secondVideoPlayer.backPress();
            }
        });

    }
}
