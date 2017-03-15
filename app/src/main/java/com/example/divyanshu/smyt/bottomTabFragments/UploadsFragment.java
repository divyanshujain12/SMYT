package com.example.divyanshu.smyt.bottomTabFragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomTabLayout;
import com.example.divyanshu.smyt.Fragments.RuntimePermissionHeadlessFragment;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.PermissionUtil;
import com.example.divyanshu.smyt.uploadFragments.PostChallengeFragment;
import com.example.divyanshu.smyt.uploadFragments.RecordNewVideoDataFragment;
import com.example.divyanshu.smyt.uploadFragments.UploadMusicFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshuPC on 2/24/2017.
 */

public class UploadsFragment extends BaseFragment implements RuntimePermissionHeadlessFragment.PermissionCallback {

    @InjectView(R.id.goLiveLL)
    LinearLayout goLiveLL;
    @InjectView(R.id.uploadVideoLL)
    LinearLayout uploadVideoLL;
    @InjectView(R.id.challengeSomeoneLL)
    LinearLayout challengeSomeoneLL;
    @InjectView(R.id.uploadMusicLL)
    LinearLayout uploadMusicLL;
    @InjectView(R.id.uploadsTabLayout)
    CustomTabLayout uploadsTabLayout;
    @InjectView(R.id.uploadsViewPager)
    ViewPager uploadsViewPager;


    private RuntimePermissionHeadlessFragment runtimePermissionHeadlessFragment;
    private static final int CAMERA_REQUEST = 101;
    protected String[] mRequiredPermissions = {};
    private ViewPagerAdapter viewPagerAdapter;

    public static UploadsFragment getInstance(String categoryID) {
        UploadsFragment uploadsFragment = new UploadsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CATEGORY_ID, categoryID);
        uploadsFragment.setArguments(bundle);
        return uploadsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uploads_fragment, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
//        checkHasPermissions();
    }

    private void initViews() {
        mRequiredPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };
        runtimePermissionHeadlessFragment = CommonFunctions.getInstance().addRuntimePermissionFragment((AppCompatActivity) getActivity(), this);
        configViewPager();
    }

    private void configViewPager() {
        uploadsTabLayout.setTabTextColors(getResources().getColor(R.color.white_with_ninty_nine_alpha), Color.WHITE);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(PostChallengeFragment.getInstance(), getString(R.string.post_challenge));
        viewPagerAdapter.addFragment(RecordNewVideoDataFragment.getInstance(), getString(R.string.go_live_upload_video));
        viewPagerAdapter.addFragment(UploadMusicFragment.getInstance(), getString(R.string.upload_music));
        uploadsViewPager.setAdapter(viewPagerAdapter);
        uploadsViewPager.setOffscreenPageLimit(2);

        uploadsTabLayout.post(new Runnable() {
            @Override
            public void run() {
                uploadsTabLayout.setupWithViewPager(uploadsViewPager);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.goLiveLL, R.id.uploadVideoLL, R.id.challengeSomeoneLL, R.id.uploadMusicLL})
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.goLiveLL:
                checkHasPermissions();
                break;
            case R.id.uploadVideoLL:
                checkHasPermissions();
                break;
            case R.id.challengeSomeoneLL:
                showDialogFragment(PostChallengeFragment.getInstance());
                break;
            case R.id.uploadMusicLL:
                CustomToasts.getInstance(getContext()).showErrorToast(getString(R.string.feature_coming_soon));
                break;*/
        }
    }

    private void checkHasPermissions() {
        if (PermissionUtil.isMNC())
            runtimePermissionHeadlessFragment.addAndCheckPermission(mRequiredPermissions, CAMERA_REQUEST);
        //else goToRecordNewVideoDataActivity();
    }

    @Override
    public void onPermissionGranted(int permissionType) {
        //goToRecordNewVideoDataActivity();
    }

    @Override
    public void onPermissionDenied(int permissionType) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.uploadsViewPager + ":" + uploadsViewPager.getCurrentItem());
        page.onActivityResult(requestCode, resultCode, data);
    }

    private void goToRecordNewVideoDataActivity() {
        Intent intent = new Intent(getActivity(), RecordNewVideoDataFragment.class);
        startActivity(intent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            checkHasPermissions();
    }
}
