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

package com.silver.droid.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 
* @ClassName: DensityUtil 
* @Description: TODO(dp2px or px2dp) 
* @author wangde dongen_wang@163.com 
* @date 2014-3-12 上午9:58:35
*
 */
public class DensityUtil {
	 /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }   
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  

	//获取屏幕宽度
	public static DisplayMetrics getDisplayMetrics(Activity activity){
	    //定义DisplayMetrics 对象     
        DisplayMetrics  dm = new DisplayMetrics();   
        //取得窗口属性   
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);   
        
        return dm;
	}
}
