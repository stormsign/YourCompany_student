package com.miuhouse.yourcompany.student.view.ui.activity.interf;

import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by kings on 2/21/2017.
 */
public interface IDeleteTwitterView extends BaseView {
    void showDelete(int type, int index, String msg);

    void showDeleteDialog();

    void hideDeletDialog();
}
