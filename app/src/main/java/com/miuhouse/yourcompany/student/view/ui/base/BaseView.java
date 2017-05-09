package com.miuhouse.yourcompany.student.view.ui.base;

import android.app.ProgressDialog;

/**
 * Created by khb on 2016/6/30.
 */
public interface BaseView {
    void showLoading(String msg);
    void showError(int type);
    void hideLoading();
    void hideError();
    void netError();

    // ProgressDialog
    abstract ProgressDialog showWaitDialog();

    abstract ProgressDialog showWaitDialog(int msg);

    abstract ProgressDialog showWaitDialog(String msg);

    void hideWaitDialog();
    //参数错误
    void onTokenExpired();
}
