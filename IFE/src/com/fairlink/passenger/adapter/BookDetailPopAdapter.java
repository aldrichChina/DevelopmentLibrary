package com.fairlink.passenger.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fairlink.passenger.R;
import com.fairlink.passenger.networkrequest.BookDetailRequest.BookDetail;
import com.fairlink.passenger.util.ComUtil;

/**
 * @ClassName  ：  BookDetailPopAdapter 
 * @Description: 电子书弹出框适配器
 * @author     ：  jiaxue 
 * @date       ：  2014-10-31 上午10:12:00 

 */

public class BookDetailPopAdapter extends BaseAdapter {

	private List<BookDetail> list;
	private Context context;
	
	public BookDetailPopAdapter(Context context, ArrayList<BookDetail> list) {
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public BookDetail getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final BookDetail item = list.get(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_book_pop, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.tv_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		holder.title.setText(""+ComUtil.getNoToString(item.bookPicsNo));
		return convertView;
	}
	
	
	static class ViewHolder {

		public TextView title;
		
	}

}
