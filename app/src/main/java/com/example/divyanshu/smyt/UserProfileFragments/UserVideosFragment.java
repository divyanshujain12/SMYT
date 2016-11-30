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
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.example.divyanshu.smyt.Adapters.UserVideoAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.InAppDialogs;
import com.example.divyanshu.smyt.DialogActivities.UserVideoDescActivity;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/31/2016.
 */
public class UserVideosFragment extends BaseFragment implements BillingProcessor.IBillingHandler {

    UserVideoAdapter userVideoAdapter;
    @InjectView(R.id.videosRV)
    RecyclerView videosRV;
    ArrayList<VideoModel> userVideoModels = new ArrayList<>();
    private TSnackbar continuousSB = null;
    private String customerID = "";


    private BillingProcessor billingProcessor;
    private boolean readyToPurchase = false;

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

        billingProcessor = new BillingProcessor(getActivity(), Constants.LICENSE_KEY, Constants.MERCHANT_ID, this);

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
        billingProcessor.consumePurchase(Constants.OTHER_CATEGORY_BANNER_SINGLE_VIDEOS_PACK);
        switch (view.getId()) {

            case R.id.commentsTV:
                goVideoDescActivity(position);
                break;
            case R.id.addVideoToBannerTV:
                checkAndPayForBannerVideo(position);
                InAppDialogs.getInstance().showOtherCategoryBannerDialog(getActivity(), billingProcessor);
                break;
            case R.id.addVideoToPremiumTV:
                InAppDialogs.getInstance().showOtherCategoryToPremiumDialog(getActivity(), billingProcessor);
                break;
        }


    }

    private void checkAndPayForBannerVideo(int position) {

    }

    private void goVideoDescActivity(int position) {
        Intent intent = new Intent(getActivity(), UserVideoDescActivity.class);
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, userVideoAdapter.videoModels.get(position).getCustomers_videos_id());
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(getContext(), details.purchaseInfo.responseData, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    @Nullable
    public void onBillingError(int errorCode, Throwable error) {


    }

    @Override
    public void onBillingInitialized() {

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
