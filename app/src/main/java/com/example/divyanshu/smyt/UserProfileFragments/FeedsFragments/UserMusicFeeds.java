package com.example.divyanshu.smyt.UserProfileFragments.FeedsFragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Adapters.UserMusicPlayerAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomMusicPlayer;
import com.example.divyanshu.smyt.DialogActivities.MusicDescriptionActivity;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.activities.PlayMusicActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.divyanshu.smyt.Constants.ApiCodes.ALL_DATA;
import static com.example.divyanshu.smyt.Constants.ApiCodes.DELETE;
import static com.example.divyanshu.smyt.Constants.Constants.COMMENT_COUNT;
import static com.example.divyanshu.smyt.Constants.Constants.FAVORITE_STATUS;
import static com.example.divyanshu.smyt.Constants.Constants.LIKE_COUNT;

/**
 * Created by divyanshuPC on 2/1/2017.
 */

public class UserMusicFeeds extends BaseFragment {
    private static String customerID = "";
    @InjectView(R.id.videosRV)
    RecyclerView videosRV;
    private UserMusicPlayerAdapter userMusicPlayerAdapter;
    private ArrayList<AllVideoModel> allVideoModels = new ArrayList<>();

    public static UserMusicFeeds getInstance(String customerID) {
        UserMusicFeeds userFavoriteFeeds = new UserMusicFeeds();
        UserMusicFeeds.customerID = customerID;
        return userFavoriteFeeds;
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
        CommonFunctions.stopVideoOnScroll(videosRV);
        hitGetMusicApi();

    }

    private void hitGetMusicApi() {
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
        switch (view.getId()) {
            case R.id.playMusicFL:
                PlayMusicActivity.allVideoModels = allVideoModels;
                Intent intent1 = new Intent(getActivity(), PlayMusicActivity.class);
                intent1.putExtra(Constants.SELECTED_SONG_POS, position);
                startActivity(intent1);
                break;
            default:
                Intent intent = new Intent(getActivity(), MusicDescriptionActivity.class);
                intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, allVideoModels.get(position).getCustomers_videos_id());
                startActivity(intent);
                break;
        }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateUserMusicFragmentUI, new IntentFilter(Constants.USER_MUSIC_TAB_UI));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateUserMusicFragmentUI);
    }

    private BroadcastReceiver updateUserMusicFragmentUI = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra(Constants.TYPE, -1);
            switch (type) {

                case ALL_DATA:
                    hitGetMusicApi();
                    break;
                case DELETE:
                    hitGetMusicApi();
                    break;
                case COMMENT_COUNT:
                    updateCommentCount(intent);
                    break;
                case FAVORITE_STATUS:
                    updateFavStatus(intent);
                    break;
                case LIKE_COUNT:
                    updateLikeCount(intent);
                    break;
            }
        }
    };

    private void updateFavStatus(Intent intent) {
        AllVideoModel videoModel = intent.getParcelableExtra(Constants.DATA);
        int status = intent.getIntExtra(Constants.STATUS, 0);
        int position = allVideoModels.indexOf(videoModel);
        if (position >= 0) {
            allVideoModels.get(position).setFavourite_status(status);
            userMusicPlayerAdapter.notifyDataSetChanged();
        }
    }

    private void updateCommentCount(Intent intent) {
        String customerVideoID = intent.getStringExtra(Constants.CUSTOMERS_VIDEO_ID);
        int commentCount = intent.getIntExtra(Constants.COUNT, 0);
        AllVideoModel allVideoModel = new AllVideoModel();
        allVideoModel.setCustomers_videos_id(customerVideoID);
        int position = allVideoModels.indexOf(allVideoModel);
        if (position > 0) {
            allVideoModels.get(position).setVideo_comment_count(commentCount);
            userMusicPlayerAdapter.notifyDataSetChanged();
        }
    }

    private void updateLikeCount(Intent intent) {
        String customerVideoID = intent.getStringExtra(Constants.CUSTOMERS_VIDEO_ID);
        String likesCount = intent.getStringExtra(Constants.COUNT);
        AllVideoModel allVideoModel = new AllVideoModel();
        allVideoModel.setCustomers_videos_id(customerVideoID);
        int position = allVideoModels.indexOf(allVideoModel);
        if (position > 0) {
            allVideoModels.get(position).setLikes(likesCount);
            userMusicPlayerAdapter.notifyDataSetChanged();
        }
    }
}

