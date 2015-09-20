package com.fairlink.passenger.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.fairlink.passenger.R;
import com.fairlink.passenger.bean.GoodsItem;
import com.fairlink.passenger.bean.Order;

/**
 * @ClassName ： OrderRecordAdapter
 * @Description: 订单记录适配器
 */

@SuppressLint("UseSparseArrays")
public class OrderRecordAdapter extends BaseAdapter {

	private Context context;
	private List<Order> mList;
	private LayoutInflater mLayoutInflater;

	ViewHolder holder;
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	static class ViewHolder {
		public TextView tvStatus;
		public Button btnProcess;
		public Button btnOrderDetail;
		public TextView tvOrderNo;
		public TextView tvPrice;
		public TextView tv_date;

		public GridView GoodsImgsGrid;
		public TextView tvReceiverName;
		public TextView tvTotalPrice;
		public TextView tvOrderDate;
		public TextView tvOrderPayWay;
	}

	public static interface OnProcessClickListener {
		public void onPay(String orderId);
	}

	private OnProcessClickListener mListener;

	public OnProcessClickListener getmListener() {
		return mListener;
	}

	public void setListener(OnProcessClickListener mListener) {
		this.mListener = mListener;
	}

	public void setOrderList(List<Order> list) {
		mList = list;
		updateGoodsCount(mList);
	}

	public OrderRecordAdapter(Context context, List<Order> list) {
		this.context = context;
		mList = list;
		mLayoutInflater = LayoutInflater.from(context);
		updateGoodsCount(mList);
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Order getItem(int position) {
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
			convertView = mLayoutInflater.inflate(R.layout.order_record_item, null);
			holder = new ViewHolder();
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
			holder.btnProcess = (Button) convertView.findViewById(R.id.btn_process);
			holder.btnOrderDetail = (Button) convertView.findViewById(R.id.btn_order_detail);
			holder.tvOrderNo = (TextView) convertView.findViewById(R.id.tv_order_no);

			holder.GoodsImgsGrid = (GridView) convertView.findViewById(R.id.tv_list_goods_pic);
			holder.tvReceiverName = (TextView) convertView.findViewById(R.id.tv_receiver_name);
			holder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tv_order_total_price);
			holder.tvOrderDate = (TextView) convertView.findViewById(R.id.tv_order_date);
			holder.tvOrderPayWay = (TextView) convertView.findViewById(R.id.tv_order_pay_way);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Order item = mList.get(position);
		if (null != item) {

			holder.tvOrderNo.setText("订单号: " + item.getOrderNoOnflight());

			OrderRecordGoodsAdapter adapter = new OrderRecordGoodsAdapter(context, item.getGoodsList());
			holder.GoodsImgsGrid.setAdapter(adapter);

			float totalPrice = 0.0f;
			for (GoodsItem goodsItem : item.getGoodsList()) {
				totalPrice += goodsItem.getGoods().getGoodsPreferentialPrice() * goodsItem.getCount();
			}
			holder.tvTotalPrice.setText(String.format("￥%.2f",totalPrice));
			holder.tvOrderDate.setText(sdf.format(item.getOrderTime()));
			int payWay = Integer.parseInt(item.getOrderPayWay());
			if (payWay == 0) {
				holder.tvOrderPayWay.setText(context.getString(R.string.pay_cash));
			} else if (payWay == 1) {
				holder.tvOrderPayWay.setText(context.getString(R.string.pay_score));
			} else {
				holder.tvOrderPayWay.setText(context.getString(R.string.pay_card));
			}
			holder.tvReceiverName.setText(item.getOrderReceiveMan());

			holder.btnProcess.setVisibility(View.VISIBLE);
			holder.btnProcess.setClickable(true);
			switch (item.getOrderStatus()) {

			case 0: // 未支付
				holder.tvStatus.setText("未支付");
				holder.btnProcess.setText("继续支付");
				break;
			case 1: // 待验证
				holder.tvStatus.setText("待验证 ");
				holder.btnProcess.setText("取消订单");
				holder.btnProcess.setVisibility(View.GONE);
				break;
			case 2: // 已取消
				holder.tvStatus.setText("已取消 ");
				holder.btnProcess.setText("已取消");
				holder.btnProcess.setVisibility(View.GONE);
				break;
			case 3: // 已完成
				holder.tvStatus.setText("已完成 ");
				// holder.btnProcess.setText("退货");
				holder.btnProcess.setVisibility(View.GONE);
				break;
			case 4: // 支付失败
				holder.tvStatus.setText("支付失败");
				holder.btnProcess.setText("支付失败");
				holder.btnProcess.setClickable(false);
				break;
			case 5: // 已退款 、退货
				holder.tvStatus.setText("已退款");
				holder.btnProcess.setText("已退款");
				holder.btnProcess.setClickable(false);
				break;
			case 6: // 已退款 、退货
				holder.tvStatus.setText("已退款");
				holder.btnProcess.setVisibility(View.GONE);
				break;
			}

			holder.btnProcess.setOnClickListener(new LvButtonListener(position, item.getOrderStatus()));
			
			holder.btnOrderDetail.setText(Html.fromHtml(context.getString(R.string.order_detail)));
			holder.btnOrderDetail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});

		}

		return convertView;
	}

	/** 继续支付、取消订单、退货操作 */
	private class LvButtonListener implements View.OnClickListener {
		private int position;
		private int type;

		LvButtonListener(int pos, int mType) {
			position = pos;
			type = mType;
		}

		@Override
		public void onClick(View v) {

			switch (type) {
			case 0:
				if (mListener != null)
					mListener.onPay(mList.get(position).getOrderId());
				break;
			}

		}
	}

	/**
	 * 根据内嵌ListView高度调整外部ListView Item的高度
	 */
	private void setListViewHeightBasedOnChildren(ListView listView) {
		android.widget.ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	private void updateGoodsCount(List<Order> list) {
		for (Order order : list) {
			String[] goodsInfo = order.getOrderGoodsInfo().split(",");
			List<GoodsItem> goodsList = order.getGoodsList();
			for (int i = 0; i < goodsInfo.length; i += 2) {
				goodsList.get(i/2).setCount(Integer.parseInt(goodsInfo[i+1]));
			}
		}
	}
}
