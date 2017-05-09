package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.DeleteTwitterInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IDeleteTwitterInteractor;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.presenter.interf.IDeleteTwitterPresenter;
import com.miuhouse.yourcompany.student.view.ui.fragment.DongTaiFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.DongTaiFragment;
import mabeijianxi.camera.util.Log;

/**
 * Created by kings on 2/20/2017.
 */
public class DeleteTwitterPresenter implements IDeleteTwitterPresenter {

    private IDeleteTwitterInteractor deleteTwitterInteractor;
    private DongTaiFragment deleteTeacherView;

    public DeleteTwitterPresenter(DongTaiFragment fragment) {
        deleteTeacherView = fragment;
        deleteTwitterInteractor = new DeleteTwitterInteractor();
    }

    @Override public void deleteTwitter(String schoolTeacherId, String id, final int position) {
        deleteTeacherView.showDeleteDialog();
        deleteTwitterInteractor.deleteTwitter(new Response.Listener<BaseBean>() {
            @Override public void onResponse(BaseBean response) {
                Log.i("TAG", "response=" + response.getMsg());
                deleteTeacherView.hideDeletDialog();
                if (response.getCode() == 0) {
                    deleteTeacherView.showDelete(DongTaiFragment.RESULT_SUCESS, position,
                        response.getMsg());
                } else {
                    deleteTeacherView.showDelete(DongTaiFragment.RESULT_ERROR, position,
                        response.getMsg());
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                deleteTeacherView.hideDeletDialog();
                deleteTeacherView.showDelete(DongTaiFragment.RESULT_ERROR, position, "删除失败");
            }
        }, schoolTeacherId, id);
    }
}
