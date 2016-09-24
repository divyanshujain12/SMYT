package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.CommentModel;
import com.example.divyanshu.smyt.Models.VideoDetailModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu on 9/24/2016.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private ArrayList<CommentModel> commentModels;
    private Context context;
    private ImageLoading imageLoading;
    private RecyclerViewClick recyclerViewClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTV, commentTV;
        public ImageView userIV, deleteVideoIV;

        public MyViewHolder(View view) {
            super(view);
            userNameTV = (TextView) view.findViewById(R.id.userNameTV);
            commentTV = (TextView) view.findViewById(R.id.commentTV);
            userIV = (ImageView) view.findViewById(R.id.userIV);
            deleteVideoIV = (ImageView) view.findViewById(R.id.deleteVideoIV);
        }
    }

    public CommentsAdapter(Context context, ArrayList<CommentModel> commentModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.commentModels = commentModels;
        this.context = context;
        imageLoading = new ImageLoading(context, 5);
    }

    @Override
    public CommentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_adapter_item, parent, false);
        return new CommentsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentsAdapter.MyViewHolder holder, final int position) {
        CommentModel commentModel = commentModels.get(position);

        holder.userNameTV.setText(commentModel.getFirst_name());
        imageLoading.LoadImage(commentModel.getProfileimage(), holder.userIV, null);
        holder.commentTV.setText(commentModel.getComment());
        holder.deleteVideoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // recyclerViewClick.onClickItem(position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }

}
