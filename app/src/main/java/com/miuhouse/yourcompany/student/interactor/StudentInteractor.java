package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IStudentInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.model.AddressBean;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khb on 2016/8/30.
 */
public class StudentInteractor implements IStudentInteractor {

    public StudentInteractor(){

    }

    @Override
    public void getStudentInfo(String parentId, final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
        String url = Constants.URL_VALUE + "parentInfo";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        VolleyManager.getInstance().sendGsonRequest(null, url, params, SPUtils.getData(SPUtils.TOKEN, ""),
                ParentInfoBean.class, new Response.Listener<ParentInfoBean>() {
                    @Override
                    public void onResponse(ParentInfoBean response) {
                        if (response.getCode() == 0 && null != response.getParent()){
                            onLoadCallBack.onLoadSuccess(response.getParent());
                        }else {
                            onLoadCallBack.onLoadFailed(null);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLoadCallBack.onLoadFailed(error.toString());
                    }
                });
    }

    @Override
    public void getDefaultAddress(String parentId, final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
        String url = Constants.URL_VALUE + "addressDefault";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        VolleyManager.getInstance().sendGsonRequest(null, url, params, SPUtils.getData(SPUtils.TOKEN, ""),
                AddressInfoBean.class, new Response.Listener<AddressInfoBean>() {
                    @Override
                    public void onResponse(AddressInfoBean response) {
                        if (response.getCode() == 0){
                            onLoadCallBack.onLoadSuccess(response.getAddressInfo());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLoadCallBack.onLoadFailed(null);
                    }
                });
    }

    class ParentInfoBean extends BaseBean{
        ParentInfo parent;

        public ParentInfo getParent() {
            return parent;
        }

        public void setParent(ParentInfo parent) {
            this.parent = parent;
        }
    }

    class AddressInfoBean extends BaseBean{
        AddressBean addressInfo;

        public AddressBean getAddressInfo() {
            return addressInfo;
        }

        public void setAddressInfo(AddressBean addressInfo) {
            this.addressInfo = addressInfo;
        }
    }


}
