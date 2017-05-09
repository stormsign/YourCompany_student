package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;

/**
 * Created by khb on 2017/1/10.
 */
public class ClassEntity implements Serializable {
    int id;
    String name;
    boolean selected;

    public ClassEntity(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
