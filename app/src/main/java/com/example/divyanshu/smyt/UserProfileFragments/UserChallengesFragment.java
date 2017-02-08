package com.example.divyanshu.smyt.UserProfileFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.divyanshu.smyt.Adapters.ViewPagerAdapter;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.NonSwipeableViewPager;
import com.example.divyanshu.smyt.Utils.RecyclerItemClickListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu.jain on 8/31/2016.
 */
public class UserChallengesFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, RecyclerItemClickListener.OnItemClickListener {

    @InjectView(R.id.challengesTB)
    SwitchCompat challengesTB;
    @InjectView(R.id.viewPager)
    NonSwipeableViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    public static UserChallengesFragment getInstance(boolean newChallenges) {
        UserChallengesFragment userChallengesFragment = new UserChallengesFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.NEW_CHALLENGE, newChallenges);
        userChallengesFragment.setArguments(args);
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
        challengesTB.setOnCheckedChangeListener(this);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(UserCompletedChallengeFragment.newInstance(), "");
        viewPagerAdapter.addFragment(UserOngoingChallengeFragment.newInstance(getArguments().getBoolean(Constants.NEW_CHALLENGE)), "");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked)
            viewPager.setCurrentItem(0);
        else
            viewPager.setCurrentItem(1);

    }

    @Override
    public void onItemClick(View view, int position) {

    }
}

