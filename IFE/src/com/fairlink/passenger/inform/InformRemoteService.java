package com.fairlink.passenger.inform;

import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.fairlink.passenger.music.MusicControlWindow;
import com.fairlink.passenger.util.Constant;

public class InformRemoteService extends Service {

	private MulticastReceived mReceiver;

	private InformReceiver informReceiver;
	private NoticeReceiver noticeReceiver;
	private static final String SHOW_INFORM = "show inform";
	private MusicControlWindow mMusicControlWindow;

	private class InformReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			mMusicControlWindow.play();
		}
	}

	/** 机上广播通知 */
	private class NoticeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (mMusicControlWindow.isPlaying) {
				mMusicControlWindow.pause();
			}

			if (InformDialog.showInformDialog) {
				Intent i = new Intent(InformRemoteService.this, InformDialog.class);
				i.putExtra("type_notice", intent.getStringExtra("type_notice"));
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				InformDialog.showInformDialog = false;
			}
		}

	};
	@Override
	public void onCreate() {

		super.onCreate();

		try {
			mReceiver = new MulticastReceived(this);
		} catch (IOException e) {
			e.printStackTrace();
		}

		mMusicControlWindow = new MusicControlWindow(this);

		informReceiver = new InformReceiver();
		IntentFilter filterInf = new IntentFilter();
		filterInf.addAction(Constant.Action.ACTION_INFORM);
		registerReceiver(informReceiver, filterInf);

		noticeReceiver = new NoticeReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SHOW_INFORM);
		registerReceiver(noticeReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(noticeReceiver);
		unregisterReceiver(informReceiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		return START_STICKY;
	}
}
