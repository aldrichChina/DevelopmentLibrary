package com.fairlink.passenger.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.fairlink.passenger.R;
import com.fairlink.passenger.networkrequest.BookDetailRequest.BookDetail;
import com.fairlink.passenger.util.ImageUtil;
import com.fairlink.passenger.util.ImageUtil.ImageViewHolder;
import com.fairlink.passenger.view.ScaleImageView;

public class BookImgListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<BookDetail> list;
	private boolean isVertical = true;

	public BookImgListAdapter(Context ctx, ArrayList<BookDetail> lst) {
		context = ctx;
		list = lst;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		ImageViewHolder photoView;
		ProgressBar spinner;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			if (isVertical) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.book_detail_list_item, parent, false);
			} else {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.book_detail_horizon_list_item, parent, false);
			}
			holder = new ViewHolder();
			holder.photoView = new ImageViewHolder();
			holder.photoView.imageView = (ScaleImageView) convertView
					.findViewById(R.id.image);
			holder.spinner = (ProgressBar) convertView
					.findViewById(R.id.loading);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.photoView.imageView = null;
			holder.photoView = new ImageViewHolder();
			holder.photoView.imageView = (ScaleImageView) convertView
					.findViewById(R.id.image);
		}

		String imgUrl = list.get(position).bookPicsPath;
		ImageUtil.setImage(imgUrl, ImageUtil.BIG, holder.photoView.imageView, 
				holder.photoView, holder.spinner);

		return convertView;
	}

	public void setDisplaySetting(boolean isVertical) {
		this.isVertical = isVertical;
	}

}
