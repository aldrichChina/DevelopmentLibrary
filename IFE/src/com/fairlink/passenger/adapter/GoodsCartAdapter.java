package com.fairlink.passenger.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairlink.passenger.R;
import com.fairlink.passenger.cart.GoodsCartItem;
import com.fairlink.passenger.util.ComUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GoodsCartAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;
	private Context context;
	private List<GoodsCartItem> mList;
	ViewHolder holder;

	public static interface OnProcessClickListener {

		public void onchangeNum(int position);

		public void onAddClick(int position);

		public void onDesClick(int position);

		public void onChecked(int position);

		public void onCancel(int position);

		public void onItem(int position);
	}

	private OnProcessClickListener mListener;

	public OnProcessClickListener getmListener() {
		return mListener;
	}

	public void setList(List<GoodsCartItem> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	public void setmListener(OnProcessClickListener mListener) {
		this.mListener = mListener;
	}

	static class ViewHolder {
		public CheckBox cbCheck;
		public ImageView imgGoods;
		public TextView tvProductName;
		public TextView tvPriceScore;
		public TextView tvNum;
		public ImageButton btnAdd;
		public ImageButton btnDes;
		public TextView tvGoodsPrice;
		public Button btnCancel;
		public TextView tvGoodsName;

	}

	public GoodsCartAdapter(Context context, List<GoodsCartItem> list) {
		this.context = context;
		mList = list;
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public GoodsCartItem getItem(int position) {
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
			convertView = mLayoutInflater.inflate(R.layout.goods_cart_item, null);
			holder = new ViewHolder();
			holder.cbCheck = (CheckBox) convertView.findViewById(R.id.cb_check);
			holder.imgGoods = (ImageView) convertView.findViewById(R.id.img_goods);
			holder.tvProductName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvGoodsName = (TextView) convertView.findViewById(R.id.tv_GoodsName);
			holder.tvPriceScore = (TextView) convertView.findViewById(R.id.tv_price_score);
			holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
			holder.btnAdd = (ImageButton) convertView.findViewById(R.id.num_add);
			holder.btnDes = (ImageButton) convertView.findViewById(R.id.num_des);
			holder.tvGoodsPrice = (TextView) convertView.findViewById(R.id.tv_goods_price);
			holder.btnCancel = (Button) convertView.findViewById(R.id.btn_cancel);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final GoodsCartItem item = mList.get(position);

		if (null != item) {
			holder.cbCheck.setChecked(item.isChecked());
			ImageLoader.getInstance().displayImage(ComUtil.getPic(item.getGoodsItem().getGoods().getGoodsPic()), holder.imgGoods);
			holder.tvProductName.setText(item.getGoodsItem().getGoods().getProductName());
			holder.tvGoodsName.setText(item.getGoodsItem().getGoods().getGoodsName());
			holder.tvNum.setText("" + item.getGoodsItem().getCount());
			holder.tvPriceScore.setText(String.format("￥%.2f", item.getGoodsItem().getGoods()
					.getGoodsPreferentialPrice()));
			holder.tvGoodsPrice.setText(String.format("￥%.2f",item.getGoodsItem().getGoods()
					.getGoodsPreferentialPrice() * item.getGoodsItem().getCount()));
			holder.tvNum.setOnClickListener(new LvButtonListener(position));
			holder.btnAdd.setOnClickListener(new LvButtonListener(position));
			holder.btnDes.setOnClickListener(new LvButtonListener(position));
			holder.btnCancel.setOnClickListener(new LvButtonListener(position));
			holder.cbCheck.setOnClickListener(new LvButtonListener(position));
			holder.cbCheck.setOnCheckedChangeListener(new LvCheckBoxListener());
			holder.imgGoods.setOnClickListener(new LvButtonListener(position));
			holder.tvProductName.setOnClickListener(new LvButtonListener(position));
		}

		return convertView;
	}

	/** item 数量增加、减少 事件 */
	private class LvButtonListener implements View.OnClickListener {
		private int position;

		LvButtonListener(int pos) {
			position = pos;
		}

		@Override
		public void onClick(View v) {
			int vid = v.getId();

			if (vid == holder.tvNum.getId()) {
				if (mListener != null)
					mListener.onchangeNum(position);

			} else if (vid == holder.btnAdd.getId()) {
				if (mListener != null)
					mListener.onAddClick(position);

			} else if (vid == holder.btnDes.getId()) {

				if (mListener != null)
					mListener.onDesClick(position);

			} else if (vid == holder.cbCheck.getId()) {
				if (mListener != null) {
					mListener.onChecked(position);

				}
			} else if (vid == holder.btnCancel.getId()) {
				if (mListener != null) {
					mListener.onCancel(position);

				}
			} else if (vid == holder.imgGoods.getId()) {
				if (mListener != null) {
					mListener.onItem(position);
				}
			} else if (vid == holder.tvProductName.getId()) {
				if (mListener != null) {
					mListener.onItem(position);
				}
			}
		}
	}

	/** item选中事件 */
	private class LvCheckBoxListener implements CompoundButton.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		}

	}
}
