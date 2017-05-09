package com.miuhouse.yourcompany.student.interactor;

import com.android.volley.Request;
import com.android.volley.Response;
import com.miuhouse.yourcompany.student.http.VolleyManager;
import com.miuhouse.yourcompany.student.interactor.interf.IPublishInteractor;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2017/1/6.
 */
public class PublishInteractor implements IPublishInteractor {

    /**
     * 发布动态
     * @param teacherId 班级老师id
     * @param schoolId  午托班id
     * @param linkId    发布人id
     * @param linkType  发布人Type “2”家长 “3”教师
     * @param linkName  发布人姓名
     * @param content   内容
     * @param contentType   “1” 照片 “2”视频
     * @param contentShowtype   “1” 评论 “2” 点赞
     * @param images    图片链接列表
     * @param video     视频链接
     * @param videoLength   视频时长
     * @param cid   班级id
     * @param listener
     * @param errorListener
     */
    @Override
    public void publish(String teacherId, String schoolId, String linkId, String linkType,
                        String linkName, String content, String contentType,
                        String contentShowtype, List<String> images,
                        String video, String videoLength, List<String> cid,
                        Response.Listener listener, Response.ErrorListener errorListener) {
        String url = Constants.URL_VALUE + "schoolNews";
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", teacherId);
        map.put("schoolId", schoolId);
        map.put("linkId", linkId);
        map.put("linkType", linkType);
        map.put("linkName", linkName);
        map.put("content", content);
        map.put("contentType", contentType);
        map.put("contentShowtype", contentShowtype);
        map.put("image", images);
        map.put("video", video);
        map.put("videoLength", videoLength);
        map.put("classArr", cid);
        VolleyManager.getInstance().sendStringRequest(Request.Method.POST, url, map,
                SPUtils.getData(SPUtils.TOKEN, null), listener, errorListener);
    }
}
