package com.player.divyanshu.customvideoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by divyanshu.jain on 10/20/2016.
 */

public class TwoVideoPlayers extends FrameLayout implements StopPlayingInterface, View.OnClickListener {
    private StandardVideoPlayer standardVideoPlayer;
    private SecondVideoPlayer secondVideoPlayer;
    private ImageView playAllVideosIV;
    private LinearLayout layerLL;
    private ImageView playVideosInFullScreenIV;
    private ImageLoading imageLoading;

    public TwoVideoPlayers(Context context) {
        super(context);
        setUp();
    }

    public TwoVideoPlayers(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    private void setUp() {
        imageLoading = new ImageLoading(getContext());
        LayoutInflater.from(getContext()).inflate(R.layout.two_video_player, this);

        standardVideoPlayer = (StandardVideoPlayer) findViewById(R.id.standardVideoPlayer);
        secondVideoPlayer = (SecondVideoPlayer) findViewById(R.id.secondVideoPlayer);
        playAllVideosIV = (ImageView) findViewById(R.id.playAllVideosIV);
        playAllVideosIV.setOnClickListener(this);
        layerLL = (LinearLayout) findViewById(R.id.layerLL);
        playVideosInFullScreenIV = (ImageView) findViewById(R.id.playVideosInFullScreenIV);
        playVideosInFullScreenIV.setOnClickListener(this);

        setShowPlayButton(false);
    }

    public void setVideoUrls(String firstUrl, String secondUrl) {
        standardVideoPlayer.setUrl(firstUrl);
        secondVideoPlayer.setUrl(secondUrl);
    }

    public void setThumbnail(String firstThumbUrl, String secondThumbUrl) {
        imageLoading.LoadImage(firstThumbUrl, standardVideoPlayer.getVideoThumbnail(),null);
        imageLoading.LoadImage(secondThumbUrl, secondVideoPlayer.getVideoThumbnail(),null);
    }

    @Override
    public void onStopVideoPlaying() {
        setShowPlayButton(false);
        showHideBlackLayer(true);
    }

    public StandardVideoPlayer getStandardVideoPlayer() {
        return standardVideoPlayer;
    }

    public SecondVideoPlayer getSecondVideoPlayer() {
        return secondVideoPlayer;
    }

    public ImageView getPlayAllVideosIV() {
        return playAllVideosIV;
    }

    public LinearLayout getLayerLL() {
        return layerLL;
    }

    public ImageView getPlayVideosInFullScreenIV() {
        return playVideosInFullScreenIV;
    }


    @Override
    public void onClick(View view) {
        if (view == playAllVideosIV) {
            onPlayButtonClick();
        } else if (view == playVideosInFullScreenIV) {
            onPlayVideosInFullScreenClick();
        }
    }

    protected void onPlayButtonClick() {
        MediaPlayerHelper.getInstance().releaseAllVideos();
        MediaPlayerHelper.getInstance().setTwoVideoPlayers(this);
        setShowPlayButton(true);
        standardVideoPlayer.onPlayButtonClick();
        secondVideoPlayer.onPlayButtonClick();
        showHideBlackLayer(false);
        MediaPlayerHelper.getInstance().setStopPlayingInterface(this);
    }

    /*private void resetPreviousPlayer() {
        TwoVideoPlayers twoVideoPlayers = MediaPlayerHelper.getInstance().getTwoVideoPlayers();
        if (twoVideoPlayers != null) {
            twoVideoPlayers.standardVideoPlayer.releaseVideo();
            twoVideoPlayers.secondVideoPlayer.releaseVideo();
        }
    }*/

    protected void setShowPlayButton(boolean enabled) {
        standardVideoPlayer.setShowPlayButton(enabled);
        secondVideoPlayer.setShowPlayButton(enabled);
    }
    private void showHideBlackLayer(boolean show) {
        layerLL.setVisibility(show ? VISIBLE : GONE);
        playVideosInFullScreenIV.setVisibility(show ? GONE : VISIBLE);
    }


    protected void onPlayVideosInFullScreenClick() {

        MediaPlayerHelper.getInstance().playTwoVideoPlayers(standardVideoPlayer, secondVideoPlayer);
    }
}
