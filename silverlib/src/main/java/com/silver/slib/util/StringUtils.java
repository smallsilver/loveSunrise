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

import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	/**
	 * 
	 * @param cTime
	 * @param format
	 *            "yyyy/MM/dd HH:mm"
	 * @return
	 */
	public static String FormatUnixTime(long cTime, String format) {
		Timestamp time = new Timestamp(cTime * 1000);
		String stime = "";
		SimpleDateFormat formatString = new SimpleDateFormat(format);
		stime = formatString.format(time);
		return stime;
	}
	
	/**
	 * 获取随机串
	 * 
	 * @return
	 */
	public static String getRandomString() {
		StringBuffer result = new StringBuffer();
		result.append(String.valueOf((int) (Math.random() * 10))).append("_");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		result.append(str);
		
		return result.toString();
	}
	
	/**
	 * 检查email
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String strEmail) {
		if (TextUtils.isEmpty(strEmail)) {
			return false;
		}
		
		String strPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}
	
	/**
	 * 检查手机号
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isPhone(String content) {
		if (TextUtils.isEmpty(content)) {
			return false;
		}
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(content);
		if (!m.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 检查密码(0-9a-zA-Z, 6~20)
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isPassword(String content) {
		if (TextUtils.isEmpty(content)) {
			return false;
		}
		Pattern p = Pattern.compile("^([0-9a-zA-Z])*$");
		Matcher m = p.matcher(content);
		if (!m.matches()) {
			return false;
		}
		p = Pattern.compile("[\\d\\D]{6,20}");
		m = p.matcher(content);
		if (!m.matches()) {
			return false;
		}
		return true;
	}
	
	public static boolean isAlias(String content, boolean cantNull) {
		
		if (!cantNull) {
			if (TextUtils.isEmpty(content)) {
				return false;
			}
		} else {
			if (TextUtils.isEmpty(content)) {
				return true;
			}
			Pattern p = Pattern.compile("^([\u4e00-\u9fa5]|[0-9a-zA-Z])+$");
			Matcher m = p.matcher(content);
			if (!m.matches()) {
				return false;
			}
		}
		return true;
		
	}
	
	/**
	 * MD5
	 * 
	 * @param input
	 * @return
	 */
	public static String md5(String input) {
		String result = input;
		if (input != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(input.getBytes());
				BigInteger hash = new BigInteger(1, md.digest());
				result = hash.toString(16);
				if ((result.length() % 2) != 0) {
					result = "0" + result;
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 将key-value形式的参数集转换成用&号链接的URL查询参数形式。
	 * ***生成请求时，不需要做encode
	 * @param parameters
	 *            key-value形式的参数集
	 * @return 用&号链接的URL查询参数
	 */
	public static String encodeUrl(HashMap<String, String> parameters) {
		if (parameters == null) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Entry<String, String> entry : parameters.entrySet()) {
			if (first) {
				first = false;
			} else {
				if (TextUtils.isEmpty(entry.getKey())) {
					continue;
				} else {
					sb.append("&");
				}
			}
			String key = entry.getKey();
			String value = entry.getValue();
			if (value == null) {
				value = "";
			}
			sb.append(URLEncoder.encode(key)).append("=").append(URLEncoder.encode(value));
		}
		return sb.toString();
	}
	
	/**
     * ***生成请求时，不需要做encode
	 * @param value
	 * @return
	 */
	public static String encodeUrl(String value) {
		if (value == null) {
			return "";
		}
		String encoded = null;
		try {
			encoded = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException ignore) {
		}
		StringBuffer buf = new StringBuffer(encoded.length());
		char focus;
		for (int i = 0; i < encoded.length(); i++) {
			focus = encoded.charAt(i);
			if (focus == '*') {
				buf.append("%2A");
			} else if (focus == '+') {
				buf.append("%20");
			} else if (focus == '%' && (i + 1) < encoded.length() && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
				buf.append('~');
				i += 2;
			} else {
				buf.append(focus);
			}
		}
		return buf.toString();
	}
	
	/**
	 * 将用&号链接的URL参数转换成key-value形式的参数集
	 * 
	 * @param s
	 *            将用&号链接的URL参数
	 * @return key-value形式的参数集
	 */
	public static HashMap<String, String> decodeUrl(String s) {
		HashMap<String, String> params = new HashMap<String, String>();
		if (s != null) {
			String array[] = s.split("&");
			for (String parameter : array) {
				String v[] = parameter.split("=");
				if (v.length > 1) {
					params.put(v[0], URLDecoder.decode(v[1]));
				}
			}
		}
		return params;
	}
	
	/**
	 * 将URL中的查询串转换成key-value形式的参数集
	 * 
	 * @param url
	 *            待解析的url
	 * @return key-value形式的参数集
	 */
	public static HashMap<String, String> parseUrl(String url) {
		url = url.replace("/#", "?");
		try {
			URL u = new URL(url);
			HashMap<String, String> b = decodeUrl(u.getQuery());
			HashMap<String, String> ref = decodeUrl(u.getRef());
			if (ref != null)
				b.putAll(ref);
			return b;
		} catch (MalformedURLException e) {
			return new HashMap<String, String>();
		}
	}
	
	/**
	 * check if the string contains chinese character
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese(String str) {
		if (str == null || str.trim().length() <= 0) {
			return false;
		}
		
		int len = str.length();
		for (int i = 0; i < len; i++) {
			char word = str.charAt(i);
			if ((word >= 0x4e00) && (word <= 0x9fbb)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * check if the string is int value
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (TextUtils.isEmpty(str)) {
			return false;
		}
		
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * 
	* @Title: isNumber 
	* @Description: 检查是否是数字
	* @param @param str
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	public static boolean isNumber(String str) {
		if (TextUtils.isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[\\d]*$");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * check if the string is double or float
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str) {
		if (TextUtils.isEmpty(str)) {
			return false;
		}
		
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}
	
	/**
	  * 借助字节输出流ByteArrayOutputStream来实现字节数组的合并
	  * */
	 public static byte[] streamCopy(List<byte[]> srcArrays) {   
	     byte[] destAray = null;   
	     ByteArrayOutputStream bos = new ByteArrayOutputStream();   
	     try {
	         for (byte[] srcArray:srcArrays) {   
	             bos.write(srcArray);           
	         }   
	         bos.flush();   
	         destAray = bos.toByteArray();   
	     } catch (IOException e) {   
	         e.printStackTrace();   
	     } finally {   
	         try {   
	             bos.close();   
	             bos = null;
	         } catch (IOException e) {   
	         }   
	     }   
	     return destAray;
	 }
	 
	 public static String getPriceStr(String str1){
		 	str1 = new StringBuilder(str1).reverse().toString();     //先将字符串颠倒顺序  
	        String str2 = "";  
	        for(int i=0;i<str1.length();i++){  
	            if(i*3+3>str1.length()){  
	                str2 += str1.substring(i*3, str1.length());  
	                break;  
	            }  
	            str2 += str1.substring(i*3, i*3+3)+",";  
	        }  
	        if(str2.endsWith(",")){  
	            str2 = str2.substring(0, str2.length()-1);  
	        }  
	        //最后再将顺序反转过来  
	        return new StringBuilder(str2).reverse().toString(); 
	 }

	public static String getEndPointStr(String str){
		if(str == null)
			return str;
		if(str.length() > 4){
			return str.substring(0,3)+"..";
		}
		return str;
	}
}