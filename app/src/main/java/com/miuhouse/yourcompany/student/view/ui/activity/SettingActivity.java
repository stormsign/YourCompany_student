package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Intent;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.ActivityManager;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 7/26/2016.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private String phone;

    @Override
    protected String setTitle() {
        return "设置";
    }

    @Override
    protected String setRight() {
        return null;
    }

    @Override
    protected void initViewAndEvents() {

        phone = getIntent().getStringExtra("phone");

        //        findViewById(R.id.relative_replace_phone).setOnClickListener(this);
        findViewById(R.id.relative_about).setOnClickListener(this);
        findViewById(R.id.relative_logout).setOnClickListener(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.relative_about:
                Intent intentAbout = new Intent(this, BrowserActivity.class);
                intentAbout.putExtra(BrowserActivity.BROWSER_KEY,
                    Constants.URL_HEAD + "/mobile/aboutM");
                intentAbout.putExtra("title", "关于e学陪");
                startActivity(intentAbout);
                break;
            case R.id.relative_logout:
                String urlPath = Constants.URL_VALUE + "logout";
                Map map = new HashMap();
                map.put("parentId", AccountDBTask.getAccount().getId());
                VolleyManager.getInstance()
                    .sendGsonRequest(null, urlPath, map, SPUtils.getData(SPUtils.TOKEN, null),
                        BaseBean.class, new Response.Listener<BaseBean>() {
                            @Override
                            public void onResponse(BaseBean response) {
                                App.getInstance().logout();
                                ActivityManager.getInstance().finishAll();
                                startActivity(
                                    new Intent(SettingActivity.this, LoginRegistActivity.class));
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                break;
        }
    }
}
