package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomTabLayout;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.HomeFragments.AllVideosFragment;
import com.example.divyanshu.smyt.HomeFragments.LiveVideosFragment;
import com.example.divyanshu.smyt.HomeFragments.SearchFragment;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class HomeActivity extends BaseActivity {
    @InjectView(R.id.homeTabLayout)
    CustomTabLayout homeTabLayout;
    @InjectView(R.id.homeViewPager)
    ViewPager homeViewPager;
    ViewPagerAdapter viewPagerAdapter;
    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {

        CategoryModel categoryModel = getIntent().getExtras().getParcelable(Constants.DATA);
        Utils.configureToolbarWithBackButton(this, toolbarView, categoryModel.getcategory_name() + "(" + categoryModel.getUsercount() + ")");

        configViewPager();
    }


    private void configViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(AllVideosFragment.getInstance(), getString(R.string.tab_all_videos));
        viewPagerAdapter.addFragment(LiveVideosFragment.getInstance(), getString(R.string.tab_live_videos));
        viewPagerAdapter.addFragment(SearchFragment.getInstance(), getString(R.string.tab_search));
        homeViewPager.setAdapter(viewPagerAdapter);
        homeViewPager.setOffscreenPageLimit(3);
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
        switch (item.getItemId()) {

            case R.id.action_user_profile:
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}
