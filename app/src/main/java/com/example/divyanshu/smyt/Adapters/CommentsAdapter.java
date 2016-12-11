package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.CommentModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

import static com.example.divyanshu.smyt.Constants.Constants.COMMENT_COUNT;

/**
 * Created by divyanshu on 9/24/2016.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private ArrayList<CommentModel> commentModels = new ArrayList<>();
    private Context context;
    private ImageLoading imageLoading;
    private RecyclerViewClick recyclerViewClick;
    private String currentCustomerID = "";

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
        if (commentModels != null)
            this.commentModels = commentModels;
        this.context = context;
        imageLoading = new ImageLoading(context, 5);
        currentCustomerID = MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID);
    }

    @Override
    public CommentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_adapter_item, parent, false);
        return new CommentsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentsAdapter.MyViewHolder holder, final int position) {
        final CommentModel commentModel = commentModels.get(position);

        holder.userNameTV.setText(commentModel.getFirst_name());
        imageLoading.LoadImage(commentModel.getProfileimage(), holder.userIV, null);
        holder.commentTV.setText(commentModel.getComment());
        setDeleteCommentIvVisibility(holder, commentModel);
        holder.deleteVideoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(position, v);
                removeComment(position);
            }
        });
    }

    private void setDeleteCommentIvVisibility(MyViewHolder holder, CommentModel commentModel) {
        if (commentModel.getCustomer_id().equals(currentCustomerID))
            holder.deleteVideoIV.setVisibility(View.VISIBLE);
        else
            holder.deleteVideoIV.setVisibility(View.GONE);
    }

    public void removeComment(int position) {
        commentModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public CommentModel getCommentModel(int position) {
        return commentModels.get(position);
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }

    public void sendLocalBroadCastForCommentCount(String customer_video_id, int commentCount) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CUSTOMERS_VIDEO_ID, customer_video_id);
        intent.putExtra(Constants.COUNT, commentCount);
        intent.putExtra(Constants.TYPE,COMMENT_COUNT);
        intent.setAction(Constants.UPDATE_UI_VIDEO_FRAGMENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        intent.setAction(Constants.ALL_VIDEO_TAB_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void addNewComment(CommentModel commentModel) {
        if (commentModels == null)
            commentModels = new ArrayList<>();
        commentModels.add(commentModels.size(), commentModel);
        notifyItemInserted(commentModels.size());
    }
}
