package com.miuhouse.yourcompany.student.presenter.interf;

/**
 * Created by kings on 7/1/2016.
 */
public interface ILoginPresenter {
    void clear();
    void doLogin(String name, String passwd);
    void doRegist(int typeMark, String name, String password);
}
