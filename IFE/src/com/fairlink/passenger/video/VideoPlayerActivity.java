package com.fairlink.passenger.video;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.analytics.Analytics;
import com.fairlink.analytics.AnalyticsType;
import com.fairlink.passenger.BaseActivity;
import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.R;
import com.fairlink.passenger.networkrequest.ADNewPicRequest;
import com.fairlink.passenger.networkrequest.ADNewPicRequest.ADNEW;
import com.fairlink.passenger.networkrequest.ADNewVideoRequest;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.DownloadTask;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.networkrequest.PhotoManager;
import com.fairlink.passenger.networkrequest.PhotoManager.PhotoDownloadCallback;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.ShopUtil;
import com.fairlink.passenger.video.exoplayerwrapper.DashRendererBuilder;
import com.fairlink.passenger.video.exoplayerwrapper.DefaultRendererBuilder;
import com.fairlink.passenger.video.exoplayerwrapper.DemoPlayer;
import com.fairlink.passenger.video.exoplayerwrapper.DemoPlayer.RendererBuilder;
import com.fairlink.passenger.video.exoplayerwrapper.HlsRendererBuilder;
import com.fairlink.passenger.video.exoplayerwrapper.UnsupportedDrmException;
import com.fairlink.passenger.video.exoplayerwrapper.Util;
import com.fairlink.passenger.view.DialogLoading;
import com.fairlink.passenger.view.VideoDialog;
import com.fairlink.passenger.view.VideoDialog.OnSureListener;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.VideoSurfaceView;

public class VideoPlayerActivity extends BaseActivity implements DemoPlayer.Listener, OnClickListener, HttpTaskCallback,
		NetworkRequestAPI, PhotoDownloadCallback {

	private VideoSurfaceView mVideoView;
	private DemoPlayer mVideoPlayer;

	private View mVolumeBrightnessLayout;
	private ImageView mOperationBg;
	private ImageView mOperationPercent;
	private AudioManager mAudioManager;
	/** 最大声音 */
	private int mMaxVolume;
	/** 当前声音 */
	private int mVolume = -1;
	/** 当前亮度 */
	private float mBrightness = -1f;
	private GestureDetector mGestureDetector;

	private View controlView;
	private PopupWindow controler;
	private TextView durationTextView;
	private TextView playedTextView;
	private SeekBar durationSeekBar;
	private SeekBar blightSeekBar;
	int Max_Brightness = 100;
	float fBrightness = -1f;
	WindowManager.LayoutParams lp;
	private SeekBar volumeSeekBar;
	private ImageView mPlayed;
	private ImageView mAddGo;
	private ImageView mAddBack;
	private boolean isPaused;

	private String name; // 视频名称
	private String videoId; // 视频id
	private int videoType; // 视频类型
	private String videoImg; // 视频图片
	private String mVideoPath; // 视频地址

	private long mLastPosition = 0;
	private boolean isPlayingAd = false;
	private boolean mShowing = false;
	private TextView mADTime;

	private final static int HIDE_MESSAGE = 1;
	private final static int UPDAT_TIME_MESSAGE = 2;
	private final static int PROGRESS_CHANGED = 3;
	private final static int VIDEO_DURATION = 4;

	private static final int AD_TIME = 90;
	private DialogLoading diaLoading;

	private View mAD;
	private ImageView mADPic;
	private String adsExternalUrl;
	private int relatedType;

	/* Handler信息传递，用来监听和实现播放视频区域的实时变化 */
	@SuppressLint("HandlerLeak")
	Handler _handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case VIDEO_DURATION:
				long duration = mVideoPlayer.getDuration();
				if (duration > 0) {

					if (isPlayingAd) {
						long position = mVideoPlayer.getCurrentPosition();
						mADTime.setVisibility(View.VISIBLE);
						updateLeftADtime(duration, position);

					} else {
						int time = (int)mVideoPlayer.getDuration();
						durationSeekBar.setMax(time);
						durationTextView.setText(ComUtil.Time(time));
						_handler.removeMessages(VIDEO_DURATION);
						_handler.sendEmptyMessage(PROGRESS_CHANGED);
					}

				} else {
					_handler.sendEmptyMessageDelayed(VIDEO_DURATION, 500);
				}

				break;

			case PROGRESS_CHANGED:

				int time = (int)mVideoPlayer.getCurrentPosition();
				if (time < 0) {
					time = 0;
				}
				durationSeekBar.setProgress(time);
				playedTextView.setText(ComUtil.Time(time));

				sendEmptyMessageDelayed(PROGRESS_CHANGED, 500);
				break;

			case HIDE_MESSAGE:
				hide();
				break;

			case UPDAT_TIME_MESSAGE:
				updateLeftADtime(mVideoPlayer.getDuration(), mVideoPlayer.getCurrentPosition());
				break;

			}

			super.handleMessage(msg);
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_player);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		initData();
		initView();
		setVolum();
		setListener();

		new ADNewVideoRequest(this, this, 2).execute((String) null);
		mShowing = false;
		diaLoading = new DialogLoading(this, new DialogLoading.LoadingListener() {

			@Override
			public void onCancel() {
				VideoPlayerActivity.this.finish();
			}
		});
		diaLoading.show();

		mVideoView.getHolder().addCallback(new SurfaceHolder.Callback() {
			public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			}

			public void surfaceCreated(SurfaceHolder holder) {
				if (mVideoPlayer != null) {
					mVideoPlayer.setSurface(holder.getSurface());
				}
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				if (mVideoPlayer != null) {
					mVideoPlayer.blockingClearSurface();
				}
			}
		});
		isPaused = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		DownloadTask.resume();
		_handler.removeMessages(UPDAT_TIME_MESSAGE);
		if (!isPlayingAd) {
			mLastPosition = mVideoPlayer.getCurrentPosition();
			long duration = mVideoPlayer.getDuration();
			if (mLastPosition >= duration-500) {
				mLastPosition = 0;
			}
			IFEApplication.getInstance().setVideoPosition(mVideoPath, mLastPosition);
		}
		mVideoPlayer.getPlayerControl().pause();
		// mVideoView.suspend();

	}


	@Override
	protected void onResume() {
		super.onResume();
		DownloadTask.suspend();
       mLastPosition = IFEApplication.getInstance().getVideoPosition(mVideoPath);
		if (mVideoPlayer != null) {
			mVideoPlayer.setBackgrounded(false);
			if (!isPaused) {
				mVideoPlayer.getPlayerControl().start();
			}
			mVideoView.requestFocus();
		} else {
			setVideoURI(mVideoPath, mLastPosition);
		}
		mPlayed.setImageResource(isPaused ? R.drawable.movie_control_pause :
				R.drawable.movie_control_play);
		hideControllerDelay();

		_handler.sendEmptyMessageDelayed(UPDAT_TIME_MESSAGE, 1000);
	}

	@Override
	protected void onDestroy() {
		finishEvent();
		if (diaLoading != null) {
			diaLoading.dismiss();
		}
		releasePlayer();

		super.onDestroy();
	}

	private void initData() {
		name = getIntent().getStringExtra("name");
		videoId = getIntent().getStringExtra("id");
		videoType = getIntent().getIntExtra("type", 0);
		videoImg = getIntent().getStringExtra("img");
		mVideoPath = getIntent().getStringExtra("url");
	}

	private void initView() {
		mVideoView = (VideoSurfaceView) findViewById(R.id.video_player);
		mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
		mOperationBg = (ImageView) findViewById(R.id.operation_bg);
		mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
		mADTime = (TextView) findViewById(R.id.ad_time);

		/** 开始、暂停、快进、快退 */
		controlView = getLayoutInflater().inflate(R.layout.movie_control_layout, null);
		controler = new PopupWindow(controlView);
		durationTextView = (TextView) controlView.findViewById(R.id.duration);
		playedTextView = (TextView) controlView.findViewById(R.id.has_played);
		mPlayed = (ImageView) controlView.findViewById(R.id.movic_control_played);
		mAddGo = (ImageView) controlView.findViewById(R.id.addgo);
		mAddBack = (ImageView) controlView.findViewById(R.id.addback);
		blightSeekBar = (SeekBar) controlView.findViewById(R.id.movie_control_bright);
		volumeSeekBar = (SeekBar) controlView.findViewById(R.id.movie_control_volume);
		durationSeekBar = (SeekBar) controlView.findViewById(R.id.movie_control_duration);

		// ad
		mAD = findViewById(R.id.pic_ad);
		mADPic = (ImageView) mAD.findViewById(R.id.pic);
		mADPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!ComUtil.isEmpty(adsExternalUrl)) {
					ShopUtil.showShop(VideoPlayerActivity.this, relatedType, adsExternalUrl);
					hidePicAD();
				}

			}
		});

	}

	private void setListener() {
		mGestureDetector = new GestureDetector(this, new MyGestureListener());

		durationSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				hideControllerDelay();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				cancelDelayHide();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {

					mVideoPlayer.seekTo(progress);
				}
			}
		});
		lp = getWindow().getAttributes();
		blightSeekBar.setMax(Max_Brightness);
		fBrightness = getWindow().getAttributes().screenBrightness;
		if (fBrightness < 0) {
			if (fBrightness <= 0.00f)
				fBrightness = 0.50f;
			if (fBrightness < 0.01f)
				fBrightness = 0.01f;
		}
		blightSeekBar.setProgress((int) (fBrightness * Max_Brightness));
		blightSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				hideControllerDelay();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				cancelDelayHide();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				fBrightness = (float) progress / (float) Max_Brightness;
				if (fBrightness <= 0.01f)
					fBrightness = 0.01f;
				lp.screenBrightness = fBrightness;
				// 这句得加上，否则屏幕亮度不启作用
				getWindow().setAttributes(lp);

			}
		});

		volumeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				hideControllerDelay();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				cancelDelayHide();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

			}
		});

		mPlayed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelDelayHide();
				if (isPaused) {
					// 使广告消失
					hidePicAD();
					mVideoPlayer.getPlayerControl().start();
					mVideoView.requestFocus();
					mPlayed.setImageResource(R.drawable.movie_control_play);
					hideControllerDelay();
				} else {
					getADPic();
					mVideoPlayer.getPlayerControl().pause();
					mPlayed.setImageResource(R.drawable.movie_control_pause);
				}

				Analytics.logEvent(VideoPlayerActivity.this, AnalyticsType.getOperationVideoMus(isPaused ? 4 : 5), AnalyticsType.ORIGIN_VIDEO,
						AnalyticsType.cperationData(ComUtil.getVideoType(videoType), name));

				isPaused = !isPaused;
			}
		});

		mAddGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelDelayHide();
				hideControllerDelay();
				addgo();
			}
		});

		mAddBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelDelayHide();
				hideControllerDelay();
				addback();

			}
		});

	}

	private void keepScreenOn(boolean screenOn) {
		if (screenOn && mVideoPlayer.getPlayerControl().isPlaying()) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	// DemoPlayer.Listener implementation

	@Override
	public void onStateChanged(boolean playWhenReady, int playbackState) {
		switch(playbackState) {
			case ExoPlayer.STATE_BUFFERING:
				keepScreenOn(true);
				if (mVideoPlayer.getDuration() == 0) {
					diaLoading.hide();
					_handler.sendEmptyMessageDelayed(VIDEO_DURATION, 500);
				} else {
					diaLoading.show();
				}
				break;
			case ExoPlayer.STATE_ENDED:
				keepScreenOn(false);
				ADisPlaying();
				break;
			case ExoPlayer.STATE_IDLE:
				keepScreenOn(false);
				break;
			case ExoPlayer.STATE_PREPARING:
				keepScreenOn(true);
				diaLoading.show();
				break;
			case ExoPlayer.STATE_READY:
				keepScreenOn(true);
				diaLoading.hide();
				if (isPlayingAd) {
					long duration = mVideoPlayer.getDuration();
					long position = mVideoPlayer.getCurrentPosition();
					mADTime.setVisibility(View.VISIBLE);
					updateLeftADtime(duration, position);

				} else {
					int time = (int)mVideoPlayer.getDuration();
					durationSeekBar.setMax(time);
					durationTextView.setText(ComUtil.Time(time));
					_handler.sendEmptyMessage(PROGRESS_CHANGED);
				}
				break;
			default:
				keepScreenOn(false);
				break;
		}
	}

	@Override
	public void onError(Exception e) {
		if (e instanceof UnsupportedDrmException) {
			// Special case DRM failures.
			UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
			int stringId = unsupportedDrmException.reason == UnsupportedDrmException.REASON_NO_DRM
					? R.string.video_player_info_drm_error_not_supported
					: unsupportedDrmException.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
					? R.string.video_player_info_drm_error_unsupported_scheme
					: R.string.video_player_info_drm_error_unknown;
			Toast.makeText(getApplicationContext(), stringId, Toast.LENGTH_LONG).show();
		}
		diaLoading.hide();
		showErrorDialog();
	}

	@Override
	public void onVideoSizeChanged(int width, int height, float pixelWidthAspectRatio) {
		mVideoView.setVideoWidthHeightRatio(
				height == 0 ? 1 : (width * pixelWidthAspectRatio) / height);
	}

	private void updateLeftADtime(long duration, long position) {
		if (isFinishing()) {
			return;
		}
		long lefttime = Math.min(duration, AD_TIME * 1000) - position;
		int sec = (int)(lefttime / 1000);
		String begin = getString(R.string.video_ad_time) + " ";
		StringBuilder text = new StringBuilder(begin);
		int hourstart = begin.length(), minstart = begin.length();
		if (sec > 0) {
			int hour = sec / 3600;
			int minute = sec / 60 - hour * 60;
			int secs = sec % 60;
			if (hour > 0) {
				if (hour < 10) {
					text.append("0" + hour).append(getString(R.string.hour));
				} else {
					text.append(hour).append(getString(R.string.hour));
				}
				text.append(hour).append(getString(R.string.hour));
				hourstart = text.length();
			}
			if (minute > 0 || hour > 0) {
				if (minute < 10) {
					text.append("0" + minute).append(getString(R.string.minute));
				} else {
					text.append(minute).append(getString(R.string.minute));
				}
				minstart = text.length();
			}
			if (secs < 10) {
				text.append("0" + secs).append(getString(R.string.second));
			} else {
				text.append(secs).append(getString(R.string.second));
			}
			SpannableString ss = new SpannableString(text.toString());
			if (hour > 0) {
				ss.setSpan(new ForegroundColorSpan(Color.YELLOW), begin.length(), begin.length() + 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
			}
			if (minute > 0 || hour > 0) {
				ss.setSpan(new ForegroundColorSpan(Color.YELLOW), hourstart, hourstart + 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
			}
			ss.setSpan(new ForegroundColorSpan(Color.YELLOW), minstart, minstart + 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
			mADTime.setText(ss);
		}

		if (lefttime > 0) {
			_handler.sendEmptyMessageDelayed(UPDAT_TIME_MESSAGE, 1000);
		}
	}

	/**
	 * 判断如果广告正在播放的处理
	 */
	private void ADisPlaying() {

		if (isPlayingAd) {
			isPlayingAd = false;
			_handler.removeMessages(UPDAT_TIME_MESSAGE);
			mADTime.setVisibility(View.GONE);

			setVideoURI(mVideoPath, mLastPosition);
		} else {
			finishEvent();
			finish();
		}
	}

	/* 结束事件要做的操作 */
	private void finishEvent() {
		if (controler.isShowing()) {
			controler.dismiss();

		}
		if (mVideoPlayer != null) {
			mVideoPlayer.stopPlayback();
		}
		hide();
		_handler.removeMessages(UPDAT_TIME_MESSAGE);
		_handler.removeMessages(PROGRESS_CHANGED);
		_handler.removeMessages(VIDEO_DURATION);
		cancelDelayHide();
	}

	// 视频播放出错提示框
	private void showErrorDialog() {

		VideoDialog dialogBuilder = VideoDialog.getInstance(this);
		dialogBuilder.withMessage(false, isPlayingAd ? getString(R.string.fail_play_ad_msg) : getString(R.string.fail_play_msg))
				.setmListener(videoErrorCallback).show();

	}

	OnSureListener videoErrorCallback = new OnSureListener() {

		@Override
		public void doSomeThings() {
			if (isPlayingAd) {
				isPlayingAd = false;
				_handler.removeMessages(UPDAT_TIME_MESSAGE);
				mADTime.setVisibility(View.GONE);

				setVideoURI(mVideoPath, mLastPosition);
				mVideoPlayer.getPlayerControl().start();
				diaLoading.show();
			} else {
				finishEvent();
				finish();
			}

		}

	};

	private void show() {
		if (!mShowing) {
			mShowing = true;
		}
		hideControllerDelay();
	}

	private void hide() {
		cancelDelayHide();

		if (mShowing) {

			controler.dismiss();
			mShowing = false;
		}
	}

	@Override
	public void onClick(View arg0) {
		finishEvent();
		finish();
	}

	/** 定时隐藏 */
	private Handler mDismissHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mVolumeBrightnessLayout.setVisibility(View.GONE);
		}
	};

	/**
	 * 滑动改变声音大小
	 * 
	 * @param percent
	 */
	private void onVolumeSlide(float percent) {
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0)
				mVolume = 0;

			// 显示
			mOperationBg.setImageResource(R.drawable.video_volumn_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}

		int index = (int) (percent * mMaxVolume) + mVolume;
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;
		volumeSeekBar.setProgress(index);

		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		// 变更进度条
		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;
		mOperationPercent.setLayoutParams(lp);
	}

	/**
	 * 滑动改变亮度
	 * 
	 * @param percent
	 */
	private void onBrightnessSlide(float percent) {
		if (mBrightness < 0) {
			mBrightness = getWindow().getAttributes().screenBrightness;
			if (mBrightness <= 0.00f)
				mBrightness = 0.50f;
			if (mBrightness < 0.01f)
				mBrightness = 0.01f;

			// 显示
			mOperationBg.setImageResource(R.drawable.video_brightness_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}
		WindowManager.LayoutParams lpa = getWindow().getAttributes();
		lpa.screenBrightness = mBrightness + percent;
		if (lpa.screenBrightness > 1.0f)
			lpa.screenBrightness = 1.0f;
		else if (lpa.screenBrightness < 0.01f)
			lpa.screenBrightness = 0.01f;
		getWindow().setAttributes(lpa);
		blightSeekBar.setProgress((int) (lpa.screenBrightness * Max_Brightness));
		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
		mOperationPercent.setLayoutParams(lp);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}

		// 处理手势结束
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			endGesture();
			break;
		}

		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean key = super.onKeyUp(keyCode, event);
		if (keyCode==KeyEvent.KEYCODE_VOLUME_UP || keyCode==KeyEvent.KEYCODE_VOLUME_DOWN) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			volumeSeekBar.setProgress(mVolume);
		}
		return key;
	}

	/** 手势结束 */
	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;

		// 隐藏
		mDismissHandler.removeMessages(0);
		mDismissHandler.sendEmptyMessageDelayed(0, 1000);
	}

	private class MyGestureListener extends SimpleOnGestureListener {

		/** 滑动 */
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			float mOldX = e1.getX(), mOldY = e1.getY();
			int y = (int) e2.getRawY();
			Display disp = getWindowManager().getDefaultDisplay();
			int windowWidth = disp.getWidth();
			int windowHeight = disp.getHeight();

			if (mOldX > windowWidth * 2.0 / 3.0)// 右边滑动
				onVolumeSlide((mOldY - y) / windowHeight);
			else if (mOldX < windowWidth / 3.0)// 左边滑动
				onBrightnessSlide((mOldY - y) / windowHeight);

			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		public void onLongPress(MotionEvent e) {
			// mMediaController.hide();
		}

		public boolean onSingleTapConfirmed(MotionEvent e) {
			Display display = getWindowManager().getDefaultDisplay();
			if (isPlayingAd) {
				if (!controler.isShowing()) {
					show();
				} else {
					hide();
				}
			} else {
				if (controler.isShowing()) {
					controler.dismiss();
					hide();
				} else {
					controler.showAtLocation(mVideoView, Gravity.TOP, 135, 555);
					controler.update(0, (display.getHeight() * 7) / 10, (display.getWidth() * 4) / 5, display.getHeight() / 5);
					show();
				}
			}

			return super.onSingleTapConfirmed(e);
		}
	}

	private void cancelDelayHide() {
		_handler.removeMessages(HIDE_MESSAGE);
	}

	private void hideControllerDelay() {
		_handler.sendEmptyMessageDelayed(HIDE_MESSAGE, 3000);
	}

	private void addgo() {
		mVideoPlayer.seekTo(mVideoPlayer.getCurrentPosition() + 20000);
	}

	private void addback() {
		mVideoPlayer.seekTo(mVideoPlayer.getCurrentPosition() - 20000);
	}

	private void setVolum() {
		mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volumeSeekBar.setMax(mMaxVolume);
		mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		volumeSeekBar.setProgress(mVolume);
	}

	/** 播放影视 */
	private void setVideoURI(final String videoPath, long position) {
		if (mVideoPlayer == null) {
			mVideoPlayer = new DemoPlayer();
			mVideoPlayer.addListener(this);
		}
		mVideoPlayer.prepare(getRendererBuilder(videoPath));
		mVideoPlayer.setSurface(mVideoView.getHolder().getSurface());
		mVideoPlayer.setPlayWhenReady(!isPaused);
		mVideoPlayer.seekTo(position);
	}

	@Override
	public void onGetResult(int requestType, Object result) {

		if (requestType == ADS_VIDEO_API) {
			if (result == null) {
				setVideoURI(mVideoPath, mLastPosition);
				return;
			}

			ADNEW data = (ADNEW) result;
			final String videoAD = data.adsPath;

			if (videoAD != null) {
				setVideoURI(videoAD, 0);
				isPlayingAd = true;
			} else {
				setVideoURI(mVideoPath, mLastPosition);
			}
		} else if (requestType == ADS_PIC_API) {

			if (result == null) {
				return;
			}

			ADNEW data = (ADNEW) result;
			adsExternalUrl = data.adsExternalUrl;
			relatedType = data.adsRelatedType;
			String pic = PhotoManager.getInstance().getImageFile(data.adsPath);
			if (pic != null) {
				mAD.setVisibility(View.VISIBLE);
				mADPic.setImageURI(Uri.fromFile(new File(pic)));
			} else {
				PhotoManager.getInstance().downloadImage(data.adsPath, this);
			}

		}

	}

	/*******************************************************
	 * 
	 * 广告 相关代码
	 * 
	 *******************************************************/

	private void getADPic() {
		new ADNewPicRequest(this, ADNewPicRequest.ADS_TYPE_VIDEO_PAUSE).execute((String) null);
	}

	private void hidePicAD() {
		mAD.setVisibility(View.GONE);
	}

	@Override
	public void onPhotoDownload(String url, final String path) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				if (path == null) {
					hidePicAD();
					return;
				}

				mAD.setVisibility(View.VISIBLE);
				mADPic.setImageURI(Uri.fromFile(new File(path)));
			}
		});

	}
	
	@Override
	public void onPhotoDownloadError(String url, String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(int requestType) {
		diaLoading.hide();
		if (requestType != REDIRECT_API) {
			ComUtil.toastText(this, "连接服务器出错", Toast.LENGTH_SHORT);
		}
		finish();
	}

	private RendererBuilder getRendererBuilder(String contentUri) {
		String userAgent = Util.getUserAgent(this);
		String contentId = Util.getContentId(contentUri);
		switch (contentUri.substring(contentUri.lastIndexOf(".")).toLowerCase()) {
//			case DemoUtil.TYPE_SS:
//				return new SmoothStreamingRendererBuilder(userAgent, contentUri.toString(), contentId,
//						new SmoothStreamingTestMediaDrmCallback(), debugTextView);
//			case DemoUtil.TYPE_DASH:
			case ".mpd":
				return new DashRendererBuilder(userAgent, contentUri.toString(), contentId, null);
//						new WidevineTestMediaDrmCallback(contentId), debugTextView);
//			case DemoUtil.TYPE_HLS:
			case ".m3u8":
				return new HlsRendererBuilder(userAgent, contentUri, contentId);
			default:
				return new DefaultRendererBuilder(this, Uri.parse(contentUri));
		}
	}

	private void releasePlayer() {
		if (mVideoPlayer != null) {
			mVideoPlayer.release();
			mVideoPlayer = null;
		}
	}
}
