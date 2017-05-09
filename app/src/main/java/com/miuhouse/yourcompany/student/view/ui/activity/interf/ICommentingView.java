package com.miuhouse.yourcompany.student.view.ui.activity.interf;

import com.miuhouse.yourcompany.student.model.CommentsBean;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by kings on 1/5/2017.
 */
public interface ICommentingView extends BaseView {
    void showError(int type, CommentsBean mCommentsBean);

}
