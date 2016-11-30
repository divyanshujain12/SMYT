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

import com.example.divyanshu.smyt.Adapters.OngoingChallengesAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.DialogActivities.LiveBattleDescActivity;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.ChallengeModel;
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

import static com.example.divyanshu.smyt.Utils.Utils.CURRENT_DATE_FORMAT;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class LiveVideosFragment extends BaseFragment {
    @InjectView(R.id.liveVideosRV)
    RecyclerView liveVideosRV;
    private OngoingChallengesAdapter liveVideosAdapter;
    private ArrayList<ChallengeModel> challengeModels = new ArrayList<>();

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
        liveVideosAdapter.addItem(challengeModels);
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        Intent intent = new Intent(getActivity(), LiveBattleDescActivity.class);
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, challengeModels.get(position).getCustomers_videos_id());
        startActivity(intent);
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
}
