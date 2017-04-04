package com.example.divyanshu.smyt.bottomTabFragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.CustomViews.CustomMusicPlayer;
import com.example.divyanshu.smyt.CustomViews.CustomTabLayout;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.HomeFragments.AllVideosFragment;
import com.example.divyanshu.smyt.HomeFragments.LiveVideosFragment;
import com.example.divyanshu.smyt.HomeFragments.MusicFragment;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.UserProfileFragments.UserOngoingChallengeFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshuPC on 2/23/2017.
 */

public class CategoryFeedsFragment extends BaseFragment implements ViewPager.OnPageChangeListener {


    /*  @InjectView(R.id.toolbarView)
      Toolbar toolbarView;*/
    @InjectView(R.id.homeTabLayout)
    CustomTabLayout homeTabLayout;
    @InjectView(R.id.homeViewPager)
    ViewPager homeViewPager;
    @InjectView(R.id.fab)
    FloatingActionButton fab;

    ViewPagerAdapter viewPagerAdapter;
    private static CategoryModel categoryModel;


    public static CategoryFeedsFragment getInstance(CategoryModel categoryModel) {
        CategoryFeedsFragment feedsParentFragment = new CategoryFeedsFragment();
        CategoryFeedsFragment.categoryModel = categoryModel;
        return feedsParentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_activity, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {

        configViewPager();
    }

    private void configViewPager() {
        homeTabLayout.setTabTextColors(getResources().getColor(R.color.white_with_ninty_nine_alpha), Color.WHITE);
        fab.setVisibility(View.GONE);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(AllVideosFragment.getInstance(), getString(R.string.tab_all_videos));
        viewPagerAdapter.addFragment(LiveVideosFragment.getInstance(), getString(R.string.tab_live_videos));
        viewPagerAdapter.addFragment(MusicFragment.getInstance(), getString(R.string.music));
        viewPagerAdapter.addFragment(UserOngoingChallengeFragment.newInstance(true),getString(R.string.new_challenges));

        homeViewPager.setAdapter(viewPagerAdapter);
        homeViewPager.setOffscreenPageLimit(2);
        homeViewPager.setOnPageChangeListener(this);
        homeTabLayout.post(new Runnable() {
            @Override
            public void run() {
                homeTabLayout.setupWithViewPager(homeViewPager);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        stopPlayService();
    }

    private void stopPlayService() {
        CustomMusicPlayer.stopService();
      //  getActivity().onBackPressed();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
