package com.miuhouse.yourcompany.student.presenter;

import com.miuhouse.yourcompany.student.presenter.interf.IMessagePresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.view.ui.fragment.interf.IMessagesFragment;

/**
 * Created by khb on 2016/9/30.
 */
public class MessagePresenter implements IMessagePresenter {
    private IMessagesFragment messagesFragment;
    public MessagePresenter(IMessagesFragment messagesFragment) {
        this.messagesFragment = messagesFragment;
    }

    @Override
    public void getTopMsgs() {
        int sysCount = SPUtils.getData(Constants.UNREAD_SYSMSG_COUNT, 0);
        String latestSysMsg = SPUtils.getData(Constants.LATEST_SYSMSG, null);
        int purseCount = SPUtils.getData(Constants.UNREAD_ORDERMSG_COUNT, 0);
        String latestPurseMsg = SPUtils.getData(Constants.LATEST_ORDERMSG, null);
        messagesFragment.setTop(sysCount, latestSysMsg, purseCount, latestPurseMsg);
    }
}
