package com.example.divyanshu.smyt.CustomViews;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Interfaces.AlertDialogInterface;
import com.example.divyanshu.smyt.Interfaces.ChangePasswordInterface;
import com.example.divyanshu.smyt.Interfaces.ImagePickDialogInterface;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.Validation;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class CustomAlertDialogs {
    static AlertDialog alertDialog;

    @SuppressWarnings("deprecation")
    public static void showAlertDialog(Context context, String title, String message, final SnackBarCallback snackBarCallback) {
        alertDialog = new AlertDialog.Builder(context).create();
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


    public static void showAlertDialogWithCallBack(final Context context, String title, String message, final SnackBarCallback snackBarCallback) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
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
                //((Activity) context).onBackPressed();
                dialog.dismiss();

            }
        });

        alertDialog.show();
    }

    public static void showAlertDialogWithCallBacks(final Context context, String title, String message, final AlertDialogInterface alertDialogInterface) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogInterface.Yes();
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogInterface.No();
                dialog.dismiss();

            }
        });

        alertDialog.show();
    }

    public static void showCategoryDescDialog(final Context context, String title, String message, final SnackBarCallback snackBarCallback) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        TextView textView = new TextView(context);
        textView.setText(Html.fromHtml(message));
        textView.setPadding(20, 10, 10, 10);
        alertDialog.setView(textView);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                snackBarCallback.doAction();
                dialog.dismiss();
            }
        });


        alertDialog.show();
    }

    public static void showRuleDialog(Context context, String title, final SnackBarCallback snackBarCallback) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        View view = LayoutInflater.from(context).inflate(R.layout.activity_info, null);
        final TextView rulesTV = (TextView) view.findViewById(R.id.rulesTV);
        final ProgressBar loadingPB = (ProgressBar) view.findViewById(R.id.loadingPB);
        final WebView webView = (WebView) view.findViewById(R.id.webView);
        CallWebService.getInstance(context, false, ApiCodes.GET_RULES).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_RULES, null, new CallWebService.ObjectResponseCallBack() {
            @Override
            public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
                rulesTV.setVisibility(View.VISIBLE);
                loadingPB.setVisibility(View.GONE);
                String data = response.getString(Constants.DATA);
                final String mimeType = "text/html";
                final String encoding = "UTF-8";
                webView.loadDataWithBaseURL("", data, mimeType, encoding, "");

               //rulesTV.setText(Html.fromHtml(data));
            }

            @Override
            public void onFailure(String str, int apiType) {
                rulesTV.setText(str);
            }
        });

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                snackBarCallback.doAction();
                dialog.dismiss();
            }
        });


        alertDialog.show();
    }

    public static void showImageSelectDialog(Context context, final ImagePickDialogInterface imagePickDialogInterface) {
        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.image_pick_dialog, null);
        setupFullWidthDialog();
        TextView selectCameraTV = (TextView) layout.findViewById(R.id.selectCameraTV);
        TextView selectGalleryTV = (TextView) layout.findViewById(R.id.selectGalleryTV);
        TextView cancelTV = (TextView) layout.findViewById(R.id.cancelTV);
        selectCameraTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialogInterface.Camera();
                dismissDialog();
                ;
            }
        });

        selectGalleryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialogInterface.Gallery();
                dismissDialog();
            }
        });
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();

            }
        });
        alertDialog.setView(layout);
        alertDialog.show();
    }

    public static void showChangePasswordDialog(final Context context, final ChangePasswordInterface changePasswordInterface) {
        final Validation validation = new Validation();
        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.change_password_layout, null);
        setupFullWidthDialog();
        final EditText oldPasswordET = (EditText) layout.findViewById(R.id.oldPasswordET);
        final EditText passwordET = (EditText) layout.findViewById(R.id.passwordET);
        final EditText reEnterPasswordET = (EditText) layout.findViewById(R.id.reEnterPasswordET);
        TextView updateTV = (TextView) layout.findViewById(R.id.updateTV);
        validation.addValidationField(new ValidationModel(oldPasswordET, Validation.TYPE_PASSWORD_VALIDATION, context.getString(R.string.err_pass)));
        validation.addValidationField(new ValidationModel(passwordET, Validation.TYPE_PASSWORD_VALIDATION, context.getString(R.string.err_pass)));
        validation.addValidationField(new ValidationModel(reEnterPasswordET, Validation.TYPE_PASSWORD_VALIDATION, context.getString(R.string.err_pass)));
        updateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<View, String> hashMap = validation.validate((Activity) context);
                if (hashMap != null) {
                    String savedPassword = MySharedPereference.getInstance().getString(context, Constants.PASSWORD);

                    if (!savedPassword.equals(hashMap.get(oldPasswordET))) {
                        showErrorToast(context, "Old Password Mismatch!");
                        return;
                    } else if (!hashMap.get(passwordET).equals(hashMap.get(reEnterPasswordET))) {
                        showErrorToast(context, "New Password Mismatch!");
                        return;
                    } else {
                        changePasswordInterface.onChangeSuccess(hashMap.get(passwordET));
                        dismissDialog();
                    }
                }


            }
        });
        alertDialog.setView(layout);
        alertDialog.show();
    }

    public static void showNewNotificationAlert(Context context, final SnackBarCallback snackBarCallback) {
        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.show_notification_alert, null);
        alertDialog.setView(layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBarCallback.doAction();
            }
        });
        alertDialog.show();
    }

    private static void showErrorToast(Context context, String errorMsg) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private static void dismissDialog() {
        if (alertDialog != null)
            alertDialog.dismiss();
    }


    private static void setupFullWidthDialog() {
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }
}
