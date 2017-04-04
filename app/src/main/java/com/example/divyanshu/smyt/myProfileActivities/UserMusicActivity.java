package com.example.divyanshu.smyt.myProfileActivities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomMusicPlayer;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments.UserMusicFeeds;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu on 3/28/2017.
 */

public class UserMusicActivity extends BaseActivity {
    FragmentManager fragmentManager;
    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.fragmentContainerFL)
    FrameLayout fragmentContainerFL;
    @InjectView(R.id.activity_user_notification)
    LinearLayout activityUserNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {
        Utils.configureToolbarWithBackButton(this, toolbarView, getString(R.string.music));
        fragmentManager = getSupportFragmentManager();
        updateFragment();
    }

    private void updateFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerFL, UserMusicFeeds.getInstance(MySharedPereference.getInstance().getString(this, Constants.CUSTOMER_ID)));
        fragmentTransaction.commit();
    }

  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomMusicPlayer.stopService();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomMusicPlayer.stopService();
    }
}
