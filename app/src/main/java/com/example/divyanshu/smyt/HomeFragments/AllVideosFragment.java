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
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Adapters.UploadedAllVideoAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.UploadedBattleRoundDescActivity;
import com.example.divyanshu.smyt.DialogActivities.UserVideoDescActivity;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Interfaces.DeleteVideoInterface;
import com.example.divyanshu.smyt.Models.AllVideoModel;
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
import static com.example.divyanshu.smyt.Constants.ApiCodes.BANNER_VIDEOS;
import static com.example.divyanshu.smyt.Constants.ApiCodes.DELETE_VIDEO;
import static com.example.divyanshu.smyt.Constants.Constants.COMMENT_COUNT;
import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_BANNER;
import static com.example.divyanshu.smyt.activities.InAppActivity.OTHER_CATEGORY_TO_PREMIUM;
import static com.example.divyanshu.smyt.activities.InAppActivity.PREMIUM_CATEGORY_BANNER;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class AllVideosFragment extends BaseFragment implements InAppLocalApis.InAppAvailabilityCalBack, CompoundButton.OnCheckedChangeListener {
    @InjectView(R.id.videosRV)
    RecyclerView otherVideosRV;
    UploadedAllVideoAdapter otherAllVideoAdapter;
    @InjectView(R.id.noVideoAvailableLL)
    LinearLayout noVideoAvailableLL;
    @InjectView(R.id.videoTypeTB)
    SwitchCompat videoTypeTB;

    private ArrayList<AllVideoModel> allVideoModels;
    private ArrayList<AllVideoModel> bannerVideoModels;
    private int selectedVideo;
    private String filterType = "0";

    public static AllVideosFragment getInstance() {
        return new AllVideosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.videos_recycler_view, null);
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
        videoTypeTB.setOnCheckedChangeListener(this);
        otherVideosRV.setLayoutManager(new LinearLayoutManager(getContext()));
        allVideoModels = new ArrayList<>();
        otherAllVideoAdapter = new UploadedAllVideoAdapter(getContext(), allVideoModels, this);
        otherVideosRV.setAdapter(otherAllVideoAdapter);
        CommonFunctions.stopVideoOnScroll(otherVideosRV);

        hitAllVideosAPI();
        hitBannerAPI();
    }

    private void hitBannerAPI() {
        CallWebService.getInstance(getContext(), false, BANNER_VIDEOS).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CATEGORY_BANNER, createJsonForGetBannerVideosData(), this);
    }

    private void hitAllVideosAPI() {
        CallWebService.getInstance(getContext(), true, ALL_VIDEO_DATA).hitJsonObjectRequestAPI(CallWebService.POST, API.ALL_VIDEOS, createJsonForGetVideoData(), this);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);

        noVideoAvailableLL.setVisibility(View.GONE);
        switch (apiType) {
            case ALL_VIDEO_DATA:
                allVideoModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), AllVideoModel.class);
                otherAllVideoAdapter.updateData(allVideoModels);
                break;
            case BANNER_VIDEOS:
                bannerVideoModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.BANNERS), AllVideoModel.class);
                otherAllVideoAdapter.addDataToBannerArray(bannerVideoModels);
                break;
        }
    }

    @Override
    public void onFailure(String str, int apiType) {
        super.onFailure(str, apiType);
        switch (apiType) {
            case ALL_VIDEO_DATA:
                noVideoAvailableLL.setVisibility(View.VISIBLE);
                break;

        }
    }

    private JSONObject createJsonForGetVideoData() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.CATEGORY_ID, MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID));
            jsonObject.put(Constants.E_DATE, Utils.getCurrentTimeInMillisecond());
            jsonObject.put(Constants.FILTER, filterType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject createJsonForGetBannerVideosData() {
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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
            case R.id.deleteVideoTV:
                CommonFunctions.getInstance().deleteVideo(getContext(), allVideoModels.get(position).getCustomers_videos_id(), new DeleteVideoInterface() {
                    @Override
                    public void onDeleteVideo() {
                        otherAllVideoAdapter.removeItem(selectedVideo);
                    }
                });
                break;
            default:
                videoDescriptionActivity(position);
                break;
        }
    }

    private void videoDescriptionActivity(int position) {
        Intent intent = null;

        switch (otherAllVideoAdapter.getItemViewType(position)) {
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
                InAppLocalApis.getInstance().addBannerToCategory(getContext(), allVideoModels.get(selectedVideo).getCustomers_videos_id());
                break;
            case OTHER_CATEGORY_TO_PREMIUM:
                InAppLocalApis.getInstance().addVideoToPremiumCategory(getContext(), allVideoModels.get(selectedVideo).getCustomers_videos_id());
                break;
            case PREMIUM_CATEGORY_BANNER:
                InAppLocalApis.getInstance().addBannerToCategory(getContext(), allVideoModels.get(selectedVideo).getCustomers_videos_id());
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

    private BroadcastReceiver updateAllVideosUI = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra(Constants.TYPE, -1);
            switch (type) {
                case BANNER_VIDEOS:
                    hitBannerAPI();
                    break;
                case ALL_VIDEO_DATA:
                    hitAllVideosAPI();
                    break;
                case DELETE_VIDEO:
                    hitAllVideosAPI();
                    break;
                case COMMENT_COUNT:
                    updateCount(intent);
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateAllVideosUI, new IntentFilter(Constants.ALL_VIDEO_TAB_UI));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateAllVideosUI);
    }

    private void updateCount(Intent intent) {
        String customerVideoID = intent.getStringExtra(Constants.CUSTOMERS_VIDEO_ID);
        int commentCount = intent.getIntExtra(Constants.COUNT, 0);
        AllVideoModel allVideoModel = new AllVideoModel();
        allVideoModel.setCustomers_videos_id(customerVideoID);
        allVideoModels.get(allVideoModels.indexOf(allVideoModel)).setVideo_comment_count(commentCount);
        otherAllVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) {
            filterType = "0";
        } else {
            filterType = "1";
        }
        hitAllVideosAPI();
    }
}
