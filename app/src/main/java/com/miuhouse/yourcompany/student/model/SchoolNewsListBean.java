package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kings on 12/28/2016.
 */
public class SchoolNewsListBean extends BaseBean implements Serializable {

  private ArrayList<SchoolNewsBean> schoolNewsInfos = new ArrayList<>();

  public ArrayList<SchoolNewsBean> getSchoolNewsInfos() {
    return schoolNewsInfos;
  }

  public void setSchoolNewsInfos(
      ArrayList<SchoolNewsBean> schoolNewsInfos) {
    this.schoolNewsInfos = schoolNewsInfos;
  }
}
