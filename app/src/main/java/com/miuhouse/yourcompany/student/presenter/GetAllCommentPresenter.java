package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.AllCommentInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IGetAllCommentInteractor;
import com.miuhouse.yourcompany.student.model.EvaluateListBean;
import com.miuhouse.yourcompany.student.presenter.interf.IGetAllCommentPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IGetAllCommentView;

/**
 * Created by kings on 8/25/2016.
 */
public class GetAllCommentPresenter implements IGetAllCommentPresenter {

    private IGetAllCommentInteractor getAllCommentInteractor;
    private IGetAllCommentView getAllCommentView;

    public GetAllCommentPresenter(IGetAllCommentView getAllCommentView) {
        this.getAllCommentView = getAllCommentView;
        getAllCommentInteractor = new AllCommentInteractor();
    }

    @Override
    public void getAllComment(String teacherId, int page) {
        getAllCommentInteractor.getAllComment(teacherId, page, new Response.Listener<EvaluateListBean>() {
            @Override
            public void onResponse(EvaluateListBean response) {
                getAllCommentView.result(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
