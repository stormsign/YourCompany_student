package com.miuhouse.yourcompany.student.view.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.ActivityManager;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.presenter.LoginPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.ILoginPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.utils.MyCount;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.ILoginView;
import com.miuhouse.yourcompany.student.view.ui.fragment.ProductTourFragment;
import com.miuhouse.yourcompany.student.view.widget.LovelyCustomDialog;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

//import com.baidu.android.pushservice.PushConstants;
//import com.baidu.android.pushservice.PushManager;
//import com.baidu.android.pushservice.PushSettings;

/**
 * Created by kings on 7/6/2016.
 * 验证码-->初始化(initSms)-->获得验证码的请求:   SMSSDK.getVerificationCode
 * -->提交验证码submitVerificationCode-->
 */
public class LoginRegistActivity extends AppCompatActivity implements ILoginView {

    //用来区别是注册还是修改密码
    /**
     * 重置密码
     */
    public static final int TYPE_MARK_RESET = 1;

    /**
     * 注册标识
     */
    public static final int TYPE_MARK_REGIST = 0;

    /**
     * 登录标识
     */
    public static final int TYPE_MARK_LOGIN = 2;

    /**
     * 闪屏页数
     */
    static final int NUM_PAGES = 2;

    private EditText etAccount;
    private EditText etPassword;
    private Button login;
    private ViewPager pager;
    private LinearLayout circles;
    private ProgressBar mProgressbar;
    private TextView tvReset;
    private TextView tvCode;
    private LinearLayout linearCode;
    private EditText etCode;
    private LovelyCustomDialog mDialog;

    private ILoginPresenter loginPresenter;

    private String etNameStr;
    private String etPasswordStr;
    private String vCode;

    /**
     * 倒计时
     */
    private MyCount mc;

    private boolean isOpaque = true;

    /**
     * 假如已经点了注册，再点登录按钮清空注册时的数据,反之亦然
     */
    private boolean isLoginOrRegist = false;

    /**
     * 用来区别是注册还是修改密码
     */
    private int typeMark = TYPE_MARK_REGIST;
    private EventHandler eventHandler;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.READ_PHONE_STATE}, 1);
            L.i("permission");
        } else {
            Constants.IMEI_VALUE = Util.getIMEI(this);
        }
        //        if (Util.isHuaWeiEmui()) {
        //            //华为推送
        //            com.huawei.android.pushagent.api.PushManager.requestToken(this);
        //        } else {
        //百度推送
        PushSettings.enableDebugMode(this, true);
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
            Constants.PUSH_APIKEY);
        //        }
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        // code: token错误的时候跳转到登录界面，
        //默认值为0
        int code = getIntent().getIntExtra("code", 0);
        //假如用户已经登录了，直接跳转到主页面
        if (code != 1 && App.getInstance().isLogin()) {
            startActivity(new Intent(this, SplashActivity.class));
            finish();
            return;
        }
        //全屏
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_login_regist);

        Button btnLogin = Button.class.cast(findViewById(R.id.btn_login));
        //Button btnRegist = Button.class.cast(findViewById(R.id.btn_regist));
        pager = (ViewPager) findViewById(R.id.pager);

        initDialog();

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                //假如已经点了注册，再点登录按钮清空注册时的数据
                if (isLoginOrRegist) {
                    clearTextViewStr();
                    isLoginOrRegist = false;
                }
                mDialog.show();
                LoginHandle();
            }
        });


        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setPageTransformer(true, new CrossfadePageTransformer());
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {

                if (position == NUM_PAGES - 2 && positionOffset > 0) {
                    if (isOpaque) {
                        pager.setBackgroundColor(Color.TRANSPARENT);
                        isOpaque = false;
                    }
                } else {
                    if (!isOpaque) {
                        pager.setBackgroundColor(
                            getResources().getColor(R.color.primary_material_light));
                        isOpaque = true;
                    }
                }
            }

            @Override public void onPageSelected(int position) {

                setIndicator(position);

            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });

        loginPresenter = new LoginPresenter(LoginRegistActivity.this, this);

        buildCircles();
        initSms();
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
        int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                L.i("permission granted");
                Constants.IMEI_VALUE = Util.getIMEI(this);
            } else {
                L.i("permission denied");
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_LONG).show();
                finish();
                ActivityManager.getInstance().finishAll();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initDialog() {

        mDialog = new LovelyCustomDialog(LoginRegistActivity.this, R.style.TintTheme);
        mDialog.setView(R.layout.activity_login).goneView();
        View view = mDialog.getAddedView();
        initDialogView(view);
    }

    /**
     * 初始化短信
     */
    private void initSms() {

        SMSSDK.initSDK(this, Constants.SMSSDK_APP_KEY, Constants.SMSSDK_APP_SECRET);
        //回调完成
//提交验证码成功
//获取验证码成功
//                            showToast(getResources().getString(R.string.verifycode_sent));
//返回支持发送验证码的国家列表
        eventHandler = new EventHandler() {

            @Override public void onRegister() {

                super.onRegister();
            }

            @Override public void onUnregister() {

                super.onUnregister();
            }

            @Override public void beforeEvent(int i, Object o) {

                super.beforeEvent(i, o);
            }

            @Override public void afterEvent(int event, int result, Object data) {

                Log.w("TAG", "event = " + event + "  result = " + result);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Map<String, Object> map = (HashMap<String, Object>) data;
                        Log.w("TAG", "EVENT_SUBMIT_VERIFICATION_CODE " + map.toString());
                        runOnUiThread(new Runnable() {

                            @Override public void run() {

                                loginPresenter.doRegist(typeMark, etNameStr, etPasswordStr);
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        Log.i("TAG", "获取验证码成功");
                        runOnUiThread(new Runnable() {

                            @Override public void run() {
                                //                            showToast(getResources().getString(R.string.verifycode_sent));
                            }
                        });
                        Log.w("TAG", "get code success");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                        ArrayList<HashMap<String, Object>> countryList =
                            (ArrayList<HashMap<String, Object>>) data;
                    }
                } else {
                    runOnUiThread(new Runnable() {

                        @Override public void run() {

                            showProgressBar(false);
                            Toast.makeText(LoginRegistActivity.this, "验证码验证失败", Toast.LENGTH_LONG)
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

    public void initDialogView(View view) {

        mProgressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        etAccount = (EditText) view.findViewById(R.id.edit_user);
        etPassword = (EditText) view.findViewById(R.id.edit_password);
        login = (Button) view.findViewById(R.id.btn_login);
        linearCode = (LinearLayout) view.findViewById(R.id.linear_code);
        tvReset = (TextView) view.findViewById(R.id.tv_reset);
        tvCode = (TextView) view.findViewById(R.id.tv_code);
        etCode = (EditText) view.findViewById(R.id.edit_code);

        etAccount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {

                etNameStr = s.toString();
                if (Util.isEmpty(etNameStr)) {
                    login.setEnabled(false);
                } else {
                    if (Util.isMobile(etNameStr)) {
                        if (Util.isEmpty(etPasswordStr)) {
                            login.setEnabled(false);
                        } else {
                            login.setEnabled(true);

                        }
                    } else {
                        login.setEnabled(false);

                    }
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {

                etPasswordStr = s.toString();
                if (Util.isEmpty(vCode)) {
                    //                    etPassword.setError("密码不能为空");
                    login.setEnabled(false);
                    return;
                } else {
                    if (Util.isEmpty(etNameStr)) {
                        //手机号不能为空
                        tvPhoneMistake("手机号不能为空");
                        return;
                    }
                    if (!Util.isMobile(etNameStr)) {
                        tvPhoneMistake("手机号码格式不对哦");
                        return;
                    }
                    if (Util.isEmpty(etPasswordStr)) {
                        login.setEnabled(false);
                        return;
                    }
                    login.setEnabled(true);
                }
            }
        });
        tvReset.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {

                mDialog.dismiss();
                clearTextViewStr();
                mDialog.show();
                rigstHandle(TYPE_MARK_RESET);
            }
        });
    }

    private TextWatcher getEtAccountTextWatcher(final int index) {

        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {

                etNameStr = s.toString();
                if (Util.isEmpty(etNameStr)) {
                    login.setEnabled(false);
                } else {
                    if (Util.isMobile(etNameStr)) {
                        if (Util.isEmpty(etPasswordStr)) {
                            login.setEnabled(false);

                        } else {
                            if (index == 1 || index == 0) {
                                if (Util.isEmpty(vCode)) {
                                    login.setEnabled(false);
                                } else {
                                    login.setEnabled(true);
                                }
                            } else {
                                login.setEnabled(true);
                            }

                        }
                    } else {
                        login.setEnabled(false);

                    }
                }
            }
        };
    }

    private TextWatcher getEtPasswordTextWatcher(final int index) {

        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {

                etPasswordStr = s.toString();
                if (Util.isEmpty(etPasswordStr)) {
                    login.setEnabled(false);
                } else {
                    if (Util.isEmpty(etNameStr)) {
                        //手机号不能为空
                        login.setEnabled(false);
                        return;
                    }
                    if (!Util.isMobile(etNameStr)) {
                        login.setEnabled(false);
                        return;
                    }
                    if (index == 1 || index == 0) {
                        if (Util.isEmpty(vCode)) {
                            login.setEnabled(false);
                            return;
                        }

                    }
                    login.setEnabled(true);
                }
            }
        };
    }

    private void LoginHandle() {

        if (linearCode.getVisibility() == View.VISIBLE) {
            linearCode.setVisibility(View.GONE);
        }
        etPassword.addTextChangedListener(getEtPasswordTextWatcher(TYPE_MARK_LOGIN));
        etAccount.addTextChangedListener(getEtAccountTextWatcher(TYPE_MARK_LOGIN));
        tvReset.setVisibility(View.VISIBLE);
        login.setText(R.string.login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {

                Util.hideSoftKeyboard(login, LoginRegistActivity.this);
                login();
            }
        });

    }

    private void rigstHandle(final int typeMark) {

        this.typeMark = typeMark;
        if (linearCode.getVisibility() == View.GONE) {
            linearCode.setVisibility(View.VISIBLE);
        }
        if (typeMark == TYPE_MARK_REGIST) {
            login.setText(R.string.regist);
        } else {
            login.setText("修改密码");
        }
        tvReset.setVisibility(View.GONE);
        login.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {

                Util.hideSoftKeyboard(login, LoginRegistActivity.this);
                regsit();
            }
        });
        tvCode.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {

                sendCode();
            }
        });

        etPassword.addTextChangedListener(getEtPasswordTextWatcher(TYPE_MARK_REGIST));
        etAccount.addTextChangedListener(getEtAccountTextWatcher(TYPE_MARK_REGIST));

        etCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {

                vCode = s.toString();
                if (Util.isEmpty(vCode)) {
                    login.setEnabled(false);
                    return;
                } else {
                    if (Util.isEmpty(etNameStr)) {
                        tvPhoneMistake("手机号码不能为空");
                        return;
                    }
                    if (!Util.isMobile(etNameStr)) {
                        tvPhoneMistake("手机号码格式不对哦");
                        return;
                    }
                    if (Util.isEmpty(etPasswordStr)) {

                        login.setEnabled(false);
                        return;
                    }
                    login.setEnabled(true);

                }
            }
        });
    }

    private void clearTextViewStr() {

        etNameStr = null;
        etPasswordStr = null;
        vCode = null;
        etAccount.setText("");
        etPassword.setText("");
        etCode.setText("");
    }

    private void sendCode() {

        if (Util.isEmpty(etNameStr)) {
            etAccount.setError("请输入手机号码");
            etAccount.requestFocus();
            return;
        }
        if (!Util.isMobile(etNameStr)) {
            etAccount.setError("手机号码格式不对");
            etAccount.requestFocus();
            return;
        }
        SMSSDK.getVerificationCode("86", etNameStr);
        if (mc == null) {
            mc = new MyCount(60000, 1000, tvCode, this); // 第一参数是总的时间，第二个是间隔时间 都是毫秒为单位
        }
        mc.start();

    }

    private void login() {

        checkLoginFlow();
        showProgressBar(true);
        loginPresenter.doLogin(etAccount.getText().toString(), etPassword.getText().toString());
    }

    private void regsit() {

        checkRegistFlow();
        showProgressBar(true);
        SMSSDK.submitVerificationCode(Constants.SMSSDK_COUNTRYCODE, etNameStr, vCode);
        //        loginPresenter.doRegist(typeMark, etNameStr, etPasswordStr);

    }

    private void checkLoginFlow() {

        if (!Util.hasInternet()) {
            Toast.makeText(this, R.string.tip_no_internet, Toast.LENGTH_LONG).show();
        }
        String strPassword = etPassword.getText().toString().trim();
        String strUser = etAccount.getText().toString().trim();
        if (Util.isEmpty(strUser)) {
            etAccount.setError("请输入手机号码");
            etAccount.requestFocus();
            return;
        }
        if (!Util.isMobile(strUser)) {
            etAccount.setError("手机号码格式不对");
            etAccount.requestFocus();
            return;
        }
        if (Util.isEmpty(strPassword)) {
            etPassword.setError("请输入密码");
            etPassword.requestFocus();
            return;
        }
    }

    private void checkRegistFlow() {

        checkLoginFlow();
        vCode = etCode.getText().toString();
        if (Util.isEmpty(vCode)) {
            etCode.setError("请输入验证码");
            return;
        }
    }

    @Override protected void onResume() {

        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override protected void onPause() {

        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override protected void onDestroy() {

        super.onDestroy();
        if (pager != null) {
            pager.clearOnPageChangeListeners();
        }
        if (mDialog != null) {
            mDialog.dismiss();
        }
        if (eventHandler != null ) {
            SMSSDK.unregisterEventHandler(eventHandler);
        }
    }

    private void buildCircles() {

        circles = LinearLayout.class.cast(findViewById(R.id.circles));

        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);

        for (int i = 0; i < NUM_PAGES; i++) {
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.mipmap.ic_swipe_indicator_white_18dp);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            circles.addView(circle);
        }

        setIndicator(0);
    }

    private void setIndicator(int index) {

        if (index < NUM_PAGES) {
            for (int i = 0; i < NUM_PAGES; i++) {
                ImageView circle = (ImageView) circles.getChildAt(i);
                if (i == index) {
                    circle.setColorFilter(getResources().getColor(R.color.text_selected));
                } else {
                    circle.setColorFilter(getResources().getColor(android.R.color.transparent));
                }
            }
        }
    }

    private void endTutorial() {

        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override public void onBackPressed() {

        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    @Override public void showLoginSuccess(User user) {

        showProgressBar(false);
        mDialog.dismiss();
        Toast.makeText(this, user.getMsg(), Toast.LENGTH_LONG).show();
        if (user.getCode() == 0) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, user.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    @Override public void showRegistSuccess(BaseBean baseBean) {

        if (baseBean.getCode() == 0) {
            login();
        } else {
            showProgressBar(false);
            Toast.makeText(this, baseBean.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    private void showProgressBar(boolean show) {

        mProgressbar.setVisibility(show ? View.VISIBLE : View.GONE);
        login.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override public void showLoading(String msg) {

    }

    @Override public void showError(int type) {

    }

    @Override public void hideLoading() {

    }

    @Override public void hideError() {

    }

    @Override public void netError() {

        showProgressBar(false);
        Toast.makeText(this, R.string.exception_no_network, Toast.LENGTH_LONG).show();
    }

    @Override public ProgressDialog showWaitDialog() {

        return null;
    }

    @Override public ProgressDialog showWaitDialog(int msg) {

        return null;
    }

    @Override public ProgressDialog showWaitDialog(String msg) {

        return null;
    }

    @Override public void hideWaitDialog() {

    }

    @Override public void onTokenExpired() {

    }

    private void tvPhoneMistake(String msg) {

        etAccount.setError(msg);
        etAccount.findFocus();
        login.setEnabled(false);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override public Fragment getItem(int position) {

            ProductTourFragment tp = null;
            switch (position) {
                case 0:
                    tp = ProductTourFragment.newInstance(R.layout.welcome_fragment01);
                    break;
                case 1:
                    tp = ProductTourFragment.newInstance(R.layout.welcome_fragment02);
                    break;
                //                case 2:
                //                    tp = ProductTourFragment.newInstance(R.layout.welcome_fragment03);
                //                    break;
                //                case 3:
                //                    tp = ProductTourFragment.newInstance(R.layout.welcome_fragment04);
                //                    break;
                //                case 4:
                //                    tp = ProductTourFragment.newInstance(R.layout.welcome_fragment05);
                //
                //                    break;
                //                case 5:
                //                    tp = ProductTourFragment.newInstance(R.layout.welcome_fragment06);
                //                    break;
            }

            return tp;
        }

        @Override public int getCount() {

            return NUM_PAGES;
        }
    }

    public class CrossfadePageTransformer implements ViewPager.PageTransformer {

        @Override public void transformPage(View page, float position) {

            int pageWidth = page.getWidth();

            View backgroundView = page.findViewById(R.id.welcome_fragment);
            View welcomeImage01 = page.findViewById(R.id.welcome_01);
            View welcomeImage02 = page.findViewById(R.id.welcome_02);
            //            View welcomeImage03 = page.findViewById(R.id.welcome_03);
            //            View welcomeImage04 = page.findViewById(R.id.welcome_04);
            //            View welcomeImage05 = page.findViewById(R.id.welcome_05);
            //            View welcomeImage06 = page.findViewById(R.id.welcome_06);
            if (0 <= position && position < 1) {
                ViewHelper.setTranslationX(page, pageWidth * -position);
            }
            if (-1 < position && position < 0) {
                ViewHelper.setTranslationX(page, pageWidth * -position);
            }

            if (position <= -1.0f || position >= 1.0f) {
            } else if (position == 0.0f) {
            } else {
                if (backgroundView != null) {
                    ViewHelper.setAlpha(backgroundView, 1.0f - Math.abs(position));

                }

                //                if (text_head != null) {
                //                    ViewHelper.setTranslationX(text_head, pageWidth * position);
                //                    ViewHelper.setAlpha(text_head, 1.0f - Math.abs(position));
                //                }
                //
                //                if (text_content != null) {
                //                    ViewHelper.setTranslationX(text_content, pageWidth * position);
                //                    ViewHelper.setAlpha(text_content, 1.0f - Math.abs(position));
                //                }

                if (welcomeImage01 != null) {
                    ViewHelper.setTranslationX(welcomeImage01, (float) (pageWidth / 2 * position));
                    ViewHelper.setAlpha(welcomeImage01, 1.0f - Math.abs(position));
                }

                if (welcomeImage02 != null) {
                    ViewHelper.setTranslationX(welcomeImage02, (float) (pageWidth / 2 * position));
                    ViewHelper.setAlpha(welcomeImage02, 1.0f - Math.abs(position));
                }

                //                if (welcomeImage03 != null) {
                //                    ViewHelper.setTranslationX(welcomeImage03, (float) (pageWidth / 2 * position));
                //                    ViewHelper.setAlpha(welcomeImage03, 1.0f - Math.abs(position));
                //                }
                /*if (welcomeImage04 != null) {
                    ViewHelper.setTranslationX(welcomeImage04, (float) (pageWidth / 2 * position));
                    ViewHelper.setAlpha(welcomeImage04, 1.0f - Math.abs(position));
                }

                if (welcomeImage05 != null) {
                    ViewHelper.setTranslationX(welcomeImage05, (float) (pageWidth / 2 * position));
                    ViewHelper.setAlpha(welcomeImage05, 1.0f - Math.abs(position));
                }

                if (welcomeImage06 != null) {
                    ViewHelper.setTranslationX(welcomeImage06, (float) (pageWidth / 2 * position));
                    ViewHelper.setAlpha(welcomeImage06, 1.0f - Math.abs(position));
                }*/
            }
        }
    }

}