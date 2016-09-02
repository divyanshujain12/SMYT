package com.example.divyanshu.smyt.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.textview.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu.jain on 9/2/2016.
 */
public class ChallangeDescriptionFragment extends DialogFragment {
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
    @InjectView(R.id.acceptBT)
    Button acceptBT;
    @InjectView(R.id.declineBT)
    Button declineBT;

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

        View view = inflater.inflate(R.layout.challange_fragment, null);

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

    @OnClick({R.id.acceptBT, R.id.declineBT})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.acceptBT:
                break;
            case R.id.declineBT:
                break;
        }
    }
}


