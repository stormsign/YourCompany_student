package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;

/**
 * Created by kings on 12/28/2016.
 */
public class SchoolNewsInfo extends BaseBean implements Serializable {

    private SchoolNewsBean schoolNewsInfo;

    public SchoolNewsBean getSchoolNewsInfo() {
        return schoolNewsInfo;
    }

    public void setSchoolNewsInfo(
        SchoolNewsBean schoolNewsInfo) {
        this.schoolNewsInfo = schoolNewsInfo;
    }
}
