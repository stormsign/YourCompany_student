package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;

/**
 * Created by kings on 10/11/2016.
 */
public class MyOrderInfoMessageBean extends BaseBean implements Serializable {
    private long waitingPayNum; //未付款的订单数

    private long waitingClassNum;//带上课的订单数

    private long waitingEvaluateNum;//待评论的订单数

    private long orderGrabNum;//接单老师总数

    private long orderGrabCount; //被抢订单的数量

    private String orderInfoId;//最新订单的订单id


    public long getWaitingPayNum() {
        return waitingPayNum;
    }

    public void setWaitingPayNum(long waitingPayNum) {
        this.waitingPayNum = waitingPayNum;
    }

    public long getWaitingClassNum() {
        return waitingClassNum;
    }

    public void setWaitingClassNum(long waitingClassNum) {
        this.waitingClassNum = waitingClassNum;
    }

    public long getWaitingEvaluateNum() {
        return waitingEvaluateNum;
    }

    public void setWaitingEvaluateNum(long waitingEvaluateNum) {
        this.waitingEvaluateNum = waitingEvaluateNum;
    }

    public long getOrderGrabNum() {
        return orderGrabNum;
    }

    public void setOrderGrabNum(long orderGrabNum) {
        this.orderGrabNum = orderGrabNum;
    }

    public String getOrderInfoId() {
        return orderInfoId;
    }

    public void setOrderInfoId(String orderInfoId) {
        this.orderInfoId = orderInfoId;
    }

    public long getOrderGrabCount() {
        return orderGrabCount;
    }

    public void setOrderGrabCount(long orderGrabCount) {
        this.orderGrabCount = orderGrabCount;
    }
}