package com.miuhouse.yourcompany.student.model;

import com.miuhouse.yourcompany.student.utils.Util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by kings on 7/8/2016.
 */
public class ParentInfo implements Serializable {
    private String id;
    private String parentId;
    private String pName;
    private String cName;
    private String token;
    private String grade;//学生年级
    private String subject;//科目
    private String interest;//兴趣
    private String headUrl;//头像
    private String mobile; //电话
    private long schoolId;
    private long classId;
    private String schoolName;
    private String roleName;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public ParentInfo() {

    }

    public ParentInfo(String pName, String cName, String headUrl, String mobile) {
        this.pName = pName;
        this.cName = cName;
        this.headUrl = headUrl;
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 判断用户资料是否填写完整
     */
    public boolean isCheckPerSonalData() {

        if (Util.isEmpty(getcName())) {
            return false;
        }
        if (Util.isEmpty(getpName())) {
            return false;
        }
        if (Util.isEmpty(getHeadUrl())) {
            return false;
        }
        if (Util.isEmpty(getMobile())) {
            return false;
        }

        return true;
    }
}
