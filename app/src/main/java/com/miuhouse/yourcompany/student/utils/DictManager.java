package com.miuhouse.yourcompany.student.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.db.DictDBTask;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.model.DictBean;
import com.miuhouse.yourcompany.student.model.DictListBean;
import com.miuhouse.yourcompany.student.view.ui.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 7/19/2016.
 *
 * 当返回的内容为空时，字典表不更新，当返回的内容不为空，更新字典表内容和版本号
 */
public class DictManager {


    private DictManager() {

    }

    private static class DictManagerBuilder {
        private static DictManager instance = new DictManager();
    }

    public static DictManager getInstance() {
        return DictManagerBuilder.instance;
    }

    public void init(final MainActivity context) {
        String url = Constants.URL_VALUE + "dict";
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", App.getInstance().getParentId());
        //字典版本
        map.put("dictVersion", SPUtils.getData(SPUtils.DICT_VERSION, 0));
        VolleyManager.getInstance().sendGsonRequest(null, url, map, SPUtils.getData(SPUtils.TOKEN, null), DictListBean.class, new Response.Listener<DictListBean>() {
            @Override
            public void onResponse(DictListBean response) {

                int dictVersion = 0;
                if (response.getCode() == 1) {
                    context.onTokenExpired();
                } else {
                    if (response.getDictionaries() != null && response.getDictionaries().size() > 0) {
                        for (DictBean dictBean : response.getDictionaries()) {
                            if (dictBean.getDcType().equals("dict_version")) {
                                dictVersion = Integer.parseInt(dictBean.getDcValue());
                            }

                        }
                        SPUtils.saveData(SPUtils.DICT_VERSION, dictVersion);
                        DictDBTask.add(response);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //初始化失败，继续使用原先保存的
            }
        });

    }
}
