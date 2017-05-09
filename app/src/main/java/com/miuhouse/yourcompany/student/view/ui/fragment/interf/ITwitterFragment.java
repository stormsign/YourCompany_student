package com.miuhouse.yourcompany.student.view.ui.fragment.interf;


import com.miuhouse.yourcompany.student.model.SchoolNewsListBean;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by kings on 12/29/2016.
 */
public interface ITwitterFragment extends BaseView {

  void getSchoolNewListBean(SchoolNewsListBean schoolNewsListBean);
  //void showClasses();
  //void hideClasses();
  void unreadDisplayControll(boolean displayFlag);
}
