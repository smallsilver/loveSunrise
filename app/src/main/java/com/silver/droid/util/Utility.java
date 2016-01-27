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

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utility {
    public static void Assert(boolean cond) {
        if (!cond) {
            throw new AssertionError();
        }
    }

    public static void showToast(Context context, String message, boolean longDuration) {
        Toast.makeText(context, message, longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static void showAlert(Context context, String message) {
        new Builder(context).setTitle("提示").setPositiveButton("确定", null).setMessage(message).create().show();
    }

    public static boolean isServiceBooted(String name, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> serviceList = am.getRunningServices(30);
        for (RunningServiceInfo info : serviceList) {
            if (info.service.getClassName().equals(name)) {
                return true;
            }
        }
        return false;
    }

//    public void createShortCut(Class<Activity> launchActivityClass) {
//        SharedPreferences spf = foApplication.getInstance().getApplicationContext().getSharedPreferences("firstLaunch", Context.MODE_PRIVATE);
//        boolean isFirst = spf.getBoolean("isFirstLaunch", true);
//        if (isFirst) {
//            Intent shortCutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
//            //不允许重复创建
//            shortCutIntent.putExtra("duplicate", false);
//            shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, foApplication.getInstance().getApplicationContext().getString(R.string.app_name));
//            Parcelable icon = Intent.ShortcutIconResource.fromContext(foApplication.getInstance().getApplicationContext(), R.drawable.icon);
//            shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
//            Intent intent = new Intent(foApplication.getInstance().getApplicationContext(), launchActivityClass);
//            intent.setAction("android.intent.action.MAIN");
//            intent.addCategory("android.intent.category.LAUNCHER");
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
//            foApplication.getInstance().getApplicationContext().sendBroadcast(shortCutIntent);
//        }
//
//        SharedPreferences.Editor edit = spf.edit();
//        edit.putBoolean("isFirstLaunch", false);
//        edit.commit();
//    }

    /**
     * 安装APK文件
     */
    public static void installApk(Context context, String filePath) {
        File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            return;
        }

        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
    }

    /**
     * 判断有没有SD卡
     * 
     * @return Boolean
     */
    public static final boolean isSDcardExist() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        return sdCardExist;
    }

    /**
     * 程序是否在前台运行
     * 
     * @return
     */
    public static boolean isAppOnForeground(Context applicationContext) {
        ActivityManager activityManager = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = applicationContext.getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

//    /**
//     * 获取本机mac地址
//     *
//     * @return
//     */
//    public static String getLocalMacAddress() {
//        WifiManager wifi = (WifiManager) ProjectApplication.mContext.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = wifi.getConnectionInfo();
//        return info.getMacAddress();
//    }

    /**
     * 递归删除文件及文件夹
     * 
     * @param file
     */
    public static void recursionDeleteFile(File file) {
    	try {
	        if (file.isFile()) {
	            file.delete();
	            return;
	        }
	
	        if (file.isDirectory()) {
	            File[] childFiles = file.listFiles();
	            if (childFiles == null || childFiles.length == 0) {
	                file.delete();
	                return;
	            }
	
	            for (int i = 0; i < childFiles.length; i++) {
	                recursionDeleteFile(childFiles[i]);
	            }
	
	            file.delete();
	        }
		} catch (Exception e) {
			// TODO: handle exception
		}
    }

    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
