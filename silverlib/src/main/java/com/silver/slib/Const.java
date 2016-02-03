
/*
 *
 * Created by smallsilver on 12/31/15 6:42 PM
 * Email dongen_wang@163.com
 *
 * Copyright 2015 SmallSilver Inc.
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

package com.silver.slib;

import android.content.Context;

public class Const {

    public static boolean IS_ONLINE;

    public static Context mContext;

    public static boolean DEBUG;
    public static String PACKAGE_NAME;
    public static int VERSION_CODE;
    public static String VERSION_NAME;
    public static String CHANNEL_ID;

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static float SCREEN_DENSITY;
    public static String DEVICE_ID;



    private static final String API_DOMAIN_HOST_TEST = "http://api.diaoyu123.cc/";
    private static final String API_DOMAIN_HOST = "http://api.diaoyu123.com/";

    private static final String API_HOST_TEST = "http://api.diaoyu123.cc/app/";
    private static final String API_HOST = "http://api.diaoyu123.com/app/";

    private static final String BASE_HOST_TEST = "http://api.diaoyu123.cc/h5/";
    private static final String BASE_HOST = "http://api.diaoyu123.com/h5/";

    //线上
//    //'1704425ac7f0e26e5e79c02601e701af' => 'e508bcda0b0c6ef23b6eb549b3ec25b4'
    private static final String CONSUMER_KEY = "1704425ac7f0e26e5e79c02601e701af";
    private static final String CONSUMER_SECRET = "e508bcda0b0c6ef23b6eb549b3ec25b4";

    //线下
//     560d37dfa796513125ee9cba9d95917e  d2b38871cf0d0a9680294dff065dd8eb
    private static final String OFF_CONSUMER_KEY = "560d37dfa796513125ee9cba9d95917e";
    private static final String OFF_CONSUMER_SECRET = "d2b38871cf0d0a9680294dff065dd8eb";

    public static final String USER_AGREEMENT = API_HOST+"about/userAgreement";

    // TODO modify this
    public static String MAKE_REQUEST_URL(String module) {

        if(IS_ONLINE) {
            if(module.startsWith("app/") || module.startsWith("html/") || module.startsWith("h5/")) {
                return API_DOMAIN_HOST + module;
            }else{
                return API_HOST + module;
            }
        }else {
            if(module.startsWith("app/") || module.startsWith("html/") || module.startsWith("h5/")) {
                return API_DOMAIN_HOST_TEST + module;
            }else{
                return API_HOST_TEST + module;
            }
        }

    }

    public static String MAKE_HTML5_URL(String module){

        if(IS_ONLINE) {
            if(module.startsWith("app/") || module.startsWith("html/") || module.startsWith("h5/")) {
                return API_DOMAIN_HOST + module;
            }else{
                return BASE_HOST + module;
            }
        }else {
            if(module.startsWith("app/") || module.startsWith("html/") || module.startsWith("h5/")) {
                return API_DOMAIN_HOST_TEST + module;
            }else{
                return BASE_HOST_TEST + module;
            }
        }

    }

    public static String getConsumerKey(){
        if(IS_ONLINE) {
            return CONSUMER_KEY;
        }else {
            return OFF_CONSUMER_KEY;
        }
    }


    public static String getConsumerSecret(){
        if(IS_ONLINE) {
            return CONSUMER_SECRET;
        }else {
            return OFF_CONSUMER_SECRET;
        }
    }

//    手机型号: android.os.Build.MODEL
//    SDK版本: + android.os.Build.VERSION.SDK
//    系统版本: + android.os.Build.VERSION.RELEASE
}
