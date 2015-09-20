package com.fairlink.passenger.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.fairlink.passenger.R;
import com.fairlink.passenger.bean.GoodsTypeWithRelation;

/**
 * @ClassName ： BaseCategoryAdapter
 * @Description: 左侧栏 list 适配器
 */

public class ShopTypeAdapter extends BaseAdapter implements OnItemClickListener {

	private LayoutInflater mLayoutInflater;
	private List<GoodsTypeWithRelation> mList;
	private int mCurrentMenu = -1;
	private ShopTypeSelectedListener listener;
	
	public static interface ShopTypeSelectedListener {
		public void onTypeSelected(String typeId);
	}

	public void setmCurrentMenu(int currentMenu) {
		mCurrentMenu = currentMenu;
	}

	static class ViewHolder {
		public TextView name;
		public ImageView indicator;
	}

	public ShopTypeAdapter(Context context, List<GoodsTypeWithRelation> list) {
		mList = list;
		mLayoutInflater = LayoutInflater.from(context);
		listener = (ShopTypeSelectedListener)context; 
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public GoodsTypeWithRelation getItem(int position) {
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

		final GoodsTypeWithRelation item = mList.get(position);

		if (null != item) {
			holder.name.setText(item.getGoodsTypeName());

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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		listener.onTypeSelected(mList.get(position).getGoodsTypeId());
	}
}
