package com.miuhouse.yourcompany.student.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 8/5/2016.
 */
public class AddressListBean extends BaseBean implements Serializable {
    private List<AddressBean> addressList = new ArrayList<>();

    public List<AddressBean> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressBean> addressList) {
        this.addressList = addressList;
    }
}
