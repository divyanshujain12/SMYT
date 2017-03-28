package com.example.divyanshu.smyt.UserProfileFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Adapters.UserFollowerAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.UserFollowerModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.activities.OtherUserProfileActivity;
import com.example.divyanshu.smyt.activities.UserProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshuPC on 3/17/2017.
 */

public class UserFollowersFragment extends BaseFragment{
    @InjectView(R.id.followersRV)
    RecyclerView followersRV;
    private UserFollowerAdapter userFollowerAdapter;
    private String customerID = "";
    private ArrayList<UserFollowerModel> userFollowerModels;
    private boolean isApiHit = false;

    public static UserFollowersFragment getInstance(String customerID) {
        UserFollowersFragment userFollowersFragment = new UserFollowersFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CUSTOMER_ID, customerID);
        userFollowersFragment.setArguments(bundle);
        return userFollowersFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_follower_fragment, null);
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
        userFollowerModels = new ArrayList<>();
        customerID = getArguments().getString(Constants.CUSTOMER_ID);
        userFollowerAdapter = new UserFollowerAdapter(getActivity(), this, userFollowerModels);
        followersRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        followersRV.setAdapter(userFollowerAdapter);
        hitFollowingAPI();
    }

    private void hitFollowingAPI() {
        CallWebService.getInstance(getContext(), false, ApiCodes.GET_FOLLOWERS).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_FOLLOWERS, createJsonForGetFollowers(), this);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        userFollowerModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONArray(Constants.DATA), UserFollowerModel.class);
        if (getUserVisibleHint()) {
            setAdapter();
        }

    }

    private void setAdapter() {
        if (userFollowerAdapter != null)
            userFollowerAdapter.addItems(userFollowerModels);
    }

    private JSONObject createJsonForGetFollowers() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CUSTOMER_ID, customerID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        String customerID = userFollowerModels.get(position).getCustomer_id();
        Intent intent = new Intent(getActivity(), OtherUserProfileActivity.class);
        if (customerID.equals(MySharedPereference.getInstance().getString(getContext(), Constants.CUSTOMER_ID)))
            intent = new Intent(getActivity(), UserProfileActivity.class);
        intent.putExtra(Constants.CUSTOMER_ID, userFollowerModels.get(position).getCustomer_id());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && userFollowerModels != null) {
            setAdapter();
        }
    }

}
