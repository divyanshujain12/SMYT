package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Activities.HomeActivity;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.example.divyanshu.smyt.Activities.CategoryDescriptionActivity;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu on 8/26/2016.
 */
public class CategoryRvAdapter extends RecyclerView.Adapter<CategoryRvAdapter.MyViewHolder> {

    private ArrayList<CategoryModel> categoryModels;
    private Context context;
    private ImageLoading imageLoading;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryNameTV, userTimeTV;
        public ImageView categoryIV;

        public MyViewHolder(View view) {
            super(view);
            categoryNameTV = (TextView) view.findViewById(R.id.categoryNameTV);
            categoryIV = (ImageView) view.findViewById(R.id.categoryIV);

        }
    }

    public CategoryRvAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.categoryModels = categoryModels;
        this.context = context;
        imageLoading = new ImageLoading(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_rv_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CategoryModel categoryModel = categoryModels.get(position);

        holder.categoryNameTV.setText(categoryModel.getcategory_name());
        imageLoading.LoadImage(categoryModel.getThumbnail(), holder.categoryIV, null);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryModel.getJoin_status() == 0) {
                    Intent intent = new Intent(context, CategoryDescriptionActivity.class);
                    intent.putExtra(Constants.DATA, categoryModel);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.putExtra(Constants.DATA, categoryModel);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }
}
