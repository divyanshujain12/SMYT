package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.Models.UserModel;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu on 8/26/2016.
 */
public class CategoryRvAdapter extends RecyclerView.Adapter<CategoryRvAdapter.MyViewHolder> {

    private ArrayList<CategoryModel> categoryModels;
    private Context context;

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
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_rv_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CategoryModel userModel = categoryModels.get(position);

        holder.categoryNameTV.setText(userModel.getName());
        holder.categoryIV.setImageResource(userModel.getIcon());
        holder.categoryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }
}
