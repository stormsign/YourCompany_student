package com.miuhouse.yourcompany.student.view.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ZoomButtonsController;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.utils.Constants;
import com.miuhouse.yourcompany.student.view.ui.activity.BrowserActivity;
import com.miuhouse.yourcompany.student.view.ui.base.BaseFragment;

/**
 * Created by khb on 2017/1/19.
 */
public class KzFragment extends BaseFragment implements View.OnClickListener {

    private WebView web;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ProgressBar progressBar;
    private ImageView back;

    @Override
    public int getFragmentResourceId() {
        return R.layout.fragment_kz;
    }

    @Override
    public void getViews(View view) {
        web = (WebView) view.findViewById(R.id.web);
        back = (ImageView) view.findViewById(R.id.back);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        web.setWebViewClient(new InnerWebViewClient());
        web.setWebChromeClient(new InnerWebChromeClient());
        WebSettings settings = web.getSettings();
        /*settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);*/
        settings.setDefaultFontSize(15);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        //        webview加載优化
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath("/data/data/" + "com.miuhouse.community" + "/cache");
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }

        int sysVersion = Build.VERSION.SDK_INT;
        if (sysVersion >= 11) {
            settings.setDisplayZoomControls(false);
        } else {
            ZoomButtonsController zbc = new ZoomButtonsController(web);
            zbc.getZoomControls().setVisibility(View.GONE);
        }
        web.loadUrl(Constants.URL_HEAD + "/mobile/classroomList");
        back.setOnClickListener(this);
    }

    @Override
    public void setupView() {

    }

    @Override
    public View getOverrideParentView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (web.canGoBack()) {
                    web.goBack();
                }
                break;
        }
    }

    private class InnerWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("TAG", "url=" + url);
            //            if (url.equals(FinalData.URL_HEAD + "/mobile/miuhouse.com")) {
            Intent intent = new Intent(getActivity(), BrowserActivity.class);
            intent.putExtra(BrowserActivity.BROWSER_KEY, url);
            startActivity(intent);
            //                finish();
            //            } else if (url.contains("http://cloud.miuhouse.com/wap/huxingDetail/")) {
            //
            //                view.loadUrl(url);
            //            } else {
//            view.loadUrl(url);
            //            }
            return true;
        }
    }

    private class InnerWebChromeClient extends WebChromeClient {

        //关键代码，以下函数是没有API文档的，所以在Eclipse中会报错，如果添加了@Override关键字在这里的话。
        //以下为webview调用html的上传代码
        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            super.onProgressChanged(view, newProgress);

            if (!progressBar.isShown()) {
                progressBar.setVisibility(View.VISIBLE);
            }
            progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
