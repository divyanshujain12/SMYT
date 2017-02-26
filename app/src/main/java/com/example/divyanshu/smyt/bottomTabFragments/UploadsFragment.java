package com.example.divyanshu.smyt.bottomTabFragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomToasts;
import com.example.divyanshu.smyt.DialogActivities.RecordNewVideoDataActivity;
import com.example.divyanshu.smyt.Fragments.PostChallengeFragment;
import com.example.divyanshu.smyt.Fragments.RuntimePermissionHeadlessFragment;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.PermissionUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshuPC on 2/24/2017.
 */

public class UploadsFragment extends BaseFragment implements RuntimePermissionHeadlessFragment.PermissionCallback{

    @InjectView(R.id.goLiveLL)
    LinearLayout goLiveLL;
    @InjectView(R.id.uploadVideoLL)
    LinearLayout uploadVideoLL;
    @InjectView(R.id.challengeSomeoneLL)
    LinearLayout challengeSomeoneLL;
    @InjectView(R.id.uploadMusicLL)
    LinearLayout uploadMusicLL;

    private RuntimePermissionHeadlessFragment runtimePermissionHeadlessFragment;
    private static final int CAMERA_REQUEST = 101;
    protected String[] mRequiredPermissions = {};

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

    private void initViews() {
        mRequiredPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };
        runtimePermissionHeadlessFragment = CommonFunctions.getInstance().addRuntimePermissionFragment((AppCompatActivity) getActivity(), this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.goLiveLL, R.id.uploadVideoLL, R.id.challengeSomeoneLL, R.id.uploadMusicLL})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goLiveLL:
                checkHasPermissions();
                break;
            case R.id.uploadVideoLL:
                checkHasPermissions();
                break;
            case R.id.challengeSomeoneLL:
                showDialogFragment(PostChallengeFragment.getInstance());
                break;
            case R.id.uploadMusicLL:
                CustomToasts.getInstance(getContext()).showErrorToast("This Feature Coming Soon...");
                break;
        }
    }
    private void checkHasPermissions() {
        if (PermissionUtil.isMNC())
            runtimePermissionHeadlessFragment.addAndCheckPermission(mRequiredPermissions, CAMERA_REQUEST);
        else goToRecordNewVideoDataActivity();
    }

    @Override
    public void onPermissionGranted(int permissionType) {
        goToRecordNewVideoDataActivity();
    }

    @Override
    public void onPermissionDenied(int permissionType) {

    }
    private void goToRecordNewVideoDataActivity() {
        Intent intent = new Intent(getActivity(), RecordNewVideoDataActivity.class);
        startActivity(intent);
    }
}
