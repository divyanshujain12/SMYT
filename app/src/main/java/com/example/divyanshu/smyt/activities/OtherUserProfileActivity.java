package com.example.divyanshu.smyt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.CustomViews.CustomMusicPlayer;
import com.example.divyanshu.smyt.CustomViews.CustomTabLayout;
import com.example.divyanshu.smyt.CustomViews.RoundedImageView;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments.UserFavoriteFeeds;
import com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments.UserMusicFeeds;
import com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments.UserVideosFragment;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.myProfileActivities.MyFollowingFollowersActivity;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerStandardTwo;

/**
 * Created by divyanshu on 9/3/2016.
 */
public class OtherUserProfileActivity extends BaseActivity implements ViewPager.OnPageChangeListener, Animation.AnimationListener {

    @InjectView(R.id.viewPager)
    ViewPager viewPager;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.tabs)
    CustomTabLayout tabs;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.followersCountTV)
    TextView followersCountTV;
    @InjectView(R.id.followingCountTV)
    TextView followingCountTV;
    @InjectView(R.id.profileImage)
    RoundedImageView profileImage;
    @InjectView(R.id.nameInImgTV)
    TextView nameInImgTV;
    @InjectView(R.id.statusTV)
    TextView statusTV;
    @InjectView(R.id.userInfoLL)
    LinearLayout userInfoLL;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    @InjectView(R.id.main_content)
    CoordinatorLayout mainContent;
    @InjectView(R.id.followersLL)
    LinearLayout followersLL;
    @InjectView(R.id.followingLL)
    LinearLayout followingLL;
    private int viewPagerPos = 0;
    private ImageLoading imageLoading;
    private ViewPagerAdapter viewPagerAdapter;
    private Animation fabIn, fabOut;
    private UserModel userModel;
    private String otherCustomerID = "";
    private MenuItem followedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_profile_activity);
        ButterKnife.inject(this);
        initViews(getIntent());
    }

    private void initViews(Intent intent) {
        imageLoading = new ImageLoading(this);
        otherCustomerID = intent.getStringExtra(Constants.CUSTOMER_ID);
        createAnimation();
        ConfigViewPager();
        Utils.configureToolbarWithBackButton(this, toolbar, "");
        fab.setVisibility(View.GONE);
        CallWebService.getInstance(this, true, ApiCodes.GET_USER_INFO).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_OTHER_CUSTOMER_DETAIL, createJsonForGetUserData(), this);
    }

    private void ConfigViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(UserVideosFragment.getInstance(otherCustomerID), getString(R.string.videos));
        viewPagerAdapter.addFragment(UserMusicFeeds.getInstance(otherCustomerID), getString(R.string.music));
        viewPagerAdapter.addFragment(UserFavoriteFeeds.getInstance(otherCustomerID), getString(R.string.favorites));
        //viewPagerAdapter.addFragment(UserFollowingFragment.getInstance(otherCustomerID), getString(R.string.following));
        //viewPagerAdapter.addFragment(UserFollowersFragment.getInstance(otherCustomerID), getString(R.string.followers));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(4);
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
        if (MySharedPereference.getInstance().getString(this, Constants.CATEGORY_ID).equals("")) {
            fab.setVisibility(View.GONE);
            return;
        }
        viewPagerPos = position;
        switch (position) {
            case 0:
                fab.setImageResource(R.drawable.fav_icon_challenge);
                fab.startAnimation(fabIn);
                break;
            case 1:
                fab.startAnimation(fabOut);
                break;
        }

        CustomMusicPlayer.stopService();
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
                CustomAlertDialogs.showRuleDialog(this, getString(R.string.rules), new SnackBarCallback() {
                    @Override
                    public void doAction() {
                        Intent intent = new Intent(OtherUserProfileActivity.this, PostChallengeActivity.class);
                        intent.putExtra(Constants.USER_DATA, createBundleForPostChallenge());
                        startActivity(intent);
                        //showDialogFragment(PostChallengeFragment.getInstance(createBundleForPostChallenge()));
                    }
                });


                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.other_user_activity, menu);
        followedButton = menu.findItem(R.id.action_follow);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_follow:
                hitFollowWebService(item);
                return true;
        }
        return true;
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case ApiCodes.GET_USER_INFO:
                userModel = UniversalParser.getInstance().parseJsonObject(response.getJSONArray(Constants.DATA).getJSONObject(0), UserModel.class);
                if (!MySharedPereference.getInstance().getString(this, Constants.CATEGORY_ID).equals(""))
                    fab.setVisibility(View.VISIBLE);
                updateUi();
                break;
        }

    }

    private void updateUi() {
        //txtName.setText(userModel.getFirst_name());
        //phoneNumberTV.setText(userModel.getPhonenumber());
        nameInImgTV.setText(userModel.getUsername());
        followersCountTV.setText("" + userModel.getFollowers());
        followingCountTV.setText("" + userModel.getFollowing());
        imageLoading.LoadImage(userModel.getProfileimage(), profileImage, null);
        statusTV.setText(userModel.getTimeline_msg());
        if (userModel.getFollowStatus().equals("1"))
            followedButton.setTitle(R.string.unfollow);
        else
            followedButton.setTitle(getString(R.string.follow));
    }

    private void hitFollowWebService(MenuItem item) {
        String followStatus = userModel.getFollowStatus();
        if (followStatus.equals("1")) {
            followStatus = "0";
            setFollowStatusOnUI(item, getString(R.string.unfollow));

        } else {
            followStatus = "1";
            setFollowStatusOnUI(item, getString(R.string.follow));
        }
        userModel.setFollowStatus(followStatus);

        CallWebService.getInstance(this, false, ApiCodes.FOLLOW_USER).hitJsonObjectRequestAPI(CallWebService.POST, API.ADD_REMOVE_FOLLOWING, createJsonForFollowUser(followStatus), this);

    }

    private void setFollowStatusOnUI(MenuItem item, String status) {
        CommonFunctions.showShortLengthSnackbar(status, fab);
        item.setTitle(status);
    }

    private JSONObject createJsonForFollowUser(String followStatus) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.FOLLOWING_ID, userModel.getCustomer_id());
            jsonObject.put(Constants.FOLLOW_STATUS, followStatus);
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForGetUserData() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CUSTOMER_ID_ONE, otherCustomerID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initViews(intent);
    }

    private Bundle createBundleForPostChallenge() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, userModel);
        bundle.putBoolean(Constants.OTHER_USER, true);
        return bundle;
    }

    @Override
    public void onBackPressed() {
        CustomMusicPlayer.stopService();
        if (JCVideoPlayerStandard.backPress())
            return;
        else if (JCVideoPlayerStandardTwo.backPress())
            return;
        else {
            // MediaPlayerHelper.getInstance().releaseAllVideos();
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayerStandard.releaseAllVideos();
        JCVideoPlayerStandardTwo.releaseAllVideos();
        //MediaPlayerHelper.getInstance().releaseAllVideos();
    }

    @OnClick({R.id.followersLL, R.id.followingLL})
    public void onClick(View view) {
        Intent intent = new Intent(OtherUserProfileActivity.this, MyFollowingFollowersActivity.class);
        intent.putExtra(Constants.CUSTOMER_ID,otherCustomerID);
        startActivity(intent);
       /* switch (view.getId()) {
            case R.id.followersLL:
                break;
            case R.id.followingLL:
                break;
        }*/
    }
}

