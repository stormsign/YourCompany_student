package com.miuhouse.yourcompany.student.presenter;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.db.DictDBTask;
import com.miuhouse.yourcompany.student.interactor.OrderInteractor;
import com.miuhouse.yourcompany.student.interactor.StudentInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IOrderInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IStudentInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.model.AddressBean;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.presenter.interf.IOrderConfirmPresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Values;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IOrderConfirmActivity;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by khb on 2016/8/29.
 */
public class OrderConfirmPresenter implements IOrderConfirmPresenter {
    private IOrderConfirmActivity activity;
    private IStudentInteractor studentInteractor;
    private IOrderInteractor orderInteractor;
    public ParentInfo parentInfo;

    public OrderConfirmPresenter(IOrderConfirmActivity activity) {
        this.activity = activity;
        this.studentInteractor = new StudentInteractor();
        this.orderInteractor = new OrderInteractor();

    }

    @Override
    public void getInfo(String parentId) {
        studentInteractor.getStudentInfo(parentId, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {
                activity.showLoading(null);
            }

            @Override
            public void onLoadSuccess(Object data) {
                activity.hideLoading();
                parentInfo = (ParentInfo) data;
                activity.showInfo(parentInfo);
            }

            @Override
            public void onLoadFailed(String msg) {
//                activity.hideLoading();
                L.i("error:" + msg);
                activity.showError(ViewOverrideManager.NO_NETWORK);
            }
        });
    }

    @Override
    public void getDefaultAddress(String parentId) {
        studentInteractor.getDefaultAddress(parentId, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onLoadSuccess(Object data) {
                if (null != data) {
                    AddressBean address = (AddressBean) data;
//                    activity.showAddress(address.getCity() + address.getDistrict() + address.getStreet(), address.getHouseNumber());
                    activity.showAddress(address);
                } else {
                    activity.showAddress(null);
                }
            }

            @Override
            public void onLoadFailed(String msg) {
                activity.showAddress(null);
            }
        });
    }

    @Override
    public void getPriceDetail() {

    }

    //    String orderType, String pname, String cname,
//    String lessonDay, long classEndTimePromise,
//    final OnLoadCallBack onLoadCallBack
//String orderType,String pname
    @Override
    public void confirmOrder(String parentId, EditText etDescription, String majorDemand,
                             TextView tvMinorDemand, TextView tvInterest, TextView tvStarLevel, long classBeginTimePromise,
                             String lesson, TextView tvMobile, double singlePrice, double extraPrice, double amount, String sex,
                             String province, String city, String district, String street,
                             String houseNumber, double lon, double lat) {

        String description = null;
        String minorDemand = null;
        String starLevel = null;
        String mobile = null;
        description = etDescription.getText().toString().trim();

        if (!majorDemand.equals("3")) {
            minorDemand = DictDBTask.getDcValue("subject_type", tvMinorDemand.getText().toString().trim()).toString();
        } else {
            minorDemand = DictDBTask.getDcValue("interest_type", tvInterest.getText().toString().trim()).toString();
        }
        starLevel = Values.getKey(Values.studentGrades, tvStarLevel.getText().toString().trim()) + "";
        mobile = tvMobile.getText().toString().trim();

        orderInteractor.confirmOrder(parentId, description, majorDemand, minorDemand, starLevel,
                classBeginTimePromise, lesson, mobile,
                singlePrice, extraPrice, amount, sex,
                province, city, district, street,
                houseNumber, lon, lat,
                new OnLoadCallBack() {
                    @Override
                    public void onPreLoad() {
                        activity.showLoading(null);
                    }

                    @Override
                    public void onLoadSuccess(Object data) {
                        activity.hideLoading();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject((String)data);
                            int code = jsonObject.getInt("code");
                            if (code == 0){
                                String orderId = jsonObject.getString("orderInfoId");
                                activity.goToPay(orderId);
                            }else if (code == 2){
                                Toast.makeText(App.getInstance(), "同一时间段内已经有课程了", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadFailed(String msg) {
                        activity.hideLoading();
                        activity.showError(ViewOverrideManager.NO_NETWORK);
                    }
                });
    }

}
