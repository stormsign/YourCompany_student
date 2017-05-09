package com.miuhouse.yourcompany.student.view.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.MyCount;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by kings on 7/11/2016.
 */
public class ChangeUserNameActivity extends BaseActivity {
    private String strNiceName;
    private EditText editText;
    private boolean isShow;
    private EditText vCode;
    private EditText phoneNumber;
    private MyCount mc;
    private TextView getCode;
    private boolean isChangePhone;

    @Override
    protected String setTitle() {
        return "更改姓名";
    }

    @Override
    protected String setRight() {
        return "保存";
    }

    @Override
    protected void initViewAndEvents() {
        String title = getIntent().getStringExtra("title");
        isShow = getIntent().getBooleanExtra("isShow", false);
        strNiceName = getIntent().getStringExtra("value");
        isChangePhone = title.equals("电话");
        setLeftText("更改" + title);

        ((LinearLayout)findViewById(R.id.editText1)).setVisibility(isChangePhone ? View.GONE : View.VISIBLE);
        ((LinearLayout)findViewById(R.id.editText2)).setVisibility(isChangePhone ? View.VISIBLE : View.GONE);
        if (!isChangePhone) {
            editText = (EditText) findViewById(R.id.edit_user);
            final TextView tvHind = (TextView) findViewById(R.id.tv_hint);
            editText.setHint(title);
            editText.setText(strNiceName);
            if (!Util.isEmpty(strNiceName))
                editText.setSelection(strNiceName.length());
            if (isShow)
                tvHind.setVisibility(View.VISIBLE);
            else
                tvHind.setVisibility(View.GONE);
        }else {
            phoneNumber = (EditText) findViewById(R.id.edit_cellphone_number);
            phoneNumber.setText(strNiceName);
            phoneNumber.setSelection(strNiceName.length());
            vCode = (EditText) findViewById(R.id.edit_code);
            getCode = (TextView) findViewById(R.id.tv_code);
            getCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendCode();
                }
            });
            initSms();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_change_username;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }

    @Override
    public void onRightClick() {
        if (isChangePhone) {
            SMSSDK.submitVerificationCode(Constants.SMSSDK_COUNTRYCODE,
                    strNiceName, vCode.getText().toString().trim());
        }else {
            strNiceName = editText.getText().toString();
            getIntent().putExtra("value", strNiceName);
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    public void onBackClick() {
        showDelDialog();
    }

    public void showDelDialog() {
        new LovelyStandardDialog(this)
                .setButtonsColorRes(R.color.themeColor)
                .setMessage("退出此次编辑？").goneView()
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void sendCode() {
        String etNameStr = phoneNumber.getText().toString();
        if (Util.isEmpty(etNameStr)) {
            phoneNumber.setError("请输入手机号码");
            phoneNumber.requestFocus();
            return;
        }
        if (!Util.isMobile(etNameStr)) {
            phoneNumber.setError("手机号码格式不对");
            phoneNumber.requestFocus();
            return;
        }
        SMSSDK.getVerificationCode(Constants.SMSSDK_COUNTRYCODE, etNameStr);
        if (mc == null) {
            mc = new MyCount(60000, 1000, getCode, this); // 第一参数是总的时间，第二个是间隔时间 都是毫秒为单位
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
            public void afterEvent(int event, int result, final Object data) {
                Log.i("TAG", "result=" + result);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Map<String, Object> map = (HashMap<String, Object>) data;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                strNiceName = phoneNumber.getText().toString();
                                getIntent().putExtra("value", strNiceName);
                                setResult(RESULT_OK, getIntent());
                                finish();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
                            Throwable throwable = (Throwable) data;
                            try {
                                JSONObject jsonObject = new JSONObject(throwable.getMessage());
                                String detail = jsonObject.getString("detail");
                                Toast.makeText(ChangeUserNameActivity.this, detail, Toast.LENGTH_LONG)
                                            .show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
        SMSSDK.getSupportedCountries();
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
