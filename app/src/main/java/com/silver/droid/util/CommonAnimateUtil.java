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

import android.view.View;
import android.view.animation.Animation;

public class CommonAnimateUtil {
	
	public interface CommonAnimateInterface {
		public void onAnimationStart(Animation animation);
		public void onAnimationEnd(Animation animation);
	}
	
	public static void startCommAnimation(View v,CommonAnimateInterface commonAnimateInterface){
		final CommonAnimateInterface commonAnimateInterfaceFinal = commonAnimateInterface;
		//加载动画
//		Animation hyperspaceJumpAnimation =AnimationUtils.loadAnimation(ProjectApplication.mContext, R.anim.scall_large_small);
//		hyperspaceJumpAnimation.setAnimationListener(new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//				if(commonAnimateInterfaceFinal!=null)
//					commonAnimateInterfaceFinal.onAnimationStart(animation);
//
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				if(commonAnimateInterfaceFinal!=null)
//					commonAnimateInterfaceFinal.onAnimationEnd(animation);
//
//			}
//		});
//		//使用ImageView显示动画
//		v.startAnimation(hyperspaceJumpAnimation);
	}
}
