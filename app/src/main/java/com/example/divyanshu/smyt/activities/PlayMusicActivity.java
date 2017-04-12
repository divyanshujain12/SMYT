package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.CustomViews.CustomMusicPlayer;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.Utils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class PlayMusicActivity extends BaseActivity {

    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.customMusicPlayer)
    CustomMusicPlayer customMusicPlayer;
    public static ArrayList<AllVideoModel> allVideoModels;
    private int selectedSongPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {
        Utils.configureToolbarForHomeActivity(this, toolbarView, getString(R.string.player));
        selectedSongPos = getIntent().getIntExtra(Constants.SELECTED_SONG_POS, 0);
        customMusicPlayer.initialize(allVideoModels);
        customMusicPlayer.playAudio(selectedSongPos);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music_player_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_like:
                intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);

                break;
            case R.id.action_fav:
                CustomAlertDialogs.showRuleDialog(this, getString(R.string.rules), new SnackBarCallback() {
                    @Override
                    public void doAction() {

                    }
                });
                break;
        }
        return true;
    }
}
