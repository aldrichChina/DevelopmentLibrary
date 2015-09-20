package com.fairlink.passenger.view;

import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fairlink.passenger.util.ComUtil;

public class TimeTextView extends TextView {

	protected Handler _handler = new Handler() {
		public void handleMessage(Message msg) {
			setText(ComUtil.DateToString(new Date()));
			sendEmptyMessageDelayed(1, 60 * 1000);
		}
	};

	public TimeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		_handler.removeMessages(1);
		_handler.sendEmptyMessage(1);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		_handler.removeMessages(1);
	}
}
