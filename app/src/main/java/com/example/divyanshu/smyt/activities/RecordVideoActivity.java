package com.example.divyanshu.smyt.activities;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Fragments.UploadVideoFragment;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu.jain on 9/2/2016.
 */
public class RecordVideoActivity extends BaseActivity {
    @InjectView(R.id.backgroundStateTV)
    TextView backgroundStateTV;
    @InjectView(R.id.toneTypeTV)
    TextView toneTypeTV;
    @InjectView(R.id.startRecordingIV)
    ImageView startRecordingIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_video_activity);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {

    }

    @OnClick(R.id.startRecordingIV)
    public void onClick() {
        showDialogFragment(new UploadVideoFragment());

    }
}
