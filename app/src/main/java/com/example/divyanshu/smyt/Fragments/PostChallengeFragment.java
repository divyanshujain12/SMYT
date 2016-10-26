package com.example.divyanshu.smyt.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.androidadvance.topsnackbar.TSnackbar;
import com.example.divyanshu.smyt.Adapters.AutoCompleteArrayAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomDateTimePickerHelper;
import com.example.divyanshu.smyt.GlobalClasses.BaseDialogFragment;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.InternetCheck;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.Utils.Validation;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.divyanshu.smyt.Constants.ApiCodes.POST_CHALLENGE;
import static com.example.divyanshu.smyt.Constants.ApiCodes.SEARCH_USER;

/**
 * Created by divyanshu on 9/3/2016.
 */
public class PostChallengeFragment extends BaseDialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, TextWatcher {

    @InjectView(R.id.declineTV)
    TextView declineTV;
    @InjectView(R.id.videoTitleET)
    EditText videoTitleET;
    @InjectView(R.id.titleLL)
    LinearLayout titleLL;
    @InjectView(R.id.genreTypeSP)
    Spinner genreTypeSP;
    @InjectView(R.id.roundsCountSP)
    Spinner roundsCountSP;
    @InjectView(R.id.genreLL)
    LinearLayout genreLL;
    @InjectView(R.id.roundInfoLL)
    LinearLayout roundInfoLL;
    @InjectView(R.id.shareWithSP)
    Spinner shareWithSP;
    @InjectView(R.id.shareWithLL)
    LinearLayout shareWithLL;
    @InjectView(R.id.friendAC)
    AutoCompleteTextView friendAC;
    @InjectView(R.id.searchFriendLL)
    LinearLayout searchFriendLL;
    @InjectView(R.id.postChallengeBT)
    Button postChallengeBT;

    ArrayAdapter<String> arrayAdapter;
    @InjectView(R.id.roundTimeTV)
    TextView roundTimeTV;
    @InjectView(R.id.roundDateTV)
    TextView roundDateTV;
    @InjectView(R.id.roundTimeValueTV)
    TextView roundTimeValueTV;
    @InjectView(R.id.roundDateValueTV)
    TextView roundDateValueTV;
    @InjectView(R.id.scrollView)
    ScrollView scrollView;
    @InjectView(R.id.loadFriendsPB)
    ProgressBar loadFriendsPB;
    @InjectView(R.id.genreNameTV)
    TextView genreNameTV;

    private JSONArray roundArray;
    private String[] genreTypesArray = null, roundsCountArray = null, shareWithArray = null;
    private Validation validation;
    private String genreTypeStr, roundCountStr, shareWithStr;
    private String categoryID = "";
    private AutoCompleteArrayAdapter autoCompleteArrayAdapter;
    private ArrayList<UserModel> userModels = new ArrayList<>();
    private HashMap<View, String> hashMap;
    private UserModel userModel;
    private TSnackbar snackbar;
    private String friendUserName = "";
    private boolean friendSelected = false;

    public static PostChallengeFragment getInstance() {
        PostChallengeFragment postChallengeFragment = new PostChallengeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.FROM_FOLLOWER, false);
        postChallengeFragment.setArguments(bundle);
        return postChallengeFragment;
    }

    public static PostChallengeFragment getInstance(Bundle bundle) {
        PostChallengeFragment postChallengeFragment = new PostChallengeFragment();
        postChallengeFragment.setArguments(bundle);
        return postChallengeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_challenge_fragment, null);
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
        validation.addValidationField(new ValidationModel(videoTitleET, Validation.TYPE_EMPTY_FIELD_VALIDATION, getActivity().getString(R.string.err_post_challenge_title)));

        snackbar = CommonFunctions.getInstance().createLoadingSnackBarWithView(titleLL);

        roundsCountArray = getResources().getStringArray(R.array.rounds_count);
        shareWithArray = getResources().getStringArray(R.array.share_with);


        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_textview_sixteens_sp, roundsCountArray);
        roundsCountSP.setAdapter(arrayAdapter);
        roundsCountSP.setOnItemSelectedListener(this);

        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_textview_sixteens_sp, shareWithArray);
        shareWithSP.setAdapter(arrayAdapter);
        shareWithSP.setOnItemSelectedListener(this);

        friendAC.addTextChangedListener(this);

        autoCompleteArrayAdapter = new AutoCompleteArrayAdapter(getContext(), 0, userModels, this);
        friendAC.setAdapter(autoCompleteArrayAdapter);

        if (getArguments().getBoolean(Constants.FROM_FOLLOWER, false)) {
            setUiForFollowersData();
        }
        isPremiumGenre();
        setProgressBarVisibility(false);
    }

    private void isPremiumGenre() {
        if (categoryID.equals("111")) {
            setGenreSpinnerShow(false);
            genreTypesArray = getResources().getStringArray(R.array.genre_type);
            arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_textview_sixteens_sp, genreTypesArray);
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

    private void setUiForFollowersData() {
        userModel = getArguments().getParcelable(Constants.DATA);
        shareWithSP.setSelection(1);
        shareWithSP.setEnabled(false);
        shareWithSP.setFocusable(false);
        shareWithSP.setClickable(false);

        searchFriendLL.setVisibility(View.VISIBLE);
        friendAC.setText(userModel.getUsername());
        friendAC.setEnabled(false);
        friendAC.setFocusable(false);
        friendAC.setClickable(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.postChallengeBT)
    public void onClick() {

        hashMap = validation.validate(friendAC);
        friendUserName = friendAC.getText().toString();
        if (hashMap != null) {
            if (shareWithStr.equals("Friend") && friendUserName.length() <= 0) {
                CommonFunctions.getInstance().showErrorSnackBar(friendAC, getString(R.string.error_select_friend_first));
                return;
            }
            if (InternetCheck.isInternetOn(getContext()) && createJsonArrayForChallengeTimeAndDate()) {
                CommonFunctions.showContinuousSB(snackbar);
                CallWebService.getInstance(getContext(), false, POST_CHALLENGE).hitJsonObjectRequestAPI(CallWebService.POST, API.POST_CHALLENGE, createJsonForPostChallenge(), this);

            } else
                CommonFunctions.getInstance().showErrorSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int parentId = parent.getId();
        switch (parentId) {
            case R.id.genreTypeSP:
                genreTypeStr = genreTypesArray[position];
                break;
            case R.id.roundsCountSP:
                roundCountStr = roundsCountArray[position].substring(0, 1).trim();
                CustomDateTimePickerHelper.getInstance().addRoundDateTimeLayouts(getContext(), roundInfoLL, Integer.parseInt(roundCountStr), this);
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
    public void onClick(View v) {
        TextView dateTimeTV = (TextView) v;
        switch (v.getId()) {
            case R.id.roundTimeValueTV:
                CustomDateTimePickerHelper.getInstance().showTimeDialog(getActivity(), dateTimeTV);
                break;
            case R.id.roundDateValueTV:
                CustomDateTimePickerHelper.getInstance().showDateDialog(getActivity(), dateTimeTV,  Utils.DATE_FORMAT, dateTimeTV.getText().toString().trim());
                break;
        }
    }

    private boolean createJsonArrayForChallengeTimeAndDate() {

        roundArray = new JSONArray();
        Date previousDate = Calendar.getInstance(Locale.getDefault()).getTime();
        for (int i = 0; i < roundInfoLL.getChildCount(); i++) {
            JSONObject jsonObject = new JSONObject();

            View view = roundInfoLL.getChildAt(i);
            roundTimeValueTV = (TextView) view.findViewById(R.id.roundTimeValueTV);
            roundDateValueTV = (TextView) view.findViewById(R.id.roundDateValueTV);
            String dateValue = roundDateValueTV.getText().toString().trim();
            String timeValue = roundTimeValueTV.getText().toString().trim();
            Date date;
            try {
                date = Utils.getDateFromDateTmeString(dateValue, timeValue);
                long diff = (date.getTime() - previousDate.getTime()) / (60 * 60 * 1000);
                if (i == 0 && diff <= 0) {
                    CustomDateTimePickerHelper.getInstance().showErrorMessage(getContext(), i, getString(R.string.one_hours_err_msg));
                    return false;
                } else if (i > 0 && diff < 24) {
                    CustomDateTimePickerHelper.getInstance().showErrorMessage(getContext(), i, getString(R.string.twenty_four_hours_err_msg));
                    return false;
                }

                previousDate = date;
                jsonObject.put(Constants.ROUND_DATE,  Utils.getDateInTwentyFourHoursFormat(dateValue, timeValue));
                roundArray.put(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return true;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0 && !friendSelected) {
            friendSelected = false;
            setProgressBarVisibility(true);
            CallWebService.getInstance(getContext(), false, SEARCH_USER).hitJsonObjectRequestAPI(CallWebService.POST, API.USER_SEARCH, createJsonForUserSearch(s.toString()), this);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

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

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);

        switch (apiType) {
            case SEARCH_USER:
                setProgressBarVisibility(false);
                userModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONArray(Constants.DATA), UserModel.class);
                autoCompleteArrayAdapter.addAll(userModels);
                break;

            case POST_CHALLENGE:
                CommonFunctions.hideContinuousSB(snackbar);
                getDialog().dismiss();
                break;
        }
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        friendSelected = true;
        userModel = userModels.get(position);
        friendAC.setText(userModel.getUsername());
    }

    private JSONObject createJsonForPostChallenge() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.TITLE, hashMap.get(videoTitleET));
            jsonObject.put(Constants.GENRE, genreTypeStr);
            jsonObject.put(Constants.SHARE_STATUS, shareWithStr);
            if (shareWithStr.equals("Friend")) {
                jsonObject.put(Constants.FRIEND_ID, userModel.getCustomer_id());
            }
            jsonObject.put(Constants.TOTAL_ROUND, roundCountStr);
            jsonObject.put(Constants.CATEGORY_ID, categoryID);
            jsonObject.put(Constants.ROUND_ARRAY, roundArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void setProgressBarVisibility(boolean b) {
        loadFriendsPB.setVisibility(b ? View.VISIBLE : View.GONE);
    }
}