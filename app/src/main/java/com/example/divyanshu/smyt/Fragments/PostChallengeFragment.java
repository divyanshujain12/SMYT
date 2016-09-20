package com.example.divyanshu.smyt.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.divyanshu.smyt.GlobalClasses.BaseDialogFragment;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.DateTimePicker;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.Calendar;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu on 9/3/2016.
 */
public class PostChallengeFragment extends BaseDialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    @InjectView(R.id.declineTV)
    TextView declineTV;
    @InjectView(R.id.videoTitleET)
    EditText videoTitleET;
    @InjectView(R.id.titleLL)
    LinearLayout titleLL;
    @InjectView(R.id.genreTypeSP)
    Spinner genreTypeSP;
    @InjectView(R.id.roundsCountSP)
    Spinner roundsCountSP;
    @InjectView(R.id.genreLL)
    LinearLayout genreLL;
    @InjectView(R.id.roundInfoLL)
    LinearLayout roundInfoLL;
    @InjectView(R.id.shareWithSP)
    Spinner shareWithSP;
    @InjectView(R.id.shareWithLL)
    LinearLayout shareWithLL;
    @InjectView(R.id.friendET)
    EditText friendET;
    @InjectView(R.id.searchFriendLL)
    LinearLayout searchFriendLL;
    @InjectView(R.id.postChallengeBT)
    Button postChallengeBT;

    ArrayAdapter<String> arrayAdapter;
    @InjectView(R.id.roundTimeTV)
    TextView roundTimeTV;
    @InjectView(R.id.roundDateTV)
    TextView roundDateTV;
    @InjectView(R.id.roundTimeValueTV)
    TextView roundTimeValueTV;
    @InjectView(R.id.roundDateValueTV)
    TextView roundDateValueTV;
    private int roundCount = 0;
    private String[] genreTypesArray = null, roundsCountArray = null, shareWithArray = null;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "hh:mm aa";

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

        View view = inflater.inflate(R.layout.post_challenge_fragment, null);

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
        addRoundNumberToTV(roundDateTV, roundTimeTV, 1);

        genreTypesArray = getResources().getStringArray(R.array.genre_type);
        roundsCountArray = getResources().getStringArray(R.array.rounds_count);
        shareWithArray = getResources().getStringArray(R.array.share_with);

        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_textview_sixteens_sp, genreTypesArray);
        genreTypeSP.setAdapter(arrayAdapter);
        genreTypeSP.setOnItemSelectedListener(this);

        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_textview_sixteens_sp, roundsCountArray);
        roundsCountSP.setAdapter(arrayAdapter);
        roundsCountSP.setOnItemSelectedListener(this);

        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_textview_sixteens_sp, shareWithArray);
        shareWithSP.setAdapter(arrayAdapter);
        shareWithSP.setOnItemSelectedListener(this);
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

    @OnClick(R.id.postChallengeBT)
    public void onClick() {
        getDialog().dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int parentId = parent.getId();
        switch (parentId) {
            case R.id.genreTypeSP:
                break;
            case R.id.roundsCountSP:
                roundCount = Integer.parseInt(roundsCountArray[position].substring(0, 1).trim());
                addRoundDateTimeLayouts(roundCount);
                break;
            case R.id.shareWithSP:
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void addRoundDateTimeLayouts(int roundCount) {

        roundInfoLL.removeAllViews();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        for (int i = 0; i < roundCount; i++) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View customView = layoutInflater.inflate(R.layout.post_challenge_round_info_bar, null);
            TextView roundTimeTV = (TextView) customView.findViewById(R.id.roundTimeTV);
            TextView roundTimeValueTV = (TextView) customView.findViewById(R.id.roundTimeValueTV);
            TextView roundDateTV = (TextView) customView.findViewById(R.id.roundDateTV);
            TextView roundDateValueTV = (TextView) customView.findViewById(R.id.roundDateValueTV);
            addRoundNumberToTV(roundDateTV, roundTimeTV, i + 1);

            calendar.add(Calendar.DATE, +1);

            roundDateValueTV.setText(DateTimePicker.formatDateAndTime(calendar.getTimeInMillis(), DATE_FORMAT));
            roundTimeValueTV.setText(DateTimePicker.formatDateAndTime(calendar.getTimeInMillis(), TIME_FORMAT));

            roundTimeValueTV.setOnClickListener(PostChallengeFragment.this);
            roundDateValueTV.setOnClickListener(PostChallengeFragment.this);

            roundInfoLL.addView(customView);
        }
    }

    private void addRoundNumberToTV(TextView roundDateTV, TextView roundTimeTV, int number) {
        roundDateTV.setText(String.format(getString(R.string.round_date), String.valueOf(number)));
        roundTimeTV.setText(String.format(getString(R.string.round_timing), String.valueOf(number)));
    }

    @Override
    public void onClick(View v) {

        TextView dateTimeTV = (TextView) v;
        switch (v.getId()) {
            case R.id.roundTimeValueTV:
                DateTimePicker.getInstance().showTimeDialog1(getActivity(), dateTimeTV);
                break;
            case R.id.roundDateValueTV:
                DateTimePicker.getInstance().showDateDialog1(getActivity(), dateTimeTV, DATE_FORMAT, dateTimeTV.getText().toString().trim());
                break;
        }
    }
}