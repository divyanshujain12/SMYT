package com.example.divyanshu.smyt.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.Fragments.RuntimePermissionHeadlessFragment;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.ImagePickDialogInterface;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.TakePictureHelper;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu.jain on 9/29/2016.
 */

public class UserSettingActivity extends BaseActivity implements ImagePickDialogInterface, RuntimePermissionHeadlessFragment.PermissionCallback {


    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.userIV)
    ImageView userIV;
    @InjectView(R.id.changeUserImageIV)
    ImageView changeUserImageIV;
    @InjectView(R.id.firstNameET)
    EditText firstNameET;
    @InjectView(R.id.lastNameET)
    EditText lastNameET;
    @InjectView(R.id.mobileET)
    EditText mobileET;
    @InjectView(R.id.statusET)
    EditText statusET;
    @InjectView(R.id.aboutUsTV)
    TextView aboutUsTV;
    @InjectView(R.id.contactUsTV)
    TextView contactUsTV;
    String[] cameraPermission, externalStoragePermission;
    RuntimePermissionHeadlessFragment runtimePermissionHeadlessFragment;
    private static final int CAMERA_REQUEST = 101;
    private static final int GALLERY_REQUEST = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        ButterKnife.inject(this);


        InitViews();
    }

    private void InitViews() {
        cameraPermission = new String[]{Manifest.permission.CAMERA};
        externalStoragePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        Utils.configureToolbarWithBackButton(this, toolbarView, getString(R.string.setting));
        String firstName = MySharedPereference.getInstance().getString(this, Constants.FIRST_NAME);
        String lastName = MySharedPereference.getInstance().getString(this, Constants.LAST_NAME);
        String phoneNumber = MySharedPereference.getInstance().getString(this, Constants.PHONE_NUMBER);
        String status = MySharedPereference.getInstance().getString(this, Constants.STATUS);

        firstNameET.setText(firstName);
        lastNameET.setText(lastName);
        mobileET.setText(phoneNumber);
        statusET.setText(status);

        addRuntimePermissionFragment();
    }

    private void addRuntimePermissionFragment() {
        runtimePermissionHeadlessFragment = RuntimePermissionHeadlessFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction().add(runtimePermissionHeadlessFragment, runtimePermissionHeadlessFragment.getClass().getName()).commit();
    }


    @OnClick({R.id.aboutUsTV, R.id.contactUsTV, R.id.changeUserImageIV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aboutUsTV:

                break;
            case R.id.contactUsTV:
                break;
            case R.id.changeUserImageIV:
                CustomAlertDialogs.showImageSelectDialog(this, this);
                break;
        }
    }


    @Override
    public void Camera() {
        runtimePermissionHeadlessFragment.addAndCheckPermission(cameraPermission, CAMERA_REQUEST);
    }

    @Override
    public void Gallery() {
        runtimePermissionHeadlessFragment.addAndCheckPermission(externalStoragePermission, GALLERY_REQUEST);
    }

    @Override
    public void onPermissionGranted(int permissionType) {
        switch (permissionType) {
            case CAMERA_REQUEST:
                TakePictureHelper.getInstance().takeFromCamera(this, "Take Picture");
                break;
            case GALLERY_REQUEST:
                TakePictureHelper.getInstance().takeFromGallery(this, "Select Picture");
                break;
        }
    }

    @Override
    public void onPermissionDenied(int permissionType) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            HashMap<String, Bitmap> imageMap = TakePictureHelper.getInstance().retrievePicturePath(this, requestCode, resultCode, data);
            for (String path : imageMap.keySet()) {
                Bitmap bitmap = imageMap.get(path);
                if (bitmap != null)
                    userIV.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            CommonFunctions.getInstance().showErrorSnackBar(this, e.getMessage());
            e.printStackTrace();
        }
    }
}
