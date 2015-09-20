package com.fairlink.passenger.book;

import java.util.ArrayList;
import java.util.List;

import com.fairlink.passenger.R;
import com.fairlink.passenger.adapter.BookDetailPopAdapter;
import com.fairlink.passenger.networkrequest.BookDetailRequest.BookDetail;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * @ClassName  ：  BookDetailActivity 
 * @Description: 电子书弹出框
 * @author     ：  jiaxue 
 * @date       ：  2014-10-31 下午2:02:29 

 */
public class MtitlePopupWindow extends PopupWindow {
	/**
	 * 上下文对象
	 */
	private Context mContext;
	/**
	 * 回调接口对象
	 */
	private OnPopupWindowClickListener listener;
	/**
	 * ArrayAdapter对象
	 */
	private BookDetailPopAdapter adapter;
	/**
	 * ListView的数据源
	 */
	private ArrayList<BookDetail> mData = new ArrayList<BookDetail>();
	/**
	 * PopupWindow的宽度
	 */
//	private int width = 0;

	public MtitlePopupWindow(Context context){
		super(context); 
		mContext = context;
		initView();
	}
	
	@SuppressWarnings("deprecation")
	private void initView(){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = inflater.inflate(R.layout.book_detail_pop, null);
		setContentView(popupView);
		
		//设置宽度,若没有设置宽度为LayoutParams.WRAP_CONTENT
		setWidth(convertDpToPixel(150));  
		setHeight(LayoutParams.WRAP_CONTENT);
		
		//设置动画，也可以不设置。不设置则是显示默认的
		setAnimationStyle(R.style.PopMenuAnimation);
		
		//这里很重要，不设置这个ListView得不到相应
		this.setFocusable(true);
	    this.setBackgroundDrawable(new BitmapDrawable()); 
	    this.setOutsideTouchable(true);
	    	
		ListView listView = (ListView) popupView.findViewById(R.id.left_menus);
		
		adapter = new BookDetailPopAdapter(mContext, mData);
		listView.setAdapter(adapter);
		
		//ListView的点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MtitlePopupWindow.this.dismiss();
				if(listener != null){
					listener.onPopupWindowItemClick(position);
				}
			}
		});
		
	}
	
	// px 转dp
	private int convertDpToPixel(float dp) {
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}
	
	/**
	 * 为PopupWindow设置回调接口
	 * @param listener
	 */
	public void setOnPopupWindowClickListener(OnPopupWindowClickListener listener){
		this.listener = listener;
	}
	

	/**
	 * 设置数据的方法，供外部调用
	 * @param mList
	 */
	public void changeData(List<BookDetail> mList) {
		//这里用addAll也很重要，如果用this.list = mList，调用notifyDataSetChanged()无效
		//notifyDataSetChanged()数据源发生改变的时候调用的，this.list = mList，list并没有发送改变
		mData.clear();
		mData.addAll(mList);
		adapter.notifyDataSetChanged();
	}
	
	
	/**
	 * 回调接口.供外部调用
	 * @author xiaanming
	 *
	 */
	public interface OnPopupWindowClickListener{
		/**
		 * 当点击PopupWindow的ListView 的item的时候调用此方法，用回调方法的好处就是降低耦合性
		 * @param position 位置
		 */
		void onPopupWindowItemClick(int position);
	}


}

