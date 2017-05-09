package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 1/12/2017.
 */
public class SchoolTeacher extends SchoolTeacherInfo implements Serializable {

  private List<ClassEntity> classes = new ArrayList<>();

  public List<ClassEntity> getClasses() {
    return classes;
  }

  public void setClasses(List<ClassEntity> classes) {
    this.classes = classes;
  }
}
