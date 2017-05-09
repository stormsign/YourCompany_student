package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.TwitterInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.ITwitterInteractor;
import com.miuhouse.yourcompany.student.model.SchoolNewsListBean;
import com.miuhouse.yourcompany.student.presenter.interf.ITwitterPresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.view.ui.fragment.DongTaiFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.interf.ITwitterFragment;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;
import mabeijianxi.camera.util.Log;

/**
 * Created by kings on 12/28/2016.
 */
public class TwitterPresenter implements ITwitterPresenter {
    private ITwitterInteractor twitterInteractor;
    private ITwitterFragment twitterFragment;

    public TwitterPresenter(ITwitterFragment twitterFragment) {
        twitterInteractor = new TwitterInteractor();
        this.twitterFragment = twitterFragment;
    }

    @Override
    public void getSchoolTwitterList(String schoolTeacherId, String schoolId, String contentType,
        long classes, final int page, int pageSize) {
        twitterFragment.showLoading(null);
        twitterInteractor.getSchoolNewsList(new Response.Listener<SchoolNewsListBean>() {
            @Override public void onResponse(SchoolNewsListBean response) {
                twitterFragment.hideLoading();
                if (response.getCode() == 0) {
                    if (page == 1 && (response.getSchoolNewsInfos() == null
                        || response.getSchoolNewsInfos().size() == 0)) {
                        twitterFragment.showError(ViewOverrideManager.NO_CONTENT);
                    } else {
                        twitterFragment.getSchoolNewListBean(response);
                    }
                } else {
                    twitterFragment.showError(DongTaiFragment.ERROR);
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                twitterFragment.hideLoading();
                twitterFragment.showError(DongTaiFragment.ERROR);
            }
        }, schoolTeacherId, schoolId, contentType, classes, page, pageSize);
    }

    @Override
    public void getSchoolTwitterList(String schoolTeacherId, String schoolId, String contentType,
        String linkId, final int page, int pageSize) {
        twitterFragment.showLoading(null);
        twitterInteractor.getSchoolNewsList(new Response.Listener<SchoolNewsListBean>() {
            @Override public void onResponse(SchoolNewsListBean response) {
                twitterFragment.hideLoading();
                if (response.getCode() == 0) {
                    if (page == 1 && (response.getSchoolNewsInfos() == null
                        || response.getSchoolNewsInfos().size() == 0)) {
                        twitterFragment.showError(ViewOverrideManager.NO_CONTENT);
                    } else {
                        twitterFragment.getSchoolNewListBean(response);
                    }
                } else {
                    twitterFragment.showError(DongTaiFragment.ERROR);
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                twitterFragment.hideLoading();
                twitterFragment.showError(DongTaiFragment.ERROR);
            }
        }, schoolTeacherId, schoolId, contentType, linkId, page, pageSize);
    }

    //    不同类别账号进入页面时对其权限控制
    @Override
    public void authorizationControll() {

    }
}
