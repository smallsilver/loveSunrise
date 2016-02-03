/*
 *
 * Created by smallsilver on 1/6/16 3:19 PM
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

package com.silver.slib.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @PACKAGE com.lchr.common.util
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 15/7/28 14:32
 * @VERSION V1.0
 */
public class SortUtil {
    public static LinkedHashMap<String, String> sortHashMap(HashMap<String,String> map){
        //排序
        List<Map.Entry<String,String>> infoIds = new ArrayList<Map.Entry<String, String>>(
                map.entrySet());
        // 排序
        Collections.sort(infoIds, new Comparator<Map.Entry<String,String>>() {
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                try{
                    return Integer.valueOf(o1.getKey())-Integer.valueOf(o2.getKey());
                }catch (Exception ex){
                    return 0;
                }
            }
        });
        LinkedHashMap<String, String> newMap = new LinkedHashMap<String, String>();

        for(Map.Entry<String,String> entity : infoIds){
            newMap.put(entity.getKey(), entity.getValue());
        }

        return newMap;
    }

}
