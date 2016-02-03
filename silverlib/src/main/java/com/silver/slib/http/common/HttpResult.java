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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by dongen_wang
 */
public class HttpResult {
    public int code; // 状态码，大于0为正常 小于零为异常
    public String message; // 提示信息，由后端返回的统一提示信息
    public String tipmsg; // 用于开发人员查看的提示信息，暂未启用
    public JsonObject data; // 返回数据，json对象，包含详细的后端处理数据
    public JsonArray dataJsonArray;

    public void parse(JsonObject jsonObject) {

        if(null != jsonObject.get("code")) {
            this.code = jsonObject.get("code").getAsInt();
        }

        if(null != jsonObject.get("message")) {
            this.message = jsonObject.get("message").getAsString();
        }

        if(null != jsonObject.get("tipmsg")) {
            this.tipmsg = jsonObject.get("tipmsg").getAsString();
        }

        JsonElement dataObj = jsonObject.get("data");
        if(null == dataObj){
            return;
        }
        if(dataObj.isJsonArray()){
            this.dataJsonArray = dataObj.getAsJsonArray();
        }
        if(dataObj.isJsonObject()){
            this.data = dataObj.getAsJsonObject();
        }
    }
}
