package com.fairlink.passenger.adapter;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fairlink.passenger.R;
import com.fairlink.passenger.bean.GoodsItem;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.ShopUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @ClassName  ：  OrderRecordGoodsAdapter 
 * @Description: 商品适配器

 */

public class OrderRecordGoodsAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;
	private Context context;
	private List<GoodsItem> mList;
	private ImageView item_image;
	
	
	public OrderRecordGoodsAdapter(Context context, List<GoodsItem> mList) {
		mLayoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.mList = mList;
	}
	
	public void setList(List<GoodsItem> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public GoodsItem getItem(int position) {
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
			convertView  = mLayoutInflater.inflate(R.layout.order_record_goods_item, null);
			item_image = (ImageView) convertView.findViewById(R.id.img_goods);
			convertView.setTag(item_image);
		} else {
			item_image = (ImageView) convertView.getTag();
		}

		final GoodsItem item = getItem(position);
        ImageLoader.getInstance().displayImage(ComUtil.getPic(item.getGoods().getGoodsPic()), item_image);
        item_image.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                ShopUtil.showGoodsDetail(context, item.getGoods().getId());
            }
        });
		
		return convertView;
	}

}
