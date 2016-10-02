package com.example.divyanshu.smyt.UserProfileFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.divyanshu.smyt.Adapters.UserCompletedChallengesAdapter;
import com.example.divyanshu.smyt.Adapters.UserOngoingChallengesAdapter;
import com.example.divyanshu.smyt.Fragments.OngoingChallengeDescriptionFragment;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.RecyclerItemClickListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/31/2016.
 */
public class UserChallengesFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, RecyclerItemClickListener.OnItemClickListener {

    @InjectView(R.id.challengesTB)
    ToggleButton challengesTB;
    @InjectView(R.id.challengesRV)
    RecyclerView challengesRV;
    private UserOngoingChallengesAdapter userOngoingChallengesAdapter;
    private UserCompletedChallengesAdapter userCompletedChallengesAdapter;

    public static UserChallengesFragment getInstance() {
        UserChallengesFragment userChallengesFragment = new UserChallengesFragment();
        return userChallengesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_challenges_fragment, null);
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
        userOngoingChallengesAdapter = new UserOngoingChallengesAdapter(getContext());
        userCompletedChallengesAdapter = new UserCompletedChallengesAdapter(getContext());
        challengesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        challengesRV.setAdapter(userCompletedChallengesAdapter);

        challengesTB.setOnCheckedChangeListener(this);

        challengesRV.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), UserChallengesFragment.this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            challengesRV.setAdapter(userOngoingChallengesAdapter);
        else
            challengesRV.setAdapter(userCompletedChallengesAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}

