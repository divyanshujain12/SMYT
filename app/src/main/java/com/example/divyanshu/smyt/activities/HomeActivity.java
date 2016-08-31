package com.example.divyanshu.smyt.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.CustomViews.CustomTabLayout;
import com.example.divyanshu.smyt.CustomViews.HomeHeader;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.GlobalClasses.SingletonClass;
import com.example.divyanshu.smyt.HomeFragments.AllVideosFragment;
import com.example.divyanshu.smyt.HomeFragments.LiveVideosFragment;
import com.example.divyanshu.smyt.HomeFragments.SearchFragment;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class HomeActivity extends BaseActivity {
    @InjectView(R.id.homeHeader)
    HomeHeader homeHeader;
    @InjectView(R.id.homeTabLayout)
    CustomTabLayout homeTabLayout;
    @InjectView(R.id.homeViewPager)
    ViewPager homeViewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {

        CategoryModel categoryModel = SingletonClass.getInstance().getSelectedCategoryData(this);
        homeHeader.initHeader(this, categoryModel.getName(), categoryModel.getUsersCount());

        configViewPager();

        homeViewPager.setOffscreenPageLimit(3);
        homeTabLayout.setupWithViewPager(homeViewPager);
        homeTabLayout.setTabTextColors(getResources().getColor(R.color.color_primary_with_eight_eight), getResources().getColor(R.color.colorPrimary));
    }

    private void configViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(AllVideosFragment.getInstance(), getString(R.string.tab_all_videos));
        viewPagerAdapter.addFragment(LiveVideosFragment.getInstance(), getString(R.string.tab_live_videos));
        viewPagerAdapter.addFragment(SearchFragment.getInstance(), getString(R.string.tab_search));
        homeViewPager.setAdapter(viewPagerAdapter);
    }
}
