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
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseDialogFragment;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.CustomDateTimePicker;
import com.example.divyanshu.smyt.Utils.Validation;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    @InjectView(R.id.friendAC)
    AutoCompleteTextView friendAC;
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
    private String[] genreTypesArray = null, roundsCountArray = null, shareWithArray = null;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "hh:mm aa";
    private Validation validation;
    private String genreTypeStr, roundCountStr, shareWithStr;

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

        validation = new Validation();
        validation.addValidationField(new ValidationModel(videoTitleET, Validation.TYPE_EMPTY_FIELD_VALIDATION, getActivity().getString(R.string.err_post_challenge_title)));

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

        createJsonArrayForChallengeTimeAndDate();
        // getDialog().dismiss();
    }


    private void showErrorMessage(int i) {
        CommonFunctions.getInstance().showErrorSnackBar(declineTV, String.format(getString(R.string.time_difference_error_msg), String.valueOf(i + 1)));
    }

    private Date getDateFromDateTmeString(String dateValue, String timeValue) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.ENGLISH);
        dateValue = dateValue + " " + timeValue;
        return simpleDateFormat.parse(dateValue);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int parentId = parent.getId();
        switch (parentId) {
            case R.id.genreTypeSP:
                genreTypeStr = genreTypesArray[position];
                break;
            case R.id.roundsCountSP:
                roundCountStr = roundsCountArray[position].substring(0, 1).trim();
                addRoundDateTimeLayouts(Integer.parseInt(roundCountStr));
                break;
            case R.id.shareWithSP:
                shareWith(position);
                break;

        }
    }

    private void shareWith(int position) {
        switch (position) {
            case 0:
                searchFriendLL.setVisibility(View.GONE);
                break;

            case 1:
                searchFriendLL.setVisibility(View.VISIBLE);
                break;
        }
        shareWithStr = shareWithArray[position];
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

            roundDateValueTV.setText(CustomDateTimePicker.formatDateAndTime(calendar.getTimeInMillis(), DATE_FORMAT));
            roundTimeValueTV.setText(CustomDateTimePicker.formatDateAndTime(calendar.getTimeInMillis(), TIME_FORMAT));

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
                CustomDateTimePicker.getInstance().showTimeDialog1(getActivity(), dateTimeTV);
                break;
            case R.id.roundDateValueTV:
                CustomDateTimePicker.getInstance().showDateDialog1(getActivity(), dateTimeTV, DATE_FORMAT, dateTimeTV.getText().toString().trim());
                break;
        }
    }

    private void createJsonArrayForChallengeTimeAndDate() {

        JSONArray jsonArray = new JSONArray();
        Date previousDate = Calendar.getInstance(Locale.getDefault()).getTime();
        for (int i = 0; i < roundInfoLL.getChildCount(); i++) {
            JSONObject jsonObject = new JSONObject();

            View view = roundInfoLL.getChildAt(i);
            roundTimeValueTV = (TextView) view.findViewById(R.id.roundTimeValueTV);
            roundDateValueTV = (TextView) view.findViewById(R.id.roundDateValueTV);
            String dateValue = roundDateValueTV.getText().toString().trim();
            String timeValue = roundTimeValueTV.getText().toString().trim();
            Date date;
            try {
                date = getDateFromDateTmeString(dateValue, timeValue);
                long diff = (date.getTime() - previousDate.getTime()) / (60 * 60 * 1000);
                if (diff < 24) {
                    showErrorMessage(i);
                    break;
                }

                previousDate = date;

                jsonObject.put(Constants.ROUND_TIME, timeValue);
                jsonObject.put(Constants.ROUND_DATE, dateValue);

                jsonArray.put(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}