package com.fairlink.passenger.view;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ScaleImageView extends ImageView{
    Matrix matrix = new Matrix();
    private Bitmap bitmap = null;

    float scaleX = 1.0f;
    float scaleY = 1.0f;
    float height = 1.0f;
    float width = 1.0f;

    
    public ScaleImageView(Context context) {
		super(context);
	}
	
	public ScaleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	 @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		 
			BitmapDrawable bd = (BitmapDrawable)this.getDrawable();
			if (bd != null) {
				bitmap = bd.getBitmap();
				width = bitmap.getWidth();
				height = bitmap.getHeight();
			}
			this.setScaleType(ScaleType.MATRIX);
			
			float scale = 1.0f;
			int widthSize = MeasureSpec.getSize(widthMeasureSpec);
			int widthMode = getLayoutParams().width;
			scaleX = widthSize/width;
			if (widthMode != LayoutParams.WRAP_CONTENT) { 
				scale = scaleX;
			}
			int heightSize = MeasureSpec.getSize(heightMeasureSpec);
			int heightMode = getLayoutParams().height;
			scaleY = heightSize/height;
			if (heightMode != LayoutParams.WRAP_CONTENT) { 
				scale = scaleY;
			}
			matrix.setScale(scale, scale);
		    ScaleImageView.this.setImageMatrix(matrix);
	        int w = (int)(width*scale);
	        int h = (int)(height*scale);
	    	setMeasuredDimension(w, h);
	    }
	  
}
