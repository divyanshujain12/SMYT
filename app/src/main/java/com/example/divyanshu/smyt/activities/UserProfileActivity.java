package com.example.divyanshu.smyt.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.CustomViews.CustomTabLayout;
import com.example.divyanshu.smyt.Fragments.PostChallengeFragment;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.UserProfileFragments.UserChallengesFragment;
import com.example.divyanshu.smyt.UserProfileFragments.UserFollowersFragment;
import com.example.divyanshu.smyt.UserProfileFragments.UserVideosFragment;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu.jain on 8/31/2016.
 */
public class UserProfileActivity extends BaseActivity implements ViewPager.OnPageChangeListener, Animation.AnimationListener {
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
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    private int viewPagerPos = 0;

    private ViewPagerAdapter viewPagerAdapter;
    private Animation fabIn, fabOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {


        createAnimation();
        ConfigViewPager();
        Utils.configureToolbarWithBackButton(this, toolbar, "");
    }

    private void ConfigViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(UserVideosFragment.getInstance(), getString(R.string.videos));
        viewPagerAdapter.addFragment(UserFollowersFragment.getInstance(), getString(R.string.followers));
        viewPagerAdapter.addFragment(UserChallengesFragment.getInstance(), getString(R.string.challenges));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(this);

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
        viewPagerPos = position;
        switch (position) {
            case 0:
                fab.setImageResource(R.drawable.fav_icon_postvideo);
                fab.startAnimation(fabIn);
                break;
            case 1:
                fab.startAnimation(fabOut);
                break;
            case 2:
                fab.setImageResource(R.drawable.fav_icon_broadcast);
                fab.startAnimation(fabIn);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void createAnimation() {
        fabIn = AnimationUtils.loadAnimation(this, R.anim.fab_in);
        fabIn.setAnimationListener(this);
        fabOut = AnimationUtils.loadAnimation(this, R.anim.fab_out);
        fabOut.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation == fabIn)
            fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == fabOut)
            fab.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @OnClick(R.id.fab)
    public void onClick() {
        switch (viewPagerPos) {
            case 0:
                Intent intent = new Intent(this, RecordVideoActivity.class);
                startActivity(intent);
                break;
            case 2:
                showDialogFragment(new PostChallengeFragment());
                break;

        }
    }
}
