package com.miuhouse.yourcompany.student.model;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.miuhouse.yourcompany.student.view.ui.activity.PublishActivity;

import java.io.File;

import mabeijianxi.camera.MediaRecorderActivity;
import mabeijianxi.camera.VCamera;
import mabeijianxi.camera.model.MediaRecorderConfig;
import mabeijianxi.camera.util.DeviceUtils;

/**
 * Created by khb on 2017/1/4.
 */
public class VideoController {

    public static String setVideoCachePath(){
        // 设置拍摄视频缓存路径
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String path = null;
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                path = dcim + "/mabeijianxi/";
            } else {
                path = dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/mabeijianxi/";
            }
        } else {
            path = dcim + "/mabeijianxi/";
        }
        return path;
    }

    public static void prepareVideo(Context context){
        VCamera.setVideoCachePath(setVideoCachePath());
        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true);
        // 初始化拍摄SDK，必须
        VCamera.initialize(context);
    }

    public static void startRecordingVideo(Activity activity){
        prepareVideo(activity);
        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
                .doH264Compress(true)
                .smallVideoWidth(480)
                .smallVideoHeight(360)
                .recordTimeMax(15 * 1000)
                .maxFrameRate(20)
                .minFrameRate(8)
                .captureThumbnailsTime(1)
                .recordTimeMin((int) (1.5 * 1000))
                .build();
        MediaRecorderActivity.goSmallVideoRecorder(activity, PublishActivity.class.getName(), config);
    }

    public static void startRecordingVideo(Activity activity, MediaRecorderConfig config){
        prepareVideo(activity.getApplicationContext());
        if (config != null){
            MediaRecorderActivity.goSmallVideoRecorder(activity, PublishActivity.class.getName(), config);
        }else {
            MediaRecorderConfig defConfig = new MediaRecorderConfig.Buidler()
                .doH264Compress(true)
                .smallVideoWidth(480)
                .smallVideoHeight(360)
                .recordTimeMax(15 * 1000)
                .maxFrameRate(20)
                .minFrameRate(8)
                .captureThumbnailsTime(1)
                .recordTimeMin((int) (1.5 * 1000))
                .build();
            MediaRecorderActivity.goSmallVideoRecorder(activity, PublishActivity.class.getName(), defConfig);

        }
    }

}
