package com.sunrise.lovesunrise.app;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.silver.slib.Const;
import com.silver.slib.util.ChannelUtil;
import com.silver.slib.util.CrashHandler;
import com.silver.slib.util.DLog;
import com.umeng.analytics.AnalyticsConfig;

/**
 * @desc 系统 application
 * @author wangde
 * @version 1.0
 * @date 15/01/28
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        Const.mContext = getApplicationContext();

        CrashHandler.getInstance().init(Const.mContext);

        //SDKInitializer.initialize(this);

        Const.IS_ONLINE = false;

        Const.DEBUG = true;

        String channel = "";

        try {

            channel = ChannelUtil.getChannel(this);

            if(TextUtils.isEmpty(channel)){
                channel = "silver_test";
            }

            AnalyticsConfig.setAppkey(Const.mContext,"557ac0e067e58e089e002feb");

            AnalyticsConfig.setChannel(channel);

            DLog.d(getClass().getName(), "channel_"+channel);
            Const.CHANNEL_ID = channel;
        } catch (Exception e) {

        }

        //ProjectConst.init(mContext);

        PackageInfo packageInfo = this.getPackageInfo();

        if (packageInfo != null) {
            Const.PACKAGE_NAME = packageInfo.packageName;
            Const.VERSION_CODE = packageInfo.versionCode;
            Const.VERSION_NAME = packageInfo.versionName;
        }

        Const.DEVICE_ID = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        //
        //mLocationClient = new LocationClient(mContext);
        //
        //mMyLocationListener = new MyLocationListener();
        //
        //mLocationClient.registerLocationListener(mMyLocationListener);
        //
        //mGeofenceClient = new GeofenceClient(getApplicationContext());
        //// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        ////        SDKInitializer.initialize(this);
        ////        mLocationClient.start();
        //
        //// dong 添加URL跳转定义
        //HAURLAction.HAURLActionCenterProtocol fishingProtocol = new HAURLAction.HAURLActionCenterProtocol();
        //fishingProtocol.protocol = "fishing";
        //fishingProtocol.entryMap.put("userprofile", UserProfileAction.class.getName());
        //HAURLAction.HAURLActionCenter.getInstance().actionProtocolArray.add(fishingProtocol);
        //
        //user = UserUtil.getUser();
        //
        //// modify TODO
        //String isCover = SharePreferenceUtils.getSharePreferencesValue(CityDBManager.CITY_DB_IS_COVER);
        //
        //if("".equals(isCover)) {
        //    SharePreferenceUtils.setSharePreferencesValue(CityDBManager.CITY_DB_IS_COVER, "1");
        //}
        //
        //mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

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

}