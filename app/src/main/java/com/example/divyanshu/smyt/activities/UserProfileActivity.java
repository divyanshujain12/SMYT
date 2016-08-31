package com.example.divyanshu.smyt.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.CustomViews.CustomTabLayout;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.HomeFragments.AllVideosFragment;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.UserProfileFragments.UserChallengesFragment;
import com.example.divyanshu.smyt.UserProfileFragments.UserFollowersFragment;
import com.neopixl.pixlui.components.textview.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/31/2016.
 */
public class UserProfileActivity extends BaseActivity {
    @InjectView(R.id.profileImage)
    ImageView profileImage;
    @InjectView(R.id.nameInImgTV)
    TextView nameInImgTV;
    @InjectView(R.id.txtName)
    TextView txtName;
    @InjectView(R.id.editNameIV)
    ImageView editNameIV;
    @InjectView(R.id.nameFL)
    FrameLayout nameFL;
    @InjectView(R.id.phoneNumberTV)
    TextView phoneNumberTV;
    @InjectView(R.id.editPhoneNumberIV)
    ImageView editPhoneNumberIV;
    @InjectView(R.id.phoneNumberFL)
    FrameLayout phoneNumberFL;
    @InjectView(R.id.followersCountTV)
    TextView followersCountTV;
    @InjectView(R.id.followersFL)
    FrameLayout followersFL;
    @InjectView(R.id.followingCountTV)
    TextView followingCountTV;
    @InjectView(R.id.followingFL)
    FrameLayout followingFL;
    @InjectView(R.id.tabs)
    CustomTabLayout tabs;
    /* @InjectView(R.id.backToolbar)
     ToolbarWithBackButton backToolbar;*/
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;
    @InjectView(R.id.main_content)
    CoordinatorLayout mainContent;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {
        //backToolbar.InitToolbar(this,getString(R.string.user_profile));
        ConfigViewPager();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void ConfigViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(AllVideosFragment.getInstance(), getString(R.string.videos));
        viewPagerAdapter.addFragment(UserFollowersFragment.getInstance(), getString(R.string.followers));
        viewPagerAdapter.addFragment(UserChallengesFragment.getInstance(), getString(R.string.challenges));
        viewPager.setAdapter(viewPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }
}
