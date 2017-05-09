package com.miuhouse.yourcompany.student.view.ui.activity.interf;


import com.miuhouse.yourcompany.student.model.CommentsBean;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

import java.util.List;

/**
 * Created by khb on 2017/1/17.
 */
public interface IUnreadMsg extends BaseView {
    void showUnreaedList(List<CommentsBean> commentList);
}
