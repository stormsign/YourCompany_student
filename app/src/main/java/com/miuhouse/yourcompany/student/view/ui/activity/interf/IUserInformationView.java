package com.miuhouse.yourcompany.student.view.ui.activity.interf;

import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.MyOrderInfoMessageBean;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by kings on 7/13/2016.
 */
public interface IUserInformationView extends BaseView {
    void UpdateSuccess(BaseBean baseBean);
    void getUserInfo(User user);

}
