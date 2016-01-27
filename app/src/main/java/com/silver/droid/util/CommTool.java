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
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.silver.droid.ProjectApplication;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
* <p> @ClassName: CommTool </p>
*
* <p> @Description: CommTool </p>
*
* <p> @author wangde dongen_wang@163.com </p>
*
* <p> @version 1.00.00 </p>
*
* @date Jan 20, 2015 / 6:33:54 PM
 */
public class CommTool {
	
	private static String TAG = "CommTool";
	
	public static final String PLATFORM = "android";
	public static String API_VERSION = "3.0";
	public static String telPhoneDevice;

	private static HashMap<Enum,Typeface> typefaceMap;

	/**
	 * 得到R中资源的ID
	 * <p>因打包到jar中后，访问其中资源的方式不一样，导致jar中的R文件资源定义无法使用，故通过该方法获取正确的资源</p>
	 * @param res_name 资源名
	 * @param type 资源类型
	 * @param res 资源
	 * @param package_name 包名
	 * @return 资源ID
	 */
	public static int getRID(String res_name, String type, Resources res, String package_name){
		return res.getIdentifier(res_name,type,package_name);
	}
	/**
	 * 
	* @Title: getRID 
	* @Description: 
	* @param @param type
	* @param @param ctx
	* @param @return 
	* @return int    返回类型 
	* @date 2014-3-12 上午8:48:05 
	* @throws
	 */
	public static int getRID(String res_name, String type, Context ctx){
		Resources res = ctx.getResources();
		String package_name = ctx.getPackageName();
		return res.getIdentifier(res_name,type,package_name);
	}
	
	
	/**
	 * 初始化
	 * @param context Context引用
	 */
	public static void initSystem(Context context){
		telPhoneDevice = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		
	}

	/**
	 * 拨打电话
	 * @param phoneNumber
	 */
	public static void call(final Activity aty, String phoneNumber){
		// 单个电话
		phoneNumber = phoneNumber.replaceAll("，", ",");
		if(phoneNumber.indexOf(",") == -1){
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			aty.startActivity(intent);
			return;
		}
		// 多个电话
		final String[] numbers = phoneNumber.split(",");
		new AlertDialog.Builder(aty)
				.setItems(numbers, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						// 拨打电话
						Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + numbers[i]));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						aty.startActivity(intent);
					}
				}).show();
	}
	
	/**
	 * 
	* @Title: hideSoftInput 
	* @Description: 隐藏键盘
	* @return void    返回类型
	* @throws
	 */
	public static void hideSoftInput(View view){
//		InputMethodManager imm = (InputMethodManager) ProjectApplication.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//		if(imm.isActive())
//			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/** 
	* 实现文本复制功能 
	* @param content 
	*/  
	public static void copy(String content, Context context){  
		// 得到剪贴板管理器  
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
		cmb.setPrimaryClip(ClipData.newPlainText(null, content));
	}  
	/** 
	* 实现粘贴功能 
	* @param context 
	* @return 
	*/  
	public static String paste(Context context){  
		// 得到剪贴板管理器  
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
		return cmb.getPrimaryClip().getItemAt(0).coerceToText(context).toString();
	} 
	/** 
	 * 调用系统界面，给指定的号码发送短信，并附带短信内容 
	 *  
	 * @param context 
	 * @param body
	 */  
	public static void sendSmsWithDefault(Context context, String body) {  
	    Intent intent = new Intent(Intent.ACTION_SEND);  
	    intent.setData(Uri.parse("smsto:"));  
	    intent.putExtra("sms_body", body);  
	    intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, body); // 文案
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(intent);  
	}  
	

	/**
	 * 得到默认的sd卡路径
	 * @return String 
	 */
	public static String getSDRoot(){
		return android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath();
	}
	
	/**
	 * 得到默认下载的sd卡路径
	 * @return String 
	 */
	public static String getDownloadFolder(){
		return getSDRoot()+File.separator+"diaoyu_pic";
	}

	/**
	 * 得到默认下载的sd卡路径
	 * @return String
	 */
	public static String getUserStoreFile(){
		return getSDRoot()+File.separator+".lchr"+File.separator+".dyr_user";
	}
	/**
	 * 得到数据库文件的存储路径
	 * 返回null时使用系统默认的路径"/data/data/"+context.getPackageName()+"/database"
	 * @return
	 */
	public static String getUserDatabaseFolder(){
		return getSDRoot() +File.separator+ ProjectApplication.mContext.getPackageName();
	}

	//is user login
	public static boolean isUserLogin(){
//		User user = ProjectApplication.getUser();
//		if(user.getToken()!=null && user.getToken_secret()!=null){
//			return true;
//		}
		return false;
	}

	//is user login
	public static boolean isUserLogin(Context context){
		return isUserLogin(context,false);
	}

	//is user login
	public static boolean isUserLogin(Context context,String fragmentName,Bundle bundle){
		return isUserLogin(context,false,fragmentName,bundle);
	}

	//is user login
	public static boolean isUserLogin(Context context,boolean isShow){
		return isUserLogin(context,false,null,null);
	}

	//is user login
	public static boolean isUserLogin(Context context,boolean isShow,String fragmentName,Bundle bundle){
		if(isUserLogin()){
			return true;
		}
//		if(isShow) {
//			ToastUtil.show(ProjectApplication.mContext, R.string.must_login);
//		}
//		Intent loginIntent = new Intent(context, UserLoginActivity.class);
//
//		if(fragmentName != null)
//			loginIntent.putExtra("fragment",fragmentName);
//		if(bundle != null)
//			loginIntent.putExtra("bundle", bundle);
//
//		loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(loginIntent);
//		if(context instanceof BaseParentFragmentActivity){
//			((BaseParentFragmentActivity)context).overrideUpPendingTransition();
//		}
		return false;
	}
	/**
	 * 
	* @Title: hideSoftInput 
	* @Description: 隐藏软键盘
	* @return void    返回类型
	* @throws
	 */
	public static void hideSoftInput(Activity activity){
		if(activity!=null){
			InputMethodManager iManager = ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE));
			if(iManager!=null && activity.getCurrentFocus()!=null && activity.getCurrentFocus().getWindowToken()!=null)
				iManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);   
		}
	}

	public static void hideSoftInput(Context activity,View view){
		if(activity!=null){
			InputMethodManager iManager = ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE));
			if(iManager!=null && view.getWindowToken()!=null)
				iManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 
	* @Title: hideSoftInput 
	* @Description: 隐藏软键盘
	* @return void    返回类型
	* @throws
	 */
	public static void showSoftInput(Context context,View view){
		if(context!=null){
			InputMethodManager iManager = ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE));
			iManager.showSoftInput(view,InputMethodManager.SHOW_FORCED);   
		}
	}

	public static String getTAG(Class clazz){
		return clazz.getName();
	}
	
	/*
	* check the app is installed
	*/
	public static boolean isAppInstalled(Context context,String packagename){
		PackageInfo packageInfo;        
		try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
         }catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
         }
         if(packageInfo ==null){
            //System.out.println("没有安装");
            return false;
         }else{
            //System.out.println("已经安装");
            return true;
        }
	}
	
	public static long getProcessTotalMemery() {
        return Runtime.getRuntime().totalMemory();
    }
	
	//cpu cunt
	public static int getNumCores() {
	    //Private Class to display only CPU devices in the directory listing
	    class CpuFilter implements FileFilter {
	        @Override
	        public boolean accept(File pathname) {
	            //Check if filename is "cpu", followed by a single digit number
	            if(Pattern.matches("cpu[0-9]", pathname.getName())) {
	                return true;
	            }
	            return false;
	        }      
	    }

	    try {
	        //Get directory containing CPU info
	        File dir = new File("/sys/devices/system/cpu/");
	        //Filter to only list the devices we care about
	        File[] files = dir.listFiles(new CpuFilter());
			DLog.d(TAG, "CPU Count: "+files.length);
	        //Return the number of cores (virtual CPU devices)
	        return files.length;
	    } catch(Exception e) {
	        //Print exception
			DLog.d(TAG, "CPU Count: Failed.");
	        e.printStackTrace();
	        //Default to return 1 core
	        return 1;
	    }
	}

//	public static Typeface getTypeFace(){
//
//		String de = "";
//
//		TYPE_FACE_ENUM face = ProjectConst.TYPE_FACE;
//
//		switch (face) {
//
//			case DEFAULT:
//				de = "fonts/regular.ttf";
//				break;
//			default:
//				de = "fonts/regular.ttf";
//				break;
//
//		}
//
//		if(typefaceMap.containsKey(face)){
//			return typefaceMap.get(face);
//		}else {
//			Typeface typeface = Typeface.createFromAsset(ProjectApplication.mContext.getAssets(), de);
//			typefaceMap.put(face,typeface);
//			return typeface;
//		}

//	}

	private static long lastClickTime;
	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if ( time - lastClickTime < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/** 设置Button:drawableTop属性 */
	public static void setButtonDrawableTop(Button btn, int drawableId){
		Drawable drawable = btn.getContext().getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		btn.setCompoundDrawables(null, drawable, null, null);
	}

	//getNetWork

	public static String GetNetworkType(Context ctx){

		String strNetworkType = "",_strSubTypeName ="";

		NetworkInfo networkInfo = ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()){

			_strSubTypeName = networkInfo.getSubtypeName();

			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){

				strNetworkType = "WIFI";

			}else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){

				DLog.i("CommTool", "Network getSubtypeName : " + _strSubTypeName);

				// TD-SCDMA   networkType is 17
				int networkType = networkInfo.getSubtype();
				switch (networkType) {
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
						strNetworkType = "2G";
						break;
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
					case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
					case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
						strNetworkType = "3G";
						break;
					case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
						strNetworkType = "4G";
						break;
					default:
						// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
						if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
						{
							strNetworkType = "3G";
						}
						else
						{
							strNetworkType = _strSubTypeName;
						}

						break;
				}

				DLog.i("CommTool", "Network getSubtype : " + Integer.valueOf(networkType).toString());
			}
		}

		DLog.i("CommTool", "Network Type : " + strNetworkType);

		return networkInfo==null?"null":strNetworkType+" ["+networkInfo.getType()+" "+networkInfo.getTypeName()+" "+networkInfo.getSubtypeName()+" "+networkInfo.getSubtype()+"]";
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 验证密码是否符合标准
	 * @param pwd
	 * @return
	 */
	public static boolean verifyPassword(String pwd){
		Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}


	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
}
