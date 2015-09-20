package com.fairlink.passenger.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;

public class FlowView extends AdapterView<ListAdapter>{
	
	private int margin = 10;
	private ListAdapter mAdapter;
	private OnItemSelectedListener mOnItemSelected;
	private OnItemClickListener mOnItemClicked;
	private GestureDetector mGesture;
	//存储所有的View，按行记录   
    private List<List<View>> mAllViews = new ArrayList<List<View>>();  
    //记录每一行的最大高度  
    private List<Integer> mLineHeight = new ArrayList<Integer>();  	
    

	public FlowView(Context context) {
		super(context);
		initView();
	}

	public FlowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	void initView() {
		mGesture = new GestureDetector(getContext(), mOnGesture);
		removeAllViewsInLayout();
		if (mAdapter != null) {
			for (int i=0; i<mAdapter.getCount(); i++) {
				View child = mAdapter.getView(i, null, null);
				LayoutParams params = child.getLayoutParams();
	    		if (params == null) {
	    			params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    		}
				addViewInLayout(child, i, params, true);
			}
		}
		
	}
	
	@Override
	public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
		mOnItemSelected = listener;
	}

	@Override
	public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		mOnItemClicked = listener;
	}
	
	
	private DataSetObserver mDataObserver = new DataSetObserver() {

		@Override
		public void onChanged() {
			initView();
			requestLayout();
		}

		@Override
		public void onInvalidated() {
			initView();
			invalidate();
			requestLayout();
		}

	};
	
	private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				if (isEventWithinView(e, child)) {
					if (mOnItemClicked != null) {
						mOnItemClicked.onItemClick(FlowView.this, child, i, i);
					}
					if (mOnItemSelected != null) {
						mOnItemSelected.onItemSelected(FlowView.this, child, i, i);
					}
					break;
				}

			}
			return true;
		}

		private boolean isEventWithinView(MotionEvent e, View child) {
			Rect viewRect = new Rect();
			int[] childPosition = new int[2];
			child.getLocationOnScreen(childPosition);
			int left = childPosition[0];
			int right = left + child.getWidth();
			int top = childPosition[1];
			int bottom = top + child.getHeight();
			viewRect.set(left, top, right, bottom);
			return viewRect.contains((int) e.getRawX(), (int) e.getRawY());
		}
	};
	
	@Override
	public ListAdapter getAdapter() {
		return mAdapter;
	}

	
	public void setAdapter(ListAdapter adapter) {
		if (mAdapter != null) {
			mAdapter.unregisterDataSetObserver(mDataObserver);
		}
		mAdapter = adapter;
		mAdapter.registerDataSetObserver(mDataObserver);
		initView();
		requestLayout();
	}
	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
//		super.dispatchTouchEvent(ev);
		mGesture.onTouchEvent(ev);
		return true;
	}
	
	@Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
    {  
        // 获得它的父容器为它设置的测量模式和大小  
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);  
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);  
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);  
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);  
  
        // 如果是warp_content情况下，记录宽和高  
        int width = 0;  
        int height = 0;  
        /** 
         * 记录每一行的宽度，width不断取最大宽度 
         */  
        int lineWidth = 0;  
        /** 
         * 每一行的高度，累加至height 
         */  
        int lineHeight = 0;  
  
        int count = getChildCount();  
  
        // 遍历每个子元素  
        for (int i = 0; i < count; i++)  
        {  
            View child = getChildAt(i);
            // 测量每一个child的宽和高 ,参数不要传错
            child.measure(0, 0); 
            // 当前子空间实际占据的宽度  
            int childWidth = child.getMeasuredWidth() + 2*margin; 
            // 当前子空间实际占据的高度  
            int childHeight = child.getMeasuredHeight() + margin;
            /** 
             * 如果加入当前child，则超出最大宽度，则到目前最大宽度给width，类加height 然后开启新行 
             */  
            if (lineWidth + childWidth > sizeWidth)  
            {  
                width = Math.max(lineWidth, width);// 取最大的  
                height += lineHeight; // 叠加当前高度  
                
                //开启新行
                lineWidth = 0;
                lineHeight = 0;
            }   
            // 累加值lineWidth,lineHeight取最大高度  
            lineWidth += childWidth;  
            lineHeight = Math.max(lineHeight, childHeight);  
        }  
        //把最后一个child加入
        width = Math.max(width, lineWidth);  
        height += lineHeight;  
        
        int w = (modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width;
        int h = (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height;
        setMeasuredDimension(w, h) ;
    } 
	
	@Override  
    protected void onLayout(boolean changed, int l, int t, int r, int b)  
    {  
		super.onLayout(changed, l, t, r, b);
        mAllViews.clear();  
        mLineHeight.clear();  
  
        int width = getWidth();  
  
        int lineWidth = 0;  
        int lineHeight = 0;  
        // 存储每一行所有的childView  
        List<View> lineViews = new ArrayList<View>();
        int count = getChildCount();  
        // 遍历所有的孩子  
        for (int i = 0; i < count; i++)  
        {  
            View child = getChildAt(i);  
            child.measure(0, 0);
            int childWidth = child.getMeasuredWidth();  
            int childHeight = child.getMeasuredHeight();  
  
            // 如果已经需要换行  
            if (childWidth + 2*margin + lineWidth > width)  
            {  
                // 记录这一行所有的View以及最大高度  
                mLineHeight.add(lineHeight);  
                // 将当前行的childView保存，然后开启新的ArrayList保存下一行的childView  
                mAllViews.add(lineViews);  
                lineWidth = 0;// 重置行宽  
                lineHeight = 0;//重置行高
                lineViews = new ArrayList<View>();  
            }  
            /** 
             * 如果不需要换行，则累加 
             */  
            lineWidth += childWidth + 2*margin;  
            lineHeight = Math.max(lineHeight, childHeight + margin);  
            lineViews.add(child);  
        }  
        // 记录最后一行  
        mLineHeight.add(lineHeight);  
        mAllViews.add(lineViews);  
  
        int left = 0;  
        int top = 0;  
        // 得到总行数  
        int lineNums = mAllViews.size();  
        for (int i = 0; i < lineNums; i++)  
        {  
            // 每一行的所有的views  
            lineViews = mAllViews.get(i);  
            // 当前行的最大高度  
            lineHeight = mLineHeight.get(i);  
            // 遍历当前行所有的View  
            for (int j = 0; j < lineViews.size(); j++)  
            {  
                View child = lineViews.get(j);  
                if (child.getVisibility() == View.GONE)  
                {  
                    continue;  
                }  
  
                //计算childView的left,top,right,bottom  
                int lc = left + margin;  
                int tc = top + margin/2;  
                int rc =lc + child.getMeasuredWidth();  
                int bc = tc + child.getMeasuredHeight(); 
                
                child.layout(lc, tc, rc, bc);  
                  
                left += child.getMeasuredWidth() + 2*margin;
            }  
            left = 0;  
            top += lineHeight;  
        }  
  
    }

	@Override
	public View getSelectedView() {
		return null;
	}

	@Override
	public void setSelection(int position) {
		
	}  
}
