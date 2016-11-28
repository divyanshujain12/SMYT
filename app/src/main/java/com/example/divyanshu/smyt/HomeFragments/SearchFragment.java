package com.example.divyanshu.smyt.HomeFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.androidadvance.topsnackbar.TSnackbar;
import com.example.divyanshu.smyt.Adapters.SearchUserRvAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.InternetCheck;
import com.example.divyanshu.smyt.activities.OtherUserProfileActivity;
import com.neopixl.pixlui.components.edittext.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class SearchFragment extends BaseFragment implements TextWatcher {
    @InjectView(R.id.searchET)
    EditText searchET;
    @InjectView(R.id.usersRV)
    RecyclerView usersRV;
    String categoryID = "";
    @InjectView(R.id.searchBarFL)
    FrameLayout searchBarFL;
    private SearchUserRvAdapter searchUserRvAdapter;
    private ArrayList<UserModel> userModels;


    public static SearchFragment getInstance(String categoryID) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CATEGORY_ID, categoryID);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, null);

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
        iniViews();
    }

    private void iniViews() {

        userModels = new ArrayList<>();
        searchET.addTextChangedListener(this);
        categoryID = getArguments().getString(Constants.CATEGORY_ID);
        searchUserRvAdapter = new SearchUserRvAdapter(getContext(), this, userModels);
        usersRV.setLayoutManager(new LinearLayoutManager(getContext()));
        usersRV.setAdapter(searchUserRvAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        Intent intent = new Intent(getActivity(), OtherUserProfileActivity.class);
        intent.putExtra(Constants.CUSTOMER_ID, userModels.get(position).getCustomer_id());
        startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count > 1) {
            if (InternetCheck.isInternetOn(getContext()))
                CallWebService.getInstance(getContext(), true, ApiCodes.SEARCH_USER).hitJsonObjectRequestAPI(CallWebService.POST, API.USER_SEARCH, createJsonForUserSearch(s.toString()), this);
            else
                CommonFunctions.getInstance().showErrorSnackBar(getActivity(), getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case ApiCodes.SEARCH_USER:
                userModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONArray(Constants.DATA), UserModel.class);
                searchUserRvAdapter.setItem(userModels);
                break;
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
}
