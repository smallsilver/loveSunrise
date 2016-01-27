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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 查询当前日期前(后)x天的日期
	 * @param n
	 * @return
	 */
	public static Date afterNDayOfData(int n){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, n);
		return c.getTime();
	}

	/**
	 * 查询当前日期前(后)x天的日期
	 * @param n
	 * @return
	 */
	public static String afterNDayOfStr(int n){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(afterNDayOfData(n));
	}

	/**
	 * 获取当前日期是星期几<br>
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/*
	 * 根据字符串和转换格式，将字符串转换为日期
	 * @param strDate 待转换字符串日期
	 * @param format  待转换的格式，如：HH:mm
	 */
	public static Date getDateFromStr(String strDate,String format){
		if(strDate==null || strDate.equals("")) return null;
		if( format == null || format.equals("")) 
			format = "yyyy-MM-dd HH:mm:ss";
		
		SimpleDateFormat sformat= new SimpleDateFormat(format);
		
		Date retDate  =  sformat.parse(strDate,new ParsePosition(0));
		return retDate;
	}
	
	public static Date getDateFromStr(String strDate)
	{
		return getDateFromStr(strDate,null);
	}
	
	/*
	 * 根据日期和转换格式，将日期转换为字符串
	 * @param date    待转换日期
	 * @param format  待转换的格式，如：HH:mm
	 */
	public static String getStrFromDate(Date date,String format){
		if(date == null) return "";
		if(format == null || format.equals("")) 
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sformat = new SimpleDateFormat(format);
		return sformat.format(date);
	}
	/*
	 * 根据日期和待加减的秒，计算相加减后的日期
	 * @param date    待转换日期
	 * @param day     相加减的秒
	 */
	public static Date getDateByAddSecond(Date date,int second){
		Calendar calendar=Calendar.getInstance();   
	    calendar.setTime(date); 
	    calendar.set(Calendar.SECOND,calendar.get(Calendar.SECOND)+second);//让日期加1 
	    return calendar.getTime();//(Calendar.DATE);
//	    start = new Date(start.getTime()+ rm.startBaseday*24*60*60*1000);
	}
	public static String getStrFromDate(Date date)
	{
		return getStrFromDate(date,null);
	}
	
	/*
	 * 根据日期和待加减的天数，计算相加减后的日期
	 * @param date    待转换日期
	 * @param day     相加减的天数
	 */
	public static Date getDateByAddDay(Date date,int day){
		if(date==null) return null;
		Calendar calendar=Calendar.getInstance();   
	    calendar.setTime(date); 
	    calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+day);//让日期加1 
	    return calendar.getTime();//(Calendar.DATE);
//	    start = new Date(start.getTime()+ rm.startBaseday*24*60*60*1000);
	}
	
	/*
	 * 根据日期和待加减的小时，计算相加减后的日期
	 * @param date    待转换日期
	 * @param day     相加减的小时
	 */
	public static Date getDateByAddHour(Date date,int hour){
		Calendar calendar=Calendar.getInstance();   
	    calendar.setTime(date); 
	    calendar.set(Calendar.HOUR,calendar.get(Calendar.HOUR)+hour);//让日期加1 
	    return calendar.getTime();//(Calendar.DATE);
//	    start = new Date(start.getTime()+ rm.startBaseday*24*60*60*1000);
	}
	
	/*
	 * 根据日期和待加减的分钟，计算相加减后的日期
	 * @param date    待转换日期
	 * @param day     相加减的分钟
	 */
	public static Date getDateByAddMinu(Date date,int minute){
		Calendar calendar=Calendar.getInstance();   
	    calendar.setTime(date); 
	    calendar.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE)+minute);//让日期加1 
	    return calendar.getTime();//(Calendar.DATE);
//	    start = new Date(start.getTime()+ rm.startBaseday*24*60*60*1000);
	}
	
	/*
	 * 得到两个日期相差的秒数
	 * @param startDate    日期1
	 * @param endDate      日期2
	 */
	public static int secondsBetween(Date startDate,Date endDate){
		long sec = (startDate.getTime() - endDate.getTime())/1000;
		if (sec <= 0 && sec > -1)
            sec = 1;
        return Math.abs((int)sec);
	}
	/*
	 * 得到两个日期相差的秒数
	 * @param startDate    日期1
	 * @param endDate      日期2
	 */
	public static int secondsBetween(String startDateStr,String endDateStr){
		Date startDate = getDateFromStr(startDateStr,null);
		Date endDate = getDateFromStr(endDateStr,null);
		if(startDate==null)
			return -1;
		if(endDate==null)
			return -1;
		return secondsBetween(startDate,endDate);
	}
	
	/*
	 * 得到两个日期相差的分钟数
	 * @param startDate    日期1
	 * @param endDate      日期2
	 */
	public static int minutsBetween(Date startDate,Date endDate){
        return secondsBetween(startDate,endDate)/60;
	}

	/*
	 * 得到系统默认Date最大日期，java中不存在，先默认一个
	 */
	public static Date maxDate(){
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse("9999-12-31");
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String getNowString(){
		return getStrFromDate(new Date(),null);
	}
}
