package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by khb on 2016/7/7.
 */
public class OrderEntity implements Serializable {
    String id;
    String parentId;
    String teacherId;
    String tname;       //老师名字
    String userHeader;  //老师头像
    String tmobile;
    String cname;       //学生名称
    String mobile;      //手机号
    float receiveAmount ;       //老师实收费用
    double amount;       //家长支付费用
    String majorDemand;     //需求大类
    String minorDemand;     //需求小类
    String orderStatus;     //订单状态
    long classBeginTimePromise;     //约定上课时间
    long classBeginTimeActual;      //实际上课时间
    String lesson;      //课时
    String address;     //上课地址
    String description;     //备注
    long distance;      //距离
    String gradeLevel;  //年级
    String orderType;
    String lessonDay;
    List<String> weekLoop;
    long classEndTimePromise;
    String pname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    public String getTmobile() {
        return tmobile;
    }

    public void setTmobile(String tmobile) {
        this.tmobile = tmobile;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public float getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(float receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMajorDemand() {
        return majorDemand;
    }

    public void setMajorDemand(String majorDemand) {
        this.majorDemand = majorDemand;
    }

    public String getMinorDemand() {
        return minorDemand;
    }

    public void setMinorDemand(String minorDemand) {
        this.minorDemand = minorDemand;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getClassBeginTimePromise() {
        return classBeginTimePromise;
    }

    public void setClassBeginTimePromise(long classBeginTimePromise) {
        this.classBeginTimePromise = classBeginTimePromise;
    }

    public long getClassBeginTimeActual() {
        return classBeginTimeActual;
    }

    public void setClassBeginTimeActual(long classBeginTimeActual) {
        this.classBeginTimeActual = classBeginTimeActual;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getLessonDay() {
        return lessonDay;
    }

    public void setLessonDay(String lessonDay) {
        this.lessonDay = lessonDay;
    }

    public List<String> getWeekLoop() {
        return weekLoop;
    }

    public void setWeekLoop(List<String> weekLoop) {
        this.weekLoop = weekLoop;
    }

    public long getClassEndTimePromise() {
        return classEndTimePromise;
    }

    public void setClassEndTimePromise(long classEndTimePromise) {
        this.classEndTimePromise = classEndTimePromise;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }
}
