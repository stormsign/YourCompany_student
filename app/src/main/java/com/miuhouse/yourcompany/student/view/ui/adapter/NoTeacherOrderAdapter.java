package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.interactor.OrderManageInteractor;
import com.miuhouse.yourcompany.student.model.OrderEntity;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.base.BaseRVAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by khb on 2016/9/19.
 */
public class NoTeacherOrderAdapter extends BaseRVAdapter{

    private int selectedIndex;
    public NoTeacherOrderAdapter(Context context, List<OrderManageInteractor.WaitingOrder> orderList, NoTOnclickListener listener) {
        super(context, orderList);
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoTHOlder(LayoutInflater.from(context)
                .inflate(R.layout.item_no_teacher_orders, null));
    }


    class NoTHOlder extends RecyclerView.ViewHolder{
        ImageView selectIcon;
        TextView orderType;
        TextView beginTime;
        TextView teacherCount;
        LinearLayout itemContent;
        TextView ren;
        public NoTHOlder(View itemView) {
            super(itemView);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            itemContent = (LinearLayout) itemView.findViewById(R.id.itemContent);
            selectIcon = (ImageView) itemView.findViewById(R.id.selectIcon);
            orderType = (TextView) itemView.findViewById(R.id.orderType);
            beginTime = (TextView) itemView.findViewById(R.id.beginTime);
            teacherCount = (TextView) itemView.findViewById(R.id.teacherCount);
            ren = (TextView) itemView.findViewById(R.id.ren);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        NoTHOlder mholder = (NoTHOlder) holder;
        final OrderManageInteractor.WaitingOrder order = (OrderManageInteractor.WaitingOrder) list.get(position);
        mholder.orderType.setText(context.getResources().getString(
                Util.getResourceId(
                        context,
                        String.format("order_type_%s", order.getMajorDemand()),
                        "string")));
        mholder.beginTime.setText(new SimpleDateFormat("上课时间 MM/dd HH:mm")
                .format(new Date(order.getClassBeginTimePromise())));
        mholder.teacherCount.setText(order.getNum()+"");
        if (position == selectedIndex){
            mholder.selectIcon.setVisibility(View.VISIBLE);
            mholder.orderType.setTextColor(context.getResources().getColor(R.color.textOrange));
            mholder.beginTime.setTextColor(context.getResources().getColor(R.color.textOrange));
            mholder.teacherCount.setTextColor(context.getResources().getColor(R.color.textOrange));
            mholder.ren.setTextColor(context.getResources().getColor(R.color.textOrange));
        }else {
            mholder.selectIcon.setVisibility(View.GONE);
            mholder.orderType.setTextColor(context.getResources().getColor(R.color.textDarkone));
            mholder.beginTime.setTextColor(context.getResources().getColor(R.color.textDarkone));
            mholder.teacherCount.setTextColor(context.getResources().getColor(R.color.textDarkone));
            mholder.ren.setTextColor(context.getResources().getColor(R.color.textDarkone));
        }
        mholder.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = position;
                if (null!=listener){
                    listener.onOrderClick(order, position);
                }
                refresh();
            }
        });
    }

    private void refresh(){
        notifyDataSetChanged();
    }

    public void setSelectedIndex(int index){
        selectedIndex = index;
        notifyDataSetChanged();
    }

    public int getSelectedIndex(){
        return selectedIndex;
    }

    public interface NoTOnclickListener {
        void onOrderClick(OrderEntity order, int position);
    }

    private NoTOnclickListener listener;
}
