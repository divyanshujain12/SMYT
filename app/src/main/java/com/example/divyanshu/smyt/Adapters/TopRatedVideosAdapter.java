package com.example.divyanshu.smyt.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.divyanshu.smyt.Fragments.PlaySingleVideoFragment;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class TopRatedVideosAdapter extends RecyclerView.Adapter<TopRatedVideosAdapter.MyViewHolder>  {

    private ArrayList<VideoModel> videoList;
    private Context context;
    private ImageLoading imageLoading;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTV, userTimeTV;
        public ImageView userIV;

        public MyViewHolder(View view) {
            super(view);
            userNameTV = (TextView) view.findViewById(R.id.userNameTV);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            userIV = (ImageView) view.findViewById(R.id.userIV);


        }
    }

    public TopRatedVideosAdapter(Context context, ArrayList<VideoModel> videoList) {
        this.videoList = videoList;
        this.context = context;
        imageLoading = new ImageLoading(context, 5);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_video_top_rated_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //VideoModel userModel = videoList.get(position);

      /*  holder.userNameTV.setText(userModel.getName());
        imageLoading.LoadImage(userModel.getThumb(), holder.userIV, null);
        holder.userTimeTV.setText(userModel.getTime());
        holder.userIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                PlaySingleVideoFragment playSingleVideoFragment = new PlaySingleVideoFragment();
                playSingleVideoFragment.show(fragmentManager, playSingleVideoFragment.getClass().getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
