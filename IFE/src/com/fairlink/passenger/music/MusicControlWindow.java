package com.fairlink.passenger.music;

import com.fairlink.passenger.R;
import com.fairlink.passenger.music.DialogMusic.TimeDialogcallback;
import com.fairlink.passenger.music.MusicPlayerService.MusicStateCallback;
import com.fairlink.passenger.view.VolumeDialog;
import com.fairlink.passenger.view.VolumeDialog.Dialogcallback;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicControlWindow implements OnClickListener, OnTouchListener,
		MusicStateCallback {

	private WindowManager mWM;
	private View mView;
	private Context mContext;
	private boolean mIsShowing = false;
	private MusicPlayerService mService;
	private WindowManager.LayoutParams mParam = new LayoutParams();
	private View mPrev, mNext;
	private ImageView mPlay, mSleep, mVoice;
	private TextView mTitle;
	private int mScreenWidth;
	private AudioManager mAudiomanage;
	private int currentVolume = 0;
	public boolean isPlaying = false;
	private boolean finishplay = false;

	public MusicControlWindow(Context context) {
		mContext = context;
		mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	@SuppressWarnings("deprecation")
	public void showMusicControl() {
		if (mIsShowing) {
			return;
		}
		mService = MusicPlayerService.getInstance();
		if (mService == null) {
			return;
		}
		if (mService.getCurrentMusic() == null) {
			return;
		}

		mService.addCallback(this);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = inflater.inflate(R.layout.music_control_layout, null);
		mPrev = mView.findViewById(R.id.prev_music);
		mPlay = (ImageView) mView.findViewById(R.id.play_pause);
		mNext = mView.findViewById(R.id.next_music);
		mSleep = (ImageView) mView.findViewById(R.id.music_sleep_control);
		mVoice = (ImageView) mView.findViewById(R.id.music_voice_control);

		mAudiomanage = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		currentVolume = mAudiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
		ShowVolumeImg(currentVolume);
		mTitle = (TextView) mView.findViewById(R.id.title);
		mTitle.setText(mService.getCurrentMusic().name + "\n"
				+ mService.getCurrentMusic().actor);
		mTitle.setOnTouchListener(this);

		mVoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				DialogVolume();
			}

		});

		if (AlarmData.AlarmDataGetEnable()) {
			mSleep.setVisibility(View.VISIBLE);
		} else {
			mSleep.setVisibility(View.INVISIBLE);
		}
		mSleep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				DialogMusic();
			}

		});
		if (mService.isPlaying()) {
			isPlaying= true;
			mPlay.setImageResource(R.drawable.music_control_pause);
		} else {
			isPlaying= false;
			mPlay.setImageResource(R.drawable.music_control_play);
		}

		mPrev.setOnClickListener(this);
		mPlay.setOnClickListener(this);
		mNext.setOnClickListener(this);

		mParam.width = LayoutParams.WRAP_CONTENT;
		mParam.height = LayoutParams.WRAP_CONTENT;
		mParam.gravity = Gravity.TOP | Gravity.RIGHT;
		mScreenWidth = mWM.getDefaultDisplay().getWidth();
		mParam.x = mScreenWidth / 2 - 500;
		mParam.y = 20;
		mParam.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mParam.alpha = 1f;
		mParam.format = PixelFormat.RGBA_8888;

		mWM.addView(mView, mParam);
		mIsShowing = true;
	}

	private void ShowVolumeImg(int currentVolume) {
		if (currentVolume == 0) {
			mVoice.setImageResource(R.drawable.player_volume_img_0);
		} else if (currentVolume > 0 && currentVolume <= 5) {
			mVoice.setImageResource(R.drawable.player_volume_img_1);
		} else if (currentVolume > 5 && currentVolume <= 9) {
			mVoice.setImageResource(R.drawable.player_volume_img_small);
		} else if (currentVolume > 9 && currentVolume <= 14) {
			mVoice.setImageResource(R.drawable.player_volume_img_2);
		} else if (currentVolume == 15) {
			mVoice.setImageResource(R.drawable.player_volume_img_big);
		}
	}

	public void DialogVolume() {
		VolumeDialog n = new VolumeDialog(mContext, R.style.dialog);
		n.setCancelable(true);
		n.setDialogCallback(volcallback);
		n.show();
	}

	protected View findViewById(int rl) {
		// TODO 自动生成的方法存根
		return null;
	}

	public void DialogMusic() {
		DialogMusic m = new DialogMusic(mContext, R.style.dialog);
		m.setCancelable(true);
		m.setDialogCallback(timecallback);
		m.show();
	}

	public void dismissMusicControl() {
		if (!mIsShowing) {
			return;
		}
		mService.removeCallback(this);
		mWM.removeView(mView);
		mIsShowing = false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prev_music:
			mService.setUri(mService.getPrevSong());
			break;
		case R.id.next_music:
			mService.setUri(mService.getNextSong());
			break;
		case R.id.play_pause:
			if (mService.isPlaying()) {
				isPlaying= false;
				mService.pause();
				mPlay.setImageResource(R.drawable.music_control_play);
			} else {
				isPlaying= true;
				mService.start();
				mPlay.setImageResource(R.drawable.music_control_pause);
			}
			break;
		}

	}

	private int mStartX;
	private int mStartY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mStartX = (int) event.getX();
			mStartY = (int) event.getY();
			mTitle.setTextColor(Color.YELLOW);
			return true;
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			mTitle.setTextColor(Color.WHITE);
		}
		ViewGroup g = (ViewGroup) v.getParent();

		mParam.x = (int) (mScreenWidth - event.getRawX() + mStartX - g
				.getWidth());
		mParam.y = (int) (event.getRawY() - mStartY);
		mWM.updateViewLayout(mView, mParam);
		return true;
	}

	@Override
	public void onError(int errorcode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrepared() {
		mPlay.setEnabled(true);
		mPlay.setImageResource(R.drawable.music_control_pause);
		mTitle.setText(mService.getCurrentMusic().name + "\n"
				+ mService.getCurrentMusic().actor);
	}

	@Override
	public void onUpdated(int progress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompelted() {
		if (mService.getPlayMode() == MusicPlayerService.PLAY_RECYCLE) {
			mPlay.setEnabled(false);
			mPlay.setImageResource(R.drawable.music_control_play);
		}
	}

	@Override
	public void onStopPlaying() {
		dismissMusicControl();

	}

	Dialogcallback volcallback = new Dialogcallback() {

		@Override
		public void dialogdo(int i) {
			// TODO 自动生成的方法存根
			ShowVolumeImg(i);
		}
	};

	TimeDialogcallback timecallback = new TimeDialogcallback() {

		@Override
		public void timedialogdo() {
			// TODO 自动生成的方法存根
			if (AlarmData.AlarmDataGetEnable()) {
				mSleep.setImageResource(R.drawable.time_normal);
				mSleep.setVisibility(View.VISIBLE);
			} else {
				mSleep.setVisibility(View.INVISIBLE);
			}
		}
	};

	public void pause() {
		if (mService.isPlaying()) {
			finishplay =true;
			mService.pause();
			mPlay.setImageResource(R.drawable.music_control_play);
		}
	}

	public void play() {

		if (finishplay) {
				mService.start();
				mPlay.setImageResource(R.drawable.music_control_pause);
				finishplay =false;

		}

	}

}
