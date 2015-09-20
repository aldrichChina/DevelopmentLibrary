package com.fairlink.passenger.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.fairlink.passenger.R;

public class MarqueeText extends FrameLayout{
	private HorizontalScrollView mScroll;
	private TextView firstText;
	private TextView secondText;
	private int textWidth;
	private int scrollX;
	private String space;
	private Handler mHandler;
	
	void initView() {
		textWidth = 0;
		scrollX = 0;
		space = "                                   ";
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				scrollX = mScroll.getScrollX() + 1;
				if (textWidth == 0) {
					scrollX = 0;
				} else {
					scrollX %= textWidth;
				}
				mScroll.scrollTo(scrollX, 0);
				sendEmptyMessageDelayed(0, 30);
			}
		};
		mHandler.sendEmptyMessage(0);
	}
	
	
	
	public MarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.marquee_text, null);
		mScroll = (HorizontalScrollView)view.findViewById(R.id.scroll);
		firstText = (TextView)view.findViewById(R.id.text01);
		secondText = (TextView)view.findViewById(R.id.text02);
		addView(view);
		initView();
	}
	
	public void setText(String str) {
		firstText.setText(str + space);
		firstText.measure(0, 0);
		textWidth = firstText.getMeasuredWidth();
		secondText.setText(str + space);
	}

}
