package com.example.divyanshu.smyt.Utils;

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
public class DateTimePicker implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {


    public static DateTimePicker getInstance(){
        return new DateTimePicker();
    }


    private TextView dateTimeTV;
    private static Calendar mcurrentDate = Calendar.getInstance();
    private String DateFormat = "";



    public void showDateDialog1(Activity context, final TextView textView, final String DateFormat, String selectedDate) {
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

    public void showTimeDialog1(Activity context, final TextView textView) {
        dateTimeTV = textView;
        mcurrentDate = Calendar.getInstance();
       TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                mcurrentDate.get(Calendar.HOUR_OF_DAY),
                mcurrentDate.get(Calendar.MINUTE),
                false
        );
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

        dateTimeTV.setText(formatDateAndTime(myCalendar.getTimeInMillis(), "hh:mm aa"));
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
