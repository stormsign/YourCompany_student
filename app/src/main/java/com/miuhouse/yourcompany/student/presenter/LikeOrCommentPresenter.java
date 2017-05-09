package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.LikeOrComment;
import com.miuhouse.yourcompany.student.interactor.interf.ILikeOrComment;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.CommentsBean;
import com.miuhouse.yourcompany.student.presenter.interf.ILikeOrCommentPresenter;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.DetailTwitterActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ICommentingView;

/**
 * Created by kings on 12/30/2016.
 */
public class LikeOrCommentPresenter implements ILikeOrCommentPresenter {

    private ILikeOrComment likeOrCommentInteractor;
    private ICommentingView commentingView;

    public LikeOrCommentPresenter(ICommentingView commentingView) {
        likeOrCommentInteractor = new LikeOrComment();
        this.commentingView = commentingView;
    }

    @Override public void getLikeOrComment(String fId, String schoolTeacherId, String newsId,
        String commentType, String linkId, String linkType, String linkName, String linkedId,
        String linkedType, String linkedName, String comment, final CommentsBean mCommentsBean) {

        if (Util.isEmpty(comment) && commentType.equals("1")) {
            commentingView.showError(DetailTwitterActivity.RESULT_NO_PERFECT);
            return;
        }
        commentingView.showLoading(null);

        likeOrCommentInteractor.getLikeOrComment(new Response.Listener<BaseBean>() {
                                                     @Override
                                                     public void onResponse(BaseBean response) {
                                                         commentingView.hideLoading();
                                                         if (response.getCode() == 1) {
                                                             commentingView.onTokenExpired();
                                                         } else if (response.getCode() == 0) {
                                                             commentingView.showError(
                                                                 DetailTwitterActivity.RESULT_SUCESS,
                                                                 mCommentsBean);
                                                         } else {
                                                             commentingView.showError(
                                                                 DetailTwitterActivity.RESULT_ERROR,
                                                                 mCommentsBean);
                                                         }
                                                     }
                                                 }, new Response.ErrorListener() {
                                                     @Override
                                                     public void onErrorResponse(
                                                         VolleyError error) {
                                                         commentingView.hideLoading();
                                                         commentingView.showError(
                                                             DetailTwitterActivity.RESULT_ERROR,
                                                             mCommentsBean);
                                                     }
                                                 }, fId, schoolTeacherId, newsId, commentType,
            linkId,
            linkType, linkName, linkedId, linkedType,
            linkedName, comment);
    }

    @Override public void getLikeOrComment(String schoolTeacherId, String newsId,
        String commentType, String linkId, String linkType, String linkName) {
        getLikeOrComment(null, schoolTeacherId, newsId, commentType, linkId, linkType, linkName,
            null,
            null, null, null, null);
    }

    @Override public void cancelLike(String schoolTeacherId, String schoolNewsId) {
        likeOrCommentInteractor.cancelLike(new Response.Listener<BaseBean>() {
            @Override public void onResponse(BaseBean response) {

            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {

            }
        }, schoolTeacherId, schoolNewsId);
    }
}
