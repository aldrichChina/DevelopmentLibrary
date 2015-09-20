package com.fairlink.passenger.cart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.R;
import com.fairlink.passenger.adapter.GoodsCartAdapter;
import com.fairlink.passenger.adapter.GoodsCartAdapter.OnProcessClickListener;
import com.fairlink.passenger.bean.RecordGoodsItem;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.CartRemoveGoodsRequest;
import com.fairlink.passenger.networkrequest.CartUpdateGoodsRequest;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.networkrequest.OrderRequest.OrderSave;
import com.fairlink.passenger.order.OrderConfirmFragment;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.Constant;
import com.fairlink.passenger.util.ShopUtil;
import com.fairlink.passenger.view.ComDialog;
import com.fairlink.passenger.view.ComDialog.OnCommitListener;
import com.fairlink.passenger.view.DialogCom;
import com.fairlink.passenger.view.DialogEdit;
import com.fairlink.passenger.view.DialogEdit.OnBuyNumListener;
import com.fairlink.passenger.view.DialogLoading;
import com.fairlink.passenger.view.ListViewForScrollView;

/**
 * @ClassName ： CartFragment
 * @Description: 购物车界面
 */

public class CartFragment extends BaseFragment implements OnClickListener, HttpTaskCallback, NetworkRequestAPI {

	private LinearLayout linCart;
	private ScrollView svCart;
	private CheckBox cbAllFLC;
	private CheckBox cbAllGoodsTop;
	private CheckBox cbAllGoodsButtom;
	private TextView tvAllNumFLC;
	private TextView tvAllPriceFLC;
	private Button btnDataNo;
	private ListViewForScrollView listFLC;
	private GoodsCartAdapter adapterFLC;
	private List<GoodsCartItem> dataFLC;
	private Button btnOrderFLC;
	private DialogLoading diaLoading;
	private int curPosition;
	private int deltaNum;
	private Button btnDeleteGoods;
	private List<Integer> goodsIdsToDelete = new ArrayList<>();

	public static interface onCartFragListener {
		public void showOrderPay(String orderGroupId);
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		diaLoading = new DialogLoading(activity);
		dataFLC = IFEApplication.getInstance().getDataFLC();
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.cart_fragment, null);

		initView(view);
		setListener();

		initData();

		return view;
	}

	public void onResume() {
		super.onResume();
		dataFLC = IFEApplication.getInstance().getDataFLC();
		updateCartInfo();
	}

	public void onPause() {
		super.onPause();
		IFEApplication.getInstance().setDataFLC(dataFLC);
	}

	public void onDestroy() {
		super.onDestroy();

		if (diaLoading != null) {
			diaLoading.dismiss();
		}
	}

	public void initData() {
		dataFLC = IFEApplication.getInstance().getDataFLC();
		for (GoodsCartItem item : dataFLC) {
			item.setChecked(false);
		}
	}

	/**
	 * 更新购物车信息
	 */
	private void updateCartInfo() {
		int sum = 0;
		double cardPrice = 0;
		adapterFLC.setList(dataFLC);
		boolean isAllChecked = true;
		for (GoodsCartItem item : dataFLC) {
			if (item.isChecked()) {
				sum += item.getGoodsItem().getCount();
				cardPrice += item.getGoodsItem().getGoods().getGoodsPreferentialPrice()
						* item.getGoodsItem().getCount();
			} else {
				isAllChecked = false;
			}
		}

		cbAllFLC.setChecked(isAllChecked);
		cbAllGoodsTop.setChecked(isAllChecked);
		cbAllGoodsButtom.setChecked(isAllChecked);

		tvAllNumFLC.setText(Html.fromHtml(getString(R.string.cart_all_num, sum)));
		tvAllPriceFLC.setText(Html.fromHtml(getString(R.string.cart_all_price, cardPrice)));

		viewVisivleGone();
	}

	private void initView(View v) {
		svCart = (ScrollView) v.findViewById(R.id.sv_goods_cart);
		svCart.smoothScrollTo(0, 0);
		linCart = (LinearLayout) v.findViewById(R.id.lin_cart);
		btnDataNo = (Button) v.findViewById(R.id.btn_no_data);
		cbAllFLC = (CheckBox) v.findViewById(R.id.cb_all_flc);
		cbAllGoodsTop = (CheckBox) v.findViewById(R.id.cb_all_goods_top);
		cbAllGoodsButtom = (CheckBox) v.findViewById(R.id.cb_all_goods_buttom);
		tvAllNumFLC = (TextView) v.findViewById(R.id.tv_flc_all_goods_num);
		tvAllPriceFLC = (TextView) v.findViewById(R.id.tv_flc_all_goods_price);
		listFLC = (ListViewForScrollView) v.findViewById(R.id.list_goods_flc);
		btnOrderFLC = (Button) v.findViewById(R.id.btn_order_flc);
		btnDeleteGoods = (Button) v.findViewById(R.id.btn_delete_selected);
	}

	private void setListener() {
		cbAllFLC.setOnClickListener(this);
		cbAllGoodsTop.setOnClickListener(this);
		cbAllGoodsButtom.setOnClickListener(this);
		btnOrderFLC.setOnClickListener(this);
		adapterFLC = new GoodsCartAdapter(getActivity(), dataFLC);
		adapterFLC.setmListener(flcListener);
		listFLC.setAdapter(adapterFLC);
		btnDeleteGoods.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_order_flc:
			processBuy();
			break;

		case R.id.cb_all_flc:
			for (GoodsCartItem item : dataFLC) {
				item.setChecked(cbAllFLC.isChecked());
			}

			updateCartInfo();
			break;
		case R.id.cb_all_goods_top:
			for (GoodsCartItem item : dataFLC) {
				item.setChecked(cbAllGoodsTop.isChecked());
			}
			cbAllGoodsButtom.setChecked(cbAllGoodsTop.isChecked());
			updateCartInfo();
			break;
		case R.id.cb_all_goods_buttom:
			for (GoodsCartItem item : dataFLC) {
				item.setChecked(cbAllGoodsButtom.isChecked());
			}
			cbAllGoodsTop.setChecked(cbAllGoodsButtom.isChecked());
			updateCartInfo();
			break;
		case R.id.btn_delete_selected:
			ComDialog dialogBuilder = ComDialog.getInstance(getActivity());
			boolean isGoodsChecked = false;
			for (GoodsCartItem item : dataFLC) {
				if (item.isChecked()) {
					isGoodsChecked = true;
					break;
				}
			}
			if (isGoodsChecked == false) {
				dialogBuilder.withMessage(false, "您未选中任何商品!").withBtnSureText("确定").show();
			} else {
				dialogBuilder.withMessage(true, "确定删除选中的商品？").withBtnSureText("确定").setmListener(new OnCommitListener() {

					@Override
					public void doSomeThings() {
						StringBuilder goodsIds = new StringBuilder();
						goodsIdsToDelete.clear();
						for (GoodsCartItem item : dataFLC) {
							if (item.isChecked()) {
								goodsIds.append(item.getGoodsItem().getGoods().getId());
								goodsIds.append(',');
								goodsIdsToDelete.add(item.getGoodsItem().getGoods().getId());
							}
						}

						new CartRemoveGoodsRequest(getActivity(), CartFragment.this, goodsIds.deleteCharAt(
								goodsIds.length() - 1).toString()).execute((String) null);
					}
				}).show();
			}
			break;
		default:
			break;
		}

	}

	private OnProcessClickListener flcListener = new OnProcessClickListener() {

		@Override
		public void onDesClick(int position) {

			int num = dataFLC.get(position).getGoodsItem().getCount();
			if (num == 1) {
				return;
			}

			if (num > ShopUtil.MAX_GOODS_NUMBER) {
				deltaNum = ShopUtil.MAX_GOODS_NUMBER - num;
				num = ShopUtil.MAX_GOODS_NUMBER;
			} else {
				deltaNum = -1;
				num -= 1;
			}

			curPosition = position;
			new CartUpdateGoodsRequest(getActivity(), CartFragment.this, dataFLC.get(position).getGoodsItem()
					.getGoods().getId(), num).execute((String) null);
			diaLoading.show();
		}

		@Override
		public void onAddClick(int position) {
			int num = dataFLC.get(position).getGoodsItem().getCount();
			if (num == ShopUtil.MAX_GOODS_NUMBER) {
				return;
			}

			if (num > ShopUtil.MAX_GOODS_NUMBER) {
				deltaNum = ShopUtil.MAX_GOODS_NUMBER - num;
				num = ShopUtil.MAX_GOODS_NUMBER;
			} else {
				deltaNum = 1;
				num += 1;
			}

			curPosition = position;
			new CartUpdateGoodsRequest(getActivity(), CartFragment.this, dataFLC.get(position).getGoodsItem()
					.getGoods().getId(), num).execute((String) null);
			diaLoading.show();
		}

		@Override
		public void onChecked(int position) {
			dataFLC.get(position).setChecked(!dataFLC.get(position).isChecked());
			updateCartInfo();
		}

		@Override
		public void onCancel(final int position) {
			ComDialog dialogBuilder = ComDialog.getInstance(getActivity());
			dialogBuilder.withMessage(true, "确定删除选中的商品？").withBtnSureText("确定").setmListener(new OnCommitListener() {

				@Override
				public void doSomeThings() {
					curPosition = position;
					goodsIdsToDelete.clear();
					goodsIdsToDelete.add(dataFLC.get(position).getGoodsItem().getGoods().getId());
					new CartRemoveGoodsRequest(getActivity(), CartFragment.this, dataFLC.get(position).getGoodsItem()
							.getGoods().getId()).execute((String) null);
					diaLoading.show();
				}
			}).show();
		}

		@Override
		public void onchangeNum(final int position) {
			DialogEdit dialogBuilder = DialogEdit.getInstance(getActivity());

			dialogBuilder.withNumLimit(ShopUtil.MAX_GOODS_NUMBER)
					.withNumBuy("" + dataFLC.get(position).getGoodsItem().getCount())
					.setmListener(new OnBuyNumListener() {

						@Override
						public void onBuyNumberChanged(String temp) {
							int num = dataFLC.get(position).getGoodsItem().getCount();
							int numBuy = Integer.parseInt(temp);

							deltaNum = numBuy - num;
							curPosition = position;
							new CartUpdateGoodsRequest(getActivity(), CartFragment.this, dataFLC.get(position)
									.getGoodsItem().getGoods().getId(), numBuy).execute((String) null);
							diaLoading.show();
						}
					}).show();
		}

		@Override
		public void onItem(int position) {
			ShopUtil.showGoodsDetail(getActivity(), dataFLC.get(position).getGoodsItem().getGoods().getId());
		}
	};

	/** 控制商品有无的显示 */
	private void viewVisivleGone() {
		if (dataFLC.size() == 0) {
			linCart.setVisibility(View.GONE);
			btnDataNo.setVisibility(View.VISIBLE);
		} else {
			linCart.setVisibility(View.VISIBLE);
			btnDataNo.setVisibility(View.GONE);
		}
	}

	/**
	 * 处理购买操作的数据
	 */
	private void processBuy() {
		ArrayList<RecordGoodsItem> listData = new ArrayList<RecordGoodsItem>();
		List<GoodsCartItem> goodsList = dataFLC;

		for (GoodsCartItem goods : goodsList) {
			if (goods.isChecked()) {
				listData.add(goods.getGoodsItem().getRecordGoodsItem());
			}
		}

		if (listData == null || listData.size() == 0) {
			ComUtil.toastText(getActivity(), "您未选中任何商品!", Toast.LENGTH_SHORT);
			return;
		}

		ShopUtil.showOrderConfirm(getActivity(), goodsList.get(0).getGoodsItem().getGoods().getGoodsMall(),
				Constant.PAYTYPE_CREDIT_CARD, listData, 1, true);
	}

	/**
	 * 购买成功后删除购买的商品
	 */
	private void removeGoodsBuy() {
		Iterator<GoodsCartItem> itFLC = dataFLC.iterator();
		while (itFLC.hasNext()) {
			GoodsCartItem temp = itFLC.next();
			if (temp.isChecked()) {
				itFLC.remove();
			}
		}
	}

	@Override
	public void onGetResult(int requestType, Object result) {
		diaLoading.hide();
		if (requestType == ORDER_SAVE_API) {
			if (result == null) {
				return;
			}

			OrderSave item = (OrderSave) result;
			if (0 == item.code) {
				removeGoodsBuy();
				ShopUtil.showOrderPay(getActivity(), item.orderId);
			} else {
				DialogCom.DiaCom(getActivity(), "购买失败");
			}
		} else if (requestType == CART_REMOVE_GOODS_API) {
			if (result == null) {
				diaLoading.hide();
				return;
			}

			int code = (Integer) result;
			if (0 == code) {
				Iterator<GoodsCartItem> itFLC = dataFLC.iterator();
				while (itFLC.hasNext()) {
					GoodsCartItem temp = itFLC.next();
					for (int id : goodsIdsToDelete) {
						if (temp.getGoodsItem().getGoods().getId() == id) {
							itFLC.remove();
							break;
						}
					}
				}
				updateCartInfo();
			} else {
				ComUtil.toastText(getActivity(), "删除失败", Toast.LENGTH_SHORT);
			}

			diaLoading.hide();
		} else if (requestType == CART_UPDATE_GOODS_API) {
			if (result == null) {
				diaLoading.hide();
				return;
			}

			int code = (Integer) result;
			if (0 == code) {
				dataFLC.get(curPosition).getGoodsItem().addCount(deltaNum);
				updateCartInfo();
			} else {
				ComUtil.toastText(getActivity(), "增减失败", Toast.LENGTH_SHORT);
			}

			diaLoading.hide();
		}
	}

	@Override
	public void onError(int requestType) {
		diaLoading.hide();
		if (requestType != REDIRECT_API) {
			ComUtil.toastText(getActivity(), "连接服务器出错", Toast.LENGTH_SHORT);
		}
	}

}
