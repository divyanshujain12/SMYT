package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.CategoryModel;
import com.example.divyanshu.smyt.Models.OrdersModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshu on 20/12/16.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {

    private ArrayList<OrdersModel> ordersModels;
    private Context context;
    private RecyclerViewClick recyclerViewClick;

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTV, descTV, validTillTV, remainVideosCountTV;

        private MyViewHolder(View view) {
            super(view);
            titleTV = (TextView) view.findViewById(R.id.titleTV);
            descTV = (TextView) view.findViewById(R.id.descTV);
            validTillTV = (TextView) view.findViewById(R.id.validTillTV);
            remainVideosCountTV = (TextView) view.findViewById(R.id.remainVideosCountTV);

        }
    }

    public OrderListAdapter(Context context, ArrayList<OrdersModel> ordersModels, RecyclerViewClick recyclerViewClick) {
        this.ordersModels = ordersModels;
        this.context = context;
        this.recyclerViewClick = recyclerViewClick;
    }

    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_adapter_item, parent, false);

        return new OrderListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OrderListAdapter.MyViewHolder holder, int position) {
        OrdersModel ordersModel = ordersModels.get(position);
        holder.titleTV.setText(ordersModel.getTitle());
        holder.descTV.setText(ordersModel.getDescription());
        holder.remainVideosCountTV.setText(ordersModel.getVideo_count());
        holder.validTillTV.setText(ordersModel.getValid_till());

    }


    @Override
    public int getItemCount() {
        return ordersModels.size();
    }
    public void addData(ArrayList<OrdersModel> ordersModels){
        this.ordersModels = ordersModels;
        notifyDataSetChanged();
    }
}

