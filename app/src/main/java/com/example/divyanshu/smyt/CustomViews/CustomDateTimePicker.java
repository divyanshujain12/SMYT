package com.example.divyanshu.smyt.CustomViews;

import android.app.Activity;

import com.neopixl.pixlui.components.textview.TextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by divyanshu.jain on 9/20/2016.
 */
public class CustomDateTimePicker implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "hh:mm aa";
    public static final String DEFAULT_DATE = "1900-01-01";

    public static CustomDateTimePicker getInstance() {
        return new CustomDateTimePicker();
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
        mcurrentDate.setTime(getCurrentSelectedDate(selectedDate, DateFormat));

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
        //mcurrentDate.setTime(getCurrentSelectedDate(textView.getText().toString(), TIME_FORMAT));
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
        dateTimeTV.setText(formatDateAndTime(mcurrentDate.getTimeInMillis(), DateFormat));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        myCalendar.set(Calendar.MINUTE, minute);

        dateTimeTV.setText(formatDateAndTime(myCalendar.getTimeInMillis(), TIME_FORMAT));
    }

    public static String formatDateAndTime(long date, String format) {
        Date dt = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(dt);
    }

    private static Date getCurrentSelectedDate(String selectedDate, String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        try {
            return format.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
