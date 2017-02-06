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

import com.androidadvance.topsnackbar.TSnackbar;
import com.example.divyanshu.smyt.Adapters.UserVideoAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.UploadedBattleRoundDescActivity;
import com.example.divyanshu.smyt.DialogActivities.UserVideoDescActivity;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.AllVideoModel;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.InAppLocalApis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.PlayerTwo.JCVideoPlayerTwo;

import static com.example.divyanshu.smyt.Constants.ApiCodes.ALL_VIDEO_DATA;
import static com.example.divyanshu.smyt.Constants.ApiCodes.DELETE_VIDEO;
import static com.example.divyanshu.smyt.Constants.ApiCodes.USER_VIDEOS;
import static com.example.divyanshu.smyt.Constants.Constants.COMMENT_COUNT;
import static com.example.divyanshu.smyt.Constants.Constants.FAVORITE_STATUS;

/**
 * Created by divyanshu.jain on 8/31/2016.
 */
public class UserVideosFragment extends BaseFragment {

    UserVideoAdapter userVideoAdapter;
    @InjectView(R.id.videosRV)
    RecyclerView videosRV;
    ArrayList<AllVideoModel> allVideoModels = new ArrayList<>();
    private TSnackbar continuousSB = null;
    private static String customerID = "";
    private int selectedVideo = 0;

    public static UserVideosFragment getInstance(String customerID) {
        UserVideosFragment userVideosFragment = new UserVideosFragment();
        UserVideosFragment.customerID = customerID;
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
        // customerID = getArguments().getString(Constants.CUSTOMER_ID);
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
        userVideoAdapter = new UserVideoAdapter(getContext(), allVideoModels, this);
        videosRV.setAdapter(userVideoAdapter);
        continuousSB = CommonFunctions.getInstance().createLoadingSnackBarWithView(videosRV);
        CommonFunctions.showContinuousSB(continuousSB);
        getUserVideosAPI();
        CommonFunctions.stopVideoOnScroll(videosRV);
    }

    private void getUserVideosAPI() {
        CallWebService.getInstance(getContext(), false, ApiCodes.USER_VIDEOS).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_VIDEO, createJsonForUserVideos(), this);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        CommonFunctions.hideContinuousSB(continuousSB);
        allVideoModels = UniversalParser.getInstance().parseJsonArrayWithJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS), AllVideoModel.class);
        if (getUserVisibleHint()) {
            setAdapter();
        }
    }

    @Override
    public void onFailure(String str, int apiType) {
        CommonFunctions.hideContinuousSB(continuousSB);
        super.onFailure(str, apiType);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && allVideoModels != null)
            setAdapter();
        else if (userVideoAdapter != null)
            resetAdapter();
    }

    private void setAdapter() {
        if (userVideoAdapter != null)
            userVideoAdapter.addNewData(allVideoModels);
    }

    private void resetAdapter() {
        JCVideoPlayer.releaseAllVideos();
        JCVideoPlayerTwo.releaseAllVideos();
        videosRV.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClickItem(final int position, View view) {
        super.onClickItem(position, view);
        selectedVideo = position;
       /* switch (view.getId()) {
            case R.id.commentsTV:*/
        videoDescriptionActivity(selectedVideo);
              /*  break;
        }*/
    }

    private void videoDescriptionActivity(int position) {
        Intent intent = null;

        switch (userVideoAdapter.getItemViewType(position)) {
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
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateUiUserVideoFragment, new IntentFilter(Constants.USER_FRAGMENT_TAB_UI));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateUiUserVideoFragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        InAppLocalApis.getInstance().sendPurchasedDataToBackend(getContext(), requestCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private BroadcastReceiver updateUiUserVideoFragment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra(Constants.TYPE, -1);
            switch (type) {
                case COMMENT_COUNT:
                    updateCount(intent);
                    break;
                case USER_VIDEOS:
                    getUserVideosAPI();
                    break;
                case DELETE_VIDEO:
                    getUserVideosAPI();
                    break;
                case ALL_VIDEO_DATA:
                    getUserVideosAPI();
                    break;
                case FAVORITE_STATUS:
                    updateFavStatus(intent);
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
            setAdapter();

        }
    }

    private void updateCount(Intent intent) {
        String customerVideoID = intent.getStringExtra(Constants.CUSTOMERS_VIDEO_ID);
        int commentCount = intent.getIntExtra(Constants.COUNT, 0);
        AllVideoModel videoModel = new AllVideoModel();
        videoModel.setCustomers_videos_id(customerVideoID);

        allVideoModels.get(allVideoModels.indexOf(videoModel)).setVideo_comment_count(commentCount);
        setAdapter();
    }

    @Override
    public void onPause() {
        super.onPause();
        videosRV.getAdapter().notifyDataSetChanged();
    }
}
