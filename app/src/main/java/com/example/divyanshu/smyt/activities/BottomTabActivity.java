package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.CustomViews.CustomMusicPlayer;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.ResetPager;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.NonSwipeableViewPager;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.bottomTabFragments.CategoryFeedsFragment;
import com.example.divyanshu.smyt.bottomTabFragments.SearchFragment;
import com.example.divyanshu.smyt.bottomTabFragments.UploadsFragment;
import com.example.divyanshu.smyt.bottomTabFragments.UserProfileFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerTwo;

public class BottomTabActivity extends BaseActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, ResetPager {


    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.fragmentContainerFL)
    FrameLayout fragmentContainerFL;
    @InjectView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    @InjectView(R.id.activity_bottom_tab)
    RelativeLayout activityBottomTab;
    @InjectView(R.id.genreIV)
    ImageView genreIV;
    @InjectView(R.id.bottomTabVP)
    NonSwipeableViewPager bottomTabVP;
    private FragmentManager manager;

    //  private CategoryModel categoryModel;
    private int currentSelectedItemID = 0;
    private String categoryID, categoryName, categoryDesc;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_bottom_tab);

        ButterKnife.inject(this);
        initViews();
        //checkCategorySelected();
        // initViews();
    }


    private void initViews() {

        categoryDesc = MySharedPereference.getInstance().getString(this, Constants.DESC);
        categoryID = MySharedPereference.getInstance().getString(this, Constants.CATEGORY_ID);
        categoryName = MySharedPereference.getInstance().getString(this, Constants.CATEGORY_NAME);
        Utils.configureToolbarForHomeActivity(this, toolbarView, categoryName /*+ "(" + categoryModel.getUsercount() + ")"*/);
        toolbarView.setOnClickListener(this);
        manager = getSupportFragmentManager();
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        configViewPager();
        bottomTabVP.setCurrentItem(0);


    }

    private void configViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(CategoryFeedsFragment.getInstance(null), getString(R.string.tab_all_videos));
        viewPagerAdapter.addFragment(SearchFragment.getInstance(categoryID), getString(R.string.tab_live_videos));
        viewPagerAdapter.addFragment(UploadsFragment.getInstance(categoryID), getString(R.string.new_challenges));
        viewPagerAdapter.addFragment(UserProfileFragment.getInstance(), getString(R.string.tab_search));

        bottomTabVP.setAdapter(viewPagerAdapter);
        bottomTabVP.setOffscreenPageLimit(3);
        bottomTabVP.addOnPageChangeListener(this);

        if (getIntent().getBooleanExtra(Constants.FROM_NOTIFICATION, false)) {
            bottomTabVP.setCurrentItem(2);
            MySharedPereference.getInstance().setString(this, Constants.CATEGORY_ID, categoryID);
        }

    }


    @Override
    public void onClick(View view) {
        CustomAlertDialogs.showCategoryDescDialog(this, getString(R.string.description), categoryDesc, this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemID = item.getItemId();
        if (currentSelectedItemID == itemID)
            return false;
        else {
            switch (itemID) {
                case R.id.action_feeds:
                    bottomTabVP.setCurrentItem(0);
                    break;
                case R.id.action_search:
                    bottomTabVP.setCurrentItem(1);
                    break;
                case R.id.action_upload:
                    bottomTabVP.setCurrentItem(2);
                    break;
                case R.id.action_user_profile:
                    bottomTabVP.setCurrentItem(3);
                    break;
            }
            currentSelectedItemID = itemID;
            releaseVideos();
        }
        return true;
    }

    private void releaseVideos() {
        JCVideoPlayer.releaseAllVideos();
        JCVideoPlayerTwo.releaseAllVideos();
    }

    @OnClick(R.id.genreIV)
    public void onClick() {
        startActivity(new Intent(this, CategoriesActivity.class));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigation.getMenu().getItem(position).setChecked(true);
        if (position == 0) {
            setToolbarText(categoryName, getString(R.string.click_for_desc));
        } else
            setToolbarText(bottomNavigation.getMenu().getItem(position).getTitle().toString(), "");
        CustomMusicPlayer.stopService();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setToolbarText(String text, String subtitle) {
        getSupportActionBar().setTitle(text);
        getSupportActionBar().setSubtitle(subtitle);
    }

    private void checkCategorySelected() {
        if (MySharedPereference.getInstance().getString(this, Constants.CATEGORY_ID).equals("")) {
            Intent intent = new Intent(this, CategoriesActivity.class);
            startActivity(intent);
            finish();

        } else {
            initViews();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.bottomTabVP + ":" + bottomTabVP.getCurrentItem());
        if (page != null)
            page.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResetPager() {
        View view = bottomNavigation.findViewById(R.id.action_feeds);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigation.getMenu().getItem(0).setChecked(true);
                currentSelectedItemID = R.id.action_feeds;
                bottomTabVP.setCurrentItem(0);
                releaseVideos();
            }
        });
        view.performClick();

    }

    @Override
    public void onBackPressed() {
        if (bottomTabVP.getCurrentItem() != 0) {
            onResetPager();
        } else {
        if (JCVideoPlayer.backPress())
            return;
        if (JCVideoPlayerTwo.backPress())
            return;
        CustomMusicPlayer.stopService();
            super.onBackPressed();
        }
    }

    @Override
    public boolean isFinishing() {
       // CustomMusicPlayer.stopService();
        return super.isFinishing();
    }
}