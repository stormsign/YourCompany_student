package com.miuhouse.yourcompany.student.view.ui.activity.interf;


import com.miuhouse.yourcompany.student.interactor.SysMsgInteractor;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by khb on 2016/7/14.
 */
public interface ISysMsgActivity extends BaseView {
    void refresh(SysMsgInteractor.SysMsgBean bean);
}
