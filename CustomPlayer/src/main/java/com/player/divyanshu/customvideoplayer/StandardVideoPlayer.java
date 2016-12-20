package com.player.divyanshu.customvideoplayer;

/**
 * Created by Divyanshu on 10/6/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class StandardVideoPlayer extends FrameLayout implements OnSeekBarChangeListener, OnPreparedListener, OnBufferingUpdateListener,
        OnClickListener, OnSeekCompleteListener, AnimationListener, TextureView.SurfaceTextureListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener,MediaPlayer.OnCompletionListener {
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
    private long duration = 0;
    private MediaPlayer standardMediaPlayer = null;
    public static final int HIDE_CONTROLLER = 0;
    public static final int SHOW_CONTROLLER = 1;
    public static final int UPDATE_PROGRESS_BAR = 2;
    private ImageView videoThumbnail;
    private FrameLayout playerView;
    private int surfaceViewID = 10001;
    private Uri videoUri;
    private boolean hideControls = false;

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //resetPlayer(mediaPlayer);
    }

    public enum State {
        Retrieving,
        Preparing,
        Playing,
        Paused,
        Prepared,
        Completed;
    }

    public StandardVideoPlayer(Context context) {
        super(context);
        setUp();
    }

    public StandardVideoPlayer(Context context, AttributeSet attrs) {
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
        if (MediaPlayerHelper.getInstance().isStandardPlayerFullScreen()) {
            standardMediaPlayer = MediaPlayerHelper.getInstance().getMediaPlayer();
            playVideo();
        }
    }

    public void playVideo() {
        if (videoUri == null && !MediaPlayerHelper.getInstance().isStandardPlayerFullScreen()) {
            showToast("No Url");
            return;
        }
        if (surfaceViewFrame == null)
            addTextureView();
        if (standardMediaPlayer == null || MediaPlayerHelper.getInstance().mState == State.Completed) {
            try {
                MediaPlayerHelper.getInstance().releaseAllVideos();
                MediaPlayerHelper.getInstance().setPreviousStandardVideoPlayer(this);
                MediaPlayerHelper.getInstance().setCurrentStandardPlayer(this);
                standardMediaPlayer = new MediaPlayer();
                MediaPlayerHelper.getInstance().setMediaPlayer(standardMediaPlayer);
                MediaPlayerHelper.getInstance().mediaPlayer.setOnPreparedListener(this);
                MediaPlayerHelper.getInstance().mediaPlayer.setOnBufferingUpdateListener(this);
                MediaPlayerHelper.getInstance().mediaPlayer.setOnSeekCompleteListener(this);
                MediaPlayerHelper.getInstance().mediaPlayer.setScreenOnWhilePlaying(true);
                MediaPlayerHelper.getInstance().mediaPlayer.setOnErrorListener(this);
                MediaPlayerHelper.getInstance().mediaPlayer.setDataSource(getContext(), videoUri);
                MediaPlayerHelper.getInstance().mediaPlayer.setOnCompletionListener(this);
                MediaPlayerHelper.getInstance().mediaPlayer.prepareAsync();

                updateUiForStartPreparing(true);
                setmState(State.Preparing);
                progressBarWait.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                showToast("Error while playing video");
                e.printStackTrace();
            }

        } else if (!MediaPlayerHelper.getInstance().mediaPlayer.isPlaying() || MediaPlayerHelper.getInstance().standardPlayerFullScreen) {
            if (MediaPlayerHelper.getInstance().mState == State.Preparing) {
                progressBarWait.setVisibility(View.VISIBLE);
                isBuffering(true);
            }
            startPlayingVideo(MediaPlayerHelper.getInstance().mediaPlayer);
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
        if (MediaPlayerHelper.getInstance().mediaPlayer.isPlaying()) {
            int totalDuration = MediaPlayerHelper.getInstance().mediaPlayer.getDuration();
            int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
            MediaPlayerHelper.getInstance().mediaPlayer.seekTo(currentPosition);
            updateProgressBar();

        }
    }

    public void onPrepared(MediaPlayer mp) {
        setmState(State.Prepared);
        isBuffering(false);
        startPlayingVideo(mp);
        MediaPlayerHelper.getInstance().setVideoPlayed(this);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what){
            case -38:
               // playVideo();
                break;
            case 263:
                playVideo();
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra1) {
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
        if (MediaPlayerHelper.getInstance().isStandardPlayerFullScreen()) {
            MediaPlayerHelper.getInstance().standardPlayerFullScreen = false;
            ((Activity) getContext()).onBackPressed();
        } else {
            goToFullScreen();
            MediaPlayerHelper.getInstance().standardPlayerFullScreen = true;
        }

    }

    protected void onTinyScreenButtonClick() {
        if (MediaPlayerHelper.getInstance().isStandardPlayerFullScreen()) {
            ((Activity) getContext()).onBackPressed();
            MediaPlayerHelper.getInstance().standardPlayerFullScreen = false;
        } else {
            goToTinyScreen();
            MediaPlayerHelper.getInstance().standardPlayerFullScreen = true;
        }
    }

    public void updateUiForFullScreen() {
        if (MediaPlayerHelper.getInstance().standardPlayerFullScreen) {
            fullScreen.setImageResource(R.drawable.jc_shrink);
        } else {
            fullScreen.setImageResource(R.drawable.jc_enlarge);

        }
    }

    private void goToFullScreen() {
        Intent intent = new Intent(getContext(), FullScreenStandardVideoPlayer.class);
        getContext().startActivity(intent);
    }

    private void goToTinyScreen() {
        Intent intent = new Intent(getContext(), TwoPlayerActivity.class);
        intent.putExtra("FirstUrl", videoUri.toString());
        getContext().startActivity(intent);
    }

    private void playButtonClicked() {
        if (standardMediaPlayer != null && MediaPlayerHelper.getInstance().mState == State.Playing) {
            pauseVideoPlaying(MediaPlayerHelper.getInstance().mediaPlayer);

        } else {
            showHideMediaControls(false);
            playVideo();
        }
    }

    public void onSeekComplete(MediaPlayer mp) {
      /*  handler.removeCallbacks(mUpdateTimeTask);
        pauseVideoPlaying(MediaPlayerHelper.getInstance().getMediaPlayer());
        progressBarWait.setVisibility(View.GONE);*/
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

    private void showHideMediaControls(final boolean show) {
        if (show && !hideControls) {
            linearLayoutMediaController.setVisibility(View.VISIBLE);
            imageViewPauseIndicator.setVisibility(View.VISIBLE);
        } else {
            linearLayoutMediaController.setVisibility(View.GONE);
            if (MediaPlayerHelper.getInstance().mState != State.Completed && MediaPlayerHelper.getInstance().mState != State.Paused)
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
        if (MediaPlayerHelper.getInstance().mediaPlayer != null) {
            MediaPlayerHelper.getInstance().mediaPlayer.setSurface(new Surface(surface));
            if (MediaPlayerHelper.getInstance().mState == State.Playing || MediaPlayerHelper.getInstance().mState == State.Paused)
                setUiToPlayer();

            if (MediaPlayerHelper.getInstance().mState == State.Paused)
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
        seekBarProgress.setProgress(utils.getProgressPercentage(MediaPlayerHelper.getInstance().mediaPlayer.getCurrentPosition(), MediaPlayerHelper.getInstance().mediaPlayer.getDuration()));
        showHideThumbnail(false);
        updateProgressBar();
        enableDisableTextureView(true);
    }

    private void setProgressBarTimeUi(MediaPlayer mp) {
        duration = mp.getDuration();
        textViewLength.setText(utils.milliSecondsToTimer(duration));
        seekBarProgress.setMax(100);
    }

    protected void pauseVideoPlaying(MediaPlayer mp) {
        imageViewPauseIndicator.setVisibility(View.VISIBLE);
        surfaceViewFrame.setClickable(true);
        mp.pause();
        setmState(State.Paused);
        updatePlayButtonUi();
    }

    private void updatePlayButtonUi() {
        if (MediaPlayerHelper.getInstance().mState == State.Playing || MediaPlayerHelper.getInstance().mState == State.Prepared)
            imageViewPauseIndicator.setImageResource(R.drawable.jc_click_pause_selector);
        else if (MediaPlayerHelper.getInstance().mState == State.Paused || MediaPlayerHelper.getInstance().mState == State.Completed)
            imageViewPauseIndicator.setImageResource(R.drawable.jc_click_play_selector);
    }

    private void updateProgressBar() {
        handler.postDelayed(mUpdateTimeTask, 100);

    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                long totalDuration = MediaPlayerHelper.getInstance().mediaPlayer.getDuration();
                long currentDuration = MediaPlayerHelper.getInstance().mediaPlayer.getCurrentPosition();
                textViewLength.setText("" + utils.milliSecondsToTimer(totalDuration));
                textViewPlayed.setText("" + utils.milliSecondsToTimer(currentDuration));
                int progress = (utils.getProgressPercentage(currentDuration, totalDuration));
                seekBarProgress.setProgress(progress);
                handler.postDelayed(this, 100);
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

    public void setmState(State mState) {
        MediaPlayerHelper.getInstance().setmState(mState);
    }

    private void setValuesForNewInstance() {
        if (MediaPlayerHelper.getInstance().standardPlayerFullScreen)
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
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER);
        enableDisableTextureView(false);
        playerView.addView(surfaceViewFrame, layoutParams);
        surfaceViewFrame.setOnClickListener(this);
        surfaceViewFrame.setSurfaceTextureListener(this);
        surfaceViewFrame.setKeepScreenOn(true);
    }

    public void releaseVideo() {

        backPress(MediaPlayerHelper.getInstance().mediaPlayer);

    }

    public void backPress(MediaPlayer player) {
        if (MediaPlayerHelper.getInstance().getStopPlayingInterface() != null)
            MediaPlayerHelper.getInstance().getStopPlayingInterface().onStopVideoPlaying();
        resetPlayer(player);

    }

    private void resetPlayer(MediaPlayer player) {
        setmState(State.Completed);
        updatePlayButtonUi();
        player.stop();
        player.release();
        handler.removeCallbacks(mUpdateTimeTask);
        progressBarWait.setVisibility(View.GONE);
        MediaPlayerHelper.getInstance().resetFirstPlayer();
        linearLayoutMediaController.setVisibility(View.GONE);
        showHideThumbnail(true);
        enableDisableTextureView(false);
        surfaceViewFrame = null;
        standardMediaPlayer = null;
    }

    public ImageView getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(ImageView videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    protected void setShowPlayButton(boolean clickable) {
        //  this.imageViewPauseIndicator.setClickable(clickable);
        imageViewPauseIndicator.setVisibility(clickable ? VISIBLE : GONE);
    }

    public void setHideControls(boolean hideControls) {
        this.hideControls = hideControls;
    }
}
