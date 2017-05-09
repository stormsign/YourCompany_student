package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kings on 8/25/2016.
 */
public class EvaluateListBean extends BaseBean implements Serializable {

    private ArrayList<EvaluateBean> evaluates = new ArrayList<>();

    public ArrayList<EvaluateBean> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(ArrayList<EvaluateBean> evaluates) {
        this.evaluates = evaluates;
    }
}
