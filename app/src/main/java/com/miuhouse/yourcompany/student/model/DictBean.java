package com.miuhouse.yourcompany.student.model;

/**
 * Created by kings on 7/19/2016.
 * 字典表
 */
public class DictBean {

    private int id;
    private String dcType;
    private String dcLabel;
    private String dcName;
    private String dcValue;
    private String dcExtend;
    private String dcDescription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDcType() {
        return dcType;
    }

    public void setDcType(String dcType) {
        this.dcType = dcType;
    }

    public String getDcLabel() {
        return dcLabel;
    }

    public void setDcLabel(String dcLabel) {
        this.dcLabel = dcLabel;
    }

    public String getDcName() {
        return dcName;
    }

    public void setDcName(String dcName) {
        this.dcName = dcName;
    }

    public String getDcValue() {
        return dcValue;
    }

    public void setDcValue(String dcValue) {
        this.dcValue = dcValue;
    }

    public String getDcExtend() {
        return dcExtend;
    }

    public void setDcExtend(String dcExtend) {
        this.dcExtend = dcExtend;
    }

    public String getDcDescription() {
        return dcDescription;
    }

    public void setDcDescription(String dcDescription) {
        this.dcDescription = dcDescription;
    }
}
