/*
 *
 * Created by smallsilver on 1/6/16 3:22 PM
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

package com.silver.droid;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class ProjectApplication extends Application {

//    private RefWatcher refWatcher;
//    public static RefWatcher getRefWatcher(Context context) {
//        ProjectApplication application = (ProjectApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }

    public static Context mContext;

    // deal over 65535 method
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {

        super.onCreate();

        mContext = this.getApplicationContext();

//        Fresco.initialize(mContext);
//
//        EmoticonsUtils.initEmoticonsDB(getApplicationContext());
//
////        refWatcher = LeakCanary.install(this);
//
//        CrashHandler.getInstance().init(ProjectApplication.mContext);
//
//        SDKInitializer.initialize(this);
//
//        Const.IS_ONLINE = false;
//
//        Const.DEBUG = true;
//
//        String channel = "";
//
////        ApplicationInfo appInfo = null;
//        try {
////            appInfo = getPackageManager().getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA);
////            String channel = appInfo.metaData.getString("UMENG_CHANNEL");
////            if(channel == null){
////                channel = String.valueOf(appInfo.metaData.getInt("UMENG_CHANNEL"));
////            }
//
//            channel = ChannelUtil.getChannel(mContext);
//
//            AnalyticsConfig.setAppkey("557ac0e067e58e089e002feb");
//            AnalyticsConfig.setChannel(channel);
//
//            DLog.d(ProjectApplication.class.getName(), "channel_"+channel);
//            Const.CHANNEL_ID = channel;
//            foConst.CHANNEL_ID = channel;
//            foConst.CHANNEL_NAME = "channel_"+channel;
//            foConst.CHANNEL_ID_OPEN_COUNT = foConst.CHANNEL_ID+"_open_count";
//        } catch (Exception e) {
//
//        }
//
//        foConst.DEBUG = Const.DEBUG;
//
//        ProjectConst.init(mContext);
//
//        PackageInfo packageInfo = this.getPackageInfo();
//
//        if (packageInfo != null) {
//            Const.PACKAGE_NAME = packageInfo.packageName;
//            Const.VERSION_CODE = packageInfo.versionCode;
//            Const.VERSION_NAME = packageInfo.versionName;
//        }
//
//        Const.DEVICE_ID = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//
//        mLocationClient = new LocationClient(mContext);
//
//        mMyLocationListener = new MyLocationListener();
//
//        mLocationClient.registerLocationListener(mMyLocationListener);
//
//        mGeofenceClient = new GeofenceClient(getApplicationContext());
//        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
////        SDKInitializer.initialize(this);
////        mLocationClient.start();
//
//        // dong 添加URL跳转定义
//        HAURLAction.HAURLActionCenterProtocol fishingProtocol = new HAURLAction.HAURLActionCenterProtocol();
//        fishingProtocol.protocol = "fishing";
//        fishingProtocol.entryMap.put("userprofile", UserProfileAction.class.getName());
//        HAURLAction.HAURLActionCenter.getInstance().actionProtocolArray.add(fishingProtocol);
//
//        user = UserUtil.getUser();
//
//        // 在主进程设置信鸽相关的内容
//        if (isMainProcess()) {
//            // 为保证弹出通知前一定调用本方法，需要在application的onCreate注册
//            // 收到通知时，会调用本回调函数。
//            // 相当于这个回调会拦截在信鸽的弹出通知之前被截取
//            // 一般上针对需要获取通知内容、标题，设置通知点击的跳转逻辑等等
//            XGPushManager
//                    .setNotifactionCallback(new XGPushNotifactionCallback() {
//
//                        @Override
//                        public void handleNotify(XGNotifaction xGNotifaction) {
//                            Log.i("test", "处理信鸽通知：" + xGNotifaction);
//                            // 获取标签、内容、自定义内容
//                            String title = xGNotifaction.getTitle();
//                            String content = xGNotifaction.getContent();
//                            String customContent = xGNotifaction.getCustomContent();
//                            // 其它的处理
//                            // 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。
//                            xGNotifaction.doNotify();
//                        }
//                    });
//
//            DLog.i("ProjectApplication", "ProjectApplication--> " + XGPushConfig.getToken(this));
//        }
//
//        // modify TODO
//        String isCover = SharePreferenceUtils.getSharePreferencesValue(CityDBManager.CITY_DB_IS_COVER);
//
//        if("".equals(isCover)) {
//            SharePreferenceUtils.setSharePreferencesValue(CityDBManager.CITY_DB_IS_COVER, "1");
//        }

    }

    private PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    public boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}