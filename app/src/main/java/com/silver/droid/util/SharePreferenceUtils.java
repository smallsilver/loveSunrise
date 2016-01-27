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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.silver.droid.ProjectApplication;

import java.util.Set;
import java.util.TreeSet;

public class SharePreferenceUtils {
	static SharedPreferences a;
	static SharedPreferences.Editor b;

	private static SharedPreferences.Editor a() {
		if (b == null)
			b = a(ProjectApplication.mContext).edit();
		return b;
	}

	private static SharedPreferences a(Context paramContext) {
		if (a == null)
			a = PreferenceManager.getDefaultSharedPreferences(paramContext);
		return a;
	}


	public static boolean getSharePreferencesBoolValue(String paramString) {
		SharedPreferences localSharedPreferences = a(ProjectApplication.mContext);
		boolean bool = false;
		if (localSharedPreferences != null)
			bool = localSharedPreferences.getBoolean(paramString, false);
		return bool;
	}

	public static int getSharePreferencesIntValue(String paramString) {
		SharedPreferences localSharedPreferences = a(ProjectApplication.mContext);
		int i = 0;
		if (localSharedPreferences != null)
			i = localSharedPreferences.getInt(paramString, 0);
		return i;
	}

	public static long getSharePreferencesLongValue(String paramString) {
		long l = 0L;
		SharedPreferences localSharedPreferences = a(ProjectApplication.mContext);
		if ((localSharedPreferences != null)
				&& (localSharedPreferences.contains(paramString)))
			l = localSharedPreferences.getLong(paramString, l);
		return l;
	}

	public static String getSharePreferencesValue(String paramString) {
		String str = "";
		SharedPreferences localSharedPreferences = a(ProjectApplication.mContext);
		if (localSharedPreferences != null)
			str = localSharedPreferences.getString(paramString, "");
		return str;
	}


	public static Set<String> getSharePreferencesSetValue(String paramString) {
		Set<String> str = new TreeSet<String>();
		SharedPreferences localSharedPreferences = a(ProjectApplication.mContext);
		if (localSharedPreferences != null)
			str = localSharedPreferences.getStringSet(paramString, str);
		return str;
	}


	public static void remove(String paramString) {
		a().remove(paramString).commit();
	}

	public static void setSharePreferencesValue(String paramString, int paramInt) {
		a().putInt(paramString, paramInt);
		a().commit();
	}

	public static void setSharePreferencesValue(String paramString,
			long paramLong) {
		a().putLong(paramString, paramLong);
		a().commit();
	}

	public static void setSharePreferencesValue(String paramString1,
			String paramString2) {
		a().putString(paramString1, paramString2);
		a().commit();
	}

	public static void setSharePreferencesValue(String paramString,
			boolean paramBoolean) {
		a().putBoolean(paramString, paramBoolean);
		a().commit();
	}

	public static void setSharePreferencesSetValue(String paramString,
												Set<String> paramSet) {
		a().putStringSet(paramString, paramSet);
		a().commit();
	}
}