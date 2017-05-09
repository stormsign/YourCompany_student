package com.miuhouse.yourcompany.student.interactor.interf;

import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;

/**
 * Created by khb on 2016/9/18.
 */
public interface IOrderRefundInteractor {

    void submitRefundRequest(String orderId, String reason, String remark,
                             OnLoadCallBack onLoadCallBack);
}
