package com.miuhouse.yourcompany.student.view.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.model.OrderEntity;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.utils.Values;
import com.miuhouse.yourcompany.student.view.ui.activity.OrderCommentActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.OrderPayActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.OrderRefundActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseRVAdapter;
import com.miuhouse.yourcompany.student.view.widget.CircularImageViewHome;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by khb on 2016/7/19.
 */
public class OrderAdapter extends BaseRVAdapter {

    private int orderType;

    public OrderAdapter(Context context, List<OrderEntity> list, int orderType) {
        super(context, list);
        this.orderType = orderType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderHolder(LayoutInflater.from(context).inflate(R.layout.item_ordermanage, null));
    }

    class OrderHolder extends RecyclerView.ViewHolder {
        TextView orderType;
        TextView orderStatus;
        CircularImageViewHome teacherHead;
        TextView teacherName;
        TextView time;
        TextView totalPrice;
        TextView classCount;
        TextView button;
        LinearLayout content;
        ImageView tag;

        public OrderHolder(View itemView) {
            super(itemView);
            content = (LinearLayout) itemView.findViewById(R.id.orderContent);
            orderType = (TextView) itemView.findViewById(R.id.orderType);
            orderStatus = (TextView) itemView.findViewById(R.id.orderStatus);
            teacherHead = (CircularImageViewHome) itemView.findViewById(R.id.teacherHead);
            teacherName = (TextView) itemView.findViewById(R.id.teacherName);
            time = (TextView) itemView.findViewById(R.id.time);
            totalPrice = (TextView) itemView.findViewById(R.id.totalPrice);
            classCount = (TextView) itemView.findViewById(R.id.classCount);
            button = (TextView) itemView.findViewById(R.id.orderManageButton);
            tag = (ImageView) itemView.findViewById(R.id.orderTag);

//            if (beginTime > 0
//                    && !Util.isEmpty(timeLength)
//                    && position > 0){
//            }

        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        L.i("position :"+position + " holder : " + holder.toString());

        final OrderEntity order = (OrderEntity) list.get(position);
        OrderHolder mholder = (OrderHolder) holder;
        setOrderType(mholder.orderType, order.getMajorDemand());
        setOrderStatus(mholder.orderStatus, order.getOrderStatus());
        if (Integer.parseInt(order.getOrderStatus()) > 2) {
            if (!Util.isEmpty(order.getUserHeader())) {
                Glide.with(context).load(order.getUserHeader())
                        .placeholder(R.mipmap.ico_head_default)
                        .error(R.mipmap.ico_head_default)
                        .into(mholder.teacherHead);
            } else {
                mholder.teacherHead.setImageResource(R.mipmap.ico_head_default);
            }
            mholder.teacherName.setText(order.getTname());
        } else {
            mholder.teacherHead.setImageResource(Util.getResourceId(context, String.format("ico_ordertype_%s", order.getMajorDemand()), "mipmap"));
            mholder.teacherName.setText(Util.getResourceId(context, String.format("order_type_%s", order.getMajorDemand()), "string"));
        }
        String strTime = null;
        if (order.getOrderType().equals("1")) {
            strTime = "课程安排 " + formatTime(order);
            mholder.classCount.setText("共" + order.getLesson() + "课时");
            mholder.tag.setVisibility(View.GONE);
        } else {
            strTime = "上课日期：" + formatTime(order);
            mholder.classCount.setText(Util.getResourceId(context,
                    String.format("order_term_%s", order.getOrderType()), "string"));
            mholder.tag.setVisibility(View.VISIBLE);
            if (order.getOrderType().equals("2")) {
                mholder.tag.setImageResource(R.mipmap.ico_zhou);
            } else if (order.getOrderType().equals("3")) {
                mholder.tag.setImageResource(R.mipmap.ico_yue);
            }
        }

        mholder.time.setText(strTime);
        mholder.totalPrice.setText("￥" + order.getAmount());
        setButton(mholder.button, order, position);
        mholder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnOrderClick) {
                    mOnOrderClick.onOrderClick(order);
                }
            }
        });

//        mholder.start();

//        if (order.getOrderStatus().equals(Values.getKey(Values.orderStatuses, "进行中"))){
//            startCountTime(mholder,
//                    order.getClassBeginTimeActual(),
//                    order.getLesson(),
//                    position);
//        }

    }

    private void setButton(TextView button, final OrderEntity order, int position) {
        if (order.getOrderStatus().equals("1")) {    //待付款   fragment B
            button.setVisibility(View.VISIBLE);
            button.setText("去付款");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, OrderPayActivity.class)
                            .putExtra("orderId", order.getId()));
                }
            });
        } else if (order.getOrderStatus().equals("3")) {  //待上课 fragment C
            button.setVisibility(View.VISIBLE);
            button.setText("退款");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, OrderRefundActivity.class)
                            .putExtra("order", order));
                }
            });
        } else if (order.getOrderStatus().equals("5")) {      //待评价   fragment D
            button.setVisibility(View.VISIBLE);
            button.setText("评价");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, OrderCommentActivity.class)
                                    .putExtra("tname", order.getTname())
                                    .putExtra("tHeader", order.getUserHeader())
                                    .putExtra("classBeginTimePromise", order.getClassBeginTimePromise())
                                    .putExtra("majorDemand", order.getMajorDemand())
                                    .putExtra("orderInfoId", order.getId())
                    );
                }
            });
        } else {
            button.setVisibility(View.GONE);
        }

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mOnOrderClick){
//                    mOnOrderClick.onButtonClick(order);
//                }
//            }
//        });

    }

    private String formatTime(OrderEntity order) {
        if (order.getOrderType().equals("1")) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd  HH:mm");
            return formatter.format(new Date(order.getClassBeginTimePromise()));
        } else {
            Log.i("TAG", "ClassBeginTimePromise=" + order.getClassBeginTimePromise());
            Log.i("TAG", "ClassEndTimePromise=" + order.getClassEndTimePromise());
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
            return formatter.format(new Date(order.getClassBeginTimePromise()))
                    + " ~ " + formatter.format(new Date(order.getClassEndTimePromise()));
        }
    }


    private void setOrderStatus(TextView orderStatus, String orderStatus1) {
        orderStatus.setText(Values.orderStatuses.get(orderStatus1));
    }

    private void setOrderType(TextView orderType, String majorDemand) {
        if (Util.isEmpty(majorDemand)) {
            return;
        }
        int stringId = Util.getResourceId(context, String.format("order_type_%s", majorDemand), "string");
        orderType.setText(context.getResources().getString(stringId));
        int mipmapId = Util.getResourceId(context, String.format("home_list_title_%s", majorDemand), "mipmap");
        orderType.setBackgroundResource(mipmapId);
    }

    public interface OnOrderClick {
        void onOrderClick(OrderEntity order);
//        void onButtonClick(OrderEntity order);
    }

    private OnOrderClick mOnOrderClick;

    public void setOnOrderClick(OnOrderClick onOrderClick) {
        mOnOrderClick = onOrderClick;
    }
}
