package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.ISysMsgInteractor;
import com.miuhouse.yourcompany.student.listener.OnLoadCallBack;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.SysMsgEntity;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/7/14.
 */
public class SysMsgInteractor implements ISysMsgInteractor {
//    private OnLoadCallBack onLoadCallBack;
    public SysMsgInteractor() {

    }

    @Override
    public void getMsgs(int page, final OnLoadCallBack onLoadCallBack) {
        onLoadCallBack.onPreLoad();
        String url = Constants.URL_VALUE + "noticeMsg";
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", AccountDBTask.getAccount().getId());
        params.put("page", page);
        params.put("pageSize", 15);
        VolleyManager.getInstance().sendGsonRequest(null, url, params,
                SPUtils.getData(SPUtils.TOKEN, null),
                SysMsgBean.class,
                new Response.Listener<SysMsgBean>() {
                    @Override
                    public void onResponse(SysMsgBean response) {
                        onLoadCallBack.onLoadSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLoadCallBack.onLoadFailed(error.toString());
                    }
                });
    }

    public class SysMsgBean extends BaseBean {
        List<SysMsgEntity> noticeList;

        public List<SysMsgEntity> getNoticeList() {
            return noticeList;
        }

        public void setNoticeList(List<SysMsgEntity> noticeList) {
            this.noticeList = noticeList;
        }
    }
}
