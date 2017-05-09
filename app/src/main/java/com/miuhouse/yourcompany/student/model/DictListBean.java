package com.miuhouse.yourcompany.student.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典表
 * Created by kings on 7/19/2016.
 */
public class DictListBean extends BaseBean {

    public List<DictBean> dictionaries = new ArrayList<>();

    public List<DictBean> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(List<DictBean> dictionaries) {
        this.dictionaries = dictionaries;
    }
}
