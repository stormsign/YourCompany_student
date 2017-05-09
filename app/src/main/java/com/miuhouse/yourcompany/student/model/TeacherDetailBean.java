package com.miuhouse.yourcompany.student.model;

import android.widget.BaseAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kings on 8/24/2016.
 */
public class TeacherDetailBean extends BaseBean implements Serializable {
    
    private TeacherInfoBean teacherInfo;

    private ArrayList<EvaluateBean> evaluates = new ArrayList<>();

    private long evaluateSize;

    public TeacherInfoBean getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(TeacherInfoBean teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public ArrayList<EvaluateBean> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(ArrayList<EvaluateBean> evaluates) {
        this.evaluates = evaluates;
    }

    public long getEvaluateSize() {
        return evaluateSize;
    }

    public void setEvaluateSize(long evaluateSize) {
        this.evaluateSize = evaluateSize;
    }
}
