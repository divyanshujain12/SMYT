package com.example.divyanshu.smyt.Utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by divyanshu on 9/3/2016.
 */
public class CommonFunctions {


    public static void showShortLengthSnackbar(String message, View view) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
