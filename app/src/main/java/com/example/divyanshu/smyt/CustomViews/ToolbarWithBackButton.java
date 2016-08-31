package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

/**
 * Created by divyanshu.jain on 5/27/2016.
 */
public class ToolbarWithBackButton extends LinearLayout implements OnClickListener {
    Toolbar toolbar;
    TextView toolbar_title;
    ImageView backIV;
    AppCompatActivity context;
    RelativeLayout classNameRL;


    public ToolbarWithBackButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        //InitToolbar(context);
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
        // toolbar_title.setText(name);
        classNameRL.setOnClickListener(this);
        context.setSupportActionBar(toolbar);
        ActionBar actionBar = context.getSupportActionBar();
        actionBar.setTitle(name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
            }
        });*/

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
