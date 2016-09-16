package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.VideoModel;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 9/1/2016.
 */
public class UserVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<VideoModel> categoryModels;
    private Context context;
    private RecyclerViewClick recyclerViewClick;

    public class SingleVideoHolder extends RecyclerView.ViewHolder {
        public TextView titleTV, userTimeTV, commentsTV, uploadedTimeTV, firstUserNameTV;
        public ImageView videoThumbIV, moreIV;
        public FrameLayout videoFL;

        public SingleVideoHolder(View view) {
            super(view);
            titleTV = (TextView) view.findViewById(R.id.titleTV);
            userTimeTV = (TextView) view.findViewById(R.id.userTimeTV);
            firstUserNameTV = (TextView) view.findViewById(R.id.firstUserNameTV);
            commentsTV = (TextView) view.findViewById(R.id.commentsTV);
            uploadedTimeTV = (TextView) view.findViewById(R.id.uploadedTimeTV);
            videoThumbIV = (ImageView) view.findViewById(R.id.videoThumbIV);
            moreIV = (ImageView) view.findViewById(R.id.moreIV);
            videoFL = (FrameLayout) view.findViewById(R.id.videoFL);
        }
    }


    public UserVideoAdapter(Context context, ArrayList<VideoModel> categoryModels,RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.categoryModels = categoryModels;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_video_item, parent, false);
        return new SingleVideoHolder(itemView);


    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //  VideoModel userModel = categoryModels.get(position);

       /* holder.categoryNameTV.setText(userModel.getcategory_name());
        holder.categoryIV.setImageResource(userModel.getIcon());
        holder.categoryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingletonClass.getInstance().setSelectedCategoryPos(position);
                Intent intent = new Intent(context, CategoryDescriptionActivity.class);
                context.startActivity(intent);
            }
        });*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(position,v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 8;
    }

}


