package com.fairlink.passenger.music;

import com.fairlink.passenger.util.Constant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmBroadCast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
		if (AlarmData.AlarmDataGetEnable() == true) {
			context.sendBroadcast(new Intent(Constant.Action.ACTION_PLAY_VIDEO));
			context.sendBroadcast(new Intent("com.fairlink.passenger.musictime"));
			new AlarmData(0, 0, false);
		}
	}

}
