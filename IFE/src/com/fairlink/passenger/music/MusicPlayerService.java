package com.fairlink.passenger.music;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fairlink.passenger.RecommendGetRecently;
import com.fairlink.passenger.networkrequest.MusicLocalCacheManager;
import com.fairlink.passenger.networkrequest.MusicListRequest.MusicInfo;
import com.fairlink.passenger.util.Constant;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

public class MusicPlayerService extends Service implements OnPreparedListener,
		OnErrorListener, OnBufferingUpdateListener, OnCompletionListener {


	public static final int PLAY_RECYCLE = 1;
	public static final int PLAY_RECYCLE_ONE = 2;

	private ArrayList<MusicInfo> mPlayList;
	private static MusicPlayerService sInstance;

	public static interface MusicStateCallback {
		public void onError(int errorcode);

		public void onPrepared();

		public void onUpdated(int progress);

		public void onCompelted();

		public void onStopPlaying();
	}

	private MediaPlayer mMediaPlayer;
	private ArrayList<MusicStateCallback> mCallbacks = new ArrayList<MusicPlayerService.MusicStateCallback>();

	private MusicInfo mCurrentMusic;

	private boolean mPrepared = false;
	private boolean mPlayErr = false;
	private int mPlayMode = PLAY_RECYCLE;

	private LocalBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {
		public MusicPlayerService getService() {
			return MusicPlayerService.this;
		}
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (mMediaPlayer != null) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
			mCurrentMusic = null;
			mPrepared = false;
			for (MusicStateCallback callback : mCallbacks) {
				callback.onStopPlaying();
			}
		}

	};

	public void onCreate() {
		super.onCreate();

		mPlayList = new ArrayList<MusicInfo>();
		
		IntentFilter filter = new IntentFilter(Constant.Action.ACTION_PLAY_VIDEO);
		registerReceiver(mReceiver, filter);
		sInstance = MusicPlayerService.this;
	}
	
	public void onDestroy() {
		super.onDestroy();
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		unregisterReceiver(mReceiver);
	}

	public static MusicPlayerService getInstance() {
		if(null == sInstance) {
			sInstance = new MusicPlayerService();
		}
		return sInstance;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void addCallback(MusicStateCallback callback) {
		if (!mCallbacks.contains(callback)) {
			mCallbacks.add(callback);
		}
	}

	public void removeCallback(MusicStateCallback callback) {
		mCallbacks.remove(callback);
	}

	public void setNextPlayMode() {
		if (mPlayMode == PLAY_RECYCLE) {
			mPlayMode = PLAY_RECYCLE_ONE;
		} else {
			mPlayMode = PLAY_RECYCLE;
		}
	}

	public int getPlayMode() {
		return mPlayMode;
	}

	public void setPlayList(ArrayList<MusicInfo> list) {
		mPlayList = list;
	}

	public void setUri(MusicInfo uri) {
		try {
			if (mMediaPlayer != null) {
				mMediaPlayer.reset();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnErrorListener(this);
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			int index = uri.location.lastIndexOf("/");
			String name = uri.location.substring(index);
			File file = MusicLocalCacheManager.getInstance().getMusicFile(name);
			if (file.exists()) {
				mMediaPlayer.setDataSource(file.getAbsolutePath());
			} else {
				mMediaPlayer.setDataSource(this, Uri.parse(uri.location));
				MusicLocalCacheManager.getInstance()
						.downloadMusic(uri.location);
			}
			mMediaPlayer.prepareAsync();
			mCurrentMusic = uri;
			mPrepared = false;
			MusicSelectedShow(uri.image, uri);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			mCurrentMusic = null;
			for (MusicStateCallback mCallback : mCallbacks) {
				mCallback.onError(1);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			mCurrentMusic = null;
			for (MusicStateCallback mCallback : mCallbacks) {
				mCallback.onError(1);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
			mCurrentMusic = null;
			for (MusicStateCallback mCallback : mCallbacks) {
				mCallback.onError(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
			mCurrentMusic = null;
			for (MusicStateCallback mCallback : mCallbacks) {
				mCallback.onError(1);
			}
		}
	}

	// 首页推荐音乐
	public void MusicSelectedShow(String img, MusicInfo info) {

		new RecommendGetRecently("MUS", info.type, info.id, img,null);
		
		Intent intent = new Intent(Constant.Action.ACTION_RECOMMEND);  
	    intent.putExtra("TYPE_RECOMMEND", "MUS");  
	    this.sendBroadcast(intent);  
		
	}

	public boolean isPlaying() {
		if (mCurrentMusic == null || mMediaPlayer == null) {
			return false;
		}
		return mMediaPlayer.isPlaying();
	}

	public void start() {
		if (mMediaPlayer != null) {
			mMediaPlayer.start();
		}
	}

	public int getCurrentPercent() {
		if (mCurrentMusic == null || !mPrepared || mMediaPlayer == null) {
			return 0;
		}
		int duration = mMediaPlayer.getDuration();
		return mMediaPlayer.getCurrentPosition() * 100 / duration;
	}

	public int getCurrentPosition() {
		if (mCurrentMusic == null || !mPrepared || mMediaPlayer == null) {
			return 0;
		}
		return mMediaPlayer.getCurrentPosition();
	}

	public MusicInfo getCurrentMusic() {
		return mCurrentMusic;
	}

	public String getCurrentType(){
		return mCurrentMusic.type;
	}
	
	public void pause() {
		if (mMediaPlayer != null) {
			mMediaPlayer.pause();
		}
	}

	public void seekToPercent(int progress) {
		if (!mPrepared || mMediaPlayer == null) {
			return;
		}
		int duration = mMediaPlayer.getDuration();
		mMediaPlayer.seekTo(duration * progress / 100);
	}

	public void seekToSec(int msec) {
		if (!mPrepared || mMediaPlayer == null) {
			return;
		}
		int duration = mMediaPlayer.getDuration();
		int current = mMediaPlayer.getCurrentPosition();
		int target = current + msec;
		if (target < 0) {
			target = 0;
		}
		if (target > duration) {
			target = duration;
		}
		mMediaPlayer.seekTo(target);
	}

	public void stop() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
		}
	}

	

	
	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		for (MusicStateCallback mCallback : mCallbacks) {
			mCallback.onError(arg1);
			mPlayErr = true;
		}
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mPrepared = true;
		mPlayErr = false;
		start();
		for (MusicStateCallback mCallback : mCallbacks) {
			mCallback.onPrepared();
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		for (MusicStateCallback mCallback : mCallbacks) {
			mCallback.onUpdated(arg1);
		}

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		
		if(!mPlayErr) {
			if (mPlayMode == PLAY_RECYCLE_ONE) {
				if (mMediaPlayer != null) {
					mMediaPlayer.start();
				}
			} else {
				setUri(getNextSong());
			}
			for (MusicStateCallback mCallback : mCallbacks) {
				mCallback.onCompelted();
			}
		}
		
	}

	public MusicInfo getNextSong() {
		if (mCurrentMusic == null) {
			return mPlayList.get(0);
		}
		int index = 0;
		for (MusicInfo item : mPlayList) {
			if (MusicInfo.equals(item, mCurrentMusic)) {
				break;
			}
			index++;
		}
		if (index == mPlayList.size()) {
			return mPlayList.get(0);
		}
		index = (index + 1) % mPlayList.size();
		return mPlayList.get(index);
	} 

	public MusicInfo getPrevSong() {
		if (mCurrentMusic == null) {
			return mPlayList.get(0);
		}
		int index = 0;
		for (MusicInfo item : mPlayList) {
			if (MusicInfo.equals(item, mCurrentMusic)) {
				break;
			}
			index++;
		}
		if (index == mPlayList.size()) {
			return mPlayList.get(0);
		}
		index = (mPlayList.size() + index - 1) % mPlayList.size();
		return mPlayList.get(index);
	}
}
