package com.fairlink.passenger.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairlink.passenger.R;
import com.fairlink.passenger.bean.GoodsItem;
import com.fairlink.passenger.bean.RecordGoodsItem;
import com.fairlink.passenger.util.ComUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @ClassName ： GoodsOrderAdapter
 * @Description: 订单商品适配器
 */

public class GoodsOrderAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<RecordGoodsItem> mList;
	ViewHolder holder;

	static class ViewHolder {
		public ImageView imgGoods;
		public TextView tvGoodsName;
		public TextView tvProductName;
		public TextView tvPriceScore;
		public TextView tvNum;
		public TextView tvTotalPrice;
	}

	public void setList(List<RecordGoodsItem> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	public GoodsOrderAdapter(Context context) {
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public RecordGoodsItem getItem(int position) {
		if (mList != null && mList.size() != 0) {
			return mList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.goods_order_item, null);
			holder = new ViewHolder();
			holder.imgGoods = (ImageView) convertView.findViewById(R.id.img_goods);
			holder.tvGoodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
			holder.tvProductName = (TextView) convertView.findViewById(R.id.tv_product_name);
			holder.tvPriceScore = (TextView) convertView.findViewById(R.id.tv_price_score);
			holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
			holder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tv_total_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		RecordGoodsItem item = mList.get(position);

		if (null != item) {
			ImageLoader.getInstance().displayImage(ComUtil.getPic(item.pic), holder.imgGoods);
			holder.tvGoodsName.setText(item.goodsName);
			holder.tvProductName.setText(item.productName);
			holder.tvNum.setText("" + item.goodsBuyCount);
			holder.tvPriceScore.setText(String.format("￥%.2f", item.goodsPrice));
			holder.tvTotalPrice.setText(String.format("￥%.2f", item.goodsPrice * item.goodsBuyCount));
		}

		return convertView;
	}

}
