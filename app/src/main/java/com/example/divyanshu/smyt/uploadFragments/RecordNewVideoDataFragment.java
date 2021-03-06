package com.example.divyanshu.smyt.uploadFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.example.divyanshu.smyt.Adapters.AutoCompleteArrayAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.InAppLocalApis;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Validation;
import com.example.divyanshu.smyt.activities.BottomTabActivity;
import com.example.divyanshu.smyt.activities.InAppActivity;
import com.example.divyanshu.smyt.activities.RecordVideoActivity;
import com.example.divyanshu.smyt.broadcastreceivers.BroadcastSenderClass;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.checkbox.CheckBox;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.divyanshu.smyt.Constants.ApiCodes.POST_USER_VIDEO;
import static com.example.divyanshu.smyt.Constants.ApiCodes.SEARCH_USER;
import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_TO_PREMIUM;

/**
 * Created by divyanshu.jain on 10/7/2016.
 */

public class RecordNewVideoDataFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, TextWatcher, CompoundButton.OnCheckedChangeListener,InAppLocalApis.InAppAvailabilityCalBack {


    ArrayAdapter<String> arrayAdapter;
    @InjectView(R.id.videoTitleET)
    EditText videoTitleET;
    @InjectView(R.id.titleLL)
    LinearLayout titleLL;
    @InjectView(R.id.genreTypeSP)
    Spinner genreTypeSP;
    @InjectView(R.id.genreNameTV)
    TextView genreNameTV;
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
    @InjectView(R.id.scrollView)
    ScrollView scrollView;
    @InjectView(R.id.postVideoBT)
    Button postVideoBT;
    @InjectView(R.id.liveCB)
    CheckBox liveCB;

    private String[] genreTypesArray = null, shareWithArray = null;
    private Validation validation;
    private String genreTypeStr, shareWithStr;
    private String categoryID = "";
    private AutoCompleteArrayAdapter autoCompleteArrayAdapter;
    private ArrayList<UserModel> userModels = new ArrayList<>();
    private HashMap<View, String> hashMap;
    private UserModel userModel;

    private boolean friendSelected = false;
    private boolean isLive = false;



    public static RecordNewVideoDataFragment getInstance() {
       RecordNewVideoDataFragment recordNewVideoDataFragment = new RecordNewVideoDataFragment();
        return recordNewVideoDataFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_upload_new_video, null);
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

        categoryID = MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID);
        validation = new Validation();
        validation.addValidationField(new ValidationModel(videoTitleET, Validation.TYPE_EMPTY_FIELD_VALIDATION, getString(R.string.err_post_challenge_title)));

        genreTypesArray = getResources().getStringArray(R.array.genre_type);
        shareWithArray = getResources().getStringArray(R.array.share_with);

        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_textview_fourteen_sp, genreTypesArray);
        genreTypeSP.setAdapter(arrayAdapter);
        genreTypeSP.setOnItemSelectedListener(this);

        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_textview_fourteen_sp, shareWithArray);
        shareWithSP.setAdapter(arrayAdapter);
        shareWithSP.setOnItemSelectedListener(this);

        friendAC.addTextChangedListener(this);

        autoCompleteArrayAdapter = new AutoCompleteArrayAdapter(getContext(), 0, userModels, this);
        friendAC.setAdapter(autoCompleteArrayAdapter);
        isPremiumGenre();
        setProgressBarVisible(false);

        liveCB.setOnCheckedChangeListener(this);
    }

    private void isPremiumGenre() {
        if (categoryID.equals(getString(R.string.premium_category))) {
            setGenreSpinnerShow(true);
            genreTypesArray = getResources().getStringArray(R.array.genre_type);
            arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_textview_fourteen_sp, genreTypesArray);
            genreTypeSP.setAdapter(arrayAdapter);
            genreTypeSP.setOnItemSelectedListener(this);
        } else {
            setGenreSpinnerShow(false);
            genreTypeStr = MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_NAME);
            genreNameTV.setText(genreTypeStr);
        }
    }

    private void setGenreSpinnerShow(boolean isPremium) {
        genreNameTV.setVisibility(isPremium ? View.GONE : View.VISIBLE);
        genreTypeSP.setVisibility(isPremium ? View.VISIBLE : View.GONE);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() > 0 || !friendSelected) {
            friendSelected = false;
            setProgressBarVisible(true);
            CallWebService.getInstance(getContext(), true, SEARCH_USER).hitJsonObjectRequestAPI(CallWebService.POST, API.USER_SEARCH, createJsonForUserSearch(s.toString()), this);
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

    @OnClick({R.id.postVideoBT})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.postVideoBT:
                postVideo();
                break;
        }

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID).equals(getString(R.string.premium_category))) {
                checkAndPayForAddVideoToPremium();
            }
        }
    }


    private void postVideo() {
        hashMap = validation.validate(friendAC);
        if (hashMap != null) {
            switch (shareWithStr) {
                case "Public":
                    goToRecordVideoActivity();
                    break;
                case "Friend":
                    if (userModel != null)
                        goToRecordVideoActivity();
                    else {
                        CommonFunctions.getInstance().showErrorSnackBar(friendAC, getString(R.string.error_select_friend_first));
                    }
                    break;
            }
        } else
            CommonFunctions.getInstance().showErrorSnackBar(friendAC, getString(R.string.err_post_challenge_title));
    }


    private void checkAndPayForAddVideoToPremium() {
        setUpAvailabilityPurchase(OTHER_CATEGORY_TO_PREMIUM);
        InAppLocalApis.getInstance().checkAddVideoInPremiumCatAvailability(getContext());
    }

    private void setUpAvailabilityPurchase(int purchaseType) {
        InAppLocalApis.getInstance().setCallback(this);
        InAppLocalApis.getInstance().setPurchaseType(purchaseType);
    }

    @Override
    public void available(int purchaseType) {
     /*   Intent intent = new Intent(getContext(), RecordVideoActivity.class);
        startActivity(intent);*/
    }

    @Override
    public void notAvailable(int purchaseType) {
        Intent intent = new Intent(getContext(), InAppActivity.class);
        intent.putExtra(Constants.IN_APP_TYPE, purchaseType);
        startActivityForResult(intent, InAppActivity.PURCHASE_REQUEST);
    }

    @Override
    public void negativeButtonPressed() {
        ((BottomTabActivity) getActivity()).onResetPager();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == InAppActivity.PURCHASE_REQUEST) {

                if (data.getBooleanExtra(Constants.IS_PRCHASED, false)) {
                    String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
                    String productID = data.getStringExtra(Constants.PRODUCT_ID);
                    InAppLocalApis.getInstance().purchaseCategory(getContext(), transactionID, productID);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void goToRecordVideoActivity() {
        String postVideoJson = createJsonForPostVideo().toString();
        Intent intent = new Intent(getContext(), RecordVideoActivity.class);
        intent.putExtra(Constants.POST_VIDEO_DATA, postVideoJson);
        intent.putExtra(Constants.LIVE_STATUS, isLive);
        startActivity(intent);
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
                CommonFunctions.getInstance().showSuccessSnackBar(getActivity(), response.getString(Constants.MESSAGE));
                BroadcastSenderClass.getInstance().reloadAllVideoData(getContext());
                break;
        }
    }

    private JSONObject createJsonForUserSearch(String queryText) {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.CATEGORY_ID, categoryID);
            jsonObject.put(Constants.SEARCH_TEXT, queryText);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForPostVideo() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.CATEGORY_ID, MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID));
            jsonObject.put(Constants.TITLE, hashMap.get(videoTitleET));
            jsonObject.put(Constants.GENRE, genreTypeStr);
            jsonObject.put(Constants.SHARE_STATUS, shareWithStr);
            if (shareWithStr.equals("Friend")) {
                jsonObject.put(Constants.NAME, userModel.getFirst_name());
                jsonObject.put(Constants.PROFILE_IMAGE, userModel.getProfileimage());
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isLive = isChecked;
    }
}