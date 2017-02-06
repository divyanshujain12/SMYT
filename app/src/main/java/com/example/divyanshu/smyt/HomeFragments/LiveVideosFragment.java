package com.example.divyanshu.smyt.HomeFragments;

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

import com.example.divyanshu.smyt.Adapters.OngoingChallengesAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.LiveRoundDescActivity;
import com.example.divyanshu.smyt.DialogActivities.UploadedBattleRoundDescActivity;
import com.example.divyanshu.smyt.DialogActivities.UserVideoDescActivity;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.Models.ChallengeModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.InAppLocalApis;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.activities.InAppActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerTwo;

import static com.example.divyanshu.smyt.Constants.ApiCodes.ALL_VIDEO_DATA;
import static com.example.divyanshu.smyt.Constants.Constants.COMMENT_COUNT;
import static com.example.divyanshu.smyt.Constants.Constants.VOTE_COUNT_INT;
import static com.example.divyanshu.smyt.Utils.Utils.CURRENT_DATE_FORMAT;
import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_BANNER;
import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_TO_PREMIUM;
import static com.example.divyanshu.smyt.activities.InAppActivity.PREMIUM_CATEGORY_BANNER;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class LiveVideosFragment extends BaseFragment {
    @InjectView(R.id.liveVideosRV)
    RecyclerView liveVideosRV;
    private OngoingChallengesAdapter liveVideosAdapter;
    private ArrayList<AllVideoModel> allVideoModels = new ArrayList<>();
    private int selectedVideo;

    public static LiveVideosFragment getInstance() {
        return new LiveVideosFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_videos_fragments, null);

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
        liveVideosAdapter = new OngoingChallengesAdapter(getContext(), allVideoModels, this);
        liveVideosRV.setLayoutManager(new LinearLayoutManager(getContext()));
        liveVideosRV.setAdapter(liveVideosAdapter);
        CommonFunctions.stopVideoOnScroll(liveVideosRV);
        hitOnGoingChallengeAPI();
    }

    private void hitOnGoingChallengeAPI() {
        CallWebService.getInstance(getContext(), false, ApiCodes.HOME_CHALLENGES_VIDEOS).hitJsonObjectRequestAPI(CallWebService.POST, API.HOME_LIVE_VIDEOS, createJsonForGetVideoData(), this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        allVideoModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), AllVideoModel.class);
        if (getUserVisibleHint()) {
            setAdapter();
        }

    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        selectedVideo = position;
        videoDescriptionActivity(position);
    }

    private void videoDescriptionActivity(int position) {
        Intent intent = null;

        switch (liveVideosAdapter.getItemViewType(position)) {
            case 0:
                intent = new Intent(getActivity(), UserVideoDescActivity.class);
                intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, allVideoModels.get(position).getCustomers_videos_id());
                break;
            case 1:
                intent = new Intent(getActivity(), UploadedBattleRoundDescActivity.class);
                intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, allVideoModels.get(position).getCustomers_videos_id());
                break;

        }
        if (intent != null)
            startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        InAppLocalApis.getInstance().sendPurchasedDataToBackend(getContext(), requestCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private JSONObject createJsonForGetVideoData() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.CATEGORY_ID, MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID));
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTime(CURRENT_DATE_FORMAT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private BroadcastReceiver updateLiveVideosTabUI = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra(Constants.TYPE, -1);
            switch (type) {
                case COMMENT_COUNT:
                    updateCommentCount(intent);
                    break;
                case VOTE_COUNT_INT:
                    updateVoteCount(intent);
                    break;
                case ALL_VIDEO_DATA:
                    hitOnGoingChallengeAPI();
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateLiveVideosTabUI, new IntentFilter(Constants.LIVE_CHALLENGES_TAB_UI));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateLiveVideosTabUI);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && allVideoModels != null) {
            setAdapter();
        } else if (liveVideosAdapter != null)
            resetAdapter();
    }

    private void setAdapter() {
        if (liveVideosAdapter != null)
            liveVideosAdapter.addItem(allVideoModels);
    }

    private void updateCommentCount(Intent intent) {
        String challengeID = intent.getStringExtra(Constants.CHALLENGE_ID);
        int commentCount = intent.getIntExtra(Constants.COUNT, 0);
        ChallengeModel challengeModel = new ChallengeModel();
        challengeModel.setChallenge_id(challengeID);
        allVideoModels.get(allVideoModels.indexOf(challengeModel)).setVideo_comment_count(commentCount);
        liveVideosAdapter.notifyDataSetChanged();
    }

    private void updateVoteCount(Intent intent) {
        String challengeID = intent.getStringExtra(Constants.CHALLENGE_ID);
        String voteCount = intent.getStringExtra(Constants.VOTE_COUNT);
        ChallengeModel challengeModel = new ChallengeModel();
        challengeModel.setChallenge_id(challengeID);
        switch (intent.getIntExtra(Constants.USER_NUMBER, -1)) {
            case 0:
                allVideoModels.get(allVideoModels.indexOf(challengeModel)).setVote(voteCount);
                break;
            case 1:
                allVideoModels.get(allVideoModels.indexOf(challengeModel)).setVote1(voteCount);
                break;
        }
        liveVideosAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        resetAdapter();
    }

    private void resetAdapter() {
        JCVideoPlayer.releaseAllVideos();
        JCVideoPlayerTwo.releaseAllVideos();
        liveVideosRV.getAdapter().notifyDataSetChanged();
    }
}

