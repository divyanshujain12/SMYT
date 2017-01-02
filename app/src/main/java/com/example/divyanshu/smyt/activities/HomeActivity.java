package com.example.divyanshu.smyt.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.CustomViews.CustomTabLayout;
import com.example.divyanshu.smyt.Fragments.PostChallengeFragment;
import com.example.divyanshu.smyt.Fragments.RuntimePermissionHeadlessFragment;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.HomeFragments.AllVideosFragment;
import com.example.divyanshu.smyt.HomeFragments.LiveVideosFragment;
import com.example.divyanshu.smyt.HomeFragments.SearchFragment;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.UserProfileFragments.UserOngoingChallengeFragment;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.PermissionUtil;
import com.example.divyanshu.smyt.Utils.Utils;
import com.player.divyanshu.customvideoplayer.MediaPlayerHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class HomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener, OnClickListener, RuntimePermissionHeadlessFragment.PermissionCallback {
    @InjectView(R.id.homeTabLayout)
    CustomTabLayout homeTabLayout;
    @InjectView(R.id.homeViewPager)
    ViewPager homeViewPager;
    ViewPagerAdapter viewPagerAdapter;
    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    private CategoryModel categoryModel;
    private int viewPagerCurrentPos = 0;

    private RuntimePermissionHeadlessFragment runtimePermissionHeadlessFragment;
    private static final int CAMERA_REQUEST = 101;
    protected String[] mRequiredPermissions = {};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {

        mRequiredPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };
        runtimePermissionHeadlessFragment = CommonFunctions.getInstance().addRuntimePermissionFragment(this, this);
        fab.setVisibility(View.GONE);
        categoryModel = getIntent().getExtras().getParcelable(Constants.DATA);
        Utils.configureToolbarForHomeActivity(this, toolbarView, categoryModel.getcategory_name() /*+ "(" + categoryModel.getUsercount() + ")"*/);
        fab.setOnClickListener(this);
        configViewPager();
        toolbarView.setOnClickListener(this);
    }


    private void configViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(AllVideosFragment.getInstance(), getString(R.string.tab_all_videos));
        viewPagerAdapter.addFragment(LiveVideosFragment.getInstance(), getString(R.string.tab_live_videos));
        viewPagerAdapter.addFragment(UserOngoingChallengeFragment.newInstance(true), getString(R.string.new_challenges));
        viewPagerAdapter.addFragment(SearchFragment.getInstance(categoryModel.getId()), getString(R.string.tab_search));

        homeViewPager.setAdapter(viewPagerAdapter);
        homeViewPager.setOffscreenPageLimit(3);
        homeViewPager.setOnPageChangeListener(this);

        if (getIntent().getBooleanExtra(Constants.FROM_NOTIFICATION, false)) {
            homeViewPager.setCurrentItem(2);
            MySharedPereference.getInstance().setString(this, Constants.CATEGORY_ID, categoryModel.getId());
        }

        homeTabLayout.post(new Runnable() {
            @Override
            public void run() {
                homeTabLayout.setupWithViewPager(homeViewPager);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_activity, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_user_profile:
                intent = new Intent(this, UserProfileActivity.class);
                break;
            case R.id.action_recording:
                checkHasPermissions();
                break;
        }
        if (intent != null)
            startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        MediaPlayerHelper.getInstance().releaseAllVideos();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerHelper.getInstance().releaseAllVideos();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        viewPagerCurrentPos = position;
        if (position == 2 || position == 1)
            showFab(position);
        else
            fab.setVisibility(View.GONE);

    }

    private void showFab(int position) {
        fab.setVisibility(View.VISIBLE);
        switch (position) {
            case 1:
                fab.setImageResource(R.drawable.fav_icon_postvideo);
                break;
            case 2:
                fab.setImageResource(R.drawable.fav_icon_challenge);
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                onFabClick();
                break;
            case R.id.toolbarView:
                CustomAlertDialogs.showAlertDialog(this,getString(R.string.description),categoryModel.getDescription(),this);
                break;
        }
    }

    private void onFabClick() {
        if (viewPagerCurrentPos == 2)
            showDialogFragment(PostChallengeFragment.getInstance());
        else if (viewPagerCurrentPos == 1)
        {

        }
    }

    @Override
    public void onPermissionGranted(int permissionType) {
        goToRecordVideoActivity();
    }

    @Override
    public void onPermissionDenied(int permissionType) {

    }

    private void checkHasPermissions() {
        if (PermissionUtil.isMNC())
            runtimePermissionHeadlessFragment.addAndCheckPermission(mRequiredPermissions, CAMERA_REQUEST);
        else goToRecordVideoActivity();
    }

    private void goToRecordVideoActivity() {
        Intent intent = new Intent(this, RecordVideoActivity.class);
        startActivity(intent);
    }
}
