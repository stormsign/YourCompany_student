package com.miuhouse.yourcompany.student.model;

/**
 * Created by khb on 2016/10/8.
 */
public class OrderMsgBean {
    String id;
    String orderId;
    String userId;
    String userType;
    String orderMsgType;
    long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderMsgType() {
        return orderMsgType;
    }

    public void setOrderMsgType(String orderMsgType) {
        this.orderMsgType = orderMsgType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
