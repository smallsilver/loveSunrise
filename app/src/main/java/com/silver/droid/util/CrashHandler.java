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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.text.format.Time;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.TreeSet;

public class CrashHandler implements UncaughtExceptionHandler {

	/** Debug Log tag*/ 
	public static final String TAG = "CrashHandler"; 
	/** 是否开启日志输出,在Debug状态下开启, 
	* 在Release状态下关闭以提示程序性能 
	* */ 
	public static final boolean DEBUG = true; 
	/** 系统默认的UncaughtException处理类 */ 
	private UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */ 
	private static CrashHandler INSTANCE; 
	/** 程序的Context对象 */ 
	private Context mContext; 
	/** 使用Properties来保存设备的信息和错误堆栈信息*/ 
	private Properties mDeviceCrashInfo = new Properties(); 
	private static final String VERSION_NAME = "versionName"; 
	private static final String VERSION_CODE = "versionCode"; 
	private static final String STACK_TRACE = "STACK_TRACE"; 
	/** 错误报告文件的扩展名 */ 
	private static final String CRASH_REPORTER_EXTENSION = ".cr"; 
	
	/** 保证只有一个CrashHandler实例 */ 
	private CrashHandler() {} 
	
	/** 获取CrashHandler实例 ,单例模式*/ 
	public static CrashHandler getInstance() { 
		if (INSTANCE == null) { 
			INSTANCE = new CrashHandler(); 
		} 
		return INSTANCE; 
	} 
	
	/** 
	* 初始化,注册Context对象, 
	* 获取系统默认的UncaughtException处理器, 
	* 设置该CrashHandler为程序的默认处理器 
	* @param ctx 
	*/ 
	public void init(Context ctx) { 
		mContext = ctx; 
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler(); 
		Thread.setDefaultUncaughtExceptionHandler(this); 
		File crashFile = new File(CommTool.getUserDatabaseFolder());
		if(!crashFile.exists())
			crashFile.mkdirs();
	} 
	
	/** 
	* 当UncaughtException发生时会转入该函数来处理 
	*/ 
	@Override 
	public void uncaughtException(Thread thread, Throwable ex) { 
		if (!handleException(ex) && mDefaultHandler != null) { 
			//如果用户没有处理则让系统默认的异常处理器来处理 
			mDefaultHandler.uncaughtException(thread, ex); 
		} else { 
			//Sleep一会后结束程序 
			try { 
				Thread.sleep(5000); 
			} catch (InterruptedException e) { 
				DLog.e(TAG, e); 
			} 
			android.os.Process.killProcess(android.os.Process.myPid()); 
			System.exit(10); 
		} 
	} 
	
	/** 
	* 自定义错误处理,收集错误信息 
	* 发送错误报告等操作均在此完成. 
	* 开发者可以根据自己的情况来自定义异常处理逻辑 
	* @param ex 
	* @return true:如果处理了该异常信息;否则返回false 
	*/ 
	private boolean handleException(Throwable ex) { 
		if (ex == null) { 
			DLog.w(TAG, "handleException --- ex==null"); 
			return true; 
		} 
		final String msg = ex.getLocalizedMessage(); 
		if(msg == null) {
			return false;
		}
		//使用Toast来显示异常信息 
		new Thread() { 
			@Override 
			public void run() { 
				Looper.prepare(); 
				Toast toast = Toast.makeText(mContext, "程序出错，即将退出:\r\n" + msg,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				Looper.loop(); 
			} 
		}.start(); 
		//收集设备信息 
		collectCrashDeviceInfo(mContext); 
		//保存错误报告文件 
		saveCrashInfoToFile(ex); 
		//发送错误报告到服务器 
		sendCrashReportsToServer(mContext);
		return true; 
	} 
	
	/** 
	* 把错误报告发送给服务器,包含新产生的和以前没发送的. 
	* @param ctx 
	*/ 
	private void sendCrashReportsToServer(Context ctx) { 
//		new CrashHandleReport(mDeviceCrashInfo.getProperty(STACK_TRACE),null).execute();
	} 
	
	
	/** 
	* 获取错误报告文件名 
	* @param ctx 
	* @return 
	*/ 
	private String[] getCrashReportFiles(Context ctx) { 
		File filesDir = new File(CommTool.getUserDatabaseFolder());//ctx.getFilesDir(); 
		FilenameFilter filter = new FilenameFilter() { 
			public boolean accept(File dir, String name) { 
				return name.endsWith(CRASH_REPORTER_EXTENSION); 
			} 
		}; 
		return filesDir.list(filter); 
	} 
	
	/** 
	* 保存错误信息到文件中 
	* @param ex 
	* @return 
	*/ 
	private String saveCrashInfoToFile(Throwable ex) { 
		Writer info = new StringWriter(); 
		PrintWriter printWriter = new PrintWriter(info); 
		ex.printStackTrace(printWriter); 
		Throwable cause = ex.getCause(); 
		while (cause != null) { 
			cause.printStackTrace(printWriter); 
			cause = cause.getCause(); 
		} 
		String result = info.toString(); 
		printWriter.close(); 
		mDeviceCrashInfo.put("EXEPTION", ex.getLocalizedMessage());
		mDeviceCrashInfo.put(STACK_TRACE, result); 
		if(DEBUG)
			DLog.e(TAG,result);
		try { 
			//long timestamp = System.currentTimeMillis(); 
			Time t = new Time("GMT+8"); 
			t.setToNow(); // 取得系统时间
			int date = t.year * 10000 + t.month * 100 + t.monthDay;
			int time = t.hour * 10000 + t.minute * 100 + t.second;
//			CommSqlHelper.getUserDatabaseFolder()+File.separator+
			String fileName = CommTool.getUserDatabaseFolder()+File.separator+"crash-" + date + "-" + time + CRASH_REPORTER_EXTENSION; 
//			FileOutputStream trace = mContext.openFileOutput(fileName,Context.MODE_PRIVATE); 
			FileOutputStream trace = new FileOutputStream(new File(fileName));
			mDeviceCrashInfo.store(trace, ""); 
			trace.flush(); 
			trace.close(); 
			return fileName; 
		} catch (Exception e) { 
			DLog.e("an error occured while writing report file...", e); 
		} 
		return null; 
	} 

	/** 
	* 收集程序崩溃的设备信息 
	* 
	* @param ctx 
	*/ 
	public void collectCrashDeviceInfo(Context ctx) { 
		try { 
			PackageManager pm = ctx.getPackageManager(); 
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 
					PackageManager.GET_ACTIVITIES); 
			if (pi != null) { 
				mDeviceCrashInfo.put(VERSION_NAME, 
						pi.versionName == null ? "not set" : pi.versionName+"_"+foConst.CHANNEL_ID); 
				mDeviceCrashInfo.put(VERSION_CODE, ""+pi.versionCode); 
			} 
		} catch (NameNotFoundException e) { 
			DLog.e("Error while collect package info", e); 
		} 
		//使用反射来收集设备信息.在Build类中包含各种设备信息, 
		//例如: 系统版本号,设备生产商 等帮助调试程序的有用信息 
		Field[] fields = Build.class.getDeclaredFields(); 
		for (Field field : fields) { 
			try { 
				field.setAccessible(true); 
				mDeviceCrashInfo.put(field.getName(), ""+field.get(null)); 
				if (DEBUG) { 
					DLog.i(TAG,field.getName() + " : " + field.get(null)); 
				} 
			} catch (Exception e) { 
				DLog.e("Error while collect crash info", e); 
			} 
		} 
	}
	

	// 上传错误日志
	public void reportError(){
		String[] crFiles = getCrashReportFiles(mContext); 
		if (crFiles != null && crFiles.length > 0) { 
			TreeSet<String> sortedFiles = new TreeSet<String>(); 
			sortedFiles.addAll(Arrays.asList(crFiles)); 
			collectCrashDeviceInfo(mContext);
			for (String fileName : sortedFiles) {
				String filePath = CommTool.getUserDatabaseFolder()+File.separator+fileName;
				String error = FileUtil.readFileByChars(filePath);
//				CrashHandleReport report = new CrashHandleReport(error,filePath);
//				report.execute();
			} 
		}
	}
//
//	// 错误日志上传
//	public class CrashHandleReport implements HAHttpTaskPrePlugin,HAHttpTaskPostPlugin{
//
//		public String error_content = "";
//
//		public String filePath = "";
//
//		public CrashHandleReport(String error,String path){
//			super();
//			error_content = error;
//			filePath = path;
//		}
//
//		public void execute(){
//			HAHttpTask task = HttpRequest.getInstance().makeTask(this, ProjectConst.DIAOYU_PATH_LOG_ERROR_REPORT,ProjectConst.DIAOYU_PATH_LOG_ERROR_REPORT,HAHttpTask.HttpMethodPost,this,this);
//			HttpRequest.getInstance().executeTask(task);
////			ProjectApplication.httpTaskExecutor.executeTask(task);
//		}
//
//		@Override
//		public void onHttpTaskPostPluginExecute(HAHttpTask task,
//				HAHttpTask.HAHttpTaskResponse response) {
//			String requestData = "";
//			try {
//				requestData = new String(response.data);
//				DLog.i(getClass().getName(), requestData);
//				if (response.statusCode == 200) { // 网络请求成功，解析数据
//					JSONObject jsonObject = new JSONObject(requestData);
//					int code = jsonObject.optInt("code");
//					if(code > 0){
//						FileUtil.deleteFile(filePath);
//					}
//				}else{
//
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		@Override
//		public void onHttpTaskPrePluginExecute(HAHttpTask task,
//				HAHttpTask.HAHttpTaskRequest request) {
//			HashMap<String, String> params = new HashMap<String, String>();
//			params.put("device_model", mDeviceCrashInfo.getProperty("MODEL"));
//			params.put("app_version", mDeviceCrashInfo.getProperty("versionName"));
//			params.put("system_version", mDeviceCrashInfo.getProperty("FINGERPRINT"));
//			params.put("error_content", error_content);
//			params.put("network_type",NetWorkUtil.isWifi(mContext)?"2":"1");
//			request.params = params;
////			request.params = CommTool.generateXsign(request.url, params, ProjectApplication.getUser().getToken(), ProjectApplication.getUser().getToken_secret());
//		}
//	}
}

