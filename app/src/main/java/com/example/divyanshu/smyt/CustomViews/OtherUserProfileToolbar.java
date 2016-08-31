package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

/**
 * Created by divyanshu.jain on 8/31/2016.
 */
public class OtherUserProfileToolbar extends LinearLayout implements View.OnClickListener {
    Toolbar toolbar;
    TextView toolbar_title;
    ImageView backIV;
    AppCompatActivity context;
    RelativeLayout classNameRL;


    public OtherUserProfileToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void InitToolbar(AppCompatActivity context, String name) {

        //realm.beginTransaction();
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custom_toolbar, this);

        toolbar = (Toolbar) findViewById(R.id.toolbarView);
        backIV = (ImageView) findViewById(R.id.backIV);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        classNameRL = (RelativeLayout) findViewById(R.id.classNameRL);
        toolbar_title.setText(name);
        classNameRL.setOnClickListener(this);
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
        context.onBackPressed();
    }
}

