package com.example.divyanshu.smyt.HomeFragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
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
 * Created by divyanshu.jain on 3/24/2017.
 */

public class MusicFragment extends BaseFragment {
    @InjectView(R.id.musicRV)
    RecyclerView musicRV;
    @InjectView(R.id.customMusicPlayer)
    CustomMusicPlayer customMusicPlayer;
    private UserMusicPlayerAdapter userMusicPlayerAdapter;
    private ArrayList<AllVideoModel> allVideoModels = new ArrayList<>();

    public static MusicFragment getInstance() {
        return new MusicFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
    }

    private void initViews() {
        musicRV.setLayoutManager(new LinearLayoutManager(getContext()));
        userMusicPlayerAdapter = new UserMusicPlayerAdapter(getContext(), allVideoModels, this);
        musicRV.setAdapter(userMusicPlayerAdapter);
        CommonFunctions.stopVideoOnScroll(musicRV);
        hitGetMusicApi();

    }

    private void hitGetMusicApi() {
        CallWebService.getInstance(getContext(), false, ApiCodes.GET_CATEGORY_MP3).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CATEGORY_MP3, createJsonForGetCategoryMusic(), this);
    }

    private JSONObject createJsonForGetCategoryMusic() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.CATEGORY_ID, MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID));
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        allVideoModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), AllVideoModel.class);
        userMusicPlayerAdapter.addNewData(allVideoModels);
        if (allVideoModels != null && allVideoModels.size() > 0) {
            customMusicPlayer.initialize(allVideoModels);
        }
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        PlayMusicActivity.allVideoModels = allVideoModels;
        Intent intent1 = new Intent(getActivity(), PlayMusicActivity.class);
        intent1.putExtra(Constants.SELECTED_SONG_POS, position);
        startActivity(intent1);

     /*   switch (view.getId()) {
            case R.id.playMusicFL:
                PlayMusicActivity.allVideoModels = allVideoModels;
                Intent intent1 = new Intent(getActivity(), PlayMusicActivity.class);
                intent1.putExtra(Constants.SELECTED_SONG_POS, position);
                startActivity(intent1);
               *//* customMusicPlayer.playAudio(position);
                customMusicPlayer.setVisibility(View.VISIBLE);*//*
                break;

            default:
                Intent intent = new Intent(getActivity(), MusicDescriptionActivity.class);
                intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, allVideoModels.get(position).getCustomers_videos_id());
                startActivity(intent);
                break;
        }*/

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*if (!isVisibleToUser && customMusicPlayer != null) {
            customMusicPlayer.stopService();
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customMusicPlayer != null)
            customMusicPlayer.stopService();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateMusicFragmentUI, new IntentFilter(Constants.CATEGORY_MUSIC_TAB_UI));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateMusicFragmentUI);
    }

    private BroadcastReceiver updateMusicFragmentUI = new BroadcastReceiver() {
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
        if (position >= 0) {
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
        if (position >= 0) {
            allVideoModels.get(position).setLikes(likesCount);
            userMusicPlayerAdapter.notifyDataSetChanged();
        }
    }
}
