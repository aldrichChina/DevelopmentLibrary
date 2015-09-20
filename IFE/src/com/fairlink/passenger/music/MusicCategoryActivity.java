package com.fairlink.passenger.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;

import com.fairlink.analytics.Analytics;
import com.fairlink.analytics.AnalyticsType;
import com.fairlink.passenger.AdvertisementActivity;
import com.fairlink.passenger.R;
import com.fairlink.passenger.music.MusicCategoryFragment.MusicCategorySelectedListener;
import com.fairlink.passenger.music.MusicListFragment.MusListListener;
import com.fairlink.passenger.util.ComUtil;

public class MusicCategoryActivity extends AdvertisementActivity implements MusicCategorySelectedListener,
		MusListListener {

	private MusicListFragment mListFragment;
	private MusicCategoryFragment mCategoryFragment;
	private int mCurrentMenu = 0;
	private MsgReceiver msgReceiver;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_category_layout);

		mListFragment = (MusicListFragment) getFragmentManager().findFragmentById(R.id.music_list);
		mCategoryFragment = (MusicCategoryFragment) getFragmentManager().findFragmentById(R.id.music_category);
		mCategoryFragment.ShowVolumeImg(((AudioManager) getSystemService(Context.AUDIO_SERVICE))
				.getStreamVolume(AudioManager.STREAM_MUSIC));

		msgReceiver = new MsgReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.fairlink.passenger.musictime");
		registerReceiver(msgReceiver, intentFilter);
	}

	protected void onDestroy() {
		unregisterReceiver(msgReceiver);
		super.onDestroy();
	}

	@Override
	public void onMusicCategorySelected(int current) {
		if (isFinishing()) {
			return;
		}

		if (current != mCurrentMenu) {
			mCurrentMenu = current;
			mListFragment.setMusicCategory(current);
		}

	}

	private class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			mCategoryFragment.ShowMyRadio();
			mCategoryFragment.MusicTime();
			mListFragment.onTimeStop();

		}
	}

	@Override
	public void MusicTypeSelected(String type, boolean playing) {
		mCategoryFragment.MusicShowPlaying(type, playing);

	}

	@Override
	public void Analytics(String typeMus, String musName, int type) {

		Analytics.logEvent(this, AnalyticsType.getOperationVideoMus(type), AnalyticsType.ORIGIN_MUSIC,
				AnalyticsType.cperationData(ComUtil.getMusType(type), musName));

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			((AudioManager) getSystemService(Context.AUDIO_SERVICE)).adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
			mCategoryFragment.ShowVolumeImg(((AudioManager) getSystemService(Context.AUDIO_SERVICE))
					.getStreamVolume(AudioManager.STREAM_MUSIC));

			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			((AudioManager) getSystemService(Context.AUDIO_SERVICE)).adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
			mCategoryFragment.ShowVolumeImg(((AudioManager) getSystemService(Context.AUDIO_SERVICE))
					.getStreamVolume(AudioManager.STREAM_MUSIC));
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
