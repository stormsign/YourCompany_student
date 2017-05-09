package com.miuhouse.yourcompany.student.interactor;

import android.content.Context;

import com.miuhouse.yourcompany.student.http.MyException;
import com.miuhouse.yourcompany.student.http.UploadRequest;
import com.miuhouse.yourcompany.student.utils.AsynTaskManager;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * Created by khb on 2017/1/5.
 */
public class VideoHelper {
    public static void uploadVideoAndScreenshot(final Context context,
                                                final String videoUrl, final String videoScreenShotUrl,
                                                AsynTaskManager.TaskListener taskListener){
        AsynTaskManager atm = new AsynTaskManager(context, new AsynTaskManager.NewTaskForResult() {
            @Override
            public String newTaskForResult() {
                try {
                    return UploadRequest.getInstance().getVideoUploadRequest(context, videoUrl, videoScreenShotUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MyException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, taskListener);
        atm.executeOnExecutor(Executors.newFixedThreadPool(3));
    }
}
