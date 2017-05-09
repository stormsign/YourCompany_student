package com.miuhouse.yourcompany.student.presenter;


import com.miuhouse.yourcompany.student.interactor.OrderMsgInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IOrderMsgInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderMsgPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderMsgActivity;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

/**
 * Created by khb on 2016/7/26.
 */
public class OrderMsgPresenter implements IOrderMsgPresenter {
    private IOrderMsgActivity activity;
    private IOrderMsgInteractor interactor;
    public OrderMsgPresenter(IOrderMsgActivity activity) {
        this.activity = activity;
        this.interactor = new OrderMsgInteractor(this);

    }


    @Override
    public void getOrderMsgs(String teacherId, final int page) {
        interactor.getPurseMsgs(teacherId, page, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {
                activity.showLoading(null);
            }

            @Override
            public void onLoadSuccess(Object data) {
                activity.hideLoading();
                //        重置新通知数
                SPUtils.saveData(Constants.UNREAD_ORDERMSG_COUNT, 0);
                if (null != data){
                   OrderMsgInteractor.OrderMsgListBean bean = (OrderMsgInteractor.OrderMsgListBean) data;
                    if (null != bean.getOrderMsgs()
                            && bean.getOrderMsgs().size()>0){
                        activity.refresh(bean.getOrderMsgs());
                    }else {
                        if (page == 1)
                            activity.showError(ViewOverrideManager.NO_MSG);
                    }
                }else{
                    activity.showError(ViewOverrideManager.NO_NETWORK);
                }

            }

            @Override
            public void onLoadFailed(String msg) {
                activity.hideLoading();
                activity.showError(ViewOverrideManager.NO_NETWORK);
            }
        });
    }
}
