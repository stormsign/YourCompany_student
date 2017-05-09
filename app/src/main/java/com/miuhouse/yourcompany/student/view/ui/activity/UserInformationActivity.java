package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.db.AccountDBTask;
import com.miuhouse.yourcompany.student.model.BaseBean;
import com.miuhouse.yourcompany.student.model.HeadUrl;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.model.User;
import com.miuhouse.yourcompany.student.presenter.UserInformationPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IUserInformationPresenter;
import com.miuhouse.yourcompany.student.utils.MyAsyn;
import com.miuhouse.yourcompany.student.utils.Util;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IUserInformationView;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.widget.LovelyStandardDialog;
import com.miuhouse.yourcompany.student.view.widget.ViewOverrideManager;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 7/8/2016.
 */
public class UserInformationActivity extends BaseActivity
    implements View.OnClickListener, IUserInformationView, MyAsyn.AsyncResponse {

    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    private static final int USER_NAME = 0;
    private static final int USER_PARENT_NAME = 1;
    private static final int USER_SUBJECT = 2;
    private static final int USER_INTEREST = 3;
    private static final int REQUEST_IMAGE = 4; //头像

    private static final int USER_GENDERS = 5;
    private static final int USER_GRADES = 6;
    public static final int USER_MOBILE = 7;
    private static final int REQUEST_ALBUM = 8;

    private static final int IS_WRITE = 1;
    private static final int NOT_WRITE = 0;

    private TextView tvStudentname;
    private TextView tvParentName;

    private TextView tvMobile;
    private ImageView imgAvatar;
    private ScrollView content;
    private ProgressBar progressBar;

    private String strUserName;
    private String strParentName;

    private String strHeadUrl;
    private String strMobile;

    //保存成功返回给AccountFragment的用户名和头像地址
    private String strRequestName;
    private String strRequestHeadUrl;

    private IUserInformationPresenter userInformationPresenter;

    private ParentInfo studentInfo;

    ////资料未填写完善，硬要保存把isChange改为true，假如提交成功，资料完善程度改成false；
    //private boolean isChange;
    //资料未填写完善，硬要保存把isChange改为true，假如提交成功，资料完善程度改成false；
    private boolean isChange;
    private boolean canSubmit;

    @Override
    protected String setTitle() {
        return "我的资料";
    }

    @Override
    protected String setRight() {
        return "提交";
    }

    @Override
    protected void initViewAndEvents() {

        findViewById(R.id.relative_student_name).setOnClickListener(this);
        findViewById(R.id.relative_parent_name).setOnClickListener(this);
        findViewById(R.id.relative_grade).setOnClickListener(this);
        findViewById(R.id.relative_subject).setOnClickListener(this);
        findViewById(R.id.relative_interest).setOnClickListener(this);
        findViewById(R.id.relative_mobile).setOnClickListener(this);
        findViewById(R.id.relative_avatar).setOnClickListener(this);
        findViewById(R.id.relative_update_password).setOnClickListener(this);

        content = (ScrollView) findViewById(R.id.content);
        tvStudentname = (TextView) findViewById(R.id.tv_student_name);
        tvParentName = (TextView) findViewById(R.id.tv_parent_name);

        imgAvatar = (ImageView) findViewById(R.id.avatar);
        tvMobile = (TextView) findViewById(R.id.tv_mobile);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        userInformationPresenter = new UserInformationPresenter(this);
        userInformationPresenter.getUserInfo(null);
    }

    private void init() {
        if (studentInfo == null) {
            return;
        }
        canSubmit = true;

        if (!Util.isEmpty(studentInfo.getMobile())) {
            strMobile = studentInfo.getMobile();
            tvMobile.setText(strMobile);
        }
        if (!Util.isEmpty(studentInfo.getpName())) {
            strParentName = studentInfo.getpName();
            tvParentName.setText(strParentName);
        } else {
            tvParentName.setText(R.string.not_write);
        }
        if (!Util.isEmpty(studentInfo.getcName())) {
            strUserName = studentInfo.getcName();
            tvStudentname.setText(strUserName);
        } else {
            tvStudentname.setText(R.string.not_write);
        }
        if (!Util.isEmpty(studentInfo.getHeadUrl())) {
            strHeadUrl = studentInfo.getHeadUrl();
            Glide.with(this)
                .load(strHeadUrl)
                .centerCrop()
                .override(Util.dip2px(this, 50), Util.dip2px(this, 50))
                .into(imgAvatar);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_information;
    }

    @Override
    protected View getOverrideParentView() {
        return content;
    }

    /**
     * 点击提交
     */
    @Override
    public void onRightClick() {
        if (!canSubmit){
            return ;
        }
        User user =
            new User(new ParentInfo(strParentName, strUserName, strHeadUrl, strMobile));
        boolean isCheackPersonalData = user.getParentInfo().isCheckPerSonalData();

        //isCheackPersonalData为true表示用户资料填写完善
        if (isCheackPersonalData) {

            progressBar.setVisibility(View.VISIBLE);
            userInformationPresenter.doChangeUserInformation(strParentName, strUserName, strMobile,
                strHeadUrl);
        } else {
            new LovelyStandardDialog(this)
                .setButtonsColorRes(R.color.themeColor)
                .setMessage("资料未填写完善确定保存吗").goneView()
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        userInformationPresenter.doChangeUserInformation(strParentName, strUserName,
                            strMobile, strHeadUrl);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.relative_avatar:
                Intent intentAvatar = new Intent(this, MultiImageSelectorActivity.class);
                intentAvatar.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                    MultiImageSelectorActivity.MODE_SINGLE);
                intentAvatar.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                startActivityForResult(intentAvatar, REQUEST_IMAGE);
                break;
            case R.id.relative_student_name:
                Intent intent = new Intent(this, ChangeUserNameActivity.class);
                intent.putExtra("title", "学生姓名");
                intent.putExtra("value", strUserName);
                intent.putExtra("isShow", true);
                startActivityForResult(intent, USER_NAME);
                break;
            case R.id.relative_parent_name:
                Intent intentParentName = new Intent(this, ChangeUserNameActivity.class);
                intentParentName.putExtra("title", "家长姓名");
                intentParentName.putExtra("value", strParentName);
                intentParentName.putExtra("isShow", true);
                startActivityForResult(intentParentName, USER_PARENT_NAME);
                break;

            case R.id.relative_mobile:
                Intent intentMobile = new Intent(this, ChangeUserNameActivity.class);
                intentMobile.putExtra("title", "电话");
                intentMobile.putExtra("value", strMobile);
                intentMobile.putExtra("isShow", true);
                startActivityForResult(intentMobile, USER_MOBILE);
                break;

            case R.id.relative_update_password:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //选择图片返回的数据，然后跳转到裁剪页面
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                List<String> mSelectPath = new ArrayList<>();
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for (String p : mSelectPath) {
                    sb.append(p);
                }
                //                mResultText.setText(sb.toString());
                File file = new File(sb.toString());
                Uri sourceUri = Uri.fromFile(file);
                String destinationFileName = System.currentTimeMillis() + ".png";
                UCrop.Options oPtions = new UCrop.Options();
                oPtions.setCropFrameColor(getResources().getColor(R.color.themeColor));
                oPtions.setActiveWidgetColor(getResources().getColor(R.color.themeColor));
                oPtions.setStatusBarColor(getResources().getColor(R.color.themeColor));
                oPtions.setToolbarColor(getResources().getColor(R.color.themeColor));
                UCrop.of(sourceUri, Uri.fromFile(new File(getCacheDir(), destinationFileName)))
                    .withOptions(oPtions)
                    .withAspectRatio(9, 9)
                    .withMaxResultSize(Util.dip2px(this, 50), Util.dip2px(this, 50))
                    .start(this);
            }
        }
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            final Uri resultUri = UCrop.getOutput(data);
            try {
                File file = new File(new URI(resultUri.toString()));
                Glide.with(this)
                    .load(resultUri)
                    .centerCrop()
                    .override(Util.dip2px(this, 50), Util.dip2px(this, 50))
                    .into(imgAvatar);
                new MyAsyn(this, this, file, "pbx/studenthead_android").execute();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case USER_NAME:
                    strUserName = data.getStringExtra("value");
                    tvStudentname.setText(strUserName);
                    break;
                case USER_PARENT_NAME:
                    strParentName = data.getStringExtra("value");
                    tvParentName.setText(strParentName);
                    break;
                case USER_MOBILE:
                    strMobile = data.getStringExtra("value");
                    tvMobile.setText(strMobile);
                    break;
            }
        }
    }

    /**
     * 修改成功
     */
    @Override
    public void UpdateSuccess(BaseBean baseBean) {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }

        Toast.makeText(this, baseBean.getMsg(), Toast.LENGTH_LONG).show();
        if (baseBean.getCode() == 0) {
            strRequestName = tvStudentname.getText().toString();
            strRequestHeadUrl = strHeadUrl;
            AccountDBTask.upCName(studentInfo.getParentId(), strRequestName);
            AccountDBTask.updateHeadUrl(studentInfo.getParentId(), strRequestHeadUrl);
        }

        onBackResultClick();
    }

    /**
     * 用户资料的返回方法 给控件赋值
     */
    @Override
    public void getUserInfo(User user) {
        studentInfo = user.getParentInfo();
        init();
    }

    /**
     * 头像上传返回图片路径
     */
    @Override
    public void processFinish(String result) {
        Gson gson = new Gson();
        HeadUrl headUrl = gson.fromJson(result, HeadUrl.class);
        if (headUrl != null && headUrl.getImages().size() > 0) {
            strHeadUrl = headUrl.getImages().get(0);
        }
    }

    @Override
    public void processError() {

    }

    @Override
    public void showLoading(String msg) {
        //        super.showLoading(msg);
        if (viewOverrideManager == null) {
            viewOverrideManager = new ViewOverrideManager(getOverrideParentView());
        }
        viewOverrideManager.showLoading(msg, true);
    }

    @Override
    public void request() {
    }

    public void onBackResultClick() {
        Intent intent = new Intent();
        intent.putExtra("name", strRequestName);
        intent.putExtra("headUrl", strRequestHeadUrl);
        setResult(RESULT_OK, intent);
        finish();
    }

    //    private void showProgressDialog() {
    //        progressDialog = new ProgressDialog(this);
    //        progressDialog.show();
    //    }
}
