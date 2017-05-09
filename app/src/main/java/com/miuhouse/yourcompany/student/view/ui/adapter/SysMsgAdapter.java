package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.SysMsgEntity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseRVAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by khb on 2016/7/14.
 */
public class SysMsgAdapter extends BaseRVAdapter {


    public SysMsgAdapter(Context context, List<SysMsgEntity> list, OnListItemClick onListItemClick) {
        super(context, list, onListItemClick);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SysMsgHolder(LayoutInflater.from(context).inflate(R.layout.item_sysmsg, null));
    }

    class SysMsgHolder extends RecyclerView.ViewHolder{

        TextView time;
        TextView content;

        public SysMsgHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            content = (TextView) itemView.findViewById(R.id.content);

        }
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SysMsgHolder mholder = (SysMsgHolder) holder;
        SysMsgEntity msg = (SysMsgEntity) list.get(position);
        mholder.time.setText(new SimpleDateFormat("HH:mm").format(msg.getSendTime()));
        mholder.content.setText(msg.getDescription());
    }
}
