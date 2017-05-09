package com.miuhouse.yourcompany.student.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.miuhouse.yourcompany.student.http.MyRequest;
import com.miuhouse.yourcompany.student.model.PhotoBean;
import com.miuhouse.yourcompany.student.model.Photos;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

// 网络请求
public class MyAsyn extends AsyncTask<String, String, String> {
    //    private MyException e;
//    private ProgressFragment progressFragment = ProgressFragment.newInstance();
    private WeakReference<FragmentActivity> oAuthActivityWeakReference;
    private String returnStr; // 请求后的返回值
    private String fileName;
    private File file;
    private String folder;
    private Photos photos;
    private Context recordDetailActivity;

    public MyAsyn(Context recordDetailActivity, AsyncResponse mAsyncResPonse, File file, String folder) {
//        oAuthActivityWeakReference = new WeakReference<FragmentActivity>(recordDetailActivity);
        this.recordDetailActivity = recordDetailActivity;
        this.mAsyncResponse = mAsyncResPonse;
        this.file = file;
        this.folder = folder;
    }

    @Override
    protected String doInBackground(String... params) {
        ArrayList<Photos> photosList = new ArrayList<Photos>();
        String bitStr;
        Bitmap bitmap;
        try {
            fileName = file.getName();
            bitmap = Util.createImageThumbnail(recordDetailActivity, file.getPath(), 800);
            bitStr = Base64.encode(Util.Bitmap2Bytes(bitmap));
            photos = new Photos();
            photos.setBase64String(bitStr);
            photos.setFileName(fileName);
            photosList.add(photos);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        PhotoBean mPhotoBean = new PhotoBean();
        mPhotoBean.setFolder(folder);
        mPhotoBean.setImage(photosList);
        // 第一次请求，先将图片上传到服务器，获得返回 的图片url
        String returnStr = null;
        try {
            returnStr = MyRequest.getInstance().getRequest(mPhotoBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnStr;
    }

    @Override
    protected void onPostExecute(String result) {
        // if (progressFragment.isVisible()) {
//        progressFragment.dismissAllowingStateLoss();
        // }
//        Gson gson = new Gson();
//        MsgBean msg = gson.fromJson(result, MsgBean.class);
//        if (msg.getCode() != 0) {
//            Toast.makeText(oAuthActivityWeakReference.get(), msg.getMsg(), Toast.LENGTH_LONG).show();
//            return;
//        }
        if (result != null) {
            if (mAsyncResponse != null) {
                mAsyncResponse.processFinish(result);
            }
        } else {
            mAsyncResponse.processError();
//            Toast.makeText(recordDetailActivity, "提交时发生错误，请稍后再试", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPreExecute() {
        // progressFragment.setAsyncTask(this);
//
//        FragmentActivity activity = oAuthActivityWeakReference.get();
//        if (linearInputContent != null) {
//            linearInputContent.setVisibility(View.GONE);
//
//        }
//        if (activity != null) {
//            progressFragment.show(activity.getSupportFragmentManager(), "");
//        }
    }

    @Override
    protected void onCancelled() {
//        super.onCancelled();
//        if (progressFragment != null) {
//            progressFragment.dismissAllowingStateLoss();
//        }
//        FragmentActivity activity = oAuthActivityWeakReference.get();
//        if (activity == null) {
//            return;
//        }
//
//        if (e != null) {
//            Toast.makeText(activity, e.getError(), Toast.LENGTH_SHORT).show();
//        }
    }

    // 监听器
    private AsyncResponse mAsyncResponse = null;

    public interface AsyncResponse {
        public void processFinish(String result);

        public void processError();
    }
}
