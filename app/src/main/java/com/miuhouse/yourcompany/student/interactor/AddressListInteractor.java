package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IAddressInteractor;
import com.miuhouse.yourcompany.student.model.AddressListBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 8/5/2016.
 */
public class AddressListInteractor implements IAddressInteractor {
    @Override
    public void getAddress(Response.Listener<AddressListBean> listener, Response.ErrorListener errorListener) {
        String urlPath = Constants.URL_VALUE + "addressList";
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", App.getInstance().getParentId());
        VolleyManager.getInstance().sendGsonRequest(null, urlPath, map, SPUtils.getData(SPUtils.TOKEN, ""), AddressListBean.class, listener, errorListener);
    }
}
