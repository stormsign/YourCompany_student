package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.DeleteCommentInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IDeleteCommentInteractor;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.presenter.interf.IDeleteCommentPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.DetailTwitterActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IDeleteCommentView;

/**
 * Created by kings on 2/23/2017.
 */
public class DeleteCommentPresenter implements IDeleteCommentPresenter {

    private IDeleteCommentInteractor deleteCommentInteractor;
    private IDeleteCommentView deleteCommentView;

    public DeleteCommentPresenter(IDeleteCommentView deleteCommentView) {
        this.deleteCommentView = deleteCommentView;
        deleteCommentInteractor = new DeleteCommentInteractor();
    }

    @Override public void deleteComment(String schoolTeacherId, String id, final int position) {
        deleteCommentView.showLoading(null);
        deleteCommentInteractor.deleteComment(new Response.Listener<BaseBean>() {
            @Override public void onResponse(BaseBean response) {
                deleteCommentView.hideLoading();
                if (response.getCode() == 0) {
                    deleteCommentView.deleteResult(DetailTwitterActivity.RESULT_SUCESS,
                        response.getMsg(), position);
                } else {
                    deleteCommentView.deleteResult(DetailTwitterActivity.RESULT_ERROR,
                        response.getMsg(), position);
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                deleteCommentView.hideLoading();
                deleteCommentView.deleteResult(
                    DetailTwitterActivity.RESULT_ERROR, "网络错误",
                    position);
            }
        }, schoolTeacherId, id);
    }
}
