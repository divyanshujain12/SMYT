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

import com.example.divyanshu.smyt.Adapters.UploadedAllVideoAdapter;
import com.example.divyanshu.smyt.DialogActivities.UploadedBattleDescActivity;
import com.example.divyanshu.smyt.DialogActivities.UserVideoDescActivity;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CommonFunctions;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class AllVideosFragment extends BaseFragment {
    @InjectView(R.id.videosRV)
    RecyclerView otherVideosRV;
    UploadedAllVideoAdapter otherAllVideoAdapter;

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
        otherAllVideoAdapter = new UploadedAllVideoAdapter(getContext(), new ArrayList<VideoModel>(), this);
        otherVideosRV.setAdapter(otherAllVideoAdapter);
        CommonFunctions.stopVideoOnScroll(otherVideosRV);
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
        switch (position) {
            case 0:
                intent = new Intent(getActivity(), UserVideoDescActivity.class);
                break;
            case 1:
                intent = new Intent(getActivity(), UploadedBattleDescActivity.class);
                break;
        }
        if (intent != null)
            startActivity(intent);

    }
}
