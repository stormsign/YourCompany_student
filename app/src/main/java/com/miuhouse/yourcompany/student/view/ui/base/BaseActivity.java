package com.miuhouse.yourcompany.student.view.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.ActivityManager;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.utils.ViewUtils;
import com.miuhouse.yourcompany.student.view.widget.MyProgressDialog;
import com.miuhouse.yourcompany.student.view.widget.MyTitlebar;
import com.miuhouse.yourcompany.student.view.widget.StatusCompat;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Created by khb on 2016/6/30.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView, MyTitlebar.OnMyTitlebarClickListener {
    public Context context;
    public Activity activity;
    private MyTitlebar myTitlebar;
    public ViewOverrideManager viewOverrideManager;
    private View baseView;
    private boolean isVisible;
    private ProgressDialog waitDialog;

    private final String packageName = this.getClass().getName();
    private MyProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        activity = this;
//        新建Activity时入栈
        ActivityManager.getInstance().pushActivity(activity);
        setContentView(getContentLayoutId());
//        initTitle();
        initViewAndEvents();
//        设置沉浸式消息栏

        StatusCompat.compat(this, getResources().getColor(R.color.themeColor));
//        由于4.4版本的状态栏是一个自定义view，因此在添加内容View，但又没有加标题栏时，内容View会覆盖状态栏
//        因此这里也要单独处理
//        没有加标题栏时，内容View可以设置fitSystemWindows
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
            baseView.setFitsSystemWindows(true);
            if (rootView.findViewById(R.id.titlebar) != null)
                baseView.setFitsSystemWindows(false);
        }
//        设置异常页面管理
        if (viewOverrideManager == null) {     //viewOverriderManager需要经常做非空判断，否则其内部对象很可能会抛空指针异常
            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
        }
        request();
        isVisible = true;

//        umeng analytics 测试版为true，正式版为false
        MobclickAgent.setDebugMode(false);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    @Override
    public void setContentView(int layoutResID) {

        baseView = View.inflate(context, R.layout.activity_base, null);
        View contentView = View.inflate(this, layoutResID, null);

        initTitle();

        FrameLayout flTitle = (FrameLayout) baseView.findViewById(R.id.titlePosition);
        if (null != myTitlebar) {
            flTitle.addView(myTitlebar);
        }
        FrameLayout flContent = (FrameLayout) baseView.findViewById(R.id.contentPosition);
        flContent.addView(contentView);
        setContentView(baseView);
    }

    /**
     * 添加标题栏，子类可覆盖，若子类覆写时没有做实现，则不显示标题栏
     */
    protected void initTitle() {

        myTitlebar = new MyTitlebar(this);
        int height = 0;
//        在调用StatusCompat后，4.4版本的状态栏不像4.4以下和5.0及其以上版本一样是固定在顶端，
//        而是变成一个view添加上去，因此要考虑状态栏的高度，
//        所以4.4要单独处理
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            height = Util.getStatusBarHeight(this) * 3;
        } else {
            height = Util.getStatusBarHeight(this) * 2;
        }

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                height);
        myTitlebar.setLayoutParams(params);
        myTitlebar.setOnMyTitlebarClickListener(this);
        myTitlebar.setTitle(setTitle());
        myTitlebar.setRightButtonText(setRight());

    }

    /**
     * @param rightText
     * @author pengjun on 07-05
     * 给Right TextView 随时设置文本
     */
    public void setRightButtonText(String rightText) {
        if (myTitlebar != null) {
            myTitlebar.setRightButtonText(rightText);
        }
    }

    public void setTitlebarBackground(int resource) {
        if (myTitlebar != null) {
            myTitlebar.setTitlebarBackground(resource);
        }
    }

    /**
     * 设置title文字颜色
     *
     * @param resource
     */
    public void setTitleTextColor(int resource) {
        if (myTitlebar != null) {
            myTitlebar.setTitleTextColor(resource);
        }
    }

    /**
     * 设置返回按钮图片
     *
     * @param resource
     */
    public void setBackImageResource(int resource) {
        if (myTitlebar != null) {
            myTitlebar.setBackImageResource(resource);
        }
    }

    /**
     * @param leftText
     * @author pengjun on 07-05
     * 给Right TextView 随时设置文本
     */
    public void setLeftText(String leftText) {
        if (myTitlebar != null) {
            myTitlebar.setTitle(leftText);
        }
    }

    /**
     * @param
     * @author pengjun on 07-05
     * 给Right TextView 随时设置文本
     */
    public void setRightText(String rightText) {
        if (myTitlebar != null) {
            myTitlebar.setTitle(rightText);
        }
    }


    protected abstract String setTitle();

    protected abstract String setRight();

    /**
     * 对页面操作
     */
    protected abstract void initViewAndEvents();

    /**
     * 获取要加载的页面
     *
     * @return
     */
    protected abstract int getContentLayoutId();

    /**
     * 获取一个ViewGroup，这个ViewGroup内将显示异常页面
     *
     * @return
     */
    protected abstract View getOverrideParentView();

    /**
     * 设置加载界面，子类可覆盖
     *
     * @param msg
     */
    @Override
    public void showLoading(String msg) {
//        if (null == viewOverrideManager) {
//            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
//        }
//        viewOverrideManager.showLoading();
        if (dialog == null) {
            dialog = new MyProgressDialog(ActivityManager.getInstance().getTopActivity());
        }
        dialog.init(ActivityManager.getInstance().getTopActivity());
        dialog.show();

    }

    /**
     * 设置错误页面，子类可覆盖
     *
     * @param type
     */
    @Override
    public void showError(int type) {
        if (dialog != null){
            dialog.dismiss();
        }
        if (null == viewOverrideManager) {
            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
        }
        viewOverrideManager.showLoading(type, null);

    }

    /**
     * 隐藏加载界面，子类可覆盖
     */
    @Override
    public void hideLoading() {
        if (null == viewOverrideManager) {
            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
        }
        viewOverrideManager.restoreView();
        if (dialog != null){
            dialog.dismiss();
        }
    }

    /**
     * 隐藏错误页面，子类可覆盖
     */
    @Override
    public void hideError() {
        if (dialog != null){
            dialog.dismiss();
        }
        if (null == viewOverrideManager) {
            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
        }
    }

    @Override
    public ProgressDialog showWaitDialog() {
        return showWaitDialog("加载中...");
    }

    @Override
    public ProgressDialog showWaitDialog(int resid) {
        return showWaitDialog(getString(resid));
    }

    @Override
    public ProgressDialog showWaitDialog(String message) {
        if (isVisible) {
            if (waitDialog == null) {
                waitDialog = ViewUtils.getWaitDialog(this, message);
            }
            if (waitDialog != null) {
                waitDialog.setMessage(message);
                waitDialog.show();
            }
            return waitDialog;
        }
        return null;
    }


    @Override
    public void hideWaitDialog() {
        if (isVisible && waitDialog != null) {
            try {
                waitDialog.dismiss();
                waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 网络错误页面，子类可覆盖
     */
    @Override
    public void netError() {
        if (dialog != null){
            dialog.dismiss();
        }
        if (null == viewOverrideManager) {
            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
        }
        showError(ViewOverrideManager.NO_NETWORK);
    }

    /**
     * 设置返回按钮，子类可覆盖
     */
    @Override
    public void onBackClick() {
        finish();
    }

    /**
     * 设置右键动作，子类可覆盖
     */
    @Override
    public void onRightClick() {

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(packageName);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(packageName);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //    退出Activity时退栈
    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().popActivity(activity);
        super.onDestroy();
    }

    public void request() {

    }

    @Override
    public void onTokenExpired() {
        if (dialog != null){
            dialog.dismiss();
        }
        L.i("TAG", "onTokenExpired");
//        Toast.makeText(context, "返回结果code == 1", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(this, LoginRegistActivity.class);
//        intent.putExtra("code", 1);
//        startActivity(intent);
//        finish();
    }
}
