package com.miuhouse.yourcompany.student.view.ui.activity.interf;

import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * 这个接口封装的方法基本上都跟视图展示有关。
 * Created by kings on 7/1/2016.
 */
public interface ILoginView extends BaseView{
    void showLoginSuccess(User user);
    void showRegistSuccess(BaseBean baseBean);
}
