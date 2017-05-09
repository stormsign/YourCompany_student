package com.miuhouse.yourcompany.student.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.interactor.UpdateAddressInfo;
import com.miuhouse.yourcompany.student.interactor.interf.IUpdateAddressInteractor;
import com.miuhouse.yourcompany.student.model.AddressBean;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.presenter.interf.IUpdateAddPresenter;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IUpdateAddressView;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

/**
 * Created by kings on 8/4/2016.
 */
public class UpdateAddressPresenter implements IUpdateAddPresenter {

    private IUpdateAddressInteractor updateAddressInter;
    private IUpdateAddressView updateAddressView;

    public UpdateAddressPresenter(IUpdateAddressView updateAddressView) {
        updateAddressInter = new UpdateAddressInfo();
        this.updateAddressView = updateAddressView;
    }

    @Override
    public void updateAddress(AddressBean address) {
        updateAddressView.showWaitDialog("保存中...");
        updateAddressInter.addAddress(address, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                updateAddressView.hideWaitDialog();
                if (response.getCode() == 1) {
                    updateAddressView.onTokenExpired();
                } else {
                    updateAddressView.UpdateSuccess(response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                updateAddressView.hideWaitDialog();
                updateAddressView.hideError();
            }
        });
    }

    @Override
    public void delAddress(String addressInfoId) {
        updateAddressInter.delAddress(addressInfoId, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (response.getCode() == 1) {
                    updateAddressView.onTokenExpired();
                } else {
                    updateAddressView.delAddressSuccess(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                updateAddressView.showError(ViewOverrideManager.NO_NETWORK);
            }
        });
    }
}
