package com.example.divyanshu.smyt.HomeFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.example.divyanshu.smyt.Adapters.UploadedAllVideoAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.UploadedBattleDescActivity;
import com.example.divyanshu.smyt.DialogActivities.UserVideoDescActivity;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.divyanshu.smyt.Constants.ApiCodes.ALL_VIDEO_DATA;
import static com.example.divyanshu.smyt.Utils.Utils.CURRENT_DATE_FORMAT;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class AllVideosFragment extends BaseFragment {
    @InjectView(R.id.videosRV)
    RecyclerView otherVideosRV;
    UploadedAllVideoAdapter otherAllVideoAdapter;

    private ArrayList<AllVideoModel> allVideoModels;

    public static AllVideosFragment getInstance() {
        AllVideosFragment allVideosFragment = new AllVideosFragment();
        return allVideosFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
    }

    private void initViews() {


        otherVideosRV.setLayoutManager(new LinearLayoutManager(getContext()));
        allVideoModels = new ArrayList<>();
        otherAllVideoAdapter = new UploadedAllVideoAdapter(getContext(), allVideoModels, this);
        otherVideosRV.setAdapter(otherAllVideoAdapter);
        CommonFunctions.stopVideoOnScroll(otherVideosRV);

        CallWebService.getInstance(getContext(), true, ALL_VIDEO_DATA).hitJsonObjectRequestAPI(CallWebService.POST, API.ALL_VIDEOS, createJsonForGetVideoData(), this);
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

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        switch (apiType) {
            case ALL_VIDEO_DATA:
                allVideoModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), AllVideoModel.class);
                otherAllVideoAdapter.addData(allVideoModels);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);

        Intent intent = null;

        switch (otherAllVideoAdapter.getItemViewType(position)) {
            case 0:
                intent = new Intent(getActivity(), UserVideoDescActivity.class);
                intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, allVideoModels.get(position).getCustomers_videos_id());
                break;
            case 1:
                intent = new Intent(getActivity(), UploadedBattleDescActivity.class);
                intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, allVideoModels.get(position).getCustomers_videos_id());
                break;
        }
        if (intent != null)
            startActivity(intent);

    }
}
