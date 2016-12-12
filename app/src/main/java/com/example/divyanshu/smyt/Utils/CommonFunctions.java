package com.example.divyanshu.smyt.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomAlertDialogs;
import com.example.divyanshu.smyt.Fragments.RuntimePermissionHeadlessFragment;
import com.example.divyanshu.smyt.Interfaces.AlertDialogInterface;
import com.example.divyanshu.smyt.Interfaces.CallBackInterface;
import com.example.divyanshu.smyt.Interfaces.DeleteVideoInterface;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.R;
import com.player.divyanshu.customvideoplayer.MediaPlayerHelper;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.divyanshu.smyt.Constants.ApiCodes.DELETE_VIDEO;
import static com.example.divyanshu.smyt.Constants.Constants.COMMENT_COUNT;

/**
 * Created by divyanshu on 9/3/2016.
 */
public class CommonFunctions {
    private CommonFunctions() {
    }

    private TSnackbar customErrorSnackbar = null;
    private TSnackbar customSuccessSnackbar = null;

    public static CommonFunctions getInstance() {

        CommonFunctions commonFunctions = new CommonFunctions();
        return commonFunctions;
    }

    public static void showShortLengthSnackbar(String message, View view) {

        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public void showErrorSnackBar(Activity activity, String errorText) {
        getErrorSnackbar(activity).setText(errorText).setDuration(TSnackbar.LENGTH_SHORT).show();
    }

    public void showSuccessSnackBar(Activity activity, String msg) {
        getSuccessSnackbar(activity).setText(msg).setDuration(TSnackbar.LENGTH_SHORT).show();
    }

    public void showErrorSnackBar(View view, String errorText) {
        getErrorSnackbar(view).setText(errorText).setDuration(TSnackbar.LENGTH_SHORT).show();
    }

    public TSnackbar getErrorSnackbar(View view) {
        if (customErrorSnackbar == null)
            customErrorSnackbar = createErrorCustomSnackBar(view);
        return customErrorSnackbar;
    }

    public TSnackbar getErrorSnackbar(Activity activity) {
        if (customErrorSnackbar == null)
            customErrorSnackbar = createErrorCustomSnackBar(activity);
        return customErrorSnackbar;
    }

    public TSnackbar getSuccessSnackbar(Activity activity) {
        if (customSuccessSnackbar == null)
            customSuccessSnackbar = createSuccessCustomSnackBar(activity);
        return customSuccessSnackbar;
    }

    private TSnackbar createSuccessCustomSnackBar(Activity context) {
        TSnackbar snackbar = TSnackbar
                .make(context.findViewById(android.R.id.content), "", TSnackbar.LENGTH_LONG);
        setUpCustomSnackBar(snackbar, Color.parseColor("#00FF00"));

        return snackbar;
    }

    public TSnackbar createLoadingSnackBarWithActivity(Activity context) {
        TSnackbar snackbar = TSnackbar
                .make(context.findViewById(android.R.id.content), "", TSnackbar.LENGTH_LONG);
        setUpCustomSnackBar(snackbar, context.getResources().getColor(R.color.colorPrimary));

        return snackbar;
    }


    public TSnackbar createLoadingSnackBarWithView(View view) {
        TSnackbar snackbar = TSnackbar
                .make(view, "", TSnackbar.LENGTH_LONG);
        setUpCustomSnackBar(snackbar, Color.parseColor("#ECC11C"));

        return snackbar;
    }

    private TSnackbar createErrorCustomSnackBar(Activity context) {
        TSnackbar snackbar = TSnackbar
                .make(context.findViewById(android.R.id.content), "", TSnackbar.LENGTH_LONG);
        setUpCustomSnackBar(snackbar, Color.parseColor("#FF0000"));

        return snackbar;
    }

    private TSnackbar createErrorCustomSnackBar(View view) {
        TSnackbar snackbar = TSnackbar
                .make(view, "", TSnackbar.LENGTH_LONG);
        setUpCustomSnackBar(snackbar, Color.parseColor("#FF0000"));

        return snackbar;
    }

    private void setUpCustomSnackBar(TSnackbar snackbar, int color) {
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(color);
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
    }

    public static JSONObject customerIdJsonObject(Context context) {
        String customerID = MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CUSTOMER_ID, customerID == "" ? "2" : customerID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static void stopVideoOnScroll(RecyclerView recyclerView) {
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                MediaPlayerHelper.getInstance().releaseAllVideos();
            }
        });
    }

    public static void changeImageWithAnimation(final Context context, final ImageView imageView, final int secondImageResourceID) {
        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fab_in);
        imageView.startAnimation(fadeIn);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //   Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fa);
                imageView.setImageResource(secondImageResourceID);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public static void showContinuousSB(TSnackbar continuousSB) {
        continuousSB.setText(R.string.loading_data).setDuration(TSnackbar.LENGTH_INDEFINITE).show();
    }

    public static void hideContinuousSB(TSnackbar continuousSB) {
        continuousSB.dismiss();
    }

    public void hideKeyBoard(Activity activity, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public RuntimePermissionHeadlessFragment addRuntimePermissionFragment(AppCompatActivity activity, RuntimePermissionHeadlessFragment.PermissionCallback permissionCallback) {
        RuntimePermissionHeadlessFragment runtimePermissionHeadlessFragment = RuntimePermissionHeadlessFragment.newInstance(permissionCallback);
        activity.getSupportFragmentManager().beginTransaction().add(runtimePermissionHeadlessFragment, runtimePermissionHeadlessFragment.getClass().getName()).commit();
        return runtimePermissionHeadlessFragment;
    }

    public void deleteVideo(final Context context, final String customerVideoId, final DeleteVideoInterface deleteVideoInterface) {
        CustomAlertDialogs.showAlertDialogWithCallBack(context, context.getString(R.string.alert), context.getString(R.string.delete_video_alert_msg), new SnackBarCallback() {
            @Override
            public void doAction() {
                CallWebService.getInstance(context, false, ApiCodes.DELETE_VIDEO).hitJsonObjectRequestAPI(CallWebService.POST, API.DELETE_CUSTOMER_VIDEO, createJsonForDeleteVideo(context, customerVideoId), null);
                BroadcastSenderClass.getInstance().sendDeleteCommentBroadcast(context);
                deleteVideoInterface.onDeleteVideo();
            }
        });
    }


    private JSONObject createJsonForDeleteVideo(Context context, String customerVideoID) {

        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(context);
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, customerVideoID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


}
