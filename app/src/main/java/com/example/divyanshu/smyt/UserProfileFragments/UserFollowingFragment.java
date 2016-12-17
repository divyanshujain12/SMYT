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
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.activities.OtherUserProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/31/2016.
 */
public class UserFollowingFragment extends BaseFragment {

    @InjectView(R.id.followersRV)
    RecyclerView followersRV;
    private UserFollowerAdapter userFollowerAdapter;
    private String customerID = "";
    private ArrayList<UserModel> userModels;

    public static UserFollowingFragment getInstance(String customerID) {
        UserFollowingFragment userFollowersFragment = new UserFollowingFragment();
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
        userModels = new ArrayList<>();
        customerID = getArguments().getString(Constants.CUSTOMER_ID);
        userFollowerAdapter = new UserFollowerAdapter(getActivity(), this, userModels);
        followersRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        followersRV.setAdapter(userFollowerAdapter);
        CallWebService.getInstance(getContext(), false, ApiCodes.GET_FOLLOWING).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_FOLLOWING, createJsonForGetFollowing(), this);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        userModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONArray(Constants.DATA), UserModel.class);
        userFollowerAdapter.addItems(userModels);

    }

    private JSONObject createJsonForGetFollowing() {

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
        Intent intent = new Intent(getActivity(), OtherUserProfileActivity.class);
        intent.putExtra(Constants.CUSTOMER_ID, userModels.get(position).getCustomer_id());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
