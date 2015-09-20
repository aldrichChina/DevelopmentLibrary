package com.fairlink.passenger.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairlink.passenger.LaunchModuleListener;
import com.fairlink.passenger.bean.DynamicType;

public class MyImageView extends ImageView implements OnTouchListener {
	public static final int Rotate_Handler_Message_Start = 1;
	public static final int Rotate_Handler_Message_Turning = 2;
	public static final int Rotate_Handler_Message_Turned = 3;
	public static final int Rotate_Handler_Message_Reverse = 6;

	public static final int Scale_Handler_Message_Start = 1;
	public static final int Scale_Handler_Message_Turning = 2;
	public static final int Scale_Handler_Message_Turned = 3;
	public static final int Scale_Handler_Message_Reverse = 6;

	private boolean isAntiAlias = true;
	private boolean scaleOnly = false;
	private boolean isSizeChanged = false;
	private boolean isShowAnimation = true;
	private int rotateDegree = 15;
	private boolean isFirst = true;
	private float minScale = 0.95f;
	private int vWidth;
	private int vHeight;
	private boolean isAnimationFinish = true, isActionMove = false, isScale = false;
	private Camera camera;
	boolean XbigY = false;
	float RotateX = 0;
	float RotateY = 0;
	LaunchModuleListener onclick = null;
	private boolean isCancel = false;
	private TextView mText;
	DynamicType type;
	Matrix matrix = new Matrix();
	private Bitmap bitmap = null;
	float baseScaleX = 1.0f;
	float baseScaleY = 1.0f;
	float height = 1.0f;
	float width = 1.0f;

	public MyImageView(Context context) {
		super(context);
		camera = new Camera();
		setOnTouchListener(this);
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		camera = new Camera();
		setOnTouchListener(this);
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}

	public void SetAnimationOnOff(boolean oo) {
		isShowAnimation = oo;
	}

	public void setOnClickIntent(LaunchModuleListener onclick) {
		this.onclick = onclick;
	}

	public void setType(DynamicType type) {
		this.type = type;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isFirst) {
			isFirst = false;
			init();
		}
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
	}

	public void init() {
		vWidth = getWidth() - getPaddingLeft() - getPaddingRight();
		vHeight = getHeight() - getPaddingTop() - getPaddingBottom();
		Drawable drawable = getDrawable();
		BitmapDrawable bd = (BitmapDrawable) drawable;
		bd.setAntiAlias(isAntiAlias);
	}

	private Handler rotate_Handler = new Handler() {
		private int count = 0;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			matrix.set(getImageMatrix());
			switch (msg.what) {
			case Rotate_Handler_Message_Start:
				count = 0;
//				rotate(matrix, (XbigY ? count : 0), (XbigY ? 0 : count));
				rotate_Handler.sendEmptyMessage(Rotate_Handler_Message_Turning);
				break;
			case Rotate_Handler_Message_Turning:
//				rotate(matrix, (XbigY ? count : 0), (XbigY ? 0 : count));
				count++;
				if (count < getDegree()) {
					rotate_Handler.sendEmptyMessage(Rotate_Handler_Message_Turning);
				} else {
					isAnimationFinish = true;
				}
				break;
			case Rotate_Handler_Message_Turned:
//				rotate(matrix, (XbigY ? count : 0), (XbigY ? 0 : count));
				if (count > 0) {
					rotate_Handler.sendEmptyMessage(Rotate_Handler_Message_Turned);
				} else {
					isAnimationFinish = true;
					if (!isActionMove && onclick != null && !isCancel) {
						onclick.launchModule(type);
					}
				}
				count--;
				count--;
				break;
			case Rotate_Handler_Message_Reverse:
				count = getDegree();
//				rotate(matrix, (XbigY ? count : 0), (XbigY ? 0 : count));
				rotate_Handler.sendEmptyMessage(Rotate_Handler_Message_Turned);
				break;
			}
		}
	};

//	private synchronized void rotate(Matrix matrix, float rotateX, float rotateY) {
//
//		int scaleX = (int) (vWidth * 0.5f);
//		int scaleY = (int) (vHeight * 0.5f);
//		camera.save();
//		camera.rotateX(RotateY > 0 ? rotateY : -rotateY);
//		camera.rotateY(RotateX < 0 ? rotateX : -rotateX);
//		camera.getMatrix(matrix);
//		camera.restore();
//		
//		if (RotateX > 0 && rotateX != 0) {
//			matrix.preTranslate(-vWidth, -scaleY);
//			matrix.postTranslate(vWidth, scaleY);
//		} else if (RotateY > 0 && rotateY != 0) {
//			matrix.preTranslate(-scaleX, -vHeight);
//			matrix.postTranslate(scaleX, vHeight);
//		} else if (RotateX < 0 && rotateX != 0) {
//			matrix.preTranslate(-0, -scaleY);
//			matrix.postTranslate(0, scaleY);
//		} else if (RotateY < 0 && rotateY != 0) {
//			matrix.preTranslate(-scaleX, -0);
//			matrix.postTranslate(scaleX, 0);
//		}
//
////		matrix.setScale(baseScaleX, baseScaleY);
//
//		setImageMatrix(matrix);
//		if (rotateX > 0) {
//			mText.setRotationY(RotateX > 0 ? -getTextDegree() : getTextDegree());
//			mText.setRotationX(0);
//		} else if (rotateY > 0) {
//			mText.setRotationX(RotateY > 0 ? -getTextDegree() : getTextDegree());
//			mText.setRotationY(0);
//		} else {
//			mText.setRotationX(0);
//			mText.setRotationY(0);
//		}
//	}

	private Handler scale_handler = new Handler() {
		private float s;
		int count = 0;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			matrix.set(getImageMatrix());
			switch (msg.what) {
			case Scale_Handler_Message_Start:
				if (!isAnimationFinish) {
					return;
				} else {
					isAnimationFinish = false;
					isSizeChanged = true;
					count = 0;
					s = (float) Math.sqrt(Math.sqrt(minScale));
					beginScale(matrix, s);
					scale_handler.sendEmptyMessage(Scale_Handler_Message_Turning);
				}
				break;
			case Scale_Handler_Message_Turning:
				beginScale(matrix, s);
				if (count < 4) {
					scale_handler.sendEmptyMessage(Scale_Handler_Message_Turning);
				} else {
					isAnimationFinish = true;
					if (!isSizeChanged && !isActionMove && onclick != null && !isCancel) {
						onclick.launchModule(type);
					}
				}
				count++;
				break;
			case Scale_Handler_Message_Reverse:
				if (!isAnimationFinish) {
					scale_handler.sendEmptyMessage(Scale_Handler_Message_Reverse);
				} else {
					isAnimationFinish = false;
					count = 0;
					s = (float) Math.sqrt(Math.sqrt(1.0f / minScale));
					beginScale(matrix, s);
					scale_handler.sendEmptyMessage(Scale_Handler_Message_Turning);
					isSizeChanged = false;
				}
				break;
			}
		}
	};

	private synchronized void beginScale(Matrix matrix, float scale) {
		int scaleX = (int) (vWidth * 0.5f);
		int scaleY = (int) (vHeight * 0.5f);
		matrix.postScale(scale, scale, scaleX, scaleY);
		setImageMatrix(matrix);
		if (scale < 1) {
			mText.setScaleX(0.9f);
			mText.setScaleY(0.9f);
		} else {
			mText.setScaleX(1);
			mText.setScaleY(1);
		}
	}

	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		mText = (TextView) ((ViewGroup) getParent()).getChildAt(1);
		// mSize = mText.getTextSize();
	}

	public int getDegree() {
		return rotateDegree;
	}

	public int getTextDegree() {
		return 30;
	}

	public void setDegree(int degree) {
		rotateDegree = degree;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!isShowAnimation)
			return true;

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			isCancel = false;
			float X = event.getX();
			float Y = event.getY();
			RotateX = vWidth / 2 - X;
			RotateY = vHeight / 2 - Y;
			XbigY = Math.abs(RotateX) > Math.abs(RotateY) ? true : false;

			isScale = X > vWidth / 3 && X < vWidth * 2 / 3 && Y > vHeight / 3 && Y < vHeight * 2 / 3;
			isActionMove = false;

			if (isScale) {
				if (isAnimationFinish && !isSizeChanged) {
					isSizeChanged = true;
					scale_handler.sendEmptyMessage(Scale_Handler_Message_Start);
				}
			} else {
				if (scaleOnly) {
					scale_handler.sendEmptyMessage(Scale_Handler_Message_Start);
				} else {
					rotate_Handler.sendEmptyMessage(Rotate_Handler_Message_Start);
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float x = event.getX();
			float y = event.getY();
			if (x > vWidth || y > vHeight || x < 0 || y < 0) {
				isActionMove = true;
			} else {
				isActionMove = false;
			}

			break;
		case MotionEvent.ACTION_UP:
			if (isScale) {
				if (isSizeChanged)
					scale_handler.sendEmptyMessage(Scale_Handler_Message_Reverse);
			} else {
				rotate_Handler.sendEmptyMessage(Rotate_Handler_Message_Reverse);
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			isCancel = true;
			if (isScale) {
				if (isSizeChanged)
					scale_handler.sendEmptyMessage(Scale_Handler_Message_Reverse);
			} else {
				rotate_Handler.sendEmptyMessage(Rotate_Handler_Message_Reverse);
			}
			break;
		}
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		BitmapDrawable bd = (BitmapDrawable) this.getDrawable();
		if (bd != null) {
			bitmap = bd.getBitmap();
			width = bitmap.getWidth();
			height = bitmap.getHeight();
		}

		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		baseScaleX = widthSize / width;
		baseScaleY = widthSize / height;

		matrix.setScale(baseScaleX, baseScaleY);
		setImageMatrix(matrix);
    	setMeasuredDimension(widthSize, widthSize);
	}
}
