package com.miuhouse.yourcompany.student.presenter.interf;

import com.miuhouse.yourcompany.student.model.AddressBean;

/**
 * Created by kings on 8/4/2016.
 */
public interface IUpdateAddPresenter {
    void updateAddress(AddressBean address);

    void delAddress(String addressInfoId);
}
