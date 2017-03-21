package com.example.divyanshu.smyt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshu.smyt.Interfaces.RecyclerViewClick;
import com.example.divyanshu.smyt.Models.MusicModel;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by divyanshuPC on 3/22/2017.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private ArrayList<MusicModel> musicModels = new ArrayList<>();
    private Context context;
    private RecyclerViewClick recyclerViewClick;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView musicNameTV, uploadPercentTV;


        public MyViewHolder(View view) {
            super(view);
            musicNameTV = (TextView) view.findViewById(R.id.musicNameTV);
            uploadPercentTV = (TextView) view.findViewById(R.id.uploadPercentTV);

        }
    }

    public MusicAdapter(Context context, ArrayList<MusicModel> musicModels, RecyclerViewClick recyclerViewClick) {
        this.recyclerViewClick = recyclerViewClick;
        if (musicModels != null)
            this.musicModels = musicModels;
        this.context = context;
    }

    @Override
    public MusicAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seleted_audio_rv, parent, false);
        return new MusicAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MusicAdapter.MyViewHolder holder, final int position) {
        final MusicModel musicModel = musicModels.get(position);
        holder.musicNameTV.setText(musicModel.getFileName());
    }

    public void removeMusicModel(int position) {
        musicModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }


    @Override
    public int getItemCount() {
        return musicModels.size();
    }


    public void addNewMusicModel(MusicModel musicModel) {
        if (musicModels == null)
            musicModels = new ArrayList<>();
        musicModels.add(musicModels.size(), musicModel);
        notifyItemInserted(musicModels.size());
    }

}

