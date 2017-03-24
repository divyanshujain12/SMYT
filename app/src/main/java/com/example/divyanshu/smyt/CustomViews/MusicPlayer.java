package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.Utilities;

import java.io.IOException;

/**
 * Created by divyanshuPC on 3/24/2017.
 */

public class MusicPlayer extends LinearLayout implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private SeekBar seekBar;
    private TextView current, total;
    private LinearLayout layout_bottom;
    private ImageView start;
    private MediaPlayer mp;
    private Handler mHandler = new Handler();
    ;

    public MusicPlayer(Context context) {
        super(context);
    }

    public MusicPlayer(Context context, @Nullable AttributeSet attrs) {
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
        mp = new MediaPlayer();
        seekBar.setOnSeekBarChangeListener(this);
        start.setOnClickListener(this);
    }

    public void playSong(String musicUrl) {
        // Play song
        try {
            mp.reset();
            mp.setDataSource(musicUrl);
            mp.prepare();
            mp.start();
            start.setImageResource(R.drawable.jc_click_pause_selector);
            seekBar.setProgress(0);
            seekBar.setMax(100);
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void resetPlayer() {
        if (mp != null) {
            mp.stop();
            mp.release();
            layout_bottom.setVisibility(GONE);
            start.setImageResource(R.drawable.jc_click_play_selector);
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
        if (mp.isPlaying()) {
            mp.pause();
            start.setImageResource(R.drawable.jc_click_play_selector);
        } else {
            mp.start();
            start.setImageResource(R.drawable.jc_click_pause_selector);
        }
    }
}
