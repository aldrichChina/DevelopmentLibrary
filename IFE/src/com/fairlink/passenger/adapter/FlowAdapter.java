package com.fairlink.passenger.adapter;

import java.util.List;

import com.fairlink.passenger.R;
import com.fairlink.passenger.bean.Goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FlowAdapter extends BaseAdapter{

	private LayoutInflater mLayoutInflater;
	private List<Goods> mList;
	private int currentId = -1;
	Context context;
	
	public FlowAdapter(Context context, List<Goods> list) {
		this.context = context;
		mLayoutInflater = LayoutInflater.from(context);
		mList = list;
	}
	
	public void setList(List<Goods> list) {
		if (mList != null) {
			mList.clear();
		}
		mList = list;
	}
	
	@Override
	public int getCount() {
		return mList == null ? 0 :mList.size();
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
	
	public void setCurrentId(int id) {
		currentId = id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView flowText;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.flow_text, null);
			flowText = (TextView)convertView.findViewById(R.id.flow_textview);
			convertView.setTag(flowText);
		} else {
			flowText = (TextView)convertView.getTag();
		}
		
		if (position == currentId) {
			flowText.setTextColor(context.getResources().getColor(R.color.white));
			flowText.setBackgroundColor(context.getResources().getColor(R.color.red));
		} else {
			flowText.setTextColor(context.getResources().getColor(R.color.goods_label_color));
			flowText.setBackgroundResource(R.drawable.background_goods_tabel);
		}
		String label = getItem(position).getGoodsName();
		flowText.setText(label);
		return convertView;
	}
	
	
}
