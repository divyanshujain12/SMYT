package com.player.divyanshu.customvideoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullScreenStandardVideoPlayer extends AppCompatActivity implements HomeWatcher.OnHomePressedListener {
    StandardVideoPlayer fullScreenVideoPlayer;
    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_standard_video_plaer);
        if (!MediaPlayerHelper.getInstance().standardPlayerFullScreen)
            finish();
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(this);
        mHomeWatcher.startWatch();
        fullScreenVideoPlayer = (StandardVideoPlayer) findViewById(R.id.fullScreenVideoPlayer);
        // fullScreenVideoPlayer.setUp();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaPlayerHelper.getInstance().setStandardPlayerFullScreen(false);
        // fullScreenVideoPlayer.updateUiForFullScreen();
    }

    @Override
    public void onHomePressed() {
        MediaPlayerHelper.getInstance().pauseVideos(this, mHomeWatcher);
    }

    @Override
    public void onHomeLongPressed() {
        MediaPlayerHelper.getInstance().pauseVideos(this, mHomeWatcher);
    }
}
