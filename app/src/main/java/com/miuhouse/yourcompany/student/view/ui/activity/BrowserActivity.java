package com.miuhouse.yourcompany.student.view.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ZoomButtonsController;

import com.miuhouse.yourcompany.student.R;
import com.miuhouse.yourcompany.student.utils.L;
import com.miuhouse.yourcompany.student.view.ui.base.BaseActivity;

/**
 * Created by kings on 8/2/2016.
 */
public class BrowserActivity extends BaseActivity {

  public static final String BROWSER_KEY = "browser_url";

  private WebView mWebView;
  private ProgressBar mProgressBar;

  private String url;
  private String titleStr;

  private ValueCallback<Uri> mUploadMessage;
  private ValueCallback<Uri[]> mUploadCallbackAboveL;
  private final static int FILECHOOSER_RESULTCODE = 1;

  @Override
  protected String setTitle() {
    titleStr = getIntent().getStringExtra("title");
    return titleStr;
  }

  @Override
  protected String setRight() {
    return null;
  }

  @Override
  protected void initViewAndEvents() {

    url = getIntent().getStringExtra(BROWSER_KEY);
    Log.i("TAG", "url=" + url);
    //        if (url == null) {
    //            isSplash = true;
    //            url = FinalData.URL_INDEX;
    //        }

    mWebView = (WebView) findViewById(R.id.webView);
    mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
    //        linearWebview = (LinearLayout) findViewById(R.id.linear_webview);
    mWebView.setWebViewClient(new InnerWebViewClient());
    mWebView.setWebChromeClient(new InnerWebChromeClient());
    WebSettings settings = mWebView.getSettings();
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
      ZoomButtonsController zbc = new ZoomButtonsController(mWebView);
      zbc.getZoomControls().setVisibility(View.GONE);
    }
    mWebView.loadUrl(url);
  }

  @Override
  protected int getContentLayoutId() {
    return R.layout.activity_browser;
  }

  @Override
  protected View getOverrideParentView() {
    return null;
  }

  private class InnerWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      Log.i("TAG", "url=" + url);
      //            if (url.equals(FinalData.URL_HEAD + "/mobile/miuhouse.com")) {
      //                Intent intent = new Intent(BrowseActivity.this, MainActivity.class);
      //                startActivity(intent);
      //                finish();
      //            } else if (url.contains("http://cloud.miuhouse.com/wap/huxingDetail/")) {
      //
      //                view.loadUrl(url);
      //            } else {
      view.loadUrl(url);
      //            }
      return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

      super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
      if (mWebView != null && !mWebView.getSettings().getLoadsImagesAutomatically()) {
        mWebView.getSettings().setLoadsImagesAutomatically(true);
      }
      Log.i("TAG", "onPageFinished");
      //            if (MyUtils.isNetworkConnected(BrowseActivity.this)) {
      //                if (linearWebview.getVisibility() == View.GONE) {
      //                    linearWebview.setVisibility(View.VISIBLE);
      //                }
      //            }
      CookieManager cookieManager = CookieManager.getInstance();
      String CookieStr = cookieManager.getCookie(url);
      L.i("cookies = "+CookieStr);
      super.onPageFinished(view, url);
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

    // For Android 3.0+
    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
      mUploadMessage = uploadMsg;
      Intent i = new Intent(Intent.ACTION_GET_CONTENT);
      i.addCategory(Intent.CATEGORY_OPENABLE);
      i.setType("*/*");
      startActivityForResult(
          Intent.createChooser(i, "File Browser"),
          FILECHOOSER_RESULTCODE);
    }

    //For Android 4.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
      mUploadMessage = uploadMsg;
      Intent i = new Intent(Intent.ACTION_GET_CONTENT);
      i.addCategory(Intent.CATEGORY_OPENABLE);
      i.setType("*/*");
      startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    // For Android 5.0+
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
        FileChooserParams fileChooserParams) {
      mUploadCallbackAboveL = filePathCallback;
      Intent i = new Intent(Intent.ACTION_GET_CONTENT);
      i.addCategory(Intent.CATEGORY_OPENABLE);
      i.setType("*/*");
      startActivityForResult(
          Intent.createChooser(i, "File Browser"),
          FILECHOOSER_RESULTCODE);
      return true;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

      super.onProgressChanged(view, newProgress);

      if (!mProgressBar.isShown()) {
        mProgressBar.setVisibility(View.VISIBLE);
      }
      mProgressBar.setProgress(newProgress);
      if (newProgress == 100) {
        mProgressBar.setVisibility(View.INVISIBLE);
      }
    }
  }

  @Override
  public void onDestroy() {

    if (mWebView != null) {
      mWebView.stopLoading();
      mWebView.clearCache(true);
      mWebView.destroy();
      mWebView = null;
    }
    super.onDestroy();
  }

  @Override
  public void onResume() {

    super.onResume();
    mWebView.onResume();
  }

  @Override
  public void onPause() {

    super.onPause();
    mWebView.onPause();
  }

  //    @Override
  //    public void initVariables() {
  //        url = getIntent().getStringExtra(BROWSER_KEY);
  //        titleStr = getIntent().getStringExtra("title");
  //        if (url == null) {
  //            isSplash = true;
  //            url = FinalData.URL_INDEX;
  //        }
  //    }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      if (mWebView.canGoBack()) {
        mWebView.goBack();
      } else {
        finish();
      }
    }
    return super.onOptionsItemSelected(item);
  }

  //flipscreen not loading again
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }

  // 捕捉“回退”按键，让WebView能回退到上一页，而不是直接关闭Activity。
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
      mWebView.goBack();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  //处理选择图片后返回的uri，然后发送给后台
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == FILECHOOSER_RESULTCODE) {
      if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
      Uri result = data == null || resultCode != RESULT_OK ? null
          : data.getData();
      if (mUploadCallbackAboveL != null) {
        onActivityResultAboveL(requestCode, resultCode, data);
      } else if (null != mUploadMessage) {
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
    if (requestCode != FILECHOOSER_RESULTCODE
        || mUploadCallbackAboveL == null) {
      return;
    }
    Uri[] results = null;
    if (resultCode == Activity.RESULT_OK) {
      if (data == null) {
      } else {
        String dataString = data.getDataString();
        ClipData clipData = data.getClipData();
        if (clipData != null) {
          results = new Uri[clipData.getItemCount()];
          for (int i = 0; i < clipData.getItemCount(); i++) {
            ClipData.Item item = clipData.getItemAt(i);
            results[i] = item.getUri();
          }
        }
        if (dataString != null) {
          results = new Uri[] {Uri.parse(dataString)};
        }
      }
    }
    mUploadCallbackAboveL.onReceiveValue(results);
    mUploadCallbackAboveL = null;
    return;
  }
}
