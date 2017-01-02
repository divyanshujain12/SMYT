package com.example.divyanshu.smyt.DialogActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.example.divyanshu.smyt.Adapters.AutoCompleteArrayAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Models.ThumbnailGenerateModel;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.InternetCheck;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.Utils.Validation;
import com.example.divyanshu.smyt.activities.RecordVideoActivity;
import com.example.divyanshu.smyt.broadcastreceivers.BroadcastSenderClass;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;
import com.player.divyanshu.customvideoplayer.MediaPlayerHelper;
import com.player.divyanshu.customvideoplayer.SingleVideoPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.divyanshu.smyt.Constants.ApiCodes.POST_USER_VIDEO;
import static com.example.divyanshu.smyt.Constants.ApiCodes.POST_VIDEO_PREVIOUS;
import static com.example.divyanshu.smyt.Constants.ApiCodes.SEARCH_USER;

/**
 * Created by divyanshu.jain on 10/7/2016.
 */

public class UploadNewVideoActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, TextWatcher {
    @InjectView(R.id.declineTV)
    TextView declineTV;
    @InjectView(R.id.discardAndRecordTV)
    TextView discardAndRecordTV;
    @InjectView(R.id.videoTitleET)
    EditText videoTitleET;
    @InjectView(R.id.titleLL)
    LinearLayout titleLL;
    @InjectView(R.id.genreTypeSP)
    Spinner genreTypeSP;
    @InjectView(R.id.genreLL)
    LinearLayout genreLL;
    @InjectView(R.id.shareWithSP)
    Spinner shareWithSP;
    @InjectView(R.id.shareWithLL)
    LinearLayout shareWithLL;
    @InjectView(R.id.friendAC)
    AutoCompleteTextView friendAC;
    @InjectView(R.id.loadFriendsPB)
    ProgressBar loadFriendsPB;
    @InjectView(R.id.searchFriendLL)
    LinearLayout searchFriendLL;
    @InjectView(R.id.postVideoBT)
    Button postVideoBT;
    ArrayAdapter<String> arrayAdapter;
    @InjectView(R.id.scrollView)
    ScrollView scrollView;
    @InjectView(R.id.firstVideoPlayer)
    SingleVideoPlayer firstVideoPlayer;
    @InjectView(R.id.firstUserNameTV)
    TextView firstUserNameTV;
    @InjectView(R.id.videoFL)
    FrameLayout videoFL;
    @InjectView(R.id.genreNameTV)
    TextView genreNameTV;
    private String[] genreTypesArray = null, shareWithArray = null;
    private Validation validation;
    private String genreTypeStr, shareWithStr;
    private String categoryID = "";
    private AutoCompleteArrayAdapter autoCompleteArrayAdapter;
    private ArrayList<UserModel> userModels = new ArrayList<>();
    private HashMap<View, String> hashMap;
    private UserModel userModel;
    private ThumbnailGenerateModel thumbnailGenerateModel;
    private String videoName = "";
    private ImageLoading imageLoading;
    private boolean friendSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_video);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {

        categoryID = MySharedPereference.getInstance().getString(this, Constants.CATEGORY_ID);
        videoName = getIntent().getStringExtra(Constants.VIDEO_NAME);
        firstUserNameTV.setText(MySharedPereference.getInstance().getString(this, Constants.USER_NAME));
        validation = new Validation();
        validation.addValidationField(new ValidationModel(videoTitleET, Validation.TYPE_EMPTY_FIELD_VALIDATION, getString(R.string.err_post_challenge_title)));

        genreTypesArray = getResources().getStringArray(R.array.genre_type);
        shareWithArray = getResources().getStringArray(R.array.share_with);

        arrayAdapter = new ArrayAdapter<>(this, R.layout.single_textview_sixteens_sp, genreTypesArray);
        genreTypeSP.setAdapter(arrayAdapter);
        genreTypeSP.setOnItemSelectedListener(this);

        arrayAdapter = new ArrayAdapter<>(this, R.layout.single_textview_sixteens_sp, shareWithArray);
        shareWithSP.setAdapter(arrayAdapter);
        shareWithSP.setOnItemSelectedListener(this);

        friendAC.addTextChangedListener(this);

        autoCompleteArrayAdapter = new AutoCompleteArrayAdapter(this, 0, userModels, this);
        friendAC.setAdapter(autoCompleteArrayAdapter);
        isPremiumGenre();
        setProgressBarVisible(false);

        CallWebService.getInstance(this, true, ApiCodes.POST_VIDEO_PREVIOUS).hitJsonObjectRequestAPI(CallWebService.POST, API.THUMBNAIL_GENERATE, createJsonForGetPreviousVideoDetail(), this);
    }

    private void isPremiumGenre() {
        if (categoryID.equals(getString(R.string.premium_category))) {
            setGenreSpinnerShow(false);
            genreTypesArray = getResources().getStringArray(R.array.genre_type);
            arrayAdapter = new ArrayAdapter<>(this, R.layout.single_textview_sixteens_sp, genreTypesArray);
            genreTypeSP.setAdapter(arrayAdapter);
            genreTypeSP.setOnItemSelectedListener(this);
        } else {
            setGenreSpinnerShow(false);
            genreTypeStr = MySharedPereference.getInstance().getString(this, Constants.CATEGORY_NAME);
            genreNameTV.setText(genreTypeStr);
        }
    }

    private void setGenreSpinnerShow(boolean isPremium) {
        genreNameTV.setVisibility(isPremium ? View.GONE : View.VISIBLE);
        genreTypeSP.setVisibility(isPremium ? View.VISIBLE : View.GONE);
    }

    private void setUpVideoPlayer(ThumbnailGenerateModel thumbnailGenerateModel) {
        imageLoading = new ImageLoading(this);
        firstVideoPlayer.setVideoUrl(thumbnailGenerateModel.getVideo_url());
        firstVideoPlayer.setThumbnail(thumbnailGenerateModel.getThumbnail());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() > 0 && !friendSelected) {
            friendSelected = false;
            setProgressBarVisible(true);
            CallWebService.getInstance(this, true, SEARCH_USER).hitJsonObjectRequestAPI(CallWebService.POST, API.USER_SEARCH, createJsonForUserSearch(s.toString()), this);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int parentId = parent.getId();
        switch (parentId) {
            case R.id.genreTypeSP:
                genreTypeStr = genreTypesArray[position];
                break;
            case R.id.shareWithSP:
                shareWith(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void shareWith(int position) {
        switch (position) {
            case 0:
                searchFriendLL.setVisibility(View.GONE);
                break;

            case 1:
                searchFriendLL.setVisibility(View.VISIBLE);
                scrollView.fullScroll(View.FOCUS_DOWN);
                break;
        }
        shareWithStr = shareWithArray[position];
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        friendSelected = true;
        userModel = userModels.get(position);
        friendAC.setText(userModel.getUsername());
    }

    @OnClick({R.id.postVideoBT, R.id.discardAndRecordTV})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.postVideoBT:
                postVideo();
                break;
            case R.id.discardAndRecordTV:
                Intent intent = new Intent(this, RecordVideoActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    private void postVideo() {
        hashMap = validation.validate(friendAC);
        if (hashMap != null) {
            if (shareWithStr.equals("Friend") && userModel != null)
                CommonFunctions.getInstance().showErrorSnackBar(friendAC, getString(R.string.error_select_friend_first));


            if (InternetCheck.isInternetOn(this)) {
                CallWebService.getInstance(this, true, POST_USER_VIDEO).hitJsonObjectRequestAPI(CallWebService.POST, API.POST_VIDEO, createJsonForPostVideo(), this);

            } else
                CommonFunctions.getInstance().showErrorSnackBar(this, getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case SEARCH_USER:
                setProgressBarVisible(false);
                userModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONArray(Constants.DATA), UserModel.class);
                autoCompleteArrayAdapter.addAll(userModels);
                break;

            case POST_USER_VIDEO:
                CommonFunctions.getInstance().showSuccessSnackBar(this, response.getString(Constants.MESSAGE));
                BroadcastSenderClass.getInstance().reloadAllVideoData(this);
                finish();
                break;
            case POST_VIDEO_PREVIOUS:
                thumbnailGenerateModel = UniversalParser.getInstance().parseJsonObject(response.getJSONObject(Constants.DATA), ThumbnailGenerateModel.class);
                setUpVideoPlayer(thumbnailGenerateModel);
                break;
        }
    }

    private JSONObject createJsonForGetPreviousVideoDetail() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.VIDEO_NAME, videoName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForUserSearch(String queryText) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CATEGORY_ID, categoryID);
            jsonObject.put(Constants.SEARCH_TEXT, queryText);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForPostVideo() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(this);
        try {
            jsonObject.put(Constants.CATEGORY_ID, MySharedPereference.getInstance().getString(this, Constants.CATEGORY_ID));
            jsonObject.put(Constants.TITLE, hashMap.get(videoTitleET));
            jsonObject.put(Constants.GENRE, genreTypeStr);
            jsonObject.put(Constants.SHARE_STATUS, shareWithStr);
            jsonObject.put(Constants.VIDEO_URL, thumbnailGenerateModel.getVideo_url());
            jsonObject.put(Constants.THUMBNAIL, thumbnailGenerateModel.getThumbnail());
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
            if (shareWithStr.equals("Friend")) {
                jsonObject.put(Constants.FRIEND_ID, userModel.getCustomer_id());
            } else {
                jsonObject.put(Constants.FRIEND_ID, "0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void setProgressBarVisible(boolean b) {
        loadFriendsPB.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerHelper.getInstance().releaseAllVideos();
    }
}
