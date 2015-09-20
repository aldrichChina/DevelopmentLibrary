package com.apkdv.tour.model;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.ab.activity.AbActivity;
import com.apkdv.tour.entity.User;

public class MyApplication extends Application {
	public static ArrayList<AbActivity> activities = new ArrayList<AbActivity>();
	public static String USER_NAME;
	public static User user = new User();
	public static MyApplication instance;
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	
	@SuppressWarnings("deprecation")
	public static void exit(){
		ActivityManager manager = (ActivityManager) instance.getSystemService(Context.ACTIVITY_SERVICE);   
		manager.restartPackage(instance.getPackageName()); 
//		System.exit(0);
//		for (AbActivity iterable : activities) {
//			iterable.finish();
//		}
//		MainPagerActivity.instance.finish();
//		
	}

}
