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

public interface RequestListener<T>{

    // 请求执行之前
    public void beforRequest();

    public void onSuccess(T result);

    public void onFailure(Exception error);

    // 在 OnSuccess、OnFailure执行之后执行
    public void finishRequest();
}
