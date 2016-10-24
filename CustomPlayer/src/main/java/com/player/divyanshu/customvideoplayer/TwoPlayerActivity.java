package com.player.divyanshu.customvideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TwoPlayerActivity extends AppCompatActivity {

    private StandardVideoPlayer standardVideoPlayer;
    private SecondVideoPlayer secondVideoPlayer;
    private String firstUrl = "";
    private String secondUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);
        firstUrl = getIntent().getStringExtra("FirstUrl");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaPlayerHelper.getInstance().setStandardPlayerFullScreen(false);
        MediaPlayerHelper.getInstance().setSecondPlayerFullScreen(false);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        secondUrl = intent.getStringExtra("SecondUrl");
        standardVideoPlayer = (StandardVideoPlayer) findViewById(R.id.firstPlayer);
        standardVideoPlayer.setUrl(firstUrl);
        secondVideoPlayer = (SecondVideoPlayer) findViewById(R.id.secondPlayer);
        secondVideoPlayer.setUrl(secondUrl);

        // standardVideoPlayer.setUp();
        //  secondVideoPlayer.setUp();
    }
}
