package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 7/8/2016.
 */
public class SchoolTeacherInfo extends BaseBean implements Serializable {

    public static final String PARENT = "2";
    public static final String TEACHER = "3";

    private String id;
    /**
     * 机构老师的名字
     */
    private String tName;

    private String mobile;

    private String token;

    private long tokenTime;

    /**
     * 机构的id
     */
    private String schoolId;

    private String headUrl;

    private String roleName;

    private String roleType;

    private List<ClassEntity> schoolClassInfos = new ArrayList<>();

    public List<ClassEntity> getSchoolClassInfos() {
        return schoolClassInfos;
    }

    public void setSchoolClassInfos(
        List<ClassEntity> schoolClassInfos) {
        this.schoolClassInfos = schoolClassInfos;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTokenTime(long tokenTime) {
        this.tokenTime = tokenTime;
    }

    public String gettName() {
        return tName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getToken() {
        return token;
    }

    public long getTokenTime() {
        return tokenTime;
    }

    public String getId() {
        return id;
    }
}
