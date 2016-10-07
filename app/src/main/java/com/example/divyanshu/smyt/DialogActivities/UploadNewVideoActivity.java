package com.example.divyanshu.smyt.DialogActivities;

import android.os.Bundle;

import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.R;

import butterknife.ButterKnife;

/**
 * Created by divyanshu.jain on 10/7/2016.
 */

public class UploadNewVideoActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_video);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {

    }
}
