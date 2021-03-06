package com.player.divyanshu.customvideoplayer;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Created by divyanshu.jain on 10/21/2016.
 */

public class SingleVideoPlayer extends FrameLayout implements StopPlayingInterface, View.OnClickListener {
    StandardVideoPlayer standardVideoPlayer;
    private ImageView playAllVideosIV;
    private LinearLayout layerLL;
    private ImageLoading imageLoading;
    private PlayVideoInterface playVideoInterface;
    private int playedVideoPos = 0;

    public SingleVideoPlayer(Context context) {
        super(context);
        setUp();
    }

    public SingleVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }


    private void setUp() {
        imageLoading = new ImageLoading(getContext());
        LayoutInflater.from(getContext()).inflate(R.layout.single_video_player, this);
        standardVideoPlayer = (StandardVideoPlayer) findViewById(R.id.standardVideoPlayer);
        playAllVideosIV = (ImageView) findViewById(R.id.playAllVideosIV);
        playAllVideosIV.setOnClickListener(this);
        layerLL = (LinearLayout) findViewById(R.id.layerLL);
    }

    public void setVideoUrl(String firstUrl) {
        standardVideoPlayer.setUrl(firstUrl);

    }

    public void setThumbnail(String firstThumbUrl) {
        imageLoading.LoadImage(firstThumbUrl, standardVideoPlayer.getVideoThumbnail(), null);
    }

    public ImageView getThumbImageView() {
        return standardVideoPlayer.getVideoThumbnail();
    }

    @Override
    public void onClick(View view) {
        if (view == playAllVideosIV) {
            playButtonClicked();
        }
    }


    @Override
    public void onStopVideoPlaying() {
        setShowPlayButton(false);
        showHideBlackLayer(true);
    }

    protected void setShowPlayButton(boolean enabled) {
        standardVideoPlayer.setShowPlayButton(enabled);

    }

    private void showHideBlackLayer(boolean show) {
        layerLL.setVisibility(show ? VISIBLE : GONE);
    }

    public void playButtonClicked() {

        MediaPlayerHelper.getInstance().releaseAllVideos();
        setShowPlayButton(true);
        standardVideoPlayer.onPlayButtonClick();
        showHideBlackLayer(false);
        MediaPlayerHelper.getInstance().setStopPlayingInterface(this);
        if (playVideoInterface != null)
            playVideoInterface.onVideoPlay(playedVideoPos);
    }

    public void setHideControls(boolean hide) {
        standardVideoPlayer.setHideControls(hide);
    }

    public void playVideo() {
        standardVideoPlayer.playVideo();
    }

    public PlayVideoInterface getPlayVideoInterface() {
        return playVideoInterface;
    }

    public void setPlayVideoInterface(PlayVideoInterface playVideoInterface) {
        this.playVideoInterface = playVideoInterface;
    }

    public int getPlayedVideoPos() {
        return playedVideoPos;
    }

    public void setPlayedVideoPos(int playedVideoPos) {
        this.playedVideoPos = playedVideoPos;
    }
}
