///*
// *
// * Created by smallsilver on 1/6/16 3:19 PM
// * Email dongen_wang@163.com
// *
// * Copyright 2016 SmallSilver Inc.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// *
// */
//
//package com.silver.droid.util;
//
//import android.content.Context;
//import android.content.res.ColorStateList;
//
//import com.silver.droid.ProjectApplication;
//import com.sunrise.lovesunrise.R;
//
//
///**
// *
//* @ClassName: UIHelper
//* @Description: 界面处理换肤Helper
//* @author dongen_wang@163.com
//* @date 2015年5月13日 下午12:08:53
//*
// */
//public class UIHelper {
//
//	private static int dip2px(Context paramContext, float paramFloat) {
//		return (int) (0.5F + paramFloat
//				* paramContext.getResources().getDisplayMetrics().density);
//	}
///*
//	*/
//	private static int getColor(int paramInt) {
//		return ProjectApplication.mContext.getResources().getColor(paramInt);
//	}
//
//	private static ColorStateList getColorStateList(int paramInt) {
//		return ProjectApplication.mContext.getResources().getColorStateList(paramInt);
//	}
//
//	public static boolean isNightTheme() {
//
//		if(AccountStorage.getInstance().skinId == ProjectConst.SKIN_ID_DARK)
//			return true;
//		return false;
//
//	}
//
//	public static int getDefaultTextViewColor(){
//		if (isNightTheme())
//			return ProjectApplication.mContext.getResources().getColor(R.color.sys_default_item_color_night);
//		return ProjectApplication.mContext.getResources().getColor(R.color.sys_default_item_color);
//	}
//
//	// title
////	public static int getTitleBackground() {
////		if (isNightTheme())
////			return R.color.sys_default_color_black;
////		return R.color.sys_default_color;
////	}
//
//	//title Header Text color
//	public static int getTitleHeaderTextColor() {
//		if (isNightTheme())
//			return getColor(R.color.sys_default_item_title_color);
//		return getColor(R.color.white);
//	}
//
//	//sys default bg color
//	public static int getDefaultBgColor() {
//		if (isNightTheme())
//			return R.color.sys_default_bg_color_night;
//		return R.color.sys_default_bg_color;
//	}
//
//	//sys default touch alpha
//	public static float getTouchAlpha() {
//		if (isNightTheme())
//			return 0.5f;
//		return 1f;
//	}
////
////	public static int getDefaultLayoutColor() {
////		if (isNightTheme())
////			return R.color.sys_default_color_black;
////		return R.color.white;
////	}
//
////	public static int getEditTextBgColor(){
////
////		if (isNightTheme())
////			return R.color.sys_edit_bg_color_black;
////		return R.drawable.sys_edit_bg_color_black;
////
////	}
////
////	public static int getEditTextHintColor(){
////
////		if (isNightTheme())
////			return getColor(R.color.edit_text_hint_color_black);
////		return getColor(R.color.edit_text_hint_color);
////
////	}
//
//
////	public static int getEditTextColor(){
////
////		if (isNightTheme())
////			return getColor(R.color.sys_edit_color_black);
////		return getColor(R.color.sys_edit_color);
////
////	}
////
////
////	public static int getLayoutSplitColor(){
////
////		if (isNightTheme())
////			return R.color.color_action_top_black;
////		return R.color.color_action_top;
////
////	}
////
//	//--------------礼品-------------//
//
//	public static int getListViewBgColor() {
//		if (isNightTheme())
//			return R.color.sys_default_bg_color_night;
//		return R.color.white;
//	}
//
////
////	public static int getListItemBackground(){
////		if (isNightTheme())
////			return R.drawable.slide_list_item_bg_black;
////		return R.drawable.slide_list_item_bg;
////	}
////
////	public static Drawable getListDevider(){
////		if(isNightTheme())
////				return new ColorDrawable(ProjectApplication.mContext.getResources().getColor(R.color.color_comment_stroke_black));
////		return new ColorDrawable(ProjectApplication.mContext.getResources().getColor(R.color.color_comment_stroke));
////	}
////
////	public static int getUnderLineIndecatorColor(){
////		if(isNightTheme())
////			return R.color.sys_tab_bg_color_black;
////		return R.color.sys_tab_bg_color;
////	}
//}