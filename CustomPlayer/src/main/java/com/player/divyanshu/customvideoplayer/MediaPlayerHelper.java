package com.player.divyanshu.customvideoplayer;

import android.app.Activity;

import android.widget.FrameLayout;

import io.vov.vitamio.MediaPlayer;

/**
 * Created by divyanshu on 10/16/2016.
 */
public class MediaPlayerHelper {
    private static MediaPlayerHelper ourInstance = new MediaPlayerHelper();

    public static MediaPlayerHelper getInstance() {
        return ourInstance;
    }

    private MediaPlayerHelper() {
    }

    private StopPlayingInterface stopPlayingInterface;

    /***
     * first player Values
     ***///
    protected MediaPlayer mediaPlayer = null;
    protected StandardVideoPlayer.State mState = StandardVideoPlayer.State.Retrieving;
    protected boolean isLive = false;
    protected boolean standardPlayerFullScreen = false;
    protected StandardVideoPlayer previousStandardVideoPlayer = null, CurrentStandardPlayer = null;

    /***
     * second player Values
     ***///

    protected MediaPlayer secondMediaPlayer = null;
    protected SecondVideoPlayer.State secondPlayermState = SecondVideoPlayer.State.Retrieving;
    protected boolean secondVideoIsLive = false;
    protected boolean secondPlayerFullScreen = false;
    protected SecondVideoPlayer previousSecondVideoPlayer = null, CurrentSecondPlayer = null;

    private TwoVideoPlayers twoVideoPlayers;
    private boolean standardVideoPlayed = false, secondVideoPlayed = false;

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public StandardVideoPlayer.State getmState() {
        return mState;
    }

    public void setmState(StandardVideoPlayer.State mState) {
        this.mState = mState;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public boolean isStandardPlayerFullScreen() {
        return standardPlayerFullScreen;
    }

    public void setStandardPlayerFullScreen(boolean standardPlayerFullScreen) {
        this.standardPlayerFullScreen = standardPlayerFullScreen;
    }

    public StandardVideoPlayer getLastStandardVideoPlayer() {
        return previousStandardVideoPlayer;
    }

    public void setPreviousStandardVideoPlayer(StandardVideoPlayer standardVideoPlayer) {
        this.previousStandardVideoPlayer = standardVideoPlayer;
    }

    public StandardVideoPlayer getCurrentStandardPlayer() {
        return CurrentStandardPlayer;
    }

    public void setCurrentStandardPlayer(StandardVideoPlayer currentStandardPlayer) {
        CurrentStandardPlayer = currentStandardPlayer;
    }


    public MediaPlayer getSecondMediaPlayer() {
        return secondMediaPlayer;
    }

    public void setSecondMediaPlayer(MediaPlayer secondMediaPlayer) {
        this.secondMediaPlayer = secondMediaPlayer;
    }

    public SecondVideoPlayer.State getSecondPlayermState() {
        return secondPlayermState;
    }

    public void setSecondPlayermState(SecondVideoPlayer.State secondPlayermState) {
        this.secondPlayermState = secondPlayermState;
    }

    public boolean isSecondVideoIsLive() {
        return secondVideoIsLive;
    }

    public void setSecondVideoIsLive(boolean secondVideoIsLive) {
        this.secondVideoIsLive = secondVideoIsLive;
    }

    public boolean isSecondPlayerFullScreen() {
        return secondPlayerFullScreen;
    }

    public void setSecondPlayerFullScreen(boolean secondPlayerFullScreen) {
        this.secondPlayerFullScreen = secondPlayerFullScreen;
    }

    public SecondVideoPlayer getPreviousSecondVideoPlayer() {
        return previousSecondVideoPlayer;
    }

    public void setPreviousSecondVideoPlayer(SecondVideoPlayer secondVideoPlayer) {
        this.previousSecondVideoPlayer = secondVideoPlayer;
    }

    public SecondVideoPlayer getCurrentSecondPlayer() {
        return CurrentSecondPlayer;
    }

    public void setCurrentSecondPlayer(SecondVideoPlayer currentSecondPlayer) {
        CurrentSecondPlayer = currentSecondPlayer;
    }

    public boolean releaseAllVideos() {

        boolean isStopped = false;
        if (!standardPlayerFullScreen && !secondPlayerFullScreen) {
            isStopped = stopStandardPlayer(isStopped);
            isStopped = stopSecondPlayer(isStopped);
        }
        return isStopped;
    }

    private boolean stopStandardPlayer(boolean isStopped) {
        if (!standardPlayerFullScreen) {
            if (MediaPlayerHelper.getInstance().getCurrentStandardPlayer() != null) {
                MediaPlayerHelper.getInstance().getCurrentStandardPlayer().releaseVideo();
                isStopped = true;
            }
        }
        return isStopped;
    }

    private boolean stopSecondPlayer(boolean isStopped) {
        if (!secondPlayerFullScreen) {
            if (MediaPlayerHelper.getInstance().getCurrentSecondPlayer() != null) {
                MediaPlayerHelper.getInstance().getCurrentSecondPlayer().releaseVideo();
                isStopped = true;
            }
        }
        return isStopped;
    }

    public StopPlayingInterface getStopPlayingInterface() {
        return stopPlayingInterface;
    }

    public void setStopPlayingInterface(StopPlayingInterface stopPlayingInterface) {
        this.stopPlayingInterface = stopPlayingInterface;
    }

    public void playTwoVideoPlayers(StandardVideoPlayer standardVideoPlayer, SecondVideoPlayer secondVideoPlayer) {
        standardVideoPlayer.onTinyScreenButtonClick();
        secondVideoPlayer.onTinyScreenButtonClick();
        setCurrentStandardPlayer(standardVideoPlayer);
        setCurrentSecondPlayer(secondVideoPlayer);
    }


    public TwoVideoPlayers getTwoVideoPlayers() {
        return twoVideoPlayers;
    }

    public void setTwoVideoPlayers(TwoVideoPlayers twoVideoPlayers) {
        this.twoVideoPlayers = twoVideoPlayers;
    }

    void resetFirstPlayer() {
        mediaPlayer = null;
        mState = StandardVideoPlayer.State.Retrieving;
        isLive = false;
        standardPlayerFullScreen = false;
        previousStandardVideoPlayer = null;
        CurrentStandardPlayer = null;
        twoVideoPlayers = null;
        standardVideoPlayed = false;
    }

    void resetSecondPlayer() {
        secondMediaPlayer = null;
        secondPlayermState = SecondVideoPlayer.State.Retrieving;
        secondVideoIsLive = false;
        secondPlayerFullScreen = false;
        previousSecondVideoPlayer = null;
        CurrentSecondPlayer = null;
        twoVideoPlayers = null;
        secondVideoPlayed = false;
    }

    void pauseVideos() {

    }

    public void setVideoPlayed(FrameLayout playerType) {
        if (playerType instanceof StandardVideoPlayer) {
            standardVideoPlayed = true;
        } else if (playerType instanceof SecondVideoPlayer) {
            secondVideoPlayed = true;
        }

    }

    public boolean isBothVideoPlayed() {
        if (standardVideoPlayed && secondVideoPlayed) {
            return true;
        } else
            return false;
    }

    public void pauseVideos(Activity activity, HomeWatcher mHomeWatcher) {
        if (mediaPlayer != null)
            getCurrentStandardPlayer().pauseVideoPlaying(mediaPlayer);
        if (secondMediaPlayer != null) {
            getCurrentSecondPlayer().pauseVideoPlaying(secondMediaPlayer);
        }
        // activity.onBackPressed();
        mHomeWatcher.stopWatch();
    }


}
