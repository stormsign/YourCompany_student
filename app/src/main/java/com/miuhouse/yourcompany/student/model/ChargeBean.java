package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;

/**
 * Created by kings on 8/30/2016.
 */
public class ChargeBean extends BaseBean implements Serializable {
    private String charge;

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }
}
