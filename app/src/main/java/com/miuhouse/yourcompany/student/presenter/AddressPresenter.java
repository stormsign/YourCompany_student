package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.AddressListInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IAddressInteractor;
import com.miuhouse.yourcompany.student.model.AddressListBean;
import com.miuhouse.yourcompany.student.presenter.interf.IAddressPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IAddressView;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

/**
 * Created by kings on 8/5/2016.
 */
public class AddressPresenter implements IAddressPresenter {

    private IAddressInteractor addressInteractor;
    private IAddressView addressView;
    public AddressPresenter(IAddressView addressView) {
        this.addressView = addressView;
        addressInteractor = new AddressListInteractor();
    }

    @Override
    public void getAddressList() {
        addressView.showLoading("正在加载中...");
        addressInteractor.getAddress(new Response.Listener<AddressListBean>() {
            @Override
            public void onResponse(AddressListBean response) {
                addressView.hideLoading();
                if (response.getCode() == 1) {
                    addressView.onTokenExpired();
                } else {
                    if (response.getAddressList().size() == 0) {
                        addressView.showError(ViewOverrideManager.NO_ADDRESS);
                        return;
                    }
                    addressView.result(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                addressView.hideLoading();
                addressView.showError(ViewOverrideManager.NO_NETWORK);
            }
        });
    }
}
