package com.miuhouse.yourcompany.student.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.interactor.StudentInteractor;
import com.miuhouse.yourcompany.student.interactor.TeacherInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.IStudentInteractor;
import com.miuhouse.yourcompany.student.interactor.interf.ITeacherInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.presenter.interf.IMapPresenter;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.view.ui.fragment.MapFragment;
import com.miuhouse.yourcompany.student.view.ui.fragment.interf.IHomeFragment;

/**
 * Created by khb on 2016/8/22.
 */
public class MapPresenter implements IMapPresenter {
    private IHomeFragment homeFragment;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationClientOption;

    private ITeacherInteractor teacherInteractor;

    public MapPresenter(IHomeFragment homeFragment) {
        this.homeFragment = homeFragment;
        teacherInteractor = new TeacherInteractor();
    }


    @Override
    public void getCurrentLocation() {
        locationClient = new AMapLocationClient(App.getContext());
        locationClientOption = new AMapLocationClientOption();
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationClientOption.setOnceLocation(true);
        locationClient.setLocationOption(locationClientOption);
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                L.i("location code:" + aMapLocation.getErrorCode());
                L.i("currentlocation:" + aMapLocation.getAoiName() +
                        "  " + aMapLocation.getLatitude() + "-" + aMapLocation.getLongitude());
                homeFragment.showPosition(aMapLocation);
            }
        });
        locationClient.startLocation();
    }

    @Override
    public void getTeachers(String parentId, LatLng latlng, String pbxType) {
        teacherInteractor.getNearbyTeachers(parentId, latlng, pbxType, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onLoadSuccess(Object data) {
                TeacherInteractor.TeacherInfoListBean teacherInfo = (TeacherInteractor.TeacherInfoListBean) data;
//                if (null != teacherInfo
//                        && null != teacherInfo.getTeacherList()
//                        && teacherInfo.getTeacherList().size() > 0) {
                homeFragment.showTeachers(teacherInfo.getTeacherList());
//                } else {

//                }
            }

            @Override
            public void onLoadFailed(String msg) {

            }
        });
    }

    @Override
    public void getStudentInfo(String parentId, final int orderType) {
        IStudentInteractor studentInteractor = new StudentInteractor();
        studentInteractor.getStudentInfo(parentId, new OnLoadCallBack() {
            @Override
            public void onPreLoad() {
//                activity.showLoading(null);
            }

            @Override
            public void onLoadSuccess(Object data) {
//                homeFragment.hideLoading();
                ParentInfo parentInfo = (ParentInfo) data;
//                homeFragment.showInfo(parentInfo);
                if (checkPropfileIsComplete(parentInfo)) {
                    if (orderType == MapFragment.ORDER_TYPE_SHORT) {
                        homeFragment.goToOrderConfirm(parentInfo);
                    } else if (orderType == MapFragment.ORDER_TYPE_LONG) {
                        homeFragment.goToLongOrderConfirm(parentInfo);
                    }

                } else {
                    homeFragment.showProfileNotComplete(parentInfo);
                }
            }

            @Override
            public void onLoadFailed(String msg) {
//                activity.hideLoading();
                L.i("error:" + msg);
//                homeFragment.showError(ViewOverrideManager.NO_NETWORK);
                Toast.makeText(App.getContext(), "服务器错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkPropfileIsComplete(ParentInfo parentInfo) {
        boolean isProfileComplete = true;
//        if (TextUtils.isEmpty(parentInfo.getSubject())){
//            isProfileComplete = false;
//        }
        if (TextUtils.isEmpty(parentInfo.getpName())) {
            isProfileComplete = false;
        }
        if (TextUtils.isEmpty(parentInfo.getcName())) {
            isProfileComplete = false;
        }
        if (TextUtils.isEmpty(parentInfo.getGrade())) {
            isProfileComplete = false;
        }
        if (TextUtils.isEmpty(parentInfo.getMobile())) {
            isProfileComplete = false;
        }
        return isProfileComplete;
    }
}
