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

/**
 * 
* @ClassName: FontChangeable 
* @Description: 字体大小变化
* @author dongen_wang@163.com 
* @date 2015年3月31日 上午11:03:42 
*
 */
public class FontChangeable {
    public interface FontChangeableListener {
        public void onChangeFont(int fontId);
    }

    /**
     * single instance
     */
    private static FontChangeable instance = null;

    protected FontChangeable() {
    }

    public static synchronized FontChangeable getInstance() {
        if (instance == null) {
            instance = new FontChangeable();
        }

        return instance;
    }

    ArrayList<FontChangeableListener> fontList = new ArrayList<FontChangeableListener>();

    public synchronized void addListener(FontChangeableListener listener) {
        boolean needAddListener = true;

        for (FontChangeableListener lis : this.fontList) {
            if (lis.equals(listener)) {
                needAddListener = false;
                break;
            }
        }
        if (needAddListener) {
        	fontList.add(listener);
        }
        // 如果不是默认模式，则直接切换为设定模式
//        listener.onChangeFont(AccountStorage.getInstance().fontSize);
    }

    public void removeListener(FontChangeableListener listener) {
    	fontList.remove(listener);
    }

    public void notifySkinChange(int fontId) {
        for (FontChangeableListener listener : fontList) {
            listener.onChangeFont(fontId);
        }
    }
}
