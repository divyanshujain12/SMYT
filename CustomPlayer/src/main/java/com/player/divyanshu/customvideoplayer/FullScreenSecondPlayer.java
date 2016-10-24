package com.player.divyanshu.customvideoplayer;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by divyanshu.jain on 10/19/2016.
 */

public class FullScreenSecondPlayer extends Activity implements HomeWatcher.OnHomePressedListener {
    SecondVideoPlayer fullScreenVideoPlayer;
    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_second_player);
        if (!MediaPlayerHelper.getInstance().secondPlayerFullScreen)
            finish();
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(this);
        mHomeWatcher.startWatch();
        fullScreenVideoPlayer = (SecondVideoPlayer) findViewById(R.id.fullScreenVideoPlayer);
        //   fullScreenVideoPlayer.setUp();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaPlayerHelper.getInstance().setSecondPlayerFullScreen(false);
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
