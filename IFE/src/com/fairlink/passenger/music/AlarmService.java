package com.fairlink.passenger.music;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmService {

	public static void enabledAlarm(long timeInMillis, Context context) {

		Intent intent = new Intent(context, MyAlarmBroadCast.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		// 获取闹钟管理的实例
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		long curLongTime = System.currentTimeMillis();

		if (timeInMillis >= curLongTime) {
			am.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
		} else {// 第二天响
			am.set(AlarmManager.RTC_WAKEUP,
					(timeInMillis + AlarmManager.INTERVAL_DAY), pendingIntent);
		}
	}
}
