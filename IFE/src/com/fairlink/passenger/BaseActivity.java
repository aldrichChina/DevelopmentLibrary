package com.fairlink.passenger;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.fairlink.passenger.util.Logger;

public class BaseActivity extends Activity {
	private static List<Activity> activityList = new ArrayList<Activity>();
	protected Logger logger = new Logger(this, "activity");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityList.add(this);
		logger.info("onCreate");
	}

	@Override
	protected void onResume() {
		super.onResume();
		logger.info("onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		logger.info("onPause");
	}

	@Override
	protected void onDestroy() {
		activityList.remove(this);
		logger.info("onDestroy");
		super.onDestroy();
	}

	public static void clearAllActivity() {
		for (Activity activity : activityList) {
			if (activity instanceof LoginMain == false) {
				activity.finish();
			}
		}
	}

	public void onBackMainListener(View v) {
		clearAllActivity();
	}
}
