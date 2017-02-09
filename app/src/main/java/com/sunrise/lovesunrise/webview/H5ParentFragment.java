package com.sunrise.lovesunrise.webview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import com.sunrise.lovesunrise.R;

/**
 * @PACKAGE com.sunrise.lovesunrise.webview
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 6/17/16 15:49
 * @VERSION V1.0
 */

public class H5ParentFragment extends android.support.v4.app.Fragment
    implements Html5WebViewClient.Html5WebViewClientListener {

  public final static String key_h5_url = "url";
  public final static String key_h5_can_goback = "key_h5_can_goback";//是否可以返回上个webview
  public final static String key_h5_title = "title";//title
  public final static String key_h5_bundle = "h5_bundle";
  public final static String key_h5_close = "is_close";
  public final static String key_h5_enable_zoomin = "is_enable_zoom_in";
  public final static String key_h5_is_need_refresh = "is_need_refresh";
  public final static String KEY_H5_IS_SHOW_ANIMATION = "key_h5_is_show_animation";
  public final static String KEY_H5_IS_ENABLE_XAUTH = "KEY_H5_IS_ENABLE_XAUTH";

  //load errorC
  private boolean h5_iserror = false;
  //默认显示动画
  private boolean is_show_animation = true,is_enable_zoom ;

  private boolean is_first_in = true;

  public WebView mWebView;

  protected Html5WebViewClient webViewClient;

  protected String mUrl;
  private String mTitle;
  protected Bundle h5Bundle;
  private boolean isCloseActivity;

  protected boolean isLoadSuccess;

  protected boolean isEnableXauth = true,isNeedRefresh = true;

  protected boolean h5_can_goback = false;

  //页面加载动画
  private Animation fadeIn;

  //页面主内容
  View main_content_view ;


  public static H5ParentFragment newInstance(String url, String title,Bundle h5bundle) {
    H5ParentFragment html5Fragment = new H5ParentFragment();
    Bundle bundle = new Bundle();
    bundle.putString(H5ParentFragment.key_h5_url, url);
    bundle.putString(H5ParentFragment.key_h5_title, title);
    bundle.putBoolean(H5ParentFragment.key_h5_close, true);
    if(h5bundle != null)
      bundle.putBundle(key_h5_bundle,h5bundle);
    html5Fragment.setArguments(bundle);
    return html5Fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    mUrl = getArguments().getString(key_h5_url);
    mTitle = getArguments().getString(key_h5_title);
    h5Bundle = getArguments().getBundle(key_h5_bundle);

    if(h5Bundle != null){
      is_show_animation = h5Bundle.getBoolean(KEY_H5_IS_SHOW_ANIMATION,is_show_animation);
      //boolean isExistUp = h5Bundle.getBoolean(ParentActivity.IS_EXIT_UP);
      isNeedRefresh = h5Bundle.getBoolean(key_h5_is_need_refresh,isNeedRefresh);
      is_enable_zoom = h5Bundle.getBoolean(key_h5_enable_zoomin,false);
    }
    is_show_animation = getArguments().getBoolean(KEY_H5_IS_SHOW_ANIMATION,is_show_animation);
    isCloseActivity = getArguments().getBoolean(key_h5_close,isCloseActivity);
    isEnableXauth = getArguments().getBoolean(KEY_H5_IS_ENABLE_XAUTH,isEnableXauth);
    h5_can_goback = getArguments().getBoolean(key_h5_can_goback,h5_can_goback);
    fadeIn = new AlphaAnimation(0.0f, 1.0f);
    fadeIn.setFillAfter(true);
    fadeIn.setDuration(800);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    main_content_view = inflater.inflate(R.layout.activity_html5,null);
    mWebView = (WebView) main_content_view.findViewById(R.id.webview);
    return main_content_view;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    h5Init();
  }

  @Override public void onPause() {
    super.onPause();
  }

  //    初始化h5页面
  private void h5Init() {

    h5_iserror = false;

    if(mWebView == null) {
      return;
    }
    loadUrl(mUrl);
  }

  public void loadUrl(String url) {

    String urlTmp = "";
    //if(isEnableXauth) {
    //  urlTmp = HAXAuthSign.generateH5XauthString(url);
    //}else{
    //  urlTmp += "?platform=android&api_version="+ Const.VERSION_NAME;
    //}
    if (mWebView != null && urlTmp != null && url != null) {
      mWebView.loadUrl(urlTmp);
    }
  }

  @Override
  public void onHtml5WebViewIsRefreshing(boolean isRefreshing) {
  }

  @Override
  public void onHtml5WebViewEnableRefresh(boolean enable) {
  }

  @Override
  public void onHtml5WebViewEnableGoBack(boolean enable) {
  }

  @Override
  public void onHtml5WebViewEnableGoForward(boolean enable) {
  }

  @Override
  public boolean onHtml5WebViewUrlChanged(String url) {
    return false;
  }

  @Override
  public void onPageStarted() {
  }

  @Override
  public void onPageFinished(WebView view, String url, boolean isError) {
    //if (!h5_iserror) {
    //
    //  //setPageStatus(MultiStateView.ViewState.CONTENT);
    //
    //  if(true) {
    //
    //    //main_content_view = multiStateView.getView(MultiStateView.ViewState.CONTENT);
    //
    //
    //    if (is_first_in && is_show_animation) {
    //
    //      main_content_view.setVisibility(View.INVISIBLE);
    //
    //      mWebView.postDelayed(new Runnable() {
    //        @Override public void run() {
    //          if(main_content_view != null) {
    //            fadeIn.setAnimationListener(new Animation.AnimationListener() {
    //              @Override public void onAnimationStart(Animation animation) {
    //
    //              }
    //
    //              @Override public void onAnimationEnd(Animation animation) {
    //              }
    //
    //              @Override public void onAnimationRepeat(Animation animation) {
    //
    //              }
    //            });
    //            main_content_view.startAnimation(fadeIn);
    //          }
    //        }
    //      }, 300);
    //      is_first_in = false;
    //    }else{
    //      main_content_view.setVisibility(View.VISIBLE);
    //      //                    view.loadUrl("javascript:window.document.body.innerHTML = window.document.body.innerHTML.replace(/\\[DYR_FACE_PATH\\]/g,'file:///android_asset/face/dynamic')");
    //    }
    //
    //  }
    //
    //  isLoadSuccess = true;
    //}else{
    //  isLoadSuccess = false;
    //}
  }

  @Override
  public void onReceivedError() {
    h5_iserror = true;
  }

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, String url) {
    return false;
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);

  }

  protected void loadJavaScript(String method){
    if(mWebView != null) {
      mWebView.loadUrl("javascript:" + method);
    }
  }

  @Override
  public void onDestroy() {
    if (mWebView != null) {
      webViewClient = null;
      mWebView.removeAllViews();
      mWebView.destroy();
      mWebView = null;
    }
    super.onDestroy();
  }

}
