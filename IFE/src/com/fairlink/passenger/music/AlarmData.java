package com.fairlink.passenger.music;

public class AlarmData {

	private static int mHour = 0;
	private static int mMinute = 0;
	private static boolean mIsEnabled = false;
	
	public  AlarmData (int hour,int minute, boolean isEnabled){
		mHour = hour;
		mMinute = minute;
		mIsEnabled = isEnabled;
	}
	public static int AlarmDataGetHour(){
		return mHour;
	}
	
	public static int AlarmDataGetMinute(){
		return mMinute;
	}
	public static boolean AlarmDataGetEnable(){
		return mIsEnabled;
	}
}
