package com.fairlink.passenger.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairlink.passenger.R;
import com.fairlink.passenger.bean.Goods;
import com.fairlink.passenger.shop.ShopListFragment.ShopGoodsSelectedListener;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.ImageUtil;
import com.fairlink.passenger.util.ImageUtil.ImageViewHolder;

/**
 * @ClassName ： ShopListAdapter
 * @Description: 商品列表适配器
 */

public class ShopListAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;
	private List<Goods> mList;
	private ShopGoodsSelectedListener mListener;

	static class ViewHolder {
		public TextView name;
		public TextView price;
		public ImageViewHolder pic;
	}

	public ShopListAdapter(Context context, List<Goods> list, ShopGoodsSelectedListener mListener) {
		mList = list;
		mLayoutInflater = LayoutInflater.from(context);
		this.mListener = mListener;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Goods getItem(int position) {
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
			convertView = mLayoutInflater.inflate(R.layout.goods_list_item, null);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.pic.imageView = null;
		}
		holder = new ViewHolder();
		holder.name = (TextView) convertView.findViewById(R.id.tv_name);
		holder.price = (TextView) convertView.findViewById(R.id.tv_price);
		holder.pic = new ImageViewHolder();
		holder.pic.imageView = (ImageView) convertView.findViewById(R.id.goods_pic);
		convertView.setTag(holder);
		
		final Goods item = mList.get(position);
		holder.name.setText(item.getProductName());
		holder.price.setText("￥" + String.format("%.2f", item.getGoodsPrice()));

		String imgUrl = ComUtil.getPic(item.getGoodsPic());
		ImageUtil.setImage(imgUrl, ImageUtil.MID, holder.pic.imageView, holder.pic, null);
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.onShopGoodsSelected(item.getId());
			}
		});
		return convertView;
	}

	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

}
