package com.miuhouse.yourcompany.student.interactor.interf;

import com.android.volley.Response;

import java.util.List;

/**
 * Created by khb on 2017/1/6.
 */
public interface IPublishInteractor {
    void publish(String teacherId, String schoolId, String linkId, String linkType,
                 String linkName, String content, String contentType,
                 String contentShowtype, List<String> images,
                 String video, String videoLength, List<String> cid,
                 Response.Listener listener, Response.ErrorListener errorListener);

}
