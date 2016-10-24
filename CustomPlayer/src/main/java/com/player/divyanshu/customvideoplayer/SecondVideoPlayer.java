package com.player.divyanshu.customvideoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by divyanshu on 10/16/2016.
 */

public class SecondVideoPlayer extends FrameLayout implements SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener,
        View.OnClickListener, MediaPlayer.OnSeekCompleteListener, Animation.AnimationListener, TextureView.SurfaceTextureListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener {
    private TextView textViewPlayed;
    private TextView textViewLength;
    private SeekBar seekBarProgress;
    private ResizeTextureView surfaceViewFrame;
    private ImageView imageViewPauseIndicator;

    private ImageView fullScreen;
    private ProgressBar progressBarWait;
    private Animation hideMediaController;
    private LinearLayout linearLayoutMediaController;
    private Utilities utils;
    //private boolean isPlayed = false;
    private long duration = 0;
    public static final int HIDE_CONTROLLER = 0;
    public static final int SHOW_CONTROLLER = 1;
    public static final int UPDATE_PROGRESS_BAR = 2;
    private boolean isLiveVideo = false;
    private ImageView videoThumbnail;
    private FrameLayout playerView;
    private int surfaceViewID = 10001;
    private Uri videoUri;
    private MediaPlayer secondMediaPlayer = null;
    private long previousTime = 0;

    public enum State {
        Retrieving,
        Preparing,
        Playing,
        Paused,
        Prepared,
        Completed;

    }

    public SecondVideoPlayer(Context context) {
        super(context);
        setUp();
    }

    public SecondVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    public void setUrl(String path) {
        videoUri = Uri.parse(path);
    }

    public void setUrl(Uri path) {
        videoUri = path;
    }

    public void setUp() {
        LayoutInflater.from(getContext()).inflate(R.layout.standard_video_player, this);

        playerView = (FrameLayout) findViewById(R.id.playerView);
        utils = new Utilities();
        linearLayoutMediaController = (LinearLayout) findViewById(R.id.linearLayoutMediaController);
        linearLayoutMediaController.setVisibility(View.GONE);
        videoThumbnail = (ImageView) findViewById(R.id.videoThumbnail);
        videoThumbnail.setVisibility(View.VISIBLE);

        hideMediaController = AnimationUtils.loadAnimation(getContext(), R.anim.disapearing);
        hideMediaController.setAnimationListener(this);

        imageViewPauseIndicator = (ImageView) findViewById(R.id.imageViewPauseIndicator);

        fullScreen = (ImageView) findViewById(R.id.fullScreen);
        imageViewPauseIndicator.setOnClickListener(this);
        textViewPlayed = (TextView) findViewById(R.id.textViewPlayed);
        textViewLength = (TextView) findViewById(R.id.textViewLength);

        fullScreen.setOnClickListener(this);
        seekBarProgress = (SeekBar) findViewById(R.id.seekBarProgress);
        seekBarProgress.setOnSeekBarChangeListener(this);
        seekBarProgress.setProgress(0);

        progressBarWait = (ProgressBar) findViewById(R.id.progressBarWait);
        setValuesForNewInstance();

        if (MediaPlayerHelper.getInstance().isSecondPlayerFullScreen()) {
            secondMediaPlayer = MediaPlayerHelper.getInstance().getSecondMediaPlayer();
            playVideo();
        }
    }

    public void playVideo() {
        if (videoUri == null && !MediaPlayerHelper.getInstance().isSecondPlayerFullScreen()) {
            showToast("No Url");
            return;
        }
        if (surfaceViewFrame == null) {
            addTextureView();
        }
        if (secondMediaPlayer == null || MediaPlayerHelper.getInstance().secondPlayermState == State.Completed) {
            try {
                //resetPreviousPlayer();
                MediaPlayerHelper.getInstance().setPreviousSecondVideoPlayer(this);
                MediaPlayerHelper.getInstance().setCurrentSecondPlayer(this);
                secondMediaPlayer = new MediaPlayer();
                MediaPlayerHelper.getInstance().setSecondMediaPlayer(secondMediaPlayer);
                MediaPlayerHelper.getInstance().secondMediaPlayer.setOnPreparedListener(this);
                MediaPlayerHelper.getInstance().secondMediaPlayer.setOnBufferingUpdateListener(this);
                MediaPlayerHelper.getInstance().secondMediaPlayer.setOnSeekCompleteListener(this);
                MediaPlayerHelper.getInstance().secondMediaPlayer.setScreenOnWhilePlaying(true);
                //MediaPlayerHelper.getInstance().secondMediaPlayer.setOnErrorListener(this);
                MediaPlayerHelper.getInstance().secondMediaPlayer.setOnInfoListener(this);
                MediaPlayerHelper.getInstance().secondMediaPlayer.setDataSource(getContext(), videoUri);
                MediaPlayerHelper.getInstance().secondMediaPlayer.prepareAsync();
                updateUiForStartPreparing(true);
                setmState(State.Preparing);
                progressBarWait.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                showToast("Error while playing video");
                e.printStackTrace();
            }

        } else if (!MediaPlayerHelper.getInstance().secondMediaPlayer.isPlaying() || MediaPlayerHelper.getInstance().secondPlayerFullScreen) {
            if (MediaPlayerHelper.getInstance().secondPlayermState == State.Preparing) {
                progressBarWait.setVisibility(View.VISIBLE);
                isBuffering(true);
            }
            startPlayingVideo(MediaPlayerHelper.getInstance().secondMediaPlayer);
        }


    }

    private void hideMediaController() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    hideMediaControllerHandler();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Log.i(TAG, "========== onProgressChanged : " + progress + " from user: " + fromUser);
        updateProgressBar();
        if (!fromUser) {
            textViewPlayed.setText(utils.milliSecondsToTimer(progress * 1000));
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }


    public void onStopTrackingTouch(SeekBar seekBar) {
        if (MediaPlayerHelper.getInstance().secondMediaPlayer.isPlaying()) {
            int totalDuration = MediaPlayerHelper.getInstance().secondMediaPlayer.getDuration();
            int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
            MediaPlayerHelper.getInstance().secondMediaPlayer.seekTo(currentPosition);
            updateProgressBar();

        }
    }

    public void onPrepared(MediaPlayer mp) {
        setmState(State.Prepared);
        isBuffering(false);
        startPlayingVideo(mp);

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // onError("Error in playing");
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                isBuffering(false);
                return true;
            }
            case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                isBuffering(true);
                return true;
            }
            case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                isBuffering(false);
                return true;
            }
        }
        return false;
    }

    private void onError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void isBuffering(boolean buffering) {
        if (!buffering) {
            progressBarWait.setVisibility(View.GONE);
            surfaceViewFrame.setClickable(true);
            showHideMediaControls(true);
        } else {
            progressBarWait.setVisibility(View.VISIBLE);
            surfaceViewFrame.setClickable(false);
            showHideMediaControls(false);
        }
    }

    public void onCompletion(MediaPlayer mp) {

        setmState(State.Completed);
        backPress(MediaPlayerHelper.getInstance().secondMediaPlayer);
    }

    /**
     * Change progress of mediaController
     */
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBarProgress.setSecondaryProgress(percent);
    }

    public void onClick(View v) {
        if (v.getId() == surfaceViewID) {
            if (linearLayoutMediaController.getVisibility() == View.GONE) {
                showMediaControllerHandler();
                hideMediaController();
            } else if (linearLayoutMediaController.getVisibility() == View.VISIBLE) {
                hideMediaControllerHandler();
            }
        } else if (v.getId() == R.id.imageViewPauseIndicator) {
            onPlayButtonClick();
        } else if (v.getId() == R.id.fullScreen) {
            doFullScreenButtonAction();
        }


    }

    protected void onPlayButtonClick() {

        if (videoThumbnail.getVisibility() == View.VISIBLE)
            showHideThumbnail(false);
        playButtonClicked();
    }

    private void doFullScreenButtonAction() {
        if (MediaPlayerHelper.getInstance().isSecondPlayerFullScreen()) {
            MediaPlayerHelper.getInstance().secondPlayerFullScreen = false;
            ((Activity) getContext()).onBackPressed();
        } else {
            goToFullScreen();
            MediaPlayerHelper.getInstance().secondPlayerFullScreen = true;
        }


    }

    protected void onTinyScreenButtonClick() {
        if (MediaPlayerHelper.getInstance().isSecondPlayerFullScreen()) {
            ((Activity) getContext()).onBackPressed();
            MediaPlayerHelper.getInstance().secondPlayerFullScreen = false;
        } else {
            goToTinyScreen();
            MediaPlayerHelper.getInstance().secondPlayerFullScreen = true;
        }

    }

    public void updateUiForFullScreen() {
        if (MediaPlayerHelper.getInstance().isSecondPlayerFullScreen()) {
            fullScreen.setImageResource(R.drawable.jc_shrink);
        } else {
            fullScreen.setImageResource(R.drawable.jc_enlarge);

        }
    }

    private void goToFullScreen() {
        Intent intent = new Intent(getContext(), FullScreenSecondPlayer.class);
        getContext().startActivity(intent);
    }

    private void goToTinyScreen() {
        Intent intent = new Intent(getContext(), TwoPlayerActivity.class);
        intent.putExtra("SecondUrl", videoUri.toString());
        getContext().startActivity(intent);
    }

    private void playButtonClicked() {
        if (secondMediaPlayer != null && MediaPlayerHelper.getInstance().secondPlayermState == State.Playing) {
            pauseVideoPlaying(MediaPlayerHelper.getInstance().secondMediaPlayer);
        } else {
            showHideMediaControls(false);
            playVideo();
        }
    }


    public void onSeekComplete(MediaPlayer mp) {
        progressBarWait.setVisibility(View.GONE);
    }

    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub

    }

    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    public void onAnimationStart(Animation animation) {
        linearLayoutMediaController.setVisibility(View.GONE);
    }

    private void showHideMediaControls(final boolean visible) {
        if (visible) {
            linearLayoutMediaController.setVisibility(View.VISIBLE);
            imageViewPauseIndicator.setVisibility(View.VISIBLE);
        } else {
            linearLayoutMediaController.setVisibility(View.GONE);
            if (MediaPlayerHelper.getInstance().secondPlayermState != State.Completed && MediaPlayerHelper.getInstance().secondPlayermState != State.Paused)
                imageViewPauseIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        configurePlayerAfterTextureAvailable(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    protected void configurePlayerAfterTextureAvailable(SurfaceTexture surface) {
        if (MediaPlayerHelper.getInstance().secondMediaPlayer != null) {
            MediaPlayerHelper.getInstance().secondMediaPlayer.setSurface(new Surface(surface));
            if (MediaPlayerHelper.getInstance().secondPlayermState == State.Playing || MediaPlayerHelper.getInstance().secondPlayermState == State.Paused)
                setUiToPlayer();

            if (MediaPlayerHelper.getInstance().secondPlayermState == State.Paused)
                showHideMediaControls(true);
            updatePlayButtonUi();
        }
    }

    private void startPlayingVideo(MediaPlayer mp) {
        setUiToPlayer();
        mp.start();
        setmState(State.Playing);
        updatePlayButtonUi();
        setProgressBarTimeUi(mp);

        mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                showMediaControllerHandler();
                hideMediaController();
                progressBarWait.setVisibility(View.GONE);
            }
        });
    }

    private void setUiToPlayer() {
        updateUiForFullScreen();
        imageViewPauseIndicator.setVisibility(View.VISIBLE);
        seekBarProgress.setProgress(utils.getProgressPercentage(MediaPlayerHelper.getInstance().secondMediaPlayer.getCurrentPosition(), MediaPlayerHelper.getInstance().secondMediaPlayer.getDuration()));
        showHideThumbnail(false);
        updateProgressBar();
        enableDisableTextureView(true);
    }

    private void setProgressBarTimeUi(MediaPlayer mp) {
        duration = mp.getDuration();
        textViewLength.setText(utils.milliSecondsToTimer(duration));
        seekBarProgress.setMax(100);
    }

    private void pauseVideoPlaying(MediaPlayer mp) {
        imageViewPauseIndicator.setVisibility(View.VISIBLE);
        surfaceViewFrame.setClickable(true);
        mp.pause();
        setmState(State.Paused);
        updatePlayButtonUi();
    }

    private void updatePlayButtonUi() {
        if (MediaPlayerHelper.getInstance().secondPlayermState == State.Playing || MediaPlayerHelper.getInstance().secondPlayermState == State.Prepared)
            imageViewPauseIndicator.setImageResource(R.drawable.jc_click_pause_selector);
        else if (MediaPlayerHelper.getInstance().secondPlayermState == State.Paused || MediaPlayerHelper.getInstance().secondPlayermState == State.Completed)
            imageViewPauseIndicator.setImageResource(R.drawable.jc_click_play_selector);
    }

    private void updateProgressBar() {
        handler.postDelayed(mUpdateTimeTask, 100);

    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                long totalDuration = MediaPlayerHelper.getInstance().secondMediaPlayer.getDuration();
                long currentDuration = MediaPlayerHelper.getInstance().secondMediaPlayer.getCurrentPosition();
                textViewLength.setText("" + utils.milliSecondsToTimer(totalDuration));
                textViewPlayed.setText("" + utils.milliSecondsToTimer(currentDuration));
                int progress = (utils.getProgressPercentage(currentDuration, totalDuration));
                seekBarProgress.setProgress(progress);
                handler.postDelayed(this, 100);
                previousTime = currentDuration;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void hideMediaControllerHandler() {
        Message message = new Message();
        message.what = HIDE_CONTROLLER;
        handler.sendMessage(message);
    }

    private void showMediaControllerHandler() {
        Message message = new Message();
        message.what = SHOW_CONTROLLER;
        handler.sendMessage(message);
    }


    private void updateProgressBarHandler() {
        Message message = new Message();
        message.what = UPDATE_PROGRESS_BAR;
        handler.sendMessage(message);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HIDE_CONTROLLER:
                    showHideMediaControls(false);
                    break;
                case SHOW_CONTROLLER:
                    showHideMediaControls(true);
                    break;
                case UPDATE_PROGRESS_BAR:
                    updateProgressBar();
                    break;
            }

            return false;
        }
    });

    public void setmState(State currentState) {
        MediaPlayerHelper.getInstance().setSecondPlayermState(currentState);
    }

    private void setValuesForNewInstance() {
        if (MediaPlayerHelper.getInstance().secondPlayerFullScreen)
            showHideThumbnail(false);

    }


    private void enableDisableTextureView(boolean enable) {
        if (!enable) {
            surfaceViewFrame.setVisibility(GONE);
            surfaceViewFrame.setClickable(false);
        } else {
            surfaceViewFrame.setVisibility(VISIBLE);
            surfaceViewFrame.setClickable(true);
        }
    }

    private void updateUiForStartPreparing(boolean visible) {
        hideMediaControllerHandler();
        progressBarWait.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void showToast(final String string) {
        Toast.makeText(getContext(), string, Toast.LENGTH_LONG).show();

    }

    private void showHideThumbnail(boolean show) {
        videoThumbnail.setVisibility(show ? VISIBLE : GONE);
    }

    public void addTextureView() {

        surfaceViewFrame = new ResizeTextureView(getContext());
        surfaceViewFrame.setId(surfaceViewID);
        surfaceViewFrame.setOnClickListener(this);
        surfaceViewFrame.setSurfaceTextureListener(this);
        surfaceViewFrame.setKeepScreenOn(true);
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER);
        enableDisableTextureView(false);
        playerView.addView(surfaceViewFrame, layoutParams);
    }

    public void releaseVideo() {
        setmState(State.Completed);
        backPress(MediaPlayerHelper.getInstance().secondMediaPlayer);
        linearLayoutMediaController.setVisibility(View.GONE);
        showHideThumbnail(true);
        enableDisableTextureView(false);
        surfaceViewFrame = null;
        secondMediaPlayer = null;
    }

    public void backPress(MediaPlayer mediaPlayer) {
        if (MediaPlayerHelper.getInstance().getStopPlayingInterface() != null)
            MediaPlayerHelper.getInstance().getStopPlayingInterface().onStopVideoPlaying();
        updatePlayButtonUi();
        mediaPlayer.stop();
        mediaPlayer.release();
        handler.removeCallbacks(mUpdateTimeTask);
        progressBarWait.setVisibility(View.GONE);
        // imageViewPauseIndicator.setVisibility(View.VISIBLE);
        MediaPlayerHelper.getInstance().resetSecondPlayer();

    }

    private void resetPreviousPlayer() {
        if (MediaPlayerHelper.getInstance().getPreviousSecondVideoPlayer() != null) {
            SecondVideoPlayer previousVideoPlayer = MediaPlayerHelper.getInstance().getPreviousSecondVideoPlayer();
            previousVideoPlayer.releaseVideo();
        }
    }

    public ImageView getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(ImageView videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    protected void setShowPlayButton(boolean clickable) {
        //this.imageViewPauseIndicator.setClickable(clickable);
        this.imageViewPauseIndicator.setVisibility(clickable ? VISIBLE : GONE);
    }
}
