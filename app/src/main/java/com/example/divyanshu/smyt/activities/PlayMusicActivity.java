package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomMusicPlayer;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.MusicPlayerClickEvent;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.Utils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class PlayMusicActivity extends BaseActivity implements MusicPlayerClickEvent {

    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.customMusicPlayer)
    CustomMusicPlayer customMusicPlayer;
    public static ArrayList<AllVideoModel> allVideoModels;
    private int selectedSongPos = 0;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {
        selectedSongPos = getIntent().getIntExtra(Constants.SELECTED_SONG_POS, 0);
        customMusicPlayer.initialize(allVideoModels);
        customMusicPlayer.playAudio(selectedSongPos);
        customMusicPlayer.setMusicPlayerClickEvent(this);
        Utils.configureToolbarWithBackButton(this, toolbarView, allVideoModels.get(selectedSongPos).getTitle());
        toolbarView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
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
                updateFavStatus();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomMusicPlayer.stopService();
    }

    @Override
    public void onNextClick(int pos) {
        setUpUiOnTrackChange(pos);
    }

    @Override
    public void onPrevClick(int pos) {
        setUpUiOnTrackChange(pos);
    }

    private void setUpUiOnTrackChange(int pos) {
        selectedSongPos = pos;
        String trackTitle = allVideoModels.get(selectedSongPos).getTitle();
        getSupportActionBar().setTitle(trackTitle);
        updateFavStatus();
    }

    private void updateFavStatus() {
        int favStatus = allVideoModels.get(selectedSongPos).getFavourite_status();
        if (favStatus == 0) {
            menu.findItem(R.id.action_fav).setIcon(R.drawable.ic_fav_un_select);
        } else {
            menu.findItem(R.id.action_fav).setIcon(R.drawable.ic_fav_select);
        }
    }
}
