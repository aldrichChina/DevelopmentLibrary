package com.fairlink.passenger.music;

import java.util.Calendar;

import com.fairlink.passenger.R;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.view.OnChangedListener;
import com.fairlink.passenger.view.SlipButton;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DialogMusic extends Dialog implements
		android.view.View.OnClickListener, OnChangedListener {

	private TextView mMusicConfirm;
	private Calendar calendar;
	private long timeInMillis;
	private EditText mMins;
	private int setHour = 0, setMinute = 0, currentHour = 0, currentMinute = 0;
	private Context mContext;
	private SlipButton mSlipButton;
	private Boolean mChecked = false;
	private TimeDialogcallback timecallback;

	public DialogMusic(Context context, int theme) {
		super(context, theme);
		// TODO 自动生成的构造函数存根
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_music);
		getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		 getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		 
		mMusicConfirm = (TextView) findViewById(R.id.music_dialog_sure);
		mMins = (EditText) findViewById(R.id.music_mins);
		mMins.setEnabled(false);

		calendar = Calendar.getInstance();
		currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		currentMinute = calendar.get(Calendar.MINUTE);
		mMusicConfirm.setOnClickListener(this);

		mSlipButton = (SlipButton) findViewById(R.id.time_on);
		mSlipButton.SetOnChangedListener(this);// 设置事件监听
		if (AlarmData.AlarmDataGetEnable()) {
			mMins.setEnabled(true);
			mSlipButton .setChecked(true);
			mChecked = true;
			int hour = 0, mins = 0, i = 0;
			hour = AlarmData.AlarmDataGetHour();
			mins = AlarmData.AlarmDataGetMinute();
			i = (hour - currentHour) * 60 + (mins - currentMinute);
			mMins.setText(format(i));

		}
		

	}

	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if (mChecked) {
			if (mMins.getText().toString().equals("")) {
				ComUtil.toastText(mContext.getApplicationContext(), mContext.getResources().getString(R.string.music_time_dialog),
						Toast.LENGTH_SHORT);

			} else if (Integer.parseInt(mMins.getText().toString()) > 0) {
				timeChange(Integer.parseInt(mMins.getText().toString()));
				new AlarmData(setHour, setMinute, true);
				setHour = setHour % 24;
				Log.i("yu debug :", "hour:" + setHour + ",minute:" + setMinute);

				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, setHour);
				calendar.set(Calendar.MINUTE, setMinute);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				timeInMillis = calendar.getTimeInMillis();

				AlarmService.enabledAlarm(timeInMillis, getContext());
				timecallback.timedialogdo();
				this.dismiss();
			}
			this.dismiss();
		}
		this.dismiss();
	}

	private void timeChange(int time) {
		calendar = Calendar.getInstance();
		currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		currentMinute = calendar.get(Calendar.MINUTE);
		Log.i("yu:", "i is " + time);
		Log.i("yu debug ", "currenthour:" + currentHour + ",currentMinute:"
				+ currentMinute);
		int i = 0, j = 0;
		i = (currentMinute + time) / 60;
		j = (currentMinute + time) % 60;
		setHour = currentHour + i;
		setMinute = j;
		Log.i("yu debug :", "hour:" + setHour + ",minute:" + setMinute);
	}

	public void OnChanged(boolean CheckState) {
		if (CheckState) {
			mChecked = true;
			mMins.setEnabled(true);
		} else {
			mChecked = false;
			mMins.setEnabled(false);
			new AlarmData(0, 0, false);
			timecallback.timedialogdo();
			//mContext.sendBroadcast(new Intent("com.fairlink.passenger.musictime"));
		}
	}
	
	
	public interface TimeDialogcallback {
		public void timedialogdo();
	}

	
	public void setDialogCallback(TimeDialogcallback timecallback) {
		// TODO 自动生成的方法存根
		this.timecallback = timecallback;
	}

}
