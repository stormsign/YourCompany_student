package com.miuhouse.yourcompany.student.view.ui.fragment.interf;

import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by khb on 2016/9/30.
 */
public interface IMessagesFragment extends BaseView {
    void setTop(int sysCount, String sysMsg, int purseCount, String purseMsg);
    void refreshTop();
}
