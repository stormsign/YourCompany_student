package com.miuhouse.yourcompany.student.view.ui.activity.interf;


import com.miuhouse.yourcompany.student.model.ClassEntity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

import java.util.List;

/**
 * Created by khb on 2017/1/10.
 */
public interface IChooseClass extends BaseView {
    void showChoices(List<ClassEntity> classlist);
}
