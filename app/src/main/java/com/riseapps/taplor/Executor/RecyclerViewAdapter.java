package com.riseapps.taplor.Executor;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riseapps.taplor.Model.Player;
import com.riseapps.taplor.R;
import com.riseapps.taplor.Utils.AppConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by naimish on 26/9/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private final ArrayList<Integer> imageList;
    private ArrayList<Player> list;

    Context c;

    public RecyclerViewAdapter(Context context, RecyclerView recyclerView, ArrayList<Player> list) {
        this.list=list;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                .getLayoutManager();
        c = context;
        imageList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            imageList.add(AppConstants.rankImages[i]);
        }
        Collections.shuffle(imageList);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.pager_item, parent, false);
        vh = new PlayerViewHolder(v, c);

        return vh;

    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        ((PlayerViewHolder) holder).name.setText(list.get(position).getName());
        ((PlayerViewHolder) holder).score.setText(""+list.get(position).getScore());
        ((PlayerViewHolder)holder).imageView.setImageResource(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class PlayerViewHolder extends RecyclerView.ViewHolder{

        TextView name,score;
        Context ctx;
        CardView cardView;
        ImageView imageView;

        Player player;

        PlayerViewHolder(View v, Context context) {
            super(v);
            this.ctx = context;
            imageView=v.findViewById(R.id.imageView2);
            name =  v.findViewById(R.id.name);
            score = v.findViewById(R.id.score);
            cardView=v.findViewById(R.id.card);
        }

    }
}