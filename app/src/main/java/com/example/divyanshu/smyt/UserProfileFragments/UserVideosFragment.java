package com.example.divyanshu.smyt.UserProfileFragments;

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

import com.androidadvance.topsnackbar.TSnackbar;
import com.example.divyanshu.smyt.Adapters.UserVideoAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.UserVideoDescActivity;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.InAppLocalApis;
import com.example.divyanshu.smyt.activities.InAppActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_BANNER;
import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_TO_PREMIUM;
import static com.example.divyanshu.smyt.activities.InAppActivity.PREMIUM_CATEGORY_BANNER;

/**
 * Created by divyanshu.jain on 8/31/2016.
 */
public class UserVideosFragment extends BaseFragment implements InAppLocalApis.InAppAvailabilityCalBack {

    UserVideoAdapter userVideoAdapter;
    @InjectView(R.id.videosRV)
    RecyclerView videosRV;
    ArrayList<VideoModel> userVideoModels = new ArrayList<>();
    private TSnackbar continuousSB = null;
    private String customerID = "";
    private int selectedVideo = 0;


    public static UserVideosFragment getInstance(String customerID) {
        UserVideosFragment userVideosFragment = new UserVideosFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CUSTOMER_ID, customerID);
        userVideosFragment.setArguments(bundle);
        return userVideosFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerForContextMenu(videosRV);
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

        customerID = getArguments().getString(Constants.CUSTOMER_ID);
        videosRV.setLayoutManager(new LinearLayoutManager(getContext()));
        userVideoAdapter = new UserVideoAdapter(getContext(), userVideoModels, this);
        videosRV.setAdapter(userVideoAdapter);
        continuousSB = CommonFunctions.getInstance().createLoadingSnackBarWithView(videosRV);
        CommonFunctions.showContinuousSB(continuousSB);
        CallWebService.getInstance(getContext(), false, ApiCodes.USER_VIDEOS).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_VIDEO, createJsonForUserVideos(), this);
        CommonFunctions.stopVideoOnScroll(videosRV);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        CommonFunctions.hideContinuousSB(continuousSB);
        userVideoModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), VideoModel.class);
        userVideoAdapter.addUserVideoData(userVideoModels);
    }

    @Override
    public void onFailure(String str, int apiType) {
        CommonFunctions.hideContinuousSB(continuousSB);
        super.onFailure(str, apiType);

    }


    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        selectedVideo = position;
        switch (view.getId()) {
            case R.id.commentsTV:
                goVideoDescActivity(selectedVideo);
                break;
            case R.id.addVideoToBannerTV:
                checkAndPayForBannerVideo(OTHER_CATEGORY_BANNER);
                break;
            case R.id.addVideoToPremiumTV:
                checkAndPayForAddVideoToPremium(OTHER_CATEGORY_TO_PREMIUM);
                break;
        }


    }

    private void checkAndPayForBannerVideo(int purchaseType) {
        setUpAvailabilityPurchase(purchaseType);
        InAppLocalApis.getInstance().checkBannerAvailability(getContext(), Constants.CAT_NORMAL);
    }

    private void checkAndPayForAddVideoToPremium(int purchaseType) {
        setUpAvailabilityPurchase(purchaseType);
        InAppLocalApis.getInstance().checkAddVideoInPremiumCatAvailability(getContext());
    }

    private void goVideoDescActivity(int position) {
        Intent intent = new Intent(getActivity(), UserVideoDescActivity.class);
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, userVideoAdapter.videoModels.get(position).getCustomers_videos_id());
        startActivity(intent);
    }

    private JSONObject createJsonForUserVideos() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CUSTOMER_ID, customerID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateVideoCommentCountReceiver, new IntentFilter(Constants.UPDATE_COMMENT_COUNT));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateVideoCommentCountReceiver);
    }


    private void setUpAvailabilityPurchase(int purchaseType) {
        InAppLocalApis.getInstance().setCallback(this);
        InAppLocalApis.getInstance().setPurchaseType(purchaseType);

    }

    @Override
    public void available(int purchaseType) {
        switch (purchaseType) {
            case OTHER_CATEGORY_BANNER:
                InAppLocalApis.getInstance().addBannerToCategory(getContext(), userVideoModels.get(selectedVideo).getCustomers_videos_id());
                break;
            case OTHER_CATEGORY_TO_PREMIUM:
                InAppLocalApis.getInstance().addVideoToPremiumCategory(getContext(), userVideoModels.get(selectedVideo).getCustomers_videos_id());
                break;
            case PREMIUM_CATEGORY_BANNER:
                InAppLocalApis.getInstance().addBannerToCategory(getContext(), userVideoModels.get(selectedVideo).getCustomers_videos_id());
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

            } else {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private BroadcastReceiver updateVideoCommentCountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String customerVideoID = intent.getStringExtra(Constants.CUSTOMERS_VIDEO_ID);
            int commentCount = intent.getIntExtra(Constants.COUNT, 0);
            VideoModel videoModel = new VideoModel();
            videoModel.setCustomers_videos_id(customerVideoID);
            userVideoModels.get(userVideoModels.indexOf(videoModel)).setVideo_comment_count(commentCount);
            userVideoAdapter.addUserVideoData(userVideoModels);
        }
    };

}
