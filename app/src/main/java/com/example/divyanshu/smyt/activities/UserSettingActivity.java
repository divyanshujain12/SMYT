package com.example.divyanshu.smyt.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.CustomViews.CustomToasts;
import com.example.divyanshu.smyt.Fragments.RuntimePermissionHeadlessFragment;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Interfaces.ChangePasswordInterface;
import com.example.divyanshu.smyt.Interfaces.ImagePickDialogInterface;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.PictureHelper;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.Utils.Validation;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.divyanshu.smyt.Constants.ApiCodes.GET_USER_INFO;
import static com.example.divyanshu.smyt.Constants.ApiCodes.UPDATE_USER;

/**
 * Created by divyanshu.jain on 9/29/2016.
 */

public class UserSettingActivity extends BaseActivity implements ImagePickDialogInterface, RuntimePermissionHeadlessFragment.PermissionCallback, View.OnTouchListener {


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
    @InjectView(R.id.updateTV)
    TextView updateTV;
    @InjectView(R.id.changePasswordET)
    EditText changePasswordET;
    private UserModel userModel;
    private Validation validation;
    private HashMap<View, String> hashMap;
    private Bitmap bitmap;
    private ImageLoading imageLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        ButterKnife.inject(this);


        InitViews();
    }

    private void InitViews() {

        createPermission();
        Utils.configureToolbarWithBackButton(this, toolbarView, getString(R.string.setting));
        runtimePermissionHeadlessFragment = CommonFunctions.getInstance().addRuntimePermissionFragment(this, this);
        addValidation();
        imageLoading = new ImageLoading(this);
        CallWebService.getInstance(this, true, GET_USER_INFO).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_DETAIL, CommonFunctions.customerIdJsonObject(this), this);
    }

    private void addValidation() {
        validation = new Validation();
        validation.addValidationField(new ValidationModel(firstNameET, Validation.TYPE_NAME_VALIDATION, getString(R.string.err_first_name)));
        validation.addValidationField(new ValidationModel(lastNameET, Validation.TYPE_NAME_VALIDATION, getString(R.string.err_last_name)));
        validation.addValidationField(new ValidationModel(mobileET, Validation.TYPE_NAME_VALIDATION, getString(R.string.err_phone_number)));
        validation.addValidationField(new ValidationModel(statusET, Validation.TYPE_NAME_VALIDATION, getString(R.string.err_status)));
        validation.addValidationField(new ValidationModel(changePasswordET, Validation.TYPE_PASSWORD_VALIDATION, getString(R.string.err_pass)));
    }

    private void createPermission() {
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        externalStoragePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    /*private void addRuntimePermissionFragment() {
        runtimePermissionHeadlessFragment = RuntimePermissionHeadlessFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction().add(runtimePermissionHeadlessFragment, runtimePermissionHeadlessFragment.getClass().getName()).commit();
    }*/


    @OnClick({R.id.aboutUsTV, R.id.contactUsTV, R.id.changeUserImageIV, R.id.updateTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aboutUsTV:

                break;
            case R.id.contactUsTV:
                break;
            case R.id.changeUserImageIV:
                CustomAlertDialogs.showImageSelectDialog(this, this);
                break;
            case R.id.updateTV:
                hashMap = validation.validate(this);
                if (hashMap != null) {
                    CallWebService.getInstance(this, true, UPDATE_USER).hitJsonObjectRequestAPI(CallWebService.POST, API.UPDATE_USER, createJsonForUpdateUserInfo(), this);
                }
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
                PictureHelper.getInstance().takeFromCamera(this, getString(R.string.take_picture));
                break;
            case GALLERY_REQUEST:
                PictureHelper.getInstance().takeFromGallery(this, getString(R.string.select_picture));
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
            HashMap<String, Bitmap> imageMap = PictureHelper.getInstance().retrievePicturePath(this, requestCode, resultCode, data);
            setImageBitmap(imageMap);

        } catch (Exception e) {
            CommonFunctions.getInstance().showErrorSnackBar(this, e.getMessage());
            e.printStackTrace();
        }
    }

    private void setImageBitmap(HashMap<String, Bitmap> imageMap) {
        if (imageMap != null)
            for (String path : imageMap.keySet()) {
                bitmap = imageMap.get(path);
                if (bitmap != null) {
                    userIV.setImageBitmap(bitmap);
                    //  bitmap.recycle();
                }
            }
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case GET_USER_INFO:
                userModel = UniversalParser.getInstance().parseJsonObject(response.getJSONArray(Constants.DATA).getJSONObject(0), UserModel.class);
                updateUI();
                break;
            case UPDATE_USER:
                sendLocalBroadCastForUserProfile();
                MySharedPereference.getInstance().setString(this, Constants.PASSWORD, hashMap.get(changePasswordET));
                CustomToasts.getInstance(this).showSuccessToast(response.getString(Constants.MESSAGE));
                finish();
                break;
        }
    }

    private void updateUI() {
        imageLoading.LoadImage(userModel.getProfileimage(), userIV, null);
        firstNameET.setText(userModel.getFirst_name());
        lastNameET.setText(userModel.getLast_name());
        mobileET.setText(userModel.getPhonenumber());
        statusET.setText(userModel.getTimeline_msg());
        changePasswordET.setText(MySharedPereference.getInstance().getString(this, Constants.PASSWORD));
        changePasswordET.setOnTouchListener(this);
    }

    public void sendLocalBroadCastForUserProfile() {
        Intent intent = new Intent();
        intent.setAction(Constants.UPDATE_USER_INFO);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private JSONObject createJsonForUpdateUserInfo() {
        JSONObject jsonObject = new JSONObject();

        try {
            if (bitmap != null) {
                String encodedImage = PictureHelper.getInstance().convertBitmapToBase64(bitmap);
                jsonObject.put(Constants.PROFILE_IMAGE, encodedImage);
            }
            jsonObject.put(Constants.FIRST_NAME, hashMap.get(firstNameET));
            jsonObject.put(Constants.LAST_NAME, hashMap.get(lastNameET));
            jsonObject.put(Constants.EMAIl, userModel.getEmail());
            jsonObject.put(Constants.PHONE_NUMBER, hashMap.get(mobileET));
            jsonObject.put(Constants.DATE_OF_BIRTH, userModel.getDate_of_birth());
            jsonObject.put(Constants.PASSWORD, hashMap.get(changePasswordET));
            jsonObject.put(Constants.TIMELINE_MSG, hashMap.get(statusET));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            CustomAlertDialogs.showChangePasswordDialog(this, new ChangePasswordInterface() {
                @Override
                public void onChangeSuccess(String changedPassword) {
                    changePasswordET.setText(changedPassword);
                }
            });
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonFunctions.getInstance().hideKeyBoard(this, userIV);
    }
}
