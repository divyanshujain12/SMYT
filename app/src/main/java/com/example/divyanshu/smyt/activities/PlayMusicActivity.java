package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomMusicPlayer;
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.CustomViews.VideoTitleView;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.DeleteVideoInterface;
import com.example.divyanshu.smyt.Interfaces.MusicPlayerClickEvent;
import com.example.divyanshu.smyt.Interfaces.TitleBarButtonClickCallback;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class PlayMusicActivity extends BaseActivity implements MusicPlayerClickEvent, TitleBarButtonClickCallback {

    /*  @InjectView(R.id.toolbarView)
      Toolbar toolbarView;*/
    @InjectView(R.id.customMusicPlayer)
    CustomMusicPlayer customMusicPlayer;
    public static ArrayList<AllVideoModel> allVideoModels;
    @InjectView(R.id.musicThumbIV)
    ImageView musicThumbIV;
    @InjectView(R.id.videoTitleView)
    VideoTitleView videoTitleView;
    @InjectView(R.id.firstUserIV)
    RoundedImageView firstUserIV;
    @InjectView(R.id.firstUserNameTV)
    TextView firstUserNameTV;
    @InjectView(R.id.uploadedTimeTV)
    TextView uploadedTimeTV;
    @InjectView(R.id.firstUserLL)
    LinearLayout firstUserLL;
    @InjectView(R.id.commentsTV)
    TextView commentsTV;
    @InjectView(R.id.viewsCountTV)
    TextView viewsCountTV;
    @InjectView(R.id.userOneLikesCountTV)
    TextView userOneLikesCountTV;
    @InjectView(R.id.musicBottomRL)
    RelativeLayout musicBottomRL;
    private int selectedSongPos = 0;
    private Menu menu;
private ImageLoading imageLoading;
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
        videoTitleView.getTitleTV().setTextColor(R.color.white);
        imageLoading = new ImageLoading(this);
        setUpUi(selectedSongPos);

        //Utils.configureToolbarWithOutBackButton(this, toolbarView, allVideoModels.get(selectedSongPos).getTitle());
        //toolbarView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomMusicPlayer.stopService();
    }

    @OnClick({R.id.firstUserLL, R.id.commentsTV, R.id.viewsCountTV, R.id.userOneLikesCountTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.firstUserLL:
                break;
            case R.id.commentsTV:
                break;
            case R.id.viewsCountTV:
                break;
            case R.id.userOneLikesCountTV:
                break;
        }
    }

    @Override
    public void onNextClick(int pos) {
        setUpUi(pos);
    }

    @Override
    public void onPrevClick(int pos) {
        setUpUi(pos);
    }

    private void setUpUi(int pos) {
        selectedSongPos = pos;
        setUpMusicTitleBar(allVideoModels.get(pos));
    }

    private void setUpMusicTitleBar(AllVideoModel allVideoModel) {
        imageLoading.LoadImage(allVideoModel.getProfileimage(), firstUserIV, null);
        videoTitleView.setUpViewsForListing(allVideoModel.getTitle(), 0, allVideoModel.getCustomers_videos_id(), this);
        setUpMoreIvButtonVisibilityForSingleVideo();
        setUpFavButton(allVideoModel);
    }

    private void setUpFavButton(AllVideoModel allVideoModel) {
        videoTitleView.setUpFavIVButton(allVideoModel.getFavourite_status());
    }

    private void setUpMoreIvButtonVisibilityForSingleVideo() {
        videoTitleView.showHideMoreIvButton(false);
    }

    @Override
    public void onTitleBarButtonClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.deleteVideoTV:
                CommonFunctions.getInstance().deleteVideo(this, allVideoModels.get(position).getCustomers_videos_id(), new DeleteVideoInterface() {
                    @Override
                    public void onDeleteVideo() {
                        //  removeItem(position);
                    }
                });
                break;
            case R.id.favIV:
                CallWebService.getInstance(this, false, ApiCodes.ACTION_FAVORITE).hitJsonObjectRequestAPI(CallWebService.POST, API.ACTION_FAVORITE, CommonFunctions.getInstance().createJsonForActionFav(this, allVideoModels.get(position).getCustomers_videos_id(), updateUiForFavClick((ImageView) view, position)), null);
                break;
        }
    }

    private int updateUiForFavClick(ImageView view, int position) {
        int favStatus = allVideoModels.get(position).getFavourite_status();
        if (favStatus == 0) {
            view.setImageResource(R.drawable.ic_fav_select);
            favStatus = 1;
        } else {
            view.setImageResource(R.drawable.ic_fav_un_select);
            favStatus = 0;
        }
        allVideoModels.get(position).setFavourite_status(favStatus);
        return favStatus;
    }
}
