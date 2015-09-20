package com.fairlink.passenger.setting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.fairlink.passenger.AdvertisementActivity;
import com.fairlink.passenger.R;
import com.fairlink.passenger.admin.DevSettingActivity;
import com.fairlink.passenger.setting.SettingMyDialog.Dialogcallback;
import com.fairlink.passenger.view.DialogCom;

public class SettingMainActivity extends AdvertisementActivity implements OnClickListener {

	private String versionCode;
	private ImageView mGameBackImg;
	private SettingMainActivity that;
	private TextView mAbout = null;
	private TextView mOthers = null;
	protected static final int PROGRESS_CHANGED = 0x101;
	private SeekBar soundBar;
	private AudioManager mAudiomanage;
	private int maxVolume, currentVolume;
	private SeekBar brightnessBar;
	private int mBrightnessMode, currentBrightness;
	Thread myVolThread = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		that = this;
		setContentView(R.layout.setting_main);

		mAbout = (TextView) findViewById(R.id.setting_haihang);
		mOthers = (TextView) findViewById(R.id.setting_others);
		mGameBackImg = (ImageView) findViewById(R.id.setting_back);

		mGameBackImg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				that.finish();
			}
		});
		mAbout.setOnClickListener(this);
		mOthers.setOnClickListener(this);
		mAbout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.login_lan_enter);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.login_lan);
				}
				return false;
			}

		});
		mOthers.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.login_lan_enter);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.login_lan);
				}
				return false;
			}

		});

		soundBar = (SeekBar) findViewById(R.id.setting_voice);
		mAudiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		setVolum();
		OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mAudiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			public void onStopTrackingTouch(SeekBar seekBar) {

			}

		};

		soundBar.setOnSeekBarChangeListener(seekBarChangeListener);
		brightnessBar = (SeekBar) findViewById(R.id.setting_brightness);

		mBrightnessMode = getScreenMode();
		if(getScreenBrightness() <= 10)
			currentBrightness = 0;
		else
			currentBrightness = getScreenBrightness();
		brightnessBar.setProgress(currentBrightness);
		brightnessBar.setMax(255);
		setScreenMode(mBrightnessMode);

		brightnessBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO 自动生成的方法存根
				if (progress <= 10)
					progress = 10;
				saveScreenBrightness(progress);
				setScreenBrightness(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO 自动生成的方法存根

			}

		});
	}

	private void setVolum() {
		maxVolume = mAudiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		soundBar.setMax(maxVolume);
		currentVolume = mAudiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundBar.setProgress(currentVolume);
	}

	/**
	 * 获得当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	 */
	private int getScreenMode() {
		int screenMode = 0;
		try {
			screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
		} catch (Exception localException) {

		}
		return screenMode;
	}

	/**
	 * 获得当前屏幕亮度值 0--255
	 */
	private int getScreenBrightness() {
		int screenBrightness = 255;
		try {
			screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception localException) {

		}
		return screenBrightness;
	}

	/**
	 * 设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	 */
	private void setScreenMode(int paramInt) {
		try {
			Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 设置当前屏幕亮度值 0--255
	 */
	private void saveScreenBrightness(int paramInt) {
		try {
			Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 保存当前的屏幕亮度值，并使之生效
	 */
	private void setScreenBrightness(int paramInt) {
		Window localWindow = getWindow();
		WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
		float f = paramInt / 255.0F;
		localLayoutParams.screenBrightness = f;
		localWindow.setAttributes(localLayoutParams);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_haihang:
			try {

				PackageManager pm = getPackageManager();

				PackageInfo pinfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
				versionCode = pinfo.versionName;
			} catch (NameNotFoundException e) {
			}
			DialogCom.DiaCom(SettingMainActivity.this, getResources().getString(R.string.setting_haihang_dialog)
					+ versionCode);
			break;
		case R.id.setting_others:
			SettingMyDialog myDialog = new SettingMyDialog(SettingMainActivity.this);
			myDialog.setContent(getResources().getString(R.string.setting_input));
			myDialog.setDialogCallback(dialogcallback);
			myDialog.show();
			break;
		}

	}

	Dialogcallback dialogcallback = new Dialogcallback() {

		@Override
		public void dialogdo(String string) {
			if (string.equals("111111") == true) {
				Intent intent = new Intent(SettingMainActivity.this, DevSettingActivity.class);
				startActivity(intent);
			} else {
				DialogCom.DiaCom(SettingMainActivity.this, getResources().getString(R.string.setting_error));
			}

		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			mAudiomanage.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
					AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
			setVolum();
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			mAudiomanage.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
					AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
			setVolum();
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
