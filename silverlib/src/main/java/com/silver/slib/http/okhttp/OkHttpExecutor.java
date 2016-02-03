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

package com.silver.slib.http.okhttp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.silver.slib.Const;
import com.silver.slib.http.common.HttpResult;
import com.silver.slib.http.common.RequestConfig;
import com.silver.slib.http.common.RequestExecutor;
import com.silver.slib.http.common.RequestMethod;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func0;

/**
 * okhttpExecutor create by dongen_wang 15/11/25
 */
public class OkHttpExecutor extends RequestExecutor{

    private static final String CHARSET_NAME = "UTF-8";

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    Request.Builder requestBuilder;

    String requestUrl = null;

    private Call call;

    static{
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30,TimeUnit.SECONDS);
    }

    public OkHttpExecutor(Context mCxt) {
        super(mCxt);
    }

    private void beforeExecute(){
        if (module == null) {
            throw new IllegalArgumentException("module must be not null");
        }

        if (method == null) {
            throw new IllegalArgumentException("requestMethod must be not null");
        }

        if(listener != null){
            listener.beforRequest();
        }

        String baseUrl = Const.MAKE_REQUEST_URL(module);

        HashMap<String,String> postGetParams = dealXauth(baseUrl);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, new Gson().toJson(allParams));

        requestBuilder = new Request.Builder().tag(group);

        // 设置User-Agent
        if (userAgent != null) {
            requestBuilder.header("User-Agent",userAgent);
        } else {
            requestBuilder.header("User-Agent","HttpComponents/1.1 " + System.getProperties().getProperty("http.agent") + " " + Const.PACKAGE_NAME);
        }

        switch (method) {
            case GET:
                // Get 请求参数
                requestUrl = baseUrl + "?" + encodeParameters(allParams, RequestConfig.PARAMS_ENCODING);
                break;
            case POST:
                requestUrl = baseUrl + "?" +encodeParameters(postGetParams, RequestConfig.PARAMS_ENCODING);
                requestBuilder.post(body);
                break;
        }

        // 设置Post请求参数
        if (method.equals(RequestMethod.POST)) {

            MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);

            if(fileParams != null && fileParams.size() > 0){

                // 上传文件处理
                for (Map.Entry<String, String> entity : fileParams.entrySet()) {
                    File ff = new File(entity.getValue());
                    multipartBuilder.addFormDataPart(entity.getKey(), ff.getName(), RequestBody.create(null, ff));
                }

                for (Map.Entry<String, String> entity : allParams.entrySet()) {
                    multipartBuilder.addFormDataPart(entity.getKey(), entity.getValue());
                }

            }else{
                for (Map.Entry<String, String> entity : allParams.entrySet()) {
                    multipartBuilder.addFormDataPart(entity.getKey(), entity.getValue());
                }

            }

            requestBuilder.post(multipartBuilder.build());

        }

    }

//    @Override
//    public HttpReultRequest asHttpResult() {
//        try {
//            beforeExecute();
//            Request request = requestBuilder.url(requestUrl).build();
//            enqueue(request);
//        }catch (Exception ex){
//            listener.onFailure(ex);
//            listener.finishRequest();
//        }
//        return null;
//    }

    @Override
    public void executeJsonObject() {
        try {
            beforeExecute();
            final Request request = requestBuilder.url(requestUrl).build();
            enqueue(request, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    listener.onFailure(e);
                    listener.finishRequest();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    listener.onSuccess(response.body().toString());
                    listener.finishRequest();
                    response.body().close();
                }
            });
        }catch (Exception ex){
            listener.onFailure(ex);
            listener.finishRequest();
        }
    }

    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    private void enqueue(Request request, Callback responseCallback){
        call = mOkHttpClient.newCall(request);
        call.enqueue(responseCallback);
    }
    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     * @param request
     */
    private void enqueue(Request request){
        call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
             
            @Override
            public void onResponse(Response response) throws IOException {

                try {
                    JsonObject jsonObject = new Gson().fromJson(response.body().charStream(), JsonObject.class);
                    HttpResult taskResult = new HttpResult();
                    taskResult.parse(jsonObject);
                    listener.onSuccess(taskResult);
                    listener.finishRequest();
                    response.body().close();
                } catch (Exception e) {
                    listener.onFailure(e);
                    listener.finishRequest();
                }
            }
             
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                listener.onFailure(arg1);
                listener.finishRequest();
            }
        });
    }

    public void getStringFromServer(String url,Callback callback){

        Request request = new Request.Builder().url(url).build();
        enqueue(request,callback);

    }

    public void cancel(String g){
        call.cancel();
//        if(g != null)
//            mOkHttpClient.cancel(g);
    }


    public Observable<HttpResult> oKObservable() {

        return Observable.defer(new Func0<Observable<HttpResult>>() {
            @Override public Observable<HttpResult> call() {
                HttpResult taskResult = new HttpResult();
                Response response = null;
                try {
                    beforeExecute();
                    response = mOkHttpClient.newCall(requestBuilder.url(requestUrl).build()).execute();
                    JsonObject jsonObject = new Gson().fromJson(response.body().charStream(), JsonObject.class);
                    taskResult.parse(jsonObject);
                }catch (Exception ex){
                }finally {
                    try {
                        response.body().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return Observable.just(taskResult);
            }
        });
    }
}