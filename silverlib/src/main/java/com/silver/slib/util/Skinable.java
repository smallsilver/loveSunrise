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

public class Skinable {
    public interface SkinableListener {
        public void onChangeSkin(int skinId);
    }

    /**
     * single instance
     */
    private static Skinable instance = null;

    protected Skinable() {
    }

    public static synchronized Skinable getInstance() {
        if (instance == null) {
            instance = new Skinable();
        }

        return instance;
    }

    ArrayList<SkinableListener> skinableList = new ArrayList<SkinableListener>();

    public synchronized void addListener(SkinableListener listener) {
        boolean needAddListener = true;

        for (SkinableListener lis : this.skinableList) {
            if (lis.equals(listener)) {
                needAddListener = false;
                break;
            }
        }

        if (needAddListener) {
            skinableList.add(listener);
        }

        // 如果不是默认模式，则直接切换为设定模式
//        if (AccountStorage.getInstance().skinId != ProjectConst.SKIN_ID_LIGHT) {
//            listener.onChangeSkin(AccountStorage.getInstance().skinId);
//        }
    }

    public void removeListener(SkinableListener listener) {
    	DLog.i(Skinable.class.getName(), "移除listener-->"+listener);
        skinableList.remove(listener);
    }

    public void notifySkinChange(int skinId) {
        for (SkinableListener listener : skinableList) {
            listener.onChangeSkin(skinId);
        }
    }
    
}
