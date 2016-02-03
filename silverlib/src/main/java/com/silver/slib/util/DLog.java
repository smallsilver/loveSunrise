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

import android.util.Log;
import com.silver.slib.Const;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DLog {

  private static final String LOG_FILE =
      CommTool.getSDRoot() + File.separator + "silver_lib_log.txt";

  private static Date date;

  private static SimpleDateFormat sd = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS] ");

  public static void i(String paramString2) {
    if (!Const.DEBUG) return;
    Log.i("DLog-->", paramString2);
  }

  public static void i(String paramString1, String paramString2) {
    if (!Const.DEBUG) return;
    Log.i(paramString1, paramString2);
  }

  public static void i(String paramString1, String paramString2, Exception paramException) {
    if (!Const.DEBUG) return;
    Log.i(paramString1, paramException.toString() + ":  [" + paramString2 + "]");
  }

  public static void d(String paramString1, String paramString2) {
    if (!Const.DEBUG) return;
    Log.d(paramString1, paramString2);
  }

  public static void d(String paramString1, String paramString2, Object[] paramArrayOfObject) {
    if (!Const.DEBUG) return;
    date = new Date();
    String str = String.format(paramString2, paramArrayOfObject);
    Log.d(paramString1, sd.format(date) + str);
  }

  public static void e(String paramString1, String paramString2) {
    if (!Const.DEBUG) return;
    Log.e(paramString1, paramString2);
  }

  public static void e(String paramString1, Exception e) {
    if (!Const.DEBUG) return;
    Log.e(paramString1, e.getMessage(), e);
  }

  public static void e(String paramString1, String paramString2, Throwable paramThrowable) {
    if (!Const.DEBUG) return;
    Log.e(paramString1, paramString2, paramThrowable);
  }

  public static void f(String paramString1, String paramString2) {
    if (!Const.DEBUG) return;
    try {
      FileWriter localFileWriter = new FileWriter(LOG_FILE, true);
      localFileWriter.write(sd.format(new Date()) + paramString1 + "\n\t" + paramString2 + "\n");
      localFileWriter.close();
      return;
    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }

  public static void w(String paramString1, String paramString2) {
    if (!Const.DEBUG) return;
    Log.w(paramString1, sd.format(new Date()) + paramString2);
  }

  public static void w(String paramString1, String paramString2, Object[] paramArrayOfObject) {
    if (!Const.DEBUG) return;

    date = new Date();
    String str = String.format(paramString2, paramArrayOfObject);
    Log.w(paramString1, sd.format(date) + str);
  }
}