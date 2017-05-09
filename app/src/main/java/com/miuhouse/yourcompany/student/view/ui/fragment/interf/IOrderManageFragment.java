package com.miuhouse.yourcompany.student.view.ui.fragment.interf;


import com.miuhouse.yourcompany.student.interactor.OrderManageInteractor;
import com.miuhouse.yourcompany.student.model.OrderEntity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by khb on 2016/7/19.
 */
public interface IOrderManageFragment extends BaseView {

    void refresh(OrderManageInteractor.OrderListBean bean);
    void goToDetail(OrderEntity order, int reqCode) ;
//    void beforeBeginClass();
//    void afterBeginClass();
}
