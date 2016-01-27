/*
 *
 * Created by smallsilver on 1/6/16 3:07 PM
 * Email dongen_wang@163.com
 *
 * Copyright 2016 SmallSilver Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package com.silver.droid.http.common;

import android.content.Context;

import com.silver.droid.http.okhttp.OkHttpExecutor;
import com.squareup.okhttp.Callback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangde on 2015/8/14.
 * modify by dongen_wang on 15/11/25
 */
public abstract class RequestExecutor {
    protected Context mCxt;
    protected String module; // 请求模型
    protected String userAgent;
    protected Object group; // 请求标识
    protected Map<String, String> params; // 请求参数
    protected RequestListener listener;
    protected boolean enableXauth = true; // 是否开启Xauth认证

    protected RequestMethod method; // 请求方法
    protected HashMap<String, String> allParams = new HashMap<>(); // 所有的请求参数

    // add by wde 15/08/27
    // 文件请求参数
    protected HashMap<String, String> fileParams = new HashMap<>();

    public RequestExecutor(Context context) {
//        allParams.putAll(RequestConfig.commonParams());
        this.mCxt = context;
    }

    public static RequestExecutor with(Context mCxt){
        return new OkHttpExecutor(mCxt);
    }

    public RequestExecutor module(String module){
        this.module = module;
        return this;
    }

    // 请求类型
    public RequestExecutor requestMethod(RequestMethod requestMethod){
        method = requestMethod;
        return this;
    }

    public RequestExecutor group(Object group){
        this.group = group;
        return this;
    }

    // 是否启用Xauth认证，默认true
    public RequestExecutor enableXauth(boolean bool){
        enableXauth = bool;
        return this;
    }

    public RequestExecutor userAgent(String userAgent){
        this.userAgent = userAgent;
        return this;
    }

    public RequestExecutor params(Map<String, String> params){
        if(params != null) {
            allParams.putAll(params);
        }
        return this;
    }

    public RequestExecutor fileParams(Map<String, String> params){
        if(params != null) {
            fileParams.putAll(params);
        }
        return this;
    }

    public RequestExecutor multiParams(Map<String, String> params){
        if(params != null) {
            allParams.putAll(params);
        }
        return this;
    }

    public RequestExecutor listener(RequestListener listener){
        this.listener = listener;
        return this;
    }

    /** 返回JsonObject数据类型 */
//    public abstract HttpReultRequest asHttpResult();

    /** 返回String数据类型**/
    public abstract void getStringFromServer(String url, Callback callback);


    /** 返回JsonObject数据类型 */
    public abstract void executeJsonObject();


    public abstract void cancel(String g);

    /**
     * Converts <code>params</code> into an application/x-www-form-urlencoded encoded string.
     */
    public static String encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    public HashMap<String,String> dealXauth(String baseUrl){

        HashMap<String,String> postGetParams = new HashMap<>();
//        // XAuth参数
//        if (enableXauth) {
//            if(method.equalsIgnoreCase("get")) {
//                allParams.putAll(com.sunrise.lovesunrise.http.common.RequestConfig.commonParams());
//                allParams.putAll(com.sunrise.lovesunrise.http.common.XAuthSign.generateURLParams(method, baseUrl, allParams,
//                        Const.getConsumerKey(), Const.getConsumerSecret(),
//                        ProjectApplication.getUser().getToken(), ProjectApplication.getUser().getToken_secret()));
//            }else{
////                modify by wangde 15/11/27
//                // 通过get 方式传递post 请求
//                postGetParams = XAuthSign.generateURLParams(AsyncHttpPost.METHOD, baseUrl, com.sunrise.lovesunrise.http.common.RequestConfig.commonParams(),
//                        Const.getConsumerKey(), Const.getConsumerSecret(),
//                        ProjectApplication.getUser().getToken(), ProjectApplication.getUser().getToken_secret());
//                // 不添加到all params
////                allParams.putAll(postGetParams);
//            }
//        }else{
//            if(method.equalsIgnoreCase("get")) {
//                allParams.putAll(com.sunrise.lovesunrise.http.common.RequestConfig.commonParams());
//            }else{
//                postGetParams = RequestConfig.commonParams();
//            }
//        }
        return postGetParams;
    }

}
