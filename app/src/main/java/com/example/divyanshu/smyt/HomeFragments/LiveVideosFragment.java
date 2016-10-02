package com.example.divyanshu.smyt.HomeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Adapters.LiveVideosAdapter;
import com.example.divyanshu.smyt.Fragments.PlayLiveBattleVideoFragment;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class LiveVideosFragment extends BaseFragment {
    @InjectView(R.id.liveVideosRV)
    RecyclerView liveVideosRV;
    private LiveVideosAdapter liveVideosAdapter;

    public static LiveVideosFragment getInstance(){
        LiveVideosFragment liveVideosFragment = new LiveVideosFragment();
        return liveVideosFragment;
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
        liveVideosAdapter = new LiveVideosAdapter(getContext(), new ArrayList<VideoModel>(),this);
        liveVideosRV.setLayoutManager(new LinearLayoutManager(getContext()));
        liveVideosRV.setAdapter(liveVideosAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClickItem(int position, View view) {
        super.onClickItem(position, view);
        showDialogFragment(new PlayLiveBattleVideoFragment());
    }
}
