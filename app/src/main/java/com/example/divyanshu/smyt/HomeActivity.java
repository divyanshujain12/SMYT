package com.example.divyanshu.smyt;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.divyanshu.smyt.Adapters.HomeViewPagerAdapter;
import com.example.divyanshu.smyt.CustomViews.HomeHeader;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.GlobalClasses.SingletonClass;
import com.example.divyanshu.smyt.HomeFragments.AllVideosFragment;
import com.example.divyanshu.smyt.HomeFragments.LiveVideosFragment;
import com.example.divyanshu.smyt.HomeFragments.SearchFragment;
import com.example.divyanshu.smyt.Models.CategoryModel;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class HomeActivity extends BaseActivity {
    @InjectView(R.id.homeHeader)
    HomeHeader homeHeader;
    @InjectView(R.id.homeTabLayout)
    TabLayout homeTabLayout;
    @InjectView(R.id.homeViewPager)
    ViewPager homeViewPager;
    HomeViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {
        CategoryModel categoryModel = SingletonClass.getInstance().getSelectedCategoryData();
        homeHeader.initHeader(this, categoryModel.getName(), categoryModel.getUsersCount());

        viewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AllVideosFragment(), getString(R.string.tab_all_videos));
        viewPagerAdapter.addFragment(new LiveVideosFragment(), getString(R.string.tab_live_videos));
        viewPagerAdapter.addFragment(new SearchFragment(), getString(R.string.tab_search));

        homeViewPager.setAdapter(viewPagerAdapter);

        homeTabLayout.setupWithViewPager(homeViewPager);
    }
}
