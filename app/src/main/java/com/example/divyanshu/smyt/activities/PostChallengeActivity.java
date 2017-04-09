package com.example.divyanshu.smyt.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.uploadFragments.PostChallengeFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PostChallengeActivity extends AppCompatActivity {
    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.fragmentContainerFL)
    FrameLayout fragmentContainerFL;

    FragmentManager fragmentManager;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);
        ButterKnife.inject(this);

        initViews();
    }

    private void initViews() {
        Utils.configureToolbarWithBackButton(this, toolbarView, getString(R.string.post_challenge));
        bundle = getIntent().getBundleExtra(Constants.USER_DATA);
        fragmentManager = getSupportFragmentManager();
        updateFragment();
    }

    private void updateFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle.getBoolean(Constants.OTHER_USER, false))
            fragmentTransaction.replace(R.id.fragmentContainerFL, PostChallengeFragment.getInstance(bundle));

        else
            fragmentTransaction.replace(R.id.fragmentContainerFL, PostChallengeFragment.getInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
