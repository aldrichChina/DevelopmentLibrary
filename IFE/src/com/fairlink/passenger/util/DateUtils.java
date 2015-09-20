package com.fairlink.passenger.util;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @ClassName ： DateUtils
 * @Description: 日期转换工具类
 * @author ： jiaxue
 * @date ： 2014-12-31 下午3:45:02
 */

@SuppressLint("SimpleDateFormat")
public class DateUtils {

	// 字符串类型日期转换成date类型
	public static Date strToDate(String style, String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	// date类型转换成字符串类型日期
	public static String dateToUTCString(String style, Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));  
		return formatter.format(date);
	}
	
	public static String dateToString(String style, Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		return formatter.format(date);
	}
	
	

	// 获取calendar的日期
	public static String clanderTodatetime(Calendar calendar, String style) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		return formatter.format(calendar.getTime());
	}

}
