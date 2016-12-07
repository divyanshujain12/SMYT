package com.example.divyanshu.smyt.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.TouchImageView;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ProfileImageFullScreen extends AppCompatActivity {

    @InjectView(R.id.fullViewIV)
    TouchImageView fullViewIV;
    ImageLoading imageLoading;
    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_image_full_screen);
        ButterKnife.inject(this);
        Utils.configureToolbarWithBackButton(this, toolbarView, "");
        String imageUrl = getIntent().getStringExtra(Constants.PROFILE_IMAGE);
        imageLoading = new ImageLoading(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fullViewIV.setTransitionName(Constants.PROFILE_IMAGE);
        }
        imageLoading.LoadImage(imageUrl, fullViewIV, null);

    }

}
