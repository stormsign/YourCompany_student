package com.miuhouse.yourcompany.student.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.yourcompany.student.application.App;
import com.miuhouse.yourcompany.student.interactor.PublishInteractor;
import com.miuhouse.yourcompany.student.interactor.VideoHelper;
import com.miuhouse.yourcompany.student.presenter.interf.IPublishPresenter;
import com.miuhouse.yourcompany.student.utils.AsynTaskManager;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.view.ui.activity.PublishActivity;
import com.miuhouse.yourcompany.student.view.ui.activity.interf.IPublishActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2017/1/5.
 */
public class PublishPresenter implements IPublishPresenter, AsynTaskManager.TaskListener {
    private IPublishActivity activity;
    private final PublishInteractor interactor;

    public PublishPresenter(IPublishActivity activity){
        this.activity = activity;
        interactor = new PublishInteractor();
    }

    @Override
    public void publishWithVideo(String teacherId, String schoolId, String linkId, String linkType,
                                 String linkName, String content, String contentType, String contentShowtype,
                                 String screenShot, String video, String videoLength, ArrayList<String> cid) {

        activity.showLoading(null);
        ArrayList<String> images = new ArrayList<>();
        images.add(screenShot);
        interactor.publish(teacherId, schoolId, linkId, linkType, linkName,
                content, contentType, contentShowtype, images, video, videoLength, cid,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        activity.hideLoading();
                        try {
                            JSONObject jsonObject = new JSONObject((String)response);
                            if (jsonObject.getInt("code") == 0){
                                activity.showError(PublishActivity.RESULT_SUCCESS);
                            }
                            L.i("TAG", jsonObject.toString());
                        } catch (JSONException e) {
                            activity.showError(PublishActivity.RESULT_ERROR);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.hideLoading();
                        activity.showError(PublishActivity.RESULT_ERROR);
                        L.i("TAG", error.toString());
                    }
                });
    }

    @Override
    public void publishWithPicture(String teacherId, String schoolId, String linkId, String linkType,
                                   String linkName, String content, String contentType, String contentShowtype,
                                   List<String> images, List<String> cid) {

        activity.showLoading(null);
        interactor.publish(teacherId, schoolId, linkId, linkType, linkName,
                content, contentType, contentShowtype, images, null, null, cid,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        activity.hideLoading();
                        try {
                            JSONObject jsonObject = new JSONObject((String)response);
                            if (jsonObject.getInt("code") == 0){
                                activity.showError(PublishActivity.RESULT_SUCCESS);
                            }
                            L.i("TAG", jsonObject.toString());
                        } catch (JSONException e) {
                            activity.showError(PublishActivity.RESULT_ERROR);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.hideLoading();
                        activity.showError(PublishActivity.RESULT_ERROR);
                        L.i("TAG", error.toString());
                    }
                });
    }

    @Override
    public void uploadWithPicture() {

    }

    @Override
    public void uploadWithVideo(final Context context, String videoUrl, String videoScreenShotUrl) {
        if (!TextUtils.isEmpty(videoUrl)){
            VideoHelper.uploadVideoAndScreenshot(context, videoUrl, videoScreenShotUrl, this);
        }
    }

    @Override
    public void onPreExrcute() {
        activity.onVideoUpload();
    }

    @Override
    public void onPostExecute(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has("video")){
                activity.onVideoUploadFinished(jsonObject.getString("video"), jsonObject.getString("image"));
            }
            if (jsonObject.has("images")){
                activity.processPicturesUploadResult();
                Toast.makeText(App.getContext(), "图片上传完毕", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCancelled() {

    }
}
