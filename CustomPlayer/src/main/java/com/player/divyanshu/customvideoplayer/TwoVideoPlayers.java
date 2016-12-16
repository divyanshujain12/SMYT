package com.player.divyanshu.customvideoplayer;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by divyanshu.jain on 10/20/2016.
 */

public class TwoVideoPlayers extends FrameLayout implements StopPlayingInterface, View.OnClickListener, HomeWatcher.OnHomePressedListener {
    private StandardVideoPlayer standardVideoPlayer;
    private SecondVideoPlayer secondVideoPlayer;
    private ImageView playAllVideosIV;
    private LinearLayout layerLL;
    private ImageView playVideosInFullScreenIV;
    private ImageLoading imageLoading;
    HomeWatcher mHomeWatcher;
    String firstUrl, secondUrl;
    private PlayVideoInterface playVideoInterface;

    private int playedVideoPos = 0;

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
        attachBackPressWatcher();
        standardVideoPlayer = (StandardVideoPlayer) findViewById(R.id.standardVideoPlayer);
        secondVideoPlayer = (SecondVideoPlayer) findViewById(R.id.secondVideoPlayer);
        playAllVideosIV = (ImageView) findViewById(R.id.playAllVideosIV);
        playAllVideosIV.setOnClickListener(this);
        layerLL = (LinearLayout) findViewById(R.id.layerLL);
        playVideosInFullScreenIV = (ImageView) findViewById(R.id.playVideosInFullScreenIV);
        playVideosInFullScreenIV.setOnClickListener(this);

        setShowPlayButton(false);
    }

    private void attachBackPressWatcher() {
        mHomeWatcher = new HomeWatcher(getContext());
        mHomeWatcher.setOnHomePressedListener(this);
        mHomeWatcher.startWatch();
    }

    public void setVideoUrls(String firstUrl, String secondUrl) {
        if (firstUrl.equals(null) || secondUrl.equals(null))
            return;
        this.firstUrl = firstUrl;
        this.secondUrl = secondUrl;
        standardVideoPlayer.setUrl(firstUrl);
        secondVideoPlayer.setUrl(secondUrl);
    }

    public void setThumbnail(String firstThumbUrl, String secondThumbUrl) {
        imageLoading.LoadImage(firstThumbUrl, standardVideoPlayer.getVideoThumbnail(), null);
        imageLoading.LoadImage(secondThumbUrl, secondVideoPlayer.getVideoThumbnail(), null);
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
            if (!firstUrl.isEmpty() && !secondUrl.isEmpty())
                onPlayButtonClick();
            else
                Toast.makeText(getContext(), R.string.no_video_url, Toast.LENGTH_SHORT).show();
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
        if(playVideoInterface!=null)
            playVideoInterface.onVideoPlay(playedVideoPos);
    }

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


    @Override
    public void onHomePressed() {
        resetPlayers();
    }

    @Override
    public void onHomeLongPressed() {
        resetPlayers();
    }

    private void resetPlayers() {
        MediaPlayerHelper.getInstance().pauseVideos(((Activity) getContext()), mHomeWatcher);
    }

    public int getPlayedVideoPos() {
        return playedVideoPos;
    }

    public void setPlayedVideoPos(int playedVideoPos) {
        this.playedVideoPos = playedVideoPos;
    }

    public PlayVideoInterface getPlayVideoInterface() {
        return playVideoInterface;
    }

    public void setPlayVideoInterface(PlayVideoInterface playVideoInterface) {
        this.playVideoInterface = playVideoInterface;
    }
}
