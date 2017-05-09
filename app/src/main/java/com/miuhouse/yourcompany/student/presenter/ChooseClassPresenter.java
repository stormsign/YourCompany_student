package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.ClassInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IClass;
import com.miuhouse.yourcompany.student.model.ClassEntity;
import com.miuhouse.yourcompany.student.presenter.interf.IChooseClassPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IChooseClass;

import java.util.List;

/**
 * Created by khb on 2017/1/10.
 */
public class ChooseClassPresenter implements IChooseClassPresenter {
    private IChooseClass activity;
    private IClass interactor;

    public ChooseClassPresenter(IChooseClass activity) {
        this.activity = activity;
        interactor = new ClassInteractor();
    }

    @Override
    public void getAllClasses(String schoolTeacherId, String schoolId) {
        activity.showLoading(null);
        interactor.getAllClasses(schoolTeacherId, schoolId,
            new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    activity.hideLoading();
                    ClassInteractor.ClassListBean data = (ClassInteractor.ClassListBean) response;
                    List<ClassEntity> classList = data.getSchoolClasses();
                    if (data.getCode() == 0) {
                        if (classList != null && classList.size() > 0) {
                            activity.showChoices(classList);
                        } else {
                            activity.showError(1);
                        }
                    } else {
                        activity.showError(0);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    activity.hideLoading();
                    activity.showError(0);
                }
            });
    }

    @Override public void getAllClasses(String schoolTeacherId) {
        getAllClasses(schoolTeacherId, null);
    }
}
