package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.Utilities;

import java.io.IOException;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerTwo;

/**
 * Created by divyanshuPC on 3/24/2017.
 */

public class CustomMusicPlayer extends LinearLayout implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, MediaPlayer.OnPreparedListener {
    private SeekBar seekBar;
    private TextView current, total;
    private LinearLayout layout_bottom;
    private ImageView start;
    private MediaPlayer mp;
    private Handler mHandler = new Handler();
    private String mediaUrl = "";
    private int CURRENT_STATUS = 0;
    private int MEDIA_PREPARING = 1;
    private int MEDIA_PLAYING = 2;
    private int MEDIA_PAUSE = 3;

    private static CustomMusicPlayer prevPlayedPlayer;
    private ProgressBar progressBar2;
private PowerManager pw;
    public CustomMusicPlayer(Context context) {
        super(context);
    }

    public CustomMusicPlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.music_player_view, this);
        seekBar = (SeekBar) findViewById(R.id.progress);
        current = (TextView) findViewById(R.id.current);
        total = (TextView) findViewById(R.id.total);
        layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        start = (ImageView) findViewById(R.id.start);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        seekBar.setOnSeekBarChangeListener(this);
        start.setOnClickListener(this);
    }

    public void playSong() {
        // Play song
        try {

            resetPreviousPlayer();
            mp = new MediaPlayer();
            mp.reset();
            mp.setDataSource(mediaUrl);
            mp.prepareAsync();
            CURRENT_STATUS = MEDIA_PREPARING;
            mp.setOnPreparedListener(this);
            showProgressBar(this, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPrevPlayedPlayer(this);
    }

    private void showProgressBar(CustomMusicPlayer currentPlayedPlayer, boolean visible) {
        currentPlayedPlayer.progressBar2.setVisibility(visible ? VISIBLE : GONE);
        currentPlayedPlayer.start.setVisibility(visible ? GONE : VISIBLE);
    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();
            // Displaying Total Duration time
            total.setText("" + Utilities.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            current.setText("" + Utilities.milliSecondsToTimer(currentDuration));
            // Updating seekBar bar
            int progress = (int) (Utilities.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+seekBar);
            seekBar.setProgress(progress);
            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    public void resetPreviousPlayer() {
        if (getPrevPlayedPlayer() != null) {
            MediaPlayer mp = getPrevPlayedPlayer().mp;
            if (mp != null) {
                mp.stop();
                mp.reset();
                mp.release();
                mp = null;
                getPrevPlayedPlayer().CURRENT_STATUS = MEDIA_PREPARING;
                getPrevPlayedPlayer().getTotal().setText("0:00");
                getPrevPlayedPlayer().getCurrent().setText("0:00");
                //layout_bottom.setVisibility(GONE);
                getPrevPlayedPlayer().start.setImageResource(R.drawable.jc_click_play_selector);
                showProgressBar(getPrevPlayedPlayer(), false);
                if (getPrevPlayedPlayer().mUpdateTimeTask != null)
                    getPrevPlayedPlayer().mHandler.removeCallbacks(getPrevPlayedPlayer().mUpdateTimeTask);
            }
            prevPlayedPlayer = null;
        }
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = Utilities.progressToTimer(seekBar.getProgress(), totalDuration);
        // forward or backward to certain seconds
        mp.seekTo(currentPosition);
        // update timer progress again
        updateProgressBar();
    }

    @Override
    public void onClick(View view) {
        if (mp != null && CURRENT_STATUS != MEDIA_PREPARING) {
            if (CURRENT_STATUS == MEDIA_PLAYING) {
                mp.pause();
                start.setImageResource(R.drawable.jc_click_play_selector);
                CURRENT_STATUS = MEDIA_PAUSE;
            } else if (CURRENT_STATUS == MEDIA_PAUSE) {
                mp.start();
                start.setImageResource(R.drawable.jc_click_pause_selector);
                CURRENT_STATUS = MEDIA_PLAYING;
            }
        } else {
            JCVideoPlayer.releaseAllVideos();
            JCVideoPlayerTwo.releaseAllVideos();
            playSong();
        }
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mp.start();
        start.setImageResource(R.drawable.jc_click_pause_selector);
        seekBar.setProgress(0);
        seekBar.setMax(100);
        updateProgressBar();
        CURRENT_STATUS = MEDIA_PLAYING;
        showProgressBar(this, false);
    }

    public TextView getCurrent() {
        return current;
    }

    public void setCurrent(TextView current) {
        this.current = current;
    }

    public TextView getTotal() {
        return total;
    }

    public void setTotal(TextView total) {
        this.total = total;
    }

    public static CustomMusicPlayer getPrevPlayedPlayer() {
        return prevPlayedPlayer;
    }

    public static void setPrevPlayedPlayer(CustomMusicPlayer prevPlayedPlayer) {
        CustomMusicPlayer.prevPlayedPlayer = prevPlayedPlayer;
    }
}
