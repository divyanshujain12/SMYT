package com.example.divyanshu.smyt.HomeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Adapters.OtherAllVideoAdapter;
import com.example.divyanshu.smyt.Adapters.TopRatedVideosAdapter;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class AllVideosFragment extends BaseFragment {
    @InjectView(R.id.topRatedVideosRV)
    RecyclerView topRatedVideosRV;
    @InjectView(R.id.otherVideosRV)
    RecyclerView otherVideosRV;

    TopRatedVideosAdapter topRatedVideosAdapter;
    OtherAllVideoAdapter otherAllVideoAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_video_fragment, null);
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
        topRatedVideosRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        otherVideosRV.setLayoutManager(new LinearLayoutManager(getContext()));
        topRatedVideosAdapter = new TopRatedVideosAdapter(getContext(), new ArrayList<VideoModel>());
        otherAllVideoAdapter = new OtherAllVideoAdapter(getContext(), new ArrayList<VideoModel>());

        topRatedVideosRV.setAdapter(topRatedVideosAdapter);
        otherVideosRV.setAdapter(otherAllVideoAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
