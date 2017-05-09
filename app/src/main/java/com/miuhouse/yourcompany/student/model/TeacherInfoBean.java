package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 8/24/2016.
 */
public class TeacherInfoBean implements Serializable {

    private String id;
    private String tName;
    private String sex;
    private String college;
    private String profession;
    private String education;
    private String grade;
    private String introduction;
    private String headUrl;
    private List<String> images = new ArrayList<>();
    private String idCardCheck;
    private String educationCheck;
    private String professionCheck;
    private double lon;
    private double lat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getIsCardCheck() {
        return idCardCheck;
    }

    public void setIsCardCheck(String isCardCheck) {
        this.idCardCheck = idCardCheck;
    }

    public String getIsEducationCheck() {
        return educationCheck;
    }

    public void setIsEducationCheck(String educationCheck) {
        this.educationCheck = educationCheck;
    }

    public String getProfessionCheck() {
        return professionCheck;
    }

    public void setProfessionCheck(String professionCheck) {
        this.professionCheck = professionCheck;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
