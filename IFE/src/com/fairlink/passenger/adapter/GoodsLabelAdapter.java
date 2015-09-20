package com.fairlink.passenger.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fairlink.passenger.R;
import com.fairlink.passenger.bean.Goods;

/**
 * @ClassName ： GoodsLabelAdapter
 * @Description: 商品标签适配器
 */

public class GoodsLabelAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;
	public List<Goods> mList;
	private TextView item_text;

	public GoodsLabelAdapter(Context context, List<Goods> list) {
		mLayoutInflater = LayoutInflater.from(context);
		mList = list;
	}

	public void setList(List<Goods> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Goods getItem(int position) {
		if (mList != null && mList.size() != 0) {
			return mList.get(position);
		}
		return null;

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.shop_goods_label_item, null);
			item_text = (TextView) convertView.findViewById(R.id.text_item);
			convertView.setTag(item_text);
		} else {
			item_text = (TextView) convertView.getTag();
		}
		String label = getItem(position).getGoodsName();
		item_text.setText(label);

		return convertView;
	}

}
