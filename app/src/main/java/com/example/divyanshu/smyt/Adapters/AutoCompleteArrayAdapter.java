package com.example.divyanshu.smyt.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by divyanshu.jain on 10/13/2016.
 */

public class AutoCompleteArrayAdapter extends ArrayAdapter<UserModel> {
    private final Context mContext;
    private final ArrayList<UserModel> userModels;
    private final int mLayoutResourceId;
    private RecyclerViewClick recyclerViewClick;
    private ImageLoading imageLoading;

    public AutoCompleteArrayAdapter(Context context, int resource, ArrayList<UserModel> userModels, RecyclerViewClick recyclerViewClick) {
        super(context, resource, userModels);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.recyclerViewClick = recyclerViewClick;
        this.userModels = new ArrayList<>(userModels);
        imageLoading = new ImageLoading(context, 5);
    }

    public int getCount() {
        return userModels.size();
    }

    public UserModel getItem(int position) {
        return userModels.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(R.layout.auto_complete_array_adapter, parent, false);
            }
            UserModel userModel = getItem(position);
            ImageView userIV = (ImageView) convertView.findViewById(R.id.userIV);
            TextView name = (TextView) convertView.findViewById(R.id.userNameTV);
            TextView wins = (TextView) convertView.findViewById(R.id.winsCountTV);
            assert userModel != null;
            imageLoading.LoadImage(userModel.getProfileimage(), userIV, null);
            name.setText(userModel.getUsername());
            wins.setText("Total Wins: " + userModel.getTotal_wins());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert convertView != null;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(position, v);
            }
        });
        return convertView;
    }

    @Override
    public void addAll(Collection<? extends UserModel> collection) {
        userModels.clear();
        userModels.addAll(collection);
        notifyDataSetChanged();
    }
}