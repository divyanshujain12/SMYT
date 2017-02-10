package com.example.divyanshu.smyt.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.CustomViews.CustomTabLayout;
import com.example.divyanshu.smyt.DialogActivities.RecordNewVideoDataActivity;
import com.example.divyanshu.smyt.Fragments.PostChallengeFragment;
import com.example.divyanshu.smyt.Fragments.RuntimePermissionHeadlessFragment;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments.UserFavoriteFeeds;
import com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments.UserVideosFragment;
import com.example.divyanshu.smyt.UserProfileFragments.FeedsParentFragment;
import com.example.divyanshu.smyt.UserProfileFragments.UserChallengesFragment;
import com.example.divyanshu.smyt.UserProfileFragments.UserFollowingFragment;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.PermissionUtil;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerStandardTwo;

import static com.example.divyanshu.smyt.Constants.ApiCodes.GET_USER_INFO;

/**
 * Created by divyanshu.jain on 8/31/2016.
 */

public class UserProfileActivity extends BaseActivity implements ViewPager.OnPageChangeListener, Animation.AnimationListener, View.OnClickListener, RuntimePermissionHeadlessFragment.PermissionCallback {
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
    @InjectView(R.id.statusTV)
    TextView statusTV;
    @InjectView(R.id.statusFL)
    FrameLayout statusFL;
    private int viewPagerPos = 0;

    private ViewPagerAdapter viewPagerAdapter;
    private Animation fabIn, fabOut;
    private UserModel userModel;
    private ImageLoading imageLoading;

    private RuntimePermissionHeadlessFragment runtimePermissionHeadlessFragment;
    private static final int CAMERA_REQUEST = 101;
    protected String[] mRequiredPermissions = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(updateUserInfo, new IntentFilter(Constants.UPDATE_USER_INFO));
        setContentView(R.layout.user_profile_activity);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {

        mRequiredPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };
        runtimePermissionHeadlessFragment = CommonFunctions.getInstance().addRuntimePermissionFragment(this, this);


        imageLoading = new ImageLoading(this, 5);
        createAnimation();
        ConfigViewPager();
        Utils.configureToolbarWithBackButton(this, toolbar, "");

        hitgetUserInfoApi();
    }


    private void hitgetUserInfoApi() {
        CallWebService.getInstance(this, true, GET_USER_INFO).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_DETAIL, CommonFunctions.customerIdJsonObject(this), this);
    }

    private void ConfigViewPager() {
        String customerID = MySharedPereference.getInstance().getString(this, Constants.CUSTOMER_ID);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(UserVideosFragment.getInstance(customerID), getString(R.string.videos));
        viewPagerAdapter.addFragment(UserFavoriteFeeds.getInstance(customerID), getString(R.string.favorites));
        viewPagerAdapter.addFragment(UserFollowingFragment.getInstance(customerID), getString(R.string.following));
        viewPagerAdapter.addFragment(UserChallengesFragment.getInstance(false), getString(R.string.challenges));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);


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
        releaseVideos();
        viewPagerPos = position;
        if (MySharedPereference.getInstance().getString(this, Constants.CATEGORY_ID).equals(""))
            return;
        switch (position) {
            case 0:
                fab.setImageResource(R.drawable.fav_icon_postvideo);
                fab.startAnimation(fabIn);
                break;
            case 1:
                fab.startAnimation(fabOut);
                break;
            case 2:
                fab.startAnimation(fabOut);
                break;
            case 3:
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
                checkHasPermissions();
                break;
            case 3:

                CustomAlertDialogs.showRuleDialog(this, getString(R.string.rules), new SnackBarCallback() {
                    @Override
                    public void doAction() {
                        showDialogFragment(PostChallengeFragment.getInstance());
                    }
                });


                break;

        }
    }

    private void checkHasPermissions() {
        if (PermissionUtil.isMNC())
            runtimePermissionHeadlessFragment.addAndCheckPermission(mRequiredPermissions, CAMERA_REQUEST);
        else goToRecordNewVideoDataActivity();
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case GET_USER_INFO:
                userModel = UniversalParser.getInstance().parseJsonObject(response.getJSONArray(Constants.DATA).getJSONObject(0), UserModel.class);
                updateUI();
                break;
        }
    }

    private void updateUI() {
        txtName.setText(userModel.getFirst_name() + " " + userModel.getLast_name());
        phoneNumberTV.setText(userModel.getPhonenumber());
        nameInImgTV.setText(userModel.getFirst_name() + " " + userModel.getLast_name());
        followersCountTV.setText(userModel.getFollowers());
        followingCountTV.setText(userModel.getFollowing());
        imageLoading.LoadImage(userModel.getProfileimage(), profileImage, null);
        profileImage.setOnClickListener(this);
        statusTV.setText(userModel.getTimeline_msg());
    }


    @Override
    public void onBackPressed() {
        if (JCVideoPlayerStandard.backPress())
            return;
        else if (JCVideoPlayerStandardTwo.backPress())
            return;
        else {
            //    MediaPlayerHelper.getInstance().releaseAllVideos();
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseVideos();
    }

    private void releaseVideos() {
        JCVideoPlayerStandard.releaseAllVideos();
        JCVideoPlayerStandardTwo.releaseAllVideos();
        //  MediaPlayerHelper.getInstance().releaseAllVideos();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.user_profile_menu, menu);
        checkAndHideButtons(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_user_setting:
                intent = new Intent(this, UserSettingActivity.class);
                break;
            case R.id.action_recording:
                checkHasPermissions();
                break;
        }
        if (intent != null)
            startActivity(intent);
        return true;
    }

    private BroadcastReceiver updateUserInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hitgetUserInfoApi();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateUserInfo);
    }

    private void checkAndHideButtons(Menu menu) {
        if (MySharedPereference.getInstance().getString(this, Constants.CATEGORY_ID).equals("")) {
            fab.setVisibility(View.GONE);
            menu.findItem(R.id.action_recording).setVisible(false);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ProfileImageFullScreen.class);
        intent.putExtra(Constants.PROFILE_IMAGE, userModel.getProfileimage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profileImage.setTransitionName(Constants.PROFILE_IMAGE);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, profileImage, Constants.PROFILE_IMAGE);
            startActivity(intent, activityOptionsCompat.toBundle());
        } else
            startActivity(intent);
    }

    @Override
    public void onPermissionGranted(int permissionType) {
        goToRecordNewVideoDataActivity();
    }

    private void goToRecordNewVideoDataActivity() {
        Intent intent = new Intent(this, RecordNewVideoDataActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPermissionDenied(int permissionType) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.getCurrentItem());
        page.onActivityResult(requestCode, resultCode, data);
    }
}
