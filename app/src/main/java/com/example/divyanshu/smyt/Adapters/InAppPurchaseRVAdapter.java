package com.example.divyanshu.smyt.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anjlab.android.iab.v3.SkuDetails;
import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.List;

/**
 * Created by divyanshu.jain on 11/17/2016.
 */

public class InAppPurchaseRVAdapter extends RecyclerView.Adapter<InAppPurchaseRVAdapter.MyViewHolder> {

    private List<SkuDetails> skuDetailsList;
    private RecyclerViewClick recyclerViewClick;

    public InAppPurchaseRVAdapter(List<SkuDetails> skuDetailsList, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        this.skuDetailsList = skuDetailsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.in_app_purchase_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        SkuDetails skuDetails = skuDetailsList.get(position);
        holder.productDesc.setText(skuDetails.description);
        holder.productTitle.setText(skuDetails.title);
        holder.productPrice.setText(skuDetails.priceText);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClick.onClickItem(position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productTitle, productDesc, productPrice;

        public MyViewHolder(View view) {
            super(view);
            productTitle = (TextView) view.findViewById(R.id.productTitle);
            productDesc = (TextView) view.findViewById(R.id.productDesc);
            productPrice = (TextView) view.findViewById(R.id.productPrice);

        }
    }
}
