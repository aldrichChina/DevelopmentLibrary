package com.fairlink.passenger.adapter;

import java.util.List;

import com.fairlink.passenger.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName ： BaseCategoryAdapter
 * @Description: 左侧栏 list 适配器
 */

public class BaseCategoryAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;
	private List<String> mList;
	private int mCurrentMenu = -1;

	public void setmCurrentMenu(int currentMenu) {
		mCurrentMenu = currentMenu;
	}

	static class ViewHolder {
		public TextView name;
		public ImageView indicator;
	}

	public BaseCategoryAdapter(Context context, List<String> list) {
		mList = list;
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public String getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.menu_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.indicator = (ImageView) convertView.findViewById(R.id.current_indicator);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final String item = mList.get(position);

		if (null != item) {
			holder.name.setText(item);

			if (mCurrentMenu == position) {
				holder.name.setTextColor(Color.WHITE);
				holder.indicator.setVisibility(View.VISIBLE);
			} else {
				holder.name.setTextColor(0x99FFFFFF);
				holder.indicator.setVisibility(View.INVISIBLE);
			}

		}

		return convertView;
	}
}
