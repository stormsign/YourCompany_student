package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.MsgInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IMsgInteractor;
import com.miuhouse.yourcompany.student.model.CommentsBean;
import com.miuhouse.yourcompany.student.presenter.interf.IUnreadMsgPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.UnreadMsgActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IUnreadMsg;

import java.util.List;

/**
 * Created by khb on 2017/1/17.
 */
public class UnreadMsgPresenter implements IUnreadMsgPresenter {

    private IUnreadMsg activity;
    private IMsgInteractor interactor;

    public UnreadMsgPresenter(IUnreadMsg activity) {
        this.activity = activity;
        interactor = new MsgInteractor();
    }

    @Override
    public void getUnreadMsg(String schoolTeacherId, int page) {
        activity.showLoading(null);
        interactor.getUnreadMsgs(schoolTeacherId, page,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        MsgInteractor.CommentsBeanList list = (MsgInteractor.CommentsBeanList) response;
                        if (list.getCode() == 0){
                            List<CommentsBean> commentList =  list.getSchoolComments();
                            if (commentList!=null && commentList.size()>0){
                                activity.showUnreaedList(commentList);
                            }else {
                                activity.showError(UnreadMsgActivity.RESULT_EMPTY);
                            }
                        }else {
                            activity.showError(UnreadMsgActivity.RESULT_ERROR);
                        }
                        activity.hideLoading();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.hideLoading();
                        activity.showError(UnreadMsgActivity.RESULT_ERROR);
                        error.printStackTrace();
                    }
                });
    }
}
