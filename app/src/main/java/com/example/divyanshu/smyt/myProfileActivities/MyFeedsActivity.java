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
import com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments.UserFavoriteFeeds;
import com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments.UserVideosFragment;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyFeedsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @InjectView(R.id.tabs)
    CustomTabLayout tabs;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;
    @InjectView(R.id.activity_my_feeds)
    LinearLayout activityMyFeeds;

    ViewPagerAdapter viewPagerAdapter;
    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feeds);
        ButterKnife.inject(this);

        initViews();
    }

    private void initViews() {
        Utils.configureToolbarWithBackButton(this, toolbarView, getString(R.string.my_feeds));
        ConfigViewPager();
    }

    private void ConfigViewPager() {
        String customerID = MySharedPereference.getInstance().getString(this, Constants.CUSTOMER_ID);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(UserVideosFragment.getInstance(customerID), getString(R.string.videos));
        viewPagerAdapter.addFragment(UserFavoriteFeeds.getInstance(customerID), getString(R.string.favorites));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(2);


        tabs.post(new Runnable() {
            @Override
            public void run() {
                tabs.setupWithViewPager(viewPager);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
