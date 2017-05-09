package com.miuhouse.yourcompany.student.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.utils.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by khb on 2017/1/6.
 */
public class UploadRequest {

    private static class Instance{
        public final static UploadRequest instance = new UploadRequest();
    }
    public static UploadRequest getInstance (){
        return  Instance.instance;
    }

    public String getVideoUploadRequest(Context context, String videoUrl, String videoScreenShotUrl) throws IOException, MyException {
        // 拼接请求地址
        String urlPath = Constants.IMAGE_URL_UPLOAD+ "videoUpload";
        String videoBase64 =  getVideoBase64(videoUrl);
        String videoScreenShotBase64 = null;
        if (!TextUtils.isEmpty(videoScreenShotUrl)){
            videoScreenShotBase64 = getImageBase64(context, videoScreenShotUrl);
        }
        JSONObject jsonObj = new JSONObject();
        String json = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            // 前四个参数为固定参数
            jsonObj.put("deviceType", "3");
            jsonObj.put("imei", tm.getDeviceId());
            jsonObj.put("version_code", "1");
            jsonObj.put("folder", "video");

            jsonObj.put("video", videoBase64);
            jsonObj.put("image", videoScreenShotBase64);
            jsonObj.put("fileName", videoUrl.substring(videoUrl.lastIndexOf("/")));
            jsonObj.put("filenameImage", videoScreenShotUrl.substring(videoScreenShotUrl.lastIndexOf("/")));
//            }
            // 转为json格式String
            json = jsonObj.toJSONString(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }

// 请求数据
        String jsonString = MyHttpConnection.httpPost(urlPath, json);
        Log.i("TAG", "jsonString=" + jsonString);
        return jsonString;
        // return getJson(json, cls);
    }

    private String getVideoBase64(String videoUrl) throws IOException{
        File file = new File(videoUrl);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        fis.read(buffer);
        fis.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    private String getImageBase64(Context context, String imageUrl) throws IOException {
        Bitmap screenshotBitmap = Util.createImageThumbnail(context, imageUrl, 800);
        return com.miuhouse.yourcompany.student.utils.Base64
                .encode(Util.Bitmap2Bytes(screenshotBitmap));
    }

}
