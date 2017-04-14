package com.example.divyanshu.smyt.CustomViews;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.divyanshu.smyt.Interfaces.MusicPlayerClickEvent;
import com.example.divyanshu.smyt.Interfaces.PlayerInterface;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.Utilities;
import com.example.divyanshu.smyt.activities.MyApp;
import com.example.divyanshu.smyt.musicPlayer.MediaPlayerService;
import com.example.divyanshu.smyt.musicPlayer.StorageUtil;

import java.util.ArrayList;

import static com.example.divyanshu.smyt.Constants.Constants.Broadcast_PAUSE_AUDIO;
import static com.example.divyanshu.smyt.Constants.Constants.Broadcast_PLAY_NEW_AUDIO;
import static com.example.divyanshu.smyt.Constants.Constants.Broadcast_RESUME_AUDIO;

/**
 * Created by divyanshuPC on 3/24/2017.
 */

public class CustomMusicPlayer extends LinearLayout implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, PlayerInterface {
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
    private ImageView prevSongIV, nextSongIV;
    private static CustomMusicPlayer prevPlayedPlayer;
    private ProgressBar progressBar2;
    public static MediaPlayerService mediaPlayerService;
    static boolean serviceBound;
    private TextView musicTitleTV;
    private ArrayList<AllVideoModel> allVideoModels;
    private ImageView removeIV;
    public static ServiceConnection serviceConnectionIN;
    private ImageView musicThumbIV;

    private MusicPlayerClickEvent musicPlayerClickEvent;

    public CustomMusicPlayer(Context context) {
        super(context);
    }

    public CustomMusicPlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public void initialize(ArrayList<AllVideoModel> allVideoModels) {
        this.allVideoModels = allVideoModels;
        initViews();
    }

    private void initViews() {
        // serviceBound = false;
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.music_player_view, this);
        seekBar = (SeekBar) findViewById(R.id.progress);
        current = (TextView) findViewById(R.id.current);
        total = (TextView) findViewById(R.id.total);
        layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        start = (ImageView) findViewById(R.id.start);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        prevSongIV = (ImageView) findViewById(R.id.prevSongIV);
        nextSongIV = (ImageView) findViewById(R.id.nextSongIV);
        musicTitleTV = (TextView) findViewById(R.id.musicTitleTV);
        removeIV = (ImageView) findViewById(R.id.removeIV);
        musicThumbIV = (ImageView) findViewById(R.id.musicThumbIV);
        seekBar.setOnSeekBarChangeListener(this);
        start.setOnClickListener(this);
        nextSongIV.setOnClickListener(this);
        prevSongIV.setOnClickListener(this);
        removeIV.setOnClickListener(this);
    }

    public void playAudio(int audioIndex) {
        MediaPlayerService.playerInterface = this;
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getContext());
            storage.storeAudio(allVideoModels);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(getContext(), MediaPlayerService.class);
            MyApp.getInstance().getApplicationContext().startService(playerIntent);
            MyApp.getInstance().getApplicationContext().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            bindPlayerWithService();
            mediaPlayerService.registerReceivers();
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getContext());
            storage.storeAudioIndex(audioIndex);
            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            getContext().sendBroadcast(broadcastIntent);
        }

        showProgressBar(this, true);

    }


    private void showProgressBar(CustomMusicPlayer currentPlayedPlayer, boolean visible) {
        currentPlayedPlayer.progressBar2.setVisibility(visible ? VISIBLE : GONE);
        currentPlayedPlayer.start.setVisibility(visible ? GONE : VISIBLE);
    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (mp != null) {
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
            } else {
                getHandler().removeCallbacks(mUpdateTimeTask);
            }
        }
    };

    public void resetPreviousPlayer() {
        /*if (getPrevPlayedPlayer() != null) {
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
        }*/
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
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
        switch (view.getId()) {
            case R.id.start:
                onPlayButtonClick();
                break;
            case R.id.prevSongIV:
                mediaPlayerService.skipToPrevious();

                break;
            case R.id.nextSongIV:
                mediaPlayerService.skipToNext();
                break;
            case R.id.removeIV:
                stopService();
                setVisibility(GONE);
                break;
        }


    }

    private void onPlayButtonClick() {
        if (mp != null && CURRENT_STATUS != MEDIA_PREPARING) {
            if (CURRENT_STATUS == MEDIA_PLAYING) {
                mp.pause();
                start.setImageResource(android.R.drawable.ic_media_play);
                CURRENT_STATUS = MEDIA_PAUSE;
                Intent broadcastIntent = new Intent(Broadcast_PAUSE_AUDIO);
                getContext().sendBroadcast(broadcastIntent);
            } else if (CURRENT_STATUS == MEDIA_PAUSE) {
                mp.start();
                start.setImageResource(android.R.drawable.ic_media_pause);
                CURRENT_STATUS = MEDIA_PLAYING;
                Intent broadcastIntent = new Intent(Broadcast_RESUME_AUDIO);
                getContext().sendBroadcast(broadcastIntent);
            }
        }
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
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

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            mediaPlayerService = binder.getService();
            serviceBound = true;
            bindPlayerWithService();
            serviceConnectionIN = this;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private void bindPlayerWithService() {
        mp = mediaPlayerService.getMediaPlayer();
        mediaPlayerService.setPlayerInterface(this);
    }

    private void resetPlayerUi() {
        getTotal().setText("0:00");
        getCurrent().setText("0:00");
        seekBar.setProgress(0);
        mHandler.removeCallbacks(mUpdateTimeTask);

    }

    @Override
    public void onPlayed() {
        CURRENT_STATUS = MEDIA_PLAYING;
        updateProgressBar();
        showProgressBar(this, false);
        start.setImageResource(android.R.drawable.ic_media_pause);
        musicTitleTV.setText(mediaPlayerService.getActiveAudio().getTitle());
    }

    @Override
    public void onPaused() {
        CURRENT_STATUS = MEDIA_PAUSE;
        start.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    public void error() {

    }

    @Override
    public void onNextPlayed() {
        resetPlayerUi();
        musicTitleTV.setText(mediaPlayerService.getActiveAudio().getTitle());
        if (musicPlayerClickEvent != null)
            musicPlayerClickEvent.onNextClick(mediaPlayerService.getAudioIndex());
    }

    @Override
    public void onPrevPlayed() {
        resetPlayerUi();
        musicTitleTV.setText(mediaPlayerService.getActiveAudio().getTitle());
        if (musicPlayerClickEvent != null)
            musicPlayerClickEvent.onPrevClick(mediaPlayerService.getAudioIndex());
    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering() {
        CURRENT_STATUS = MEDIA_PREPARING;
        showProgressBar(this, true);
        resetPlayerUi();
    }

    @Override
    public void onDestroy() {
        //resetPlayerUi();
        serviceBound = false;
        mediaPlayerService = null;
        serviceConnectionIN = null;
        mHandler.removeCallbacks(mUpdateTimeTask);
        setVisibility(GONE);
    }

    public MusicPlayerClickEvent getMusicPlayerClickEvent() {
        return musicPlayerClickEvent;
    }

    public void setMusicPlayerClickEvent(MusicPlayerClickEvent musicPlayerClickEvent) {
        this.musicPlayerClickEvent = musicPlayerClickEvent;
    }

    public static void stopService() {
        if (mediaPlayerService != null && serviceConnectionIN != null) {
            mediaPlayerService.stopSelf();
            MyApp.getInstance().getApplicationContext().unbindService(serviceConnectionIN);
        }
    }
}
