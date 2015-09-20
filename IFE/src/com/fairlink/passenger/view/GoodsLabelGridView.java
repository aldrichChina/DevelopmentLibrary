package com.fairlink.passenger.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @ClassName  ：  GoodsLabelGridView 
 * @Description: 商品标签显示列表
 * @author     ：  jiaxue 
 * @date       ：  2014-11-27 下午2:02:05 

 */

public class GoodsLabelGridView extends GridView {

	
	public GoodsLabelGridView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
}
