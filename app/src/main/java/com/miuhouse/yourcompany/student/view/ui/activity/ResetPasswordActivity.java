package com.miuhouse.yourcompany.student.view.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.presenter.LoginPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.MyCount;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ILoginView;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by kings on 2/15/2017.
 */
public class ResetPasswordActivity extends BaseActivity implements ILoginView {

    public static final int TYPE_MARK_RESET = 1;

    private EditText edCellphoneNumber;
    private EditText edCode;
    private EditText edNewPassword;
    private TextView tvGetCode;

    private String vCode;
    private String etNameStr;

    private LoginPresenter loginPresenter;

    /**
     * 验证码倒计时
     */
    private MyCount mc;

    @Override protected String setTitle() {
        return "修改密码";
    }

    @Override protected String setRight() {
        return "保存";
    }

    @Override public void onRightClick() {
        vCode = edCode.getText().toString();
        if (Util.isEmpty(vCode)) {
            edCode.setError("请输入验证码");
            edCode.requestFocus();
            return;
        }
        SMSSDK.submitVerificationCode(Constants.SMSSDK_COUNTRYCODE, etNameStr, vCode);
    }

    @Override protected void initViewAndEvents() {
        edCellphoneNumber = (EditText) findViewById(R.id.edit_cellphone_number);
        edCode = (EditText) findViewById(R.id.edit_code);
        edNewPassword = (EditText) findViewById(R.id.edit_new_password);
        tvGetCode = (TextView) findViewById(R.id.tv_code);

        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                sendCode();
            }
        });

        loginPresenter = new LoginPresenter(ResetPasswordActivity.this, this);

        initSms();
    }

    private void sendCode() {
        etNameStr = edCellphoneNumber.getText().toString();
        if (Util.isEmpty(etNameStr)) {
            edCellphoneNumber.setError("请输入手机号码");
            edCellphoneNumber.requestFocus();
            return;
        }
        if (!Util.isMobile(etNameStr)) {
            edCellphoneNumber.setError("手机号码格式不对");
            edCellphoneNumber.requestFocus();
            return;
        }
        SMSSDK.getVerificationCode("86", etNameStr);
        if (mc == null) {
            mc = new MyCount(60000, 1000, tvGetCode, this); // 第一参数是总的时间，第二个是间隔时间 都是毫秒为单位
        }
        mc.start();
    }

    private void initSms() {
        SMSSDK.initSDK(this, Constants.SMSSDK_APP_KEY, Constants.SMSSDK_APP_SECRET);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void onRegister() {
                super.onRegister();
            }

            @Override
            public void onUnregister() {
                super.onUnregister();
            }

            @Override
            public void beforeEvent(int i, Object o) {
                super.beforeEvent(i, o);
            }

            @Override
            public void afterEvent(int event, int result, Object data) {
                Log.i("TAG", "result=" + result);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Map<String, Object> map = (HashMap<String, Object>) data;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loginPresenter.doRegist(TYPE_MARK_RESET, etNameStr,
                                    edNewPassword.getText().toString());
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //                            showToast(getResources().getString(R.string.verifycode_sent));
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                        ArrayList<HashMap<String, Object>> countryList =
                            (ArrayList<HashMap<String, Object>>) data;
                    }
                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //showProgressBar(false);
                            Toast.makeText(ResetPasswordActivity.this, "验证码验证失败", Toast.LENGTH_LONG)
                                .show();
                        }
                    });

                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
        SMSSDK.getSupportedCountries();
    }

    @Override protected int getContentLayoutId() {
        return R.layout.activity_reset_password;
    }

    @Override protected View getOverrideParentView() {
        return null;
    }

    @Override public void showLoginSuccess(User user) {

    }

    @Override public void showRegistSuccess(BaseBean baseBean) {

        Toast.makeText(this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
        if (baseBean.getCode() == 0) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
