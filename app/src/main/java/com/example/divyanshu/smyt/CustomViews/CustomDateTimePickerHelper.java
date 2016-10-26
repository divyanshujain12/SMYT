package com.example.divyanshu.smyt.CustomViews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by divyanshu.jain on 9/20/2016.
 */
public class CustomDateTimePickerHelper implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    public static CustomDateTimePickerHelper getInstance() {
        return new CustomDateTimePickerHelper();
    }


    private TextView dateTimeTV;
    private static Calendar mcurrentDate = Calendar.getInstance();
    private String DateFormat = "";

 /*
       Custom Date Picker
     */

    public void showDateDialog(Activity context, final TextView textView, final String DateFormat, String selectedDate) {
        this.DateFormat = DateFormat;
        dateTimeTV = textView;
        mcurrentDate = Calendar.getInstance();
        mcurrentDate.setTime(Utils.getCurrentSelectedDate(selectedDate, DateFormat));

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                mcurrentDate.get(Calendar.YEAR),
                mcurrentDate.get(Calendar.MONTH),
                mcurrentDate.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(mcurrentDate);
        dpd.show(context.getFragmentManager(), "DatePickerDialog");
    }

    /*
      Custom Time Picker
    */
    public void showTimeDialog(Activity context, final TextView textView) {
        dateTimeTV = textView;
        mcurrentDate = Calendar.getInstance();
        mcurrentDate.setTime(Utils.getCurrentSelectedDate(textView.getText().toString(), Utils.TIME_FORMAT));
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                mcurrentDate.get(Calendar.HOUR_OF_DAY),
                mcurrentDate.get(Calendar.MINUTE),
                false
        );
        //   tpd.setMinTime(mcurrentDate.get(Calendar.HOUR_OF_DAY), mcurrentDate.get(Calendar.MINUTE), mcurrentDate.get(Calendar.SECOND));
        tpd.show(context.getFragmentManager(), "TimePickerDialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mcurrentDate.set(Calendar.YEAR, year);
        mcurrentDate.set(Calendar.MONTH, monthOfYear);
        mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateTimeTV.setText(Utils.formatDateAndTime(mcurrentDate.getTimeInMillis(), DateFormat));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        myCalendar.set(Calendar.MINUTE, minute);

        dateTimeTV.setText(Utils.formatDateAndTime(myCalendar.getTimeInMillis(), Utils.TIME_FORMAT));
    }


    public void showErrorMessage(Context context, int i, String errorMsg) {
        Toast.makeText(context, String.format(errorMsg, String.valueOf(i + 1)), Toast.LENGTH_SHORT).show();
    }

    public void addRoundDateTimeLayouts(Context context, LinearLayout roundInfoLL, int roundCount, View.OnClickListener onClickListener) {
        roundInfoLL.removeAllViews();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        for (int i = 0; i < roundCount; i++) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View customView = layoutInflater.inflate(R.layout.post_challenge_round_info_bar, null);
            TextView roundTimeTV = (TextView) customView.findViewById(R.id.roundTimeTV);
            TextView roundTimeValueTV = (TextView) customView.findViewById(R.id.roundTimeValueTV);
            TextView roundDateTV = (TextView) customView.findViewById(R.id.roundDateTV);
            TextView roundDateValueTV = (TextView) customView.findViewById(R.id.roundDateValueTV);
            addRoundNumberToTV(context, roundDateTV, roundTimeTV, i + 1);
            if (i == 0)
                calendar.add(Calendar.MINUTE, +70);
            else
                calendar.add(Calendar.DATE, +1);

            roundDateValueTV.setText(Utils.formatDateAndTime(calendar.getTimeInMillis(), Utils.DATE_FORMAT));
            roundTimeValueTV.setText(Utils.formatDateAndTime(calendar.getTimeInMillis(), Utils.TIME_FORMAT));

            roundTimeValueTV.setOnClickListener(onClickListener);
            roundDateValueTV.setOnClickListener(onClickListener);

            roundInfoLL.addView(customView);
        }
    }

    private void addRoundNumberToTV(Context context, TextView roundDateTV, TextView roundTimeTV, int number) {
        roundDateTV.setText(String.format(context.getResources().getString(R.string.round_date), String.valueOf(number)));
        roundTimeTV.setText(String.format(context.getResources().getString(R.string.round_timing), String.valueOf(number)));
    }


}
