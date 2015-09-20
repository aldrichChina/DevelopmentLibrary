package com.fairlink.passenger.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairlink.passenger.R;
import com.fairlink.passenger.networkrequest.MovieListRequest.MovieInfo;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.ImageUtil;
import com.fairlink.passenger.util.ImageUtil.ImageViewHolder;

public class MovieListAdapter extends BaseAdapter {

	
	private List<MovieInfo> mList;
	private LayoutInflater mLayoutInflater;
	private Context context;
	
	static class ViewHolder {
		public TextView name;
		public ImageViewHolder pic;
	}
	
	public MovieListAdapter(Context ctx,List<MovieInfo> list)
	{
		this.context = ctx;
		this.mList = list;
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.four_three_list_item, null);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.pic.imageView = null;
		}
		
		holder = new ViewHolder();
		holder.name = (TextView) convertView.findViewById(R.id.mv_name);
		holder.pic = new ImageViewHolder();
		holder.pic.imageView = (ImageView) convertView.findViewById(R.id.movie_pic);
		convertView.setTag(holder);
		
		final MovieInfo item = mList.get(position);
		holder.name.setText(item.name);
		
		String picUrl = ComUtil.getPic(item.image);
		
		ImageUtil.setImage(picUrl, ImageUtil.MID, holder.pic.imageView, holder.pic, null);
		
		return convertView;
	}
	
}
