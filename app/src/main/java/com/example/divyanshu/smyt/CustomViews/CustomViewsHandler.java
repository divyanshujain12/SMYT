package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

/**
 * Created by divyanshu.jain on 10/3/2016.
 */
public class CustomViewsHandler {
    public static PopupWindow createPopupWindow(Context context, View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_text_view_in_cardview, null);
        TextView textView = (TextView) view.findViewById(R.id.singleTV);
        textView.setText(context.getString(R.string.delete_video));
        textView.setOnClickListener(onClickListener);
        textView.setBackgroundColor(context.getResources().getColor(R.color.white));
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        return popupWindow;
    }
}
