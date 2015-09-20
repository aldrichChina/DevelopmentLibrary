package com.fairlink.passenger.adapter;


import com.fairlink.passenger.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @ClassName  ：  GoodsPayTypeAdapter 
 * @Description: 支付方式适配器
 * @author     ：  jiaxue 
 * @date       ：  2014-12-24 下午1:47:16 

 */

public class GoodsPayTypeAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;
	private Context context;
	public String[] mList;
	private TextView item_text;
	
	
	public GoodsPayTypeAdapter(Context context, String[] mList) {
		mLayoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.mList = mList;
	}
	
	public void setList(String[] mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mList == null ? 0 : mList.length;
	}

	@Override
	public String getItem(int position) {
		if (mList != null && mList.length != 0) {
			return mList[position];
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
			convertView  = mLayoutInflater.inflate(R.layout.shop_goods_label_item, null);
			item_text = (TextView) convertView.findViewById(R.id.text_item);
			convertView.setTag(item_text);
		} else {
			item_text = (TextView) convertView.getTag();
		}
		int typePay = 0;
		String tx = getItem(position);
		if(tx != null && tx.length() > 0)
			typePay = Integer.parseInt(tx);
		
		switch (typePay) {
		case 0:
			item_text.setText(context.getString(R.string.pay_cash));
			break;
		case 1:
			item_text.setText(context.getString(R.string.pay_score));
			break;
		case 2:
			item_text.setText(context.getString(R.string.pay_card));
			break;
		default:
			item_text.setText(context.getString(R.string.pay_cash));
			break;
		}
		
		return convertView;
	}

}
