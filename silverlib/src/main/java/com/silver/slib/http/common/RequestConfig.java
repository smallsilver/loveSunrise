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

package com.silver.slib.http.common;


import com.silver.slib.Const;

import java.util.HashMap;

/**
 * Created by wangde on 2015/8/14.
 */
public class RequestConfig {

    // 请求Url
//    public static final String API_HOST = Const.API_HOST;

    // 超时时间
    public static final int TIME_OUT = 30 * 1000;

    // Get请求参数编码
    public static final String PARAMS_ENCODING = "utf-8";

    // 请求公共参数
    public static final HashMap<String, String> commonParams(){
        HashMap<String, String> params = new HashMap<>();
        params.put("platform", "android");
        params.put("client_version", Const.VERSION_NAME);
        params.put("api_version",  Const.VERSION_NAME);
        params.put("app_channel",Const.CHANNEL_ID);
        params.put("device_id", Const.DEVICE_ID);
//        params.put("city_code", ProjectConst.CITY_CODE);
//        params.put("selected_city", ProjectConst.SEL_CITY_CODE);
//        params.put("location", ProjectConst.location);
//        params.put("network_type",ProjectConst.NETWORK_TYPE);
        return params;
    }

}
