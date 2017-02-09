/*
 *   Created by dongen_wang on 10/19/15 11:35 AM
 *   Email dongen_wang@163.com
 *   Copyright 2015 www.mahua.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *   use this file except in compliance with the License.  You may obtain a copy
 *   of the License at
 *
 *   http:www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *   WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 *   License for the specific language governing permissions and limitations under
 *   the License.
 */

package com.sunrise.lovesunrise.webview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.silver.slib.Const;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sinye on 15/6/26.
 *
 * Modify by wangde 15/10/19
 * @see WebViewClient
 * @version 1.1
 */
public class Html5WebViewClient extends WebViewClient {

    public static final String TAG = Html5WebViewClient.class.getSimpleName();

    private Context mContext;
    private Html5WebViewClientListener mListener;

    @Deprecated
    private boolean isError = false;


    public static interface Html5WebViewClientListener {

        public void onHtml5WebViewIsRefreshing(boolean isRefreshing);

        public void onHtml5WebViewEnableRefresh(boolean enable);

        public void onHtml5WebViewEnableGoBack(boolean enable);

        public void onHtml5WebViewEnableGoForward(boolean enable);

        public boolean onHtml5WebViewUrlChanged(String url);

        public void onPageStarted();

        public void onPageFinished(WebView view, String url, boolean isError);

        public void onReceivedError();

        public boolean shouldOverrideUrlLoading(WebView view, String url);
    }

    public Html5WebViewClient(Context context,WebView webView,Html5WebViewClientListener listener){
        mContext = context;
        mListener = listener;
        WebSettings localWebSettings = webView.getSettings();
        localWebSettings.setLoadWithOverviewMode(true);
        localWebSettings.setUseWideViewPort(true);
        localWebSettings.setSupportZoom(true);
        localWebSettings.setDisplayZoomControls(false);
        localWebSettings.setBuiltInZoomControls(false);
        localWebSettings.setJavaScriptEnabled(true);

        localWebSettings.setBlockNetworkImage(true);
        localWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        localWebSettings.setDomStorageEnabled(true);
        localWebSettings.setAppCacheEnabled(true);
        localWebSettings.setDatabaseEnabled(true);
        localWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        localWebSettings.setUserAgentString("fishing/"+Const.VERSION_NAME+ " HttpComponents/1.1 "
                + System.getProperties().getProperty("http.agent")
                + " "
                + Const.PACKAGE_NAME);
        //        localWebSettings.setUserAgentString(System.getProperties().getProperty("http.agent") + " " + CommonAuth.getUA());
        webView.setHorizontalScrollBarEnabled(false);
        //webView.setHorizontalScrollbarOverlay(false);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                AlertDialog.Builder b2 = new AlertDialog.Builder(mContext)
                        .setTitle("title").setMessage(message)
                        .setPositiveButton("ok",
                                new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.confirm();
                                    }
                                });

                b2.setCancelable(false);
                b2.create();
                b2.show();
                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) { // 获取到Title
                super.onReceivedTitle(view, title);
            }
        });
        //使webview具有下载功能
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //if(url.contains(ProjectConst.url_schemaPre) || url.contains(ProjectConst.url_taobao_link)){
        //    return mListener.shouldOverrideUrlLoading(view,url);
        //}else if(url.contains("http://")){
        //    return false;
        //}
        return super.shouldOverrideUrlLoading(view, url);
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (mListener != null) {
            mListener.onHtml5WebViewIsRefreshing(true);
            mListener.onHtml5WebViewEnableRefresh(false);

            mListener.onHtml5WebViewEnableGoBack(view.canGoBack());
            mListener.onHtml5WebViewEnableGoForward(view.canGoForward());
        }
        mListener.onPageStarted();
        isError = false;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);

        if (mListener != null) {
            mListener.onHtml5WebViewIsRefreshing(false);
            mListener.onHtml5WebViewEnableRefresh(true);
            mListener.onReceivedError();
        }
        isError = true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        if (mListener != null) {
            mListener.onHtml5WebViewIsRefreshing(false);
            mListener.onHtml5WebViewEnableRefresh(true);

            mListener.onHtml5WebViewEnableGoBack(view.canGoBack());
            mListener.onHtml5WebViewEnableGoForward(view.canGoForward());
            mListener.onPageFinished(view, url, isError);
        }
        try {
            view.getSettings().setBlockNetworkImage(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    public static Map<String, String> getQueryParams(String url) {
        Map<String, String> params = new HashMap<String, String>();
        try {
            String urlPart = url.substring(url.indexOf("?")+1);
            if (urlPart != null) {
                String query = urlPart;
                String[] splitArr = query.split("&");
                if(splitArr.length >= 1) {
                    for (String param : splitArr) {
                        String[] pair = param.split("=");
                        String key = URLDecoder.decode(pair[0], "UTF-8");
                        String value = "";
                        if (pair.length > 1) {
                            value = URLDecoder.decode(pair[1], "UTF-8");
                            params.put(key, value);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return params;
    }
    //获取?后面的参数带?
    public static String getQueryParamsStr(String url) {
        String urlPart  = "";
        try {
            Map<String, String> params = new HashMap<String, String>();
            urlPart = url.substring(url.indexOf("?"));
        } catch (Exception ex) {
        }
        return urlPart;
    }
}
