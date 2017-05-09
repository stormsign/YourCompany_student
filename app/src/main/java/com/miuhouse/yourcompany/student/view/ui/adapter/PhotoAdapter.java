package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.Image;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.base.BaseRVAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author alessandro.balocco
 */
public class PhotoAdapter extends BaseRVAdapter {


    public PhotoAdapter(Context context, List list, OnListItemClick onListItemClick) {
        super(context, list, onListItemClick);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_image, null));
        if (holder != null) {
            holder.itemView.setTag(holder);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String imagePath = (String) list.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(context).load(imagePath).override(Util.dip2px(context, 80), Util.dip2px(context, 80)).centerCrop().into(viewHolder.imageView);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            onListItemClick.onItemClick(holder.getAdapterPosition());
        }
    }
}
