package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.activities.CategoryDescriptionActivity;
import com.example.divyanshu.smyt.activities.HomeActivity;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu on 8/26/2016.
 */
public class CategoryRvAdapter extends RecyclerView.Adapter<CategoryRvAdapter.MyViewHolder> {

    private ArrayList<CategoryModel> categoryModels;
    private Context context;
    private ImageLoading imageLoading;
    private RecyclerViewClick recyclerViewClick;

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryNameTV, userTimeTV;
        private ImageView categoryIV;
        private FrameLayout joinedCatFL;

        private MyViewHolder(View view) {
            super(view);
            categoryNameTV = (TextView) view.findViewById(R.id.genreNameTV);
            categoryIV = (ImageView) view.findViewById(R.id.categoryIV);
            joinedCatFL = (FrameLayout) view.findViewById(R.id.joinedCatFL);
        }
    }

    public CategoryRvAdapter(Context context, ArrayList<CategoryModel> categoryModels, RecyclerViewClick recyclerViewClick) {
        this.categoryModels = categoryModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
        this.recyclerViewClick = recyclerViewClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_rv_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CategoryModel categoryModel = categoryModels.get(position);

        holder.categoryNameTV.setText(categoryModel.getcategory_name());
        imageLoading.LoadImage(categoryModel.getThumbnail(), holder.categoryIV, null);
        if (categoryModel.getJoin_status() == 0)
            holder.joinedCatFL.setVisibility(View.GONE);
        else
            holder.joinedCatFL.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(holder.getAdapterPosition(),v);
                //nextActivityIntent(categoryModel);
            }
        });
    }

    public void setJoinStatus(int adapterPosition) {
        categoryModels.get(adapterPosition).setJoin_status(1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }
}
