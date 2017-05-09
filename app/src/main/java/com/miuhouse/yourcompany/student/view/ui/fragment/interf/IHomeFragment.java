package com.miuhouse.yourcompany.student.view.ui.fragment.interf;

import com.amap.api.location.AMapLocation;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.model.TeacherInfoBean;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

import java.util.List;

/**
 * Created by khb on 2016/8/22.
 */
public interface IHomeFragment extends BaseView {
    void showTeachers(List<TeacherInfoBean> list);
    void showPosition(AMapLocation aMapLocation);
    void showSchedule();
    void showMore();
    void hideMore();
    void changeType(int index);
    void showProfileNotComplete(ParentInfo parentInfo);
    void goToOrderConfirm(ParentInfo parentInfo);
    void goToLongOrderConfirm(ParentInfo parentInfo);
    void goToProfile(ParentInfo parentInfo);
}
