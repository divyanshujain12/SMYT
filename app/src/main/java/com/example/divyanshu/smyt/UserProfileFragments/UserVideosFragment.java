package com.example.divyanshu.smyt.UserProfileFragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.divyanshu.smyt.activities.UserProfileActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/31/2016.
 */
public class UserVideosFragment extends BaseFragment implements UserProfileActivity.OnBackPressedListener {

    UserVideoAdapter userVideoAdapter;
    @InjectView(R.id.videosRV)
    RecyclerView videosRV;
    ArrayList<VideoModel> userVideoModels = new ArrayList<>();
    @InjectView(R.id.loadingPB)
    ProgressBar loadingPB;


    public static UserVideosFragment getInstance() {
        UserVideosFragment userVideosFragment = new UserVideosFragment();
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
        videosRV.setLayoutManager(new LinearLayoutManager(getContext()));
        userVideoAdapter = new UserVideoAdapter(getContext(), userVideoModels, this);
        videosRV.setAdapter(userVideoAdapter);

        CallWebService.getInstance(getContext(), false, ApiCodes.USER_VIDEOS).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_VIDEO, CommonFunctions.customerIdJsonObject(getContext()), this);

        CommonFunctions.stopVideoOnScroll(videosRV);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        userVideoModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), VideoModel.class);
        userVideoAdapter.addUserVideoData(userVideoModels);
        loadingPB.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String str, int apiType) {
        super.onFailure(str, apiType);
        loadingPB.setVisibility(View.GONE);
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);

        // showDialogFragment(SingleVideoDescFragment.getInstance(userVideoModels.get(position).getCustomers_videos_id()));
        Intent intent = new Intent(getActivity(), UserVideoDescActivity.class);
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, userVideoModels.get(position).getCustomers_videos_id());
        startActivity(intent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateVideoCommentCountReceiver, new IntentFilter(Constants.UPDATE_COMMENT_COUNT));
        ((UserProfileActivity) activity).setOnBackPressedListener(this);
    }

    @Override
    public void doBack() {

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



    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateVideoCommentCountReceiver);
    }
}
