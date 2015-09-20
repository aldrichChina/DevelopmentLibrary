package com.fairlink.passenger.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fairlink.passenger.LaunchModuleListener;
import com.fairlink.passenger.R;
import com.fairlink.passenger.bean.DynamicType;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.ImageUtil;
import com.fairlink.passenger.view.MyImageView;

public class ModuleListAdapter extends BaseAdapter {

	@SuppressLint("UseSparseArrays")
	// 加入 lmap 防listview拖动重复数据显示
	List<View> lstView = new ArrayList<View>();
	List<Integer> lstPosition = new ArrayList<Integer>();

	static final int MAX_CACHE_SIZE = 30;
	private LayoutInflater mLayoutInflater;
	private List<DynamicType> mList;
	private LaunchModuleListener listener;

	static class ViewHolder {
		public TextView name;
		public MyImageView pic;
	}

	public ModuleListAdapter(Context context, List<DynamicType> list, LaunchModuleListener listener) {
		mList = list;
		mLayoutInflater = LayoutInflater.from(context);
		this.listener = listener;

	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public DynamicType getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (lstPosition.contains(position)) {
			return lstView.get(lstPosition.indexOf(position));
		}

		ViewHolder holder;

		if (lstPosition.size() > MAX_CACHE_SIZE)// 这里设置缓存的Item数量
		{
			lstPosition.remove(0);// 删除第一项
			lstView.remove(0);// 删除第一项

		}

		convertView = mLayoutInflater.inflate(R.layout.module_list_item, null);
		holder = new ViewHolder();
		holder.pic = (MyImageView) convertView.findViewById(R.id.module_img);
		holder.name = (TextView) convertView.findViewById(R.id.module_name);
		holder.pic.setType(mList.get(position));
		holder.pic.setOnClickIntent(listener);

		convertView.setTag(holder);

		lstPosition.add(position);// 添加最新项
		lstView.add(convertView);// 添加最新项

		final DynamicType item = mList.get(position);
		holder.name.setText(item.getName());
		
		String picUrl = ComUtil.getPic(item.getPicture());
		
		ImageUtil.setImage(picUrl, ImageUtil.SMALL, holder.pic, null, null);
		
		return convertView;
	}
}
