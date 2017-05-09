package com.miuhouse.yourcompany.student.presenter;

import com.miuhouse.yourcompany.student.interactor.SysMsgInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.ISysMsgInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.presenter.interf.ISysMsgPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ISysMsgActivity;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

/**
 * Created by khb on 2016/7/14.
 */
public class SysMsgPresenter implements ISysMsgPresenter {
    private ISysMsgActivity activity;
    private ISysMsgInteractor interactor;

    public SysMsgPresenter(ISysMsgActivity activity) {
        this.activity = activity;
        this.interactor = new SysMsgInteractor();
    }

    @Override
    public void getMsgs(final int page) {
        interactor.getMsgs(page, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {
                activity.showLoading(null);
            }

            @Override
            public void onLoadSuccess(Object data) {
                activity.hideLoading();
                //        重置新通知数
                SPUtils.saveData(Constants.UNREAD_SYSMSG_COUNT, 0);
                SysMsgInteractor.SysMsgBean bean = (SysMsgInteractor.SysMsgBean) data;
                if (null != bean
                        && null != bean.getNoticeList()
                        && bean.getNoticeList().size() > 0) {
                    activity.refresh(bean);
                } else {
                    if (page == 1)
                        activity.showError(ViewOverrideManager.NO_MSG);
                }
            }

            @Override
            public void onLoadFailed(String msg) {
                activity.showError(ViewOverrideManager.NO_NETWORK);
            }
        });
    }

}
