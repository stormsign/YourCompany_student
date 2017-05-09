package com.miuhouse.yourcompany.student.view.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miuhouse.yourcompany.student.utils.ViewUtils;
import com.miuhouse.yourcompany.student.view.ui.activity.LoginRegistActivity;
import com.miuhouse.yourcompany.student.view.widget.MyProgressDialog;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;

/**
 * Created by khb on 2016/7/6.
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    public Context context;
    public static final int MESSAGES = 0;
    public static final int MAP = 1;

    public static final int ACCOUNT = 2;
    public static final int DONGTAI = -1;
    public static final int KZ = -2;
    public static final int ACCOUNT2 = -3;
    public static final int ORDERSSQUARE = 3;
    public static final int MYORDERS = 4;
    public static final int A = 5;
    public static final int B = 6;
    public static final int C = 7;
    public static final int D = 8;
    public ViewOverrideManager viewOverrideManager;
    private boolean tag = true;
    private ProgressDialog waitDialog;
    private View view;

    private MyProgressDialog dialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getFragmentResourceId() != 0) {
            view = inflater.inflate(getFragmentResourceId(), null);
            getViews(view);
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        //        设置异常页面管理
        viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
    }

    public abstract int getFragmentResourceId();

    public abstract void getViews(View view);

    public abstract void setupView();

    public abstract View getOverrideParentView();


    @Override
    public void showLoading(String msg) {
//        if (null == viewOverrideManager) {
//            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
//        }
//        viewOverrideManager.showLoading();
        dialog = new MyProgressDialog(context);
        dialog.init(context);
        dialog.show();
    }

    @Override
    public void showError(int type) {
        if (dialog != null){
            dialog.dismiss();
        }
        if (null == viewOverrideManager) {
            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
        }
//        viewOverrideManager.showLoading(type);
    }

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

    @Override
    public void hideError() {
        if (null == viewOverrideManager) {
            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
        }
        viewOverrideManager.restoreView();
    }

    @Override
    public void netError() {
        if (null == viewOverrideManager) {
            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
        }
    }

    @Override
    public void onTokenExpired() {
        if (getActivity() != null) {
            Intent intent = new Intent(context, LoginRegistActivity.class);
            intent.putExtra("code", 1);
            startActivity(intent);
            getActivity().finish();
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
        if (waitDialog == null) {
            waitDialog = ViewUtils.getWaitDialog(context, message);
        }
        if (waitDialog != null) {
            waitDialog.setMessage(message);
            waitDialog.show();
        }
        return waitDialog;

    }


    @Override
    public void hideWaitDialog() {
        if (waitDialog != null) {
            try {
                waitDialog.dismiss();
                waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
