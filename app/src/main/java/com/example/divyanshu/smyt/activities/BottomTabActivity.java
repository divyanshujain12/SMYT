package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Models.CategoryModel;
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

public class BottomTabActivity extends BaseActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {


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
    private String categoryID,categoryName,categoryDesc;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_bottom_tab);

        ButterKnife.inject(this);
        checkCategorySelected();
        // initViews();
    }


    private void initViews() {

        categoryDesc = MySharedPereference.getInstance().getString(this,Constants.DESC);
        categoryID = MySharedPereference.getInstance().getString(this,Constants.CATEGORY_ID);
        categoryName = MySharedPereference.getInstance().getString(this,Constants.CATEGORY_NAME);
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
        }
        return true;
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
}
