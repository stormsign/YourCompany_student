package com.miuhouse.yourcompany.student.presenter.interf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 7/13/2016.
 */
public interface IUserInformationPresenter {
    void doChangeUserInformation(String pName, String cName, String mobile,String headUrl);

    void getUserInfo(String teacherId);

    void updatePhone(String teacherId, String mobile);
}
