package com.example.divyanshu.smyt.myProfileActivities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomTabLayout;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.UserProfileFragments.UserFollowersFragment;
import com.example.divyanshu.smyt.UserProfileFragments.UserFollowingFragment;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyFollowingFollowersActivity extends BaseActivity {

    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.activity_following)
    LinearLayout activityFollowing;
    ViewPagerAdapter viewPagerAdapter;
    @InjectView(R.id.tabs)
    CustomTabLayout tabs;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        ButterKnife.inject(this);

        initViews();
    }

    private void initViews() {
        Utils.configureToolbarWithBackButton(this, toolbarView, getString(R.string.following_followers));
        ConfigViewPager();
    }


    private void ConfigViewPager() {
        String customerID = MySharedPereference.getInstance().getString(this, Constants.CUSTOMER_ID);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(UserFollowingFragment.getInstance(customerID), getString(R.string.following));
        viewPagerAdapter.addFragment(UserFollowersFragment.getInstance(customerID), getString(R.string.followers));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);


        tabs.post(new Runnable() {
            @Override
            public void run() {
                tabs.setupWithViewPager(viewPager);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
