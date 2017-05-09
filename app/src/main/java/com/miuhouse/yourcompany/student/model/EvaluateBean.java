package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;

/**
 * Created by kings on 8/24/2016.
 */
public class EvaluateBean implements Serializable {

    private String parentId;
    private String majorDemand;//需求大类目
    private long evaluateTime;//评价时间
    private String evaluateRank;//评价等级
    private String evaluateContent;//评价内容
    private String parentName; //评价人姓名
    private String parentHeadUrl;//评价人头像
    /**
     * 订单类型
     */
    private String orderType;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMajorDemand() {
        return majorDemand;
    }

    public void setMajorDemand(String majorDemand) {
        this.majorDemand = majorDemand;
    }

    public long getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(long evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public String getEvaluateRank() {
        return evaluateRank;
    }

    public void setEvaluateRank(String evaluateRank) {
        this.evaluateRank = evaluateRank;
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentHeadUrl() {
        return parentHeadUrl;
    }

    public void setParentHeadUrl(String parentHeadUrl) {
        this.parentHeadUrl = parentHeadUrl;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
