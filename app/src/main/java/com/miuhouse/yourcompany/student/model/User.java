package com.miuhouse.yourcompany.student.model;

import com.miuhouse.yourcompany.student.utils.Util;

import java.io.Serializable;

/**
 * Created by kings on 7/1/2016.
 */
public class User extends BaseBean implements Serializable {

    private ParentInfo parent;

    private SchoolTeacherInfo schoolTeacherInfo;

    public User(ParentInfo parent) {
        this.parent = parent;
    }

    public ParentInfo getParentInfo() {
        return parent;
    }

    public void setParentInfo(ParentInfo parentInfo) {
        this.parent = parent;
    }

    public SchoolTeacherInfo getSchoolTeacherInfo() {
        return schoolTeacherInfo;
    }

    public void setSchoolTeacherInfo(SchoolTeacherInfo schoolTeacherInfo) {
        this.schoolTeacherInfo = schoolTeacherInfo;
    }

    /**
     * 判断用户资料是否填写完整
     *
     * @return
     */
    public boolean isCheckPerSonalData() {

        if (Util.isEmpty(parent.getcName()))
            return false;
        if (Util.isEmpty(parent.getpName()))
            return false;
        if (Util.isEmpty(parent.getHeadUrl()))
            return false;
        if (Util.isEmpty(parent.getMobile()))
            return false;
        if (Util.isEmpty(parent.getGrade()))
            return false;
        return true;
    }
}
