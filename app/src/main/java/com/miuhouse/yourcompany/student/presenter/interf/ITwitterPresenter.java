package com.miuhouse.yourcompany.student.presenter.interf;

/**
 * Created by kings on 12/28/2016.
 */
public interface ITwitterPresenter {
    void getSchoolTwitterList(String schoolTeacherId, String schoolId, String contentType,
        long classes,
        int page, int pageSize);

    /**
     * contentType	String	"1";// 动态 "2";// 食谱 "3";// 通知
     */
    void getSchoolTwitterList(String schoolTeacherId, String schoolId, String contentType,
        String linkId,
        int page, int pageSize);

    void authorizationControll();
}
