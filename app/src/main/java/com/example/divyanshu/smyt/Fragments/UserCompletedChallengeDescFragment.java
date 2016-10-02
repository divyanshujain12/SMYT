package com.example.divyanshu.smyt.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.Adapters.ChallengeRoundDescRvAdapter;
import com.example.divyanshu.smyt.GlobalClasses.BaseDialogFragment;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.textview.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu on 9/3/2016.
 */
public class UserCompletedChallengeDescFragment extends BaseDialogFragment {
    @InjectView(R.id.challengeHeaderTV)
    TextView challengeHeaderTV;
    @InjectView(R.id.profileImage)
    ImageView profileImage;
    @InjectView(R.id.challengerNameTV)
    TextView challengerNameTV;
    @InjectView(R.id.totalRoundsTV)
    TextView totalRoundsTV;
    @InjectView(R.id.totalRoundLL)
    LinearLayout totalRoundLL;
    @InjectView(R.id.challengeTypeTV)
    TextView challengeTypeTV;
    @InjectView(R.id.challengeTypeLL)
    LinearLayout challengeTypeLL;
    @InjectView(R.id.challengesRoundRV)
    RecyclerView challengesRoundRV;


    private ChallengeRoundDescRvAdapter challengeRoundDescRvAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.completed_challenge_desc_fragment, null);

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
        challengeRoundDescRvAdapter = new ChallengeRoundDescRvAdapter(getActivity(), null);
        challengesRoundRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        challengesRoundRV.setAdapter(challengeRoundDescRvAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}


