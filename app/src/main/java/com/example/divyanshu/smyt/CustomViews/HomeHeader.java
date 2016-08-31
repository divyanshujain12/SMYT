package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.activities.UserProfileActivity;
import com.neopixl.pixlui.components.textview.TextView;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class HomeHeader extends LinearLayout implements View.OnClickListener {
    Toolbar toolbar;
    TextView toolbar_title;
    ImageView backIV;
    AppCompatActivity context;
    RelativeLayout classNameRL;
    ImageView userIV;

    public HomeHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initHeader(AppCompatActivity context, String name, String usersCount) {
        this.context = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.home_header, this);
        toolbar = (Toolbar) findViewById(R.id.toolbarView);
        backIV = (ImageView) findViewById(R.id.backIV);
        userIV = (ImageView) findViewById(R.id.userIV);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        classNameRL = (RelativeLayout) findViewById(R.id.classNameRL);
        toolbar_title.setText(name + "(" + usersCount + ")");
        backIV.setOnClickListener(this);
        userIV.setOnClickListener(this);
        context.setSupportActionBar(toolbar);
        context.getSupportActionBar().setTitle("");
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public TextView getHeaderTextView() {
        return toolbar_title;
    }

    public void setHeaderText(String text) {
        toolbar_title.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backIV:
                context.onBackPressed();
                break;
            case R.id.userIV:
                Intent intent = new Intent(context, UserProfileActivity.class);
                context.startActivity(intent);
                break;
        }

    }
}
