package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.MsgEntity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseRVAdapter;

import java.util.List;

/**
 * Created by khb on 2016/7/14.
 */
public class MsgAdapter extends BaseRVAdapter {
    public MsgAdapter(Context context, List<MsgEntity> list, OnListItemClick onListItemClick) {
        super(context, list, onListItemClick);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MsgHolder(LayoutInflater.from(context).inflate(R.layout.item_msg, null));
    }


    class MsgHolder extends RecyclerView.ViewHolder{
        TextView msgType;
        TextView content;
        ImageView ivMsgType;
        RelativeLayout bottom;
        TextView time;

        public MsgHolder(View itemView) {
            super(itemView);
            msgType = (TextView) itemView.findViewById(R.id.msgType);
            content = (TextView) itemView.findViewById(R.id.msg_content);
            ivMsgType = (ImageView) itemView.findViewById(R.id.img_msg_type);
            bottom = (RelativeLayout) itemView.findViewById(R.id.msgBottom);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MsgHolder mholder = (MsgHolder) holder;
        final MsgEntity msg = (MsgEntity) list.get(position);
        mholder.bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onListItemClick){
                    onListItemClick.onItemClick(msg);
                }
            }
        });

    }
}
