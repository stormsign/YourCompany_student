package com.miuhouse.yourcompany.student.view.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.model.ParentInfo;
import com.miuhouse.yourcompany.student.model.SchoolTeacherInfo;
import com.miuhouse.yourcompany.student.presenter.PublishPresenter;
import com.miuhouse.yourcompany.student.presenter.interf.IPublishPresenter;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IPublishActivity;
import com.miuhouse.yourcompany.student.view.ui.adapter.UpdateImageAdapter;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import mabeijianxi.camera.MediaRecorderActivity;

/**
 * Created by khb on 2017/1/5.
 */
public class PublishActivity extends BaseActivity
    implements IPublishActivity, View.OnClickListener {

    private FrameLayout videoLayout;
    private ImageView videoFrame;
    private IPublishPresenter presenter;
    private String videoUri;
    private String videoScreenShotUri;

    private boolean isNotVideo;
    private GridView imageList;
    private UpdateImageAdapter updateAdapter;
    private List<String> imageUrlList;
    private ArrayList<String> mSelectPath;
    private ArrayList<String> cids = new ArrayList<>();

    private static final int REQUEST_IMAGE = 3;
    private static final int REQUEST_CLASSES = 2;

    public static final int RESULT_ERROR = 0;
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_ERROR_TYPE = 2;
    public static final int RESULT_ERROR_RANGE = 3;

    private LinearLayout imagesLayout;
    private EditText etContent;
    private String videoScreenShotUrl;
    private String videoUrl;
    private int videoDuration;
    private ImageView play;
    private ProgressBar progress;
    private RelativeLayout setClass;
    private ArrayList<Integer> selectedIds;
    private ArrayList<String> classNames;

    private String contentType = "1";

    private ParentInfo account;

    @Override
    protected String setTitle() {
        return null;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_publish;
    }

    @Override
    protected View getOverrideParentView() {
        return null;
    }

    @Override
    public void onRightClick() {
        //        if (cids.contains("0")){
        //            cids.clear();
        //            cids.add(0+"");
        //        }
        cids.add(account.getClassId() + "");
        if (isNotVideo) {
            String content = etContent.getText().toString();
            List<String> urls = updateAdapter.getImageUrls();

//            if (TextUtils.isEmpty(content) | urls.size()==0){
//                Toast.makeText(this, "发布内容为空或图片未上传完成", Toast.LENGTH_LONG).show();
//                return ;
//            }
            presenter.publishWithPicture(account.getParentId(), account.getSchoolId() + "",
                account.getParentId(),
                SchoolTeacherInfo.PARENT, account.getcName() + " " + account.getRoleName(),
                etContent.getText().toString(),
                contentType, "1", updateAdapter.getImageUrls(), cids);
        } else {
            contentType = "1";
            presenter.publishWithVideo(account.getParentId(), account.getSchoolId() + "",
                account.getParentId(),
                SchoolTeacherInfo.PARENT, account.getcName() + " " + account.getRoleName(),
                etContent.getText().toString(),
                contentType, "2", videoScreenShotUrl, videoUrl, videoDuration + "", cids);
        }
        super.onRightClick();
    }

    @Override
    protected String setRight() {
        return "发布";
    }

    @Override
    protected void initViewAndEvents() {
        presenter = new PublishPresenter(this);
        isNotVideo = getIntent().getBooleanExtra("isNotVideo", false);
        account = App.getInstance().getParentInfo();
        videoUri = getIntent().getStringExtra(MediaRecorderActivity.VIDEO_URI);
        videoScreenShotUri = getIntent().getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);
        videoDuration = getIntent().getIntExtra(MediaRecorderActivity.VIDEO_DURATION, 0);
        videoLayout = (FrameLayout) findViewById(R.id.video);
        videoFrame = (ImageView) findViewById(R.id.videoFrame);
        imagesLayout = (LinearLayout) findViewById(R.id.images);
        etContent = (EditText) findViewById(R.id.content);
        play = (ImageView) findViewById(R.id.play);
        progress = (ProgressBar) findViewById(R.id.uploadProgress);
        showVideoOrPicture();
    }

    private void intentMultiImageSelector() {
        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;

        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, Constants.MAX_NUM);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showVideoOrPicture() {
        imagesLayout.setVisibility(View.VISIBLE);
        if (isNotVideo) {
            showPicture();
        } else {
            showVideo();
        }
    }

    @Override
    public void showVideo() {   //显示发布带视频动态的界面
        imagesLayout.setVisibility(View.GONE);
        videoLayout.setVisibility(View.VISIBLE);
        Glide.with(this).load(videoScreenShotUri).into(videoFrame);
        videoFrame.setOnClickListener(this);
        presenter.uploadWithVideo(this, videoUri, videoScreenShotUri);
    }

    @Override
    public void showPicture() {     //显示发布带图片动态的界面
        imagesLayout.setVisibility(View.VISIBLE);
        videoLayout.setVisibility(View.GONE);
        imageUrlList = new ArrayList<>();
        imageList = (GridView) findViewById(R.id.imageList);
        imageList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        updateAdapter = new UpdateImageAdapter(this, imageUrlList);
        updateAdapter.setShape(true);
        imageList.setAdapter(updateAdapter);
        updateAdapter.setOnDelectClickListener(new UpdateImageAdapter.OnDelectClickListener() {
            @Override
            public void onDelectClick(int position) {
                updateAdapter.select(imageUrlList.get(position));
                imageUrlList.remove(imageUrlList.get(position));
            }
        });
        imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imageUrlList.size() == position) {
                    intentMultiImageSelector();
                }
            }
        });
    }

    @Override
    public void processPicturesUploadResult() {

    }

    @Override
    public void onVideoUpload() {
        play.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
                Toast.makeText(this, "正在上传", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoUploadFinished(String videoUrl, String videoScreenshotUrl) {
        this.videoUrl = videoUrl;
        this.videoScreenShotUrl = videoScreenshotUrl;
        play.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
                Toast.makeText(this, "视频上传完毕", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setClass() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videoFrame:
                startActivity(new Intent(activity, VideoPlayerActivity.class)
                    .putExtra("url", videoUri));
                break;
        }
    }

    @Override
    public void showLoading(String msg) {
        //加载时隐藏发布按钮，避免重复提交
        findViewById(R.id.right).setVisibility(View.GONE);
        super.showLoading(msg);
    }

    @Override
    public void hideLoading() {
        findViewById(R.id.right).setVisibility(View.VISIBLE);
        super.hideLoading();
    }

    @Override
    public void showError(int type) {
        if (type == RESULT_ERROR) {
            Toast.makeText(this, "发布时发生错误", Toast.LENGTH_SHORT).show();
        } else if (type == RESULT_SUCCESS) {
            Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else if (type == RESULT_ERROR_TYPE) {
            Toast.makeText(this, "请选择发布类型", Toast.LENGTH_SHORT).show();
        } else if (type == RESULT_ERROR_RANGE) {
            Toast.makeText(this, "请选择谁可以看", Toast.LENGTH_SHORT).show();
        }
        //        super.showError(type);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                imageUrlList.addAll(mSelectPath);
                for (int i = 0; i < imageUrlList.size(); i++) {
                    Log.i("TAG", "filePath=" + imageUrlList.get(i));
                }
                updateAdapter.addData(imageUrlList);
            }
        }
    }
}
