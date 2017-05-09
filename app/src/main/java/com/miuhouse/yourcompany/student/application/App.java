package com.miuhouse.yourcompany.student.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by khb on 2016/5/13.
 */
public class App extends Application {

    public static Context applicationContext;
    private boolean login;
    private String parentId;
    private static App instance;
    public ParentInfo parentInfo;

    //private SchoolTeacherInfo schoolTeacherInfo;
    {
        PlatformConfig.setQQZone("1105855086", "XYnl3pPCaRg84REL");
        PlatformConfig.setWeixin("wx7c50fb297e39bd15","4ecc938708eca89d418c368c5335fa3e");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.applicationContext = getApplicationContext();
        instance = this;
        UMShareAPI.get(this);
        //        Constants.IMEI_VALUE = Util.getIMEI(this);
        //        Constants.IMEI_VALUE = "863175020757478";
        initLogin();
    }

    private void initLogin() {
        ParentInfo info = AccountDBTask.getAccount();
        if (info != null) {
            login = true;
            parentId = info.getId();
        } else {
            login = false;
        }
    }

    public static Context getContext() {
        return applicationContext;
    }

    public static App getInstance() {
        return instance;
    }

    public boolean isLogin() {

        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
        if (login) {
            parentId = null;
        }
    }

    public String getParentId() {
        if (parentId == null) {
            parentId = AccountDBTask.getAccount().getId();
        }
        Log.i("TAG", "parentId=" + parentId);
        return parentId;
    }

    public void logout() {
        SPUtils.saveData(SPUtils.TOKEN, null);
        AccountDBTask.clear();
        parentId = null;
        login = false;
    }

    public ParentInfo getParentInfo() {
        if (parentInfo == null) {
            parentInfo = AccountDBTask.getAccount();
        }
        return parentInfo;
    }

    public void setParentInfo(ParentInfo parentInfo) {
        this.parentInfo = parentInfo;
    }

    //public SchoolTeacherInfo getSchoolTeacherInfo() {
    //    return schoolTeacherInfo;
    //}
    //
    //public void setSchoolTeacherInfo(
    //        SchoolTeacherInfo schoolTeacherInfo) {
    //    this.schoolTeacherInfo = schoolTeacherInfo;
    //}
}
