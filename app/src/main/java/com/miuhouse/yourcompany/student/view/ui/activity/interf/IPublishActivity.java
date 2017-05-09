package com.miuhouse.yourcompany.student.view.ui.activity.interf;

import com.miuhouse.yourcompany.student.view.ui.base.BaseView;

/**
 * Created by khb on 2017/1/5.
 */
public interface IPublishActivity extends BaseView {
    void showVideo();
    void showPicture();
    void onVideoUpload();
    void processPicturesUploadResult();
    void onVideoUploadFinished(String videoUrl, String videoScreenshotUrl);
    void setClass();
}
