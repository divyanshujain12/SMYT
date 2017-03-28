package com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Adapters.UserMusicPlayerAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.MusicDescriptionActivity;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshuPC on 2/1/2017.
 */

public class UserMusicFeeds extends BaseFragment {
    private static String customerID = "";
    @InjectView(R.id.videosRV)
    android.support.v7.widget.RecyclerView videosRV;
    private UserMusicPlayerAdapter userMusicPlayerAdapter;
    private ArrayList<AllVideoModel> allVideoModels = new ArrayList<>();

    public static UserMusicFeeds getInstance(String customerID) {
        UserMusicFeeds userFavoriteFeeds = new UserMusicFeeds();
        UserMusicFeeds.customerID = customerID;
        return userFavoriteFeeds;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_videos_fragment, null);
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
        videosRV.setLayoutManager(new LinearLayoutManager(getContext()));
        userMusicPlayerAdapter = new UserMusicPlayerAdapter(getContext(), allVideoModels, this);
        videosRV.setAdapter(userMusicPlayerAdapter);
        CallWebService.getInstance(getContext(), false, ApiCodes.GET_USER_MUSIC).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_MP3, createJsonForGetCustomerMusic(), this);

    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        allVideoModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), AllVideoModel.class);
        userMusicPlayerAdapter.addNewData(allVideoModels);
    }


    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        Intent intent = new Intent(getActivity(), MusicDescriptionActivity.class);
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, allVideoModels.get(position).getCustomers_videos_id());
        startActivity(intent);
    }

    private JSONObject createJsonForGetCustomerMusic() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CUSTOMER_ID, customerID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
