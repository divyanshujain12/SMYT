package com.example.divyanshu.smyt.DialogActivities;

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
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseActivity;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.InternetCheck;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Validation;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static com.example.divyanshu.smyt.Constants.ApiCodes.POST_CHALLENGE;
import static com.example.divyanshu.smyt.Constants.ApiCodes.POST_USER_VIDEO;
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
    JCVideoPlayerStandard firstVideoPlayer;
    @InjectView(R.id.firstUserNameTV)
    TextView firstUserNameTV;
    @InjectView(R.id.videoFL)
    FrameLayout videoFL;
    private String[] genreTypesArray = null, shareWithArray = null;
    private Validation validation;
    private String genreTypeStr, shareWithStr;
    private String categoryID = "";
    private AutoCompleteArrayAdapter autoCompleteArrayAdapter;
    private ArrayList<UserModel> userModels = new ArrayList<>();
    private HashMap<View, String> hashMap;
    private UserModel userModel;
    private String videoUrl = "http://www.whatsupguys.in/demo/smyt/videos/video2.mp4";
    private String videoThumbnail = "http://www.whatsupguys.in/demo/smyt/thumbnail/img2.png";
    private ImageLoading imageLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_video);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {

        categoryID = MySharedPereference.getInstance().getString(this, Constants.CATEGORY_ID);

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

        setUpVideoPlayer();
    }

    private void setUpVideoPlayer() {
        imageLoading = new ImageLoading(this);
        boolean firstVideoSetup = firstVideoPlayer.setUp(videoUrl, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
        if (firstVideoSetup)
            imageLoading.LoadImage(videoThumbnail, firstVideoPlayer.thumbImageView, null);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count > 1) {
            if (InternetCheck.isInternetOn(this)) {
                CallWebService.getInstance(this, true, SEARCH_USER).hitJsonObjectRequestAPI(CallWebService.POST, API.USER_SEARCH, createJsonForUserSearch(s.toString()), this);
            } else
                CommonFunctions.getInstance().showErrorSnackBar(this, getString(R.string.no_internet_connection));
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
        userModel = userModels.get(position);
        friendAC.setText(userModel.getUsername());
    }

    @OnClick(R.id.postVideoBT)
    public void onClick() {
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
                userModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONArray(Constants.DATA), UserModel.class);
                autoCompleteArrayAdapter.addAll(userModels);
                if (userModels.size() > 2)
                    CommonFunctions.getInstance().hideKeyBoard(this, this.getCurrentFocus());
                break;

            case POST_USER_VIDEO:
                CommonFunctions.getInstance().showSuccessSnackBar(this, response.getString(Constants.MESSAGE));
                finish();
                break;
        }
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
            jsonObject.put(Constants.TITLE, hashMap.get(videoTitleET));
            jsonObject.put(Constants.GENRE, genreTypeStr);
            jsonObject.put(Constants.SHARE_STATUS, shareWithStr);
            jsonObject.put(Constants.VIDEO_URL, videoUrl);
            jsonObject.put(Constants.THUMBNAIL, videoThumbnail);
            if (shareWithStr.equals("Friend")) {
                jsonObject.put(Constants.FRIEND_ID, userModel.getCustomer_id());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
