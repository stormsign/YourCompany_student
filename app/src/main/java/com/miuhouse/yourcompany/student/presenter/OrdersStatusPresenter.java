package com.miuhouse.yourcompany.student.presenter;

import com.miuhouse.yourcompany.student.interactor.OrderManageInteractor;
import com.miuhouse.yourcompany.student.interactor.TeacherInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IOrderManageInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.ITeacherInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.presenter.interf.IOrdersStatusPresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrdersStatusActivity;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

import java.util.List;

/**
 * Created by khb on 2016/9/18.
 */
public class OrdersStatusPresenter implements IOrdersStatusPresenter {
    private IOrdersStatusActivity activity;
    private ITeacherInteractor teacherInteractor;
    private IOrderManageInteractor orderManageInteractor;
    public OrdersStatusPresenter(IOrdersStatusActivity activity) {
        this.activity = activity;
        teacherInteractor = new TeacherInteractor();
        orderManageInteractor = new OrderManageInteractor();
    }


    @Override
    public void getInterestedTeachers(String parentId, String orderId) {
        teacherInteractor.getInterestedTeachers(parentId, orderId, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {
                activity.showListLoading();
            }

            @Override
            public void onLoadSuccess(Object data) {
                TeacherInteractor.TeacherInfoListBean teacherInfoListBean
                        = (TeacherInteractor.TeacherInfoListBean) data;
                activity.showTeachers(teacherInfoListBean.getTeacherList());
                activity.hideListLoading();
                activity.hideLoading();
            }

            @Override
            public void onLoadFailed(String msg) {
                L.i(msg);
                activity.hideListLoading();
                activity.hideLoading();
            }
        });
    }


    @Override
    public void getNoTeacherOrders(final String parentId, final String orderId) {
        orderManageInteractor.getNoTeacherOrders(parentId, orderId, 1, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {
                activity.showLoading(null);
            }

            @Override
            public void onLoadSuccess(Object data) {
                OrderManageInteractor.OrderGrabBean orderGrabBean =
                        (OrderManageInteractor.OrderGrabBean) data;
//                List<OrderManageInteractor.WaitingOrder> waitingList = new ArrayList<OrderManageInteractor.WaitingOrder>();
//                for (int i=0; i<5; i++){
//                    OrderManageInteractor.WaitingOrder wo = new OrderManageInteractor.WaitingOrder();
//                    wo.setId(i+"");
//                    wo.setNum(i);
//                    wo.setClassBeginTimePromise(System.currentTimeMillis());
//                    wo.setMajorDemand("1");
//                    wo.setAddress("???");
//                    wo.setAmount(3);
//                    wo.setCname("Frank");
//                    wo.setDescription("FFF");
//                    wo.setDistance(200);
//                    wo.setLesson("1");
//                    wo.setMinorDemand("1");
//                    wo.setMobile("123321112");
//                    wo.setOrderStatus("1");
//                    wo.setParentId("2");
//                    wo.setUserHeader("smilyface");
//                    wo.setNum(i + 1);
//                    waitingList.add(wo);
//                }
//                orderGrabBean.setOrderList(waitingList);
//                orderGrabBean.setSum(waitingList.size());
//                orderGrabBean.setTeacherList(null);

                List<OrderManageInteractor.WaitingOrder> list = orderGrabBean.getOrderList();
                if (null != list && list.size()>0) {
                    activity.showNoTeacherOrders(list);
                    int defaultIndex = 0;
                    for (int i=0; i<list.size(); i++){
                        if (list.get(i).getId().equals(orderId)){
                            defaultIndex = i;
                            break;
                        }
                    }
                    OrderManageInteractor.WaitingOrder order = list.get(defaultIndex);
                    getInterestedTeachers(parentId, order.getId());
                    activity.showSelectedOrder(order);
                }else {
//                    activity.showTeachers(null);
                    activity.hideLoading();
                    activity.showError(ViewOverrideManager.NO_ORDER);
                }
            }

            @Override
            public void onLoadFailed(String msg) {
//                activity.showNoTeacherOrders(null);
                getInterestedTeachers(parentId, "");
                activity.hideLoading();
                activity.showError(ViewOverrideManager.NO_NETWORK);
            }
        });

    }


    @Override
    public void selectTeacher(final String orderId, final String orderType,String teacherId, String parentId) {
        teacherInteractor.selectTeacher(orderId, teacherId, parentId, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {
                activity.showLoading(null);
            }

            @Override
            public void onLoadSuccess(Object data) {

                activity.hideLoading();
                activity.goToSuccessPage(orderId,orderType);
            }

            @Override
            public void onLoadFailed(String msg) {
                activity.hideLoading();
                activity.showError(ViewOverrideManager.NO_NETWORK);
            }
        });
    }
}
