package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;
import com.miuhouse.yourcompany.student.view.ui.fragment.MultiImageSelectorFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kings on 7/5/2016.
 */
public class MultiImageSelectorActivity extends BaseActivity implements MultiImageSelectorFragment.Callback {

    public static final String EXTRA_SELECT_COUNT = "max_select_count";

    public static final String EXTRA_SELECT_MODE = "select_count_mode";

    public static final String EXTRA_SHOW_CAMERA = "show_camera";

    public static final String EXTRA_RESULT = "select_result";

    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    public static final int MODE_SINGLE = 0;

    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<>();
    //    private Button mSubmitButton;
    private int mDefaultCount;
    private int mode;
    private boolean isShow;

    @Override
    protected String setTitle() {
        return "图片";
    }

    @Override
    protected String setRight() {
        return "完成";
    }

    @Override
    protected void initViewAndEvents() {
        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
        mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.framelayout_photo_album, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_photo_album;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }

    @Override
    public void onBackClick() {
        super.onBackClick();
    }

    @Override
    public void onRightClick() {
        if (resultList != null && resultList.size() > 0) {
            // 返回已选择的图片数据
            Intent data = new Intent();
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {

        if (!resultList.contains(path)) {
            resultList.add(path);
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
//            mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
        } else {
//            mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
        }
        // 当为选择图片时候的状态
        if (resultList.size() == 0) {
//            mSubmitButton.setText("完成");
//            mSubmitButton.setEnabled(false);
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
