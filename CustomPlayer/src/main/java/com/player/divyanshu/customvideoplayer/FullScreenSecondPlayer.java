package com.player.divyanshu.customvideoplayer;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by divyanshu.jain on 10/19/2016.
 */

public class FullScreenSecondPlayer extends Activity {
    SecondVideoPlayer fullScreenVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_second_player);
        fullScreenVideoPlayer = (SecondVideoPlayer) findViewById(R.id.fullScreenVideoPlayer);
     //   fullScreenVideoPlayer.setUp();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaPlayerHelper.getInstance().setSecondPlayerFullScreen(false);
        // fullScreenVideoPlayer.updateUiForFullScreen();
    }
}
