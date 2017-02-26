package com.example.divyanshu.smyt.myProfileActivities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.CustomViews.CustomTabLayout;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.UserProfileFragments.UserCompletedChallengeFragment;
import com.example.divyanshu.smyt.UserProfileFragments.UserOngoingChallengeFragment;
import com.example.divyanshu.smyt.Utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyChallengesActivity extends AppCompatActivity {

    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.tabs)
    CustomTabLayout tabs;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;
    @InjectView(R.id.activity_my_feeds)
    LinearLayout activityMyFeeds;

    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feeds);
        ButterKnife.inject(this);

        initViews();
    }

    private void initViews() {
        Utils.configureToolbarWithBackButton(this,toolbarView,getString(R.string.m_challenges));
        ConfigViewPager();
    }
    private void ConfigViewPager() {
        //String customerID = MySharedPereference.getInstance().getString(this, Constants.CUSTOMER_ID);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(UserOngoingChallengeFragment.newInstance(false), getString(R.string.ongoing));
        viewPagerAdapter.addFragment(UserCompletedChallengeFragment.newInstance(), getString(R.string.completed));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);


        tabs.post(new Runnable() {
            @Override
            public void run() {
                tabs.setupWithViewPager(viewPager);
            }
        });
    }
}
