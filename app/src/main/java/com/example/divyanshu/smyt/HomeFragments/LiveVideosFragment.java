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
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
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
public class LiveVideosFragment extends BaseFragment implements InAppLocalApis.InAppAvailabilityCalBack {
    @InjectView(R.id.liveVideosRV)
    RecyclerView liveVideosRV;
    private OngoingChallengesAdapter liveVideosAdapter;
    private ArrayList<ChallengeModel> challengeModels = new ArrayList<>();
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
        liveVideosAdapter = new OngoingChallengesAdapter(getContext(), challengeModels, this);
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
        challengeModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), ChallengeModel.class);
        if (getUserVisibleHint()) {
            setAdapter();
        }

    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        selectedVideo = position;
        switch (view.getId()) {
            case R.id.addVideoToBannerTV:
                if (MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID).equals(getString(R.string.premium_category)))
                    checkAndPayForBannerVideo(PREMIUM_CATEGORY_BANNER);
                else
                    checkAndPayForBannerVideo(OTHER_CATEGORY_BANNER);
                break;
            case R.id.addVideoToPremiumTV:
                checkAndPayForAddVideoToPremium(OTHER_CATEGORY_TO_PREMIUM);
                break;

            default:
                Intent intent = new Intent(getActivity(), LiveRoundDescActivity.class);
                intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, challengeModels.get(position).getCustomers_videos_id());
                startActivity(intent);
                break;
        }
    }

    private void checkAndPayForBannerVideo(int purchaseType) {
        setUpAvailabilityPurchase(purchaseType);
        InAppLocalApis.getInstance().checkBannerAvailability(getContext(), purchaseType);
    }

    private void checkAndPayForAddVideoToPremium(int purchaseType) {
        setUpAvailabilityPurchase(purchaseType);
        InAppLocalApis.getInstance().checkAddVideoInPremiumCatAvailability(getContext());
    }

    private void setUpAvailabilityPurchase(int purchaseType) {
        InAppLocalApis.getInstance().setCallback(this);
        InAppLocalApis.getInstance().setPurchaseType(purchaseType);

    }

    @Override
    public void available(int purchaseType) {
        switch (purchaseType) {
            case OTHER_CATEGORY_BANNER:
                InAppLocalApis.getInstance().addBannerToCategory(getContext(), challengeModels.get(selectedVideo).getCustomers_videos_id());
                break;
            case OTHER_CATEGORY_TO_PREMIUM:
                InAppLocalApis.getInstance().addVideoToPremiumCategory(getContext(), challengeModels.get(selectedVideo).getCustomers_videos_id());
                break;
            case PREMIUM_CATEGORY_BANNER:
                InAppLocalApis.getInstance().addBannerToCategory(getContext(), challengeModels.get(selectedVideo).getCustomers_videos_id());
                break;
        }
    }

    @Override
    public void notAvailable(int purchaseType) {
        Intent intent = new Intent(getContext(), InAppActivity.class);
        intent.putExtra(Constants.IN_APP_TYPE, purchaseType);
        startActivityForResult(intent, InAppActivity.PURCHASE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == InAppActivity.PURCHASE_REQUEST) {

                if (data.getBooleanExtra(Constants.IS_PRCHASED, false)) {

                    int type = data.getIntExtra(Constants.TYPE, 0);
                    String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
                    String productID = data.getStringExtra(Constants.PRODUCT_ID);
                    switch (type) {
                        case OTHER_CATEGORY_BANNER:
                            InAppLocalApis.getInstance().purchaseBanner(getContext(), transactionID, productID);
                            break;
                        case OTHER_CATEGORY_TO_PREMIUM:
                            InAppLocalApis.getInstance().purchaseCategory(getContext(), transactionID, productID);
                            break;
                        case PREMIUM_CATEGORY_BANNER:
                            InAppLocalApis.getInstance().purchaseBanner(getContext(), transactionID, productID);
                            break;
                    }
                }
            }
        }
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
        if (isVisibleToUser && challengeModels != null) {
            setAdapter();
        }

    }

    private void setAdapter() {
        if (liveVideosAdapter != null)
            liveVideosAdapter.addItem(challengeModels);
    }

    private void updateCommentCount(Intent intent) {
        String challengeID = intent.getStringExtra(Constants.CHALLENGE_ID);
        int commentCount = intent.getIntExtra(Constants.COUNT, 0);
        ChallengeModel challengeModel = new ChallengeModel();
        challengeModel.setChallenge_id(challengeID);
        challengeModels.get(challengeModels.indexOf(challengeModel)).setVideo_comment_count(commentCount);
        liveVideosAdapter.notifyDataSetChanged();
    }

    private void updateVoteCount(Intent intent) {
        String challengeID = intent.getStringExtra(Constants.CHALLENGE_ID);
        String voteCount = intent.getStringExtra(Constants.VOTE_COUNT);
        ChallengeModel challengeModel = new ChallengeModel();
        challengeModel.setChallenge_id(challengeID);
        switch (intent.getIntExtra(Constants.USER_NUMBER, -1)) {
            case 0:
                challengeModels.get(challengeModels.indexOf(challengeModel)).setVote(voteCount);
                break;
            case 1:
                challengeModels.get(challengeModels.indexOf(challengeModel)).setVote1(voteCount);
                break;
        }
        liveVideosAdapter.notifyDataSetChanged();
    }
}
