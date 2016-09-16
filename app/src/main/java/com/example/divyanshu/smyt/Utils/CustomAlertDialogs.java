package com.example.divyanshu.smyt.Utils;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;


public class CustomAlertDialogs {

    @SuppressWarnings("deprecation")
    public static void showAlertDialog(Context context, String title, String message, final SnackBarCallback snackBarCallback) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                snackBarCallback.doAction();
            }
        });
        alertDialog.show();
    }


    public static void showAlertDialogWithCallBack(Context context, String title, String message, final SnackBarCallback snackBarCallback) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                snackBarCallback.doAction();
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
