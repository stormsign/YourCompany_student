package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.listener.OnListItemClick;
import com.miuhouse.yourcompany.student.model.OrderMsgBean;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.base.BaseRVAdapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by khb on 2016/10/8.
 */
public class OrderMsgAdapter extends BaseRVAdapter<OrderMsgBean>{
    public OrderMsgAdapter(Context context, List<OrderMsgBean> msgList, OnListItemClick onListItemClick) {
        super(context, msgList, onListItemClick);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MsgHolder(LayoutInflater.from(context).inflate(R.layout.item_ordermsg, null));
    }

    class MsgHolder extends RecyclerView.ViewHolder{

        TextView msgTitle;
        TextView msgContent;
        TextView msgTime;
        LinearLayout item;

        public MsgHolder(View itemView) {
            super(itemView);
            msgTitle = (TextView) itemView.findViewById(R.id.msgTitle);
            msgContent = (TextView) itemView.findViewById(R.id.msgContent);
            msgTime = (TextView) itemView.findViewById(R.id.msgTime);
            item = (LinearLayout) itemView.findViewById(R.id.item);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MsgHolder mholder = (MsgHolder) holder;
        final OrderMsgBean orderMsg = list.get(position);
        mholder.msgTitle.setText(
                Util.getResourceId(context, String.format("order_msg_title_%s",
                                orderMsg.getOrderMsgType()),
                        "string"));
        mholder.msgContent.setText(
                Util.getResourceId(context, String.format("order_msg_content_%s",
                                orderMsg.getOrderMsgType()),
                        "string"));
        mholder.msgTime.setText(new SimpleDateFormat("MM月dd日 HH:mm").format(new Date(orderMsg.getCreateTime())));
        mholder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onListItemClick!=null){
                    onListItemClick.onItemClick(orderMsg);
                }
            }
        });
    }
}
