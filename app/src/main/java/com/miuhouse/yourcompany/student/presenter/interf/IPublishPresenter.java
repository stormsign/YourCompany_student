package com.miuhouse.yourcompany.student.presenter.interf;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2017/1/5.
 */
public interface IPublishPresenter {
    void uploadWithPicture();
    void uploadWithVideo(Context context, String videoUrl, String videoScreenShotUrl);
    void publishWithVideo(String teacherId, String schoolId, String linkId, String linkType,
                          String linkName, String content, String contentType, String contentShowtype,
                          String screenShot, String video, String videoLength, ArrayList<String> cid);
    void publishWithPicture(String teacherId, String schoolId, String linkId, String linkType,
                            String linkName, String content, String contentType, String contentShowtype,
                            List<String> images, List<String> cid);
}
