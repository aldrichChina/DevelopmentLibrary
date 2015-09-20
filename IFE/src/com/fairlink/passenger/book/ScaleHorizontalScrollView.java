package com.fairlink.passenger.book;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.widget.HorizontalScrollView;

public class ScaleHorizontalScrollView extends HorizontalScrollView {

	private float mScale;
	private float maxScale;
	private float minScale;
	private ScaleGestureDetector mScaleGestureDetector;
	private OnScaleGestureListener mOnScaleGestureListener;
	private GestureDetectorCompat mGestureDetectorCompat;

	private void initView() {
		mScale = 1.0f;
		maxScale = 2.0f;
		minScale = 1.0f;
		mOnScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {

			
			@Override
			public void onScaleEnd(ScaleGestureDetector detector) {
				
			}

			@Override
			public boolean onScaleBegin(ScaleGestureDetector detector) {
				return true;
			}

			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				float scale = detector.getScaleFactor();
				Log.i("ScaleImage", "scalefactor = "+detector.getScaleFactor());
				mScale *= ((scale+1)/2);
				Log.i("ScaleImage", "mScale = "+mScale);
				if (mScale > maxScale) {
					mScale = maxScale;
				}
				if (mScale < minScale) {
					mScale = minScale;
				}
				requestLayout();
				return true;
			}
		};
		mScaleGestureDetector = new ScaleGestureDetector(getContext(), mOnScaleGestureListener);
		mGestureDetectorCompat = new GestureDetectorCompat(getContext(), new MyGestureListener());
	}

	public ScaleHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public ScaleHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ScaleHorizontalScrollView(Context context) {
		super(context);
		initView();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.measure(MeasureSpec.makeMeasureSpec(
					(int) (MeasureSpec.getSize(widthMeasureSpec) * mScale),
					MeasureSpec.getMode(widthMeasureSpec)), heightMeasureSpec);
		}
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getPointerCount() > 1) {
			mScaleGestureDetector.onTouchEvent(ev);
		} else {
			mGestureDetectorCompat.onTouchEvent(ev);
		}
		for (int i=0; i<getChildCount(); i++) {
			View child = getChildAt(i);
			child.dispatchTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}
	
	class MyGestureListener extends SimpleOnGestureListener{

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (mScale < maxScale) {
				mScale = maxScale;
			}
			else {
				mScale = minScale;
			}
			requestLayout();
			return true;
		}

	}
}
