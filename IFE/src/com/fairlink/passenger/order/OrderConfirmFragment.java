package com.fairlink.passenger.order;

import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.R;
import com.fairlink.passenger.adapter.GoodsOrderAdapter;
import com.fairlink.passenger.bean.RecordGoodsItem;
import com.fairlink.passenger.cart.GoodsCartItem;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.CartRemoveGoodsRequest;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.networkrequest.OrderGetReceipt;
import com.fairlink.passenger.networkrequest.OrderGetReceipt.Recipient;
import com.fairlink.passenger.networkrequest.OrderRequest;
import com.fairlink.passenger.networkrequest.OrderRequest.OrderSave;
import com.fairlink.passenger.networkrequest.OrderSaveReceipt;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.Constant;
import com.fairlink.passenger.util.ShopUtil;
import com.fairlink.passenger.view.DialogCom;
import com.fairlink.passenger.view.DialogLoading;
import com.fairlink.passenger.view.ListViewForScrollView;

/**
 * @ClassName ： OrderConfirmFragment
 * @Description: 订单支付界面
 */

public class OrderConfirmFragment extends BaseFragment implements HttpTaskCallback, NetworkRequestAPI {

	private EditText etNameGetGoods;
	private EditText etAddressGetGoods;
	private EditText etPhoneGetGoods;
	private ImageView imgNameGetGoodsStar;
	private ImageView imgAddressGetGoodsStar;
	private ImageView imgPhoneGetGoodsStar;
	private Button btnUserInfoSave;
	private Button btnUserInfoChange;
	private LinearLayout ll_user_info;
	private String mall;
	private int payType;
	private boolean fromCart;
	private int deliveryType = -1;
	private Button btnOrderpay;
	private List<RecordGoodsItem> goodsList;
	private GoodsOrderAdapter goodsListAdapter;
	private ListViewForScrollView goodsListView;
	private String createdOrderId;
	private TextView tvPayType;
	private TextView tvDeliveryType;
	private DialogLoading diaLoading;
	private Button btnOrderBack;
	private TextView tvOrderTotalPrice;
	private TextView tvNoneInvoice;
	private TextView tvNormalInvoice;
	private RadioGroup rgInvoiceTitleGroup;
	private RadioButton rbInvoicePersonal;
	private RadioButton rbInvoiceCompany;
	private LinearLayout llInvoiceContent;
	private LinearLayout llInvoiceTitle;
	private TextView tvInvoiceDetail;
	private Button btnInvoiceInfoSave;
	private Button btnInvoiceInfoChange;
	private EditText etCompanyName;
	private ImageView imgCompanyNameStar;
	private TextView tvInvoiceTitle;

	private InvoiceType invoiceType = InvoiceType.NONE; // 0: 没有发票  1: 个人 2: 公司

	private boolean receiptSuccess; // 是否保存收件人信息成功
	private boolean invoiceSuccess = true; // 是否保存发票信息成功

	private static enum InvoiceType {
		NONE, //没有发票
		PERSONAL, // 个人
		COMPANY	//公司
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		diaLoading = new DialogLoading(activity);
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.order_confirm_fragment, null);

		Bundle bundle = getActivity().getIntent().getExtras();

		mall = bundle.getString("mall");
		payType = bundle.getInt("payType");
		deliveryType = bundle.getInt("wayDelivery");
		goodsList = (List<RecordGoodsItem>) bundle.getSerializable("goodsList");
		fromCart = bundle.getBoolean("fromCart", false);

		initView(view);
		setListener();
		if (deliveryType == 0)// 机上配送
			ll_user_info.setVisibility(View.GONE);

		return view;
	}

	public void onDestroy() {
		super.onDestroy();

		if (diaLoading != null) {
			diaLoading.dismiss();
		}

	}

	// 保存收货人信息
	private void OrderReceipt(String address, String phone, String name) {
		diaLoading.show();

		new OrderSaveReceipt(getActivity(), this, address, phone, name).execute((String) null);
	}

	private void initView(View v) {

		etNameGetGoods = (EditText) v.findViewById(R.id.et_name_get_goods);
		etAddressGetGoods = (EditText) v.findViewById(R.id.et_address_get_goods);
		etPhoneGetGoods = (EditText) v.findViewById(R.id.et_phone_get_goods);

		imgNameGetGoodsStar = (ImageView) v.findViewById(R.id.img_name_get_goods_star);
		imgAddressGetGoodsStar = (ImageView) v.findViewById(R.id.img_address_get_goods_star);
		imgPhoneGetGoodsStar = (ImageView) v.findViewById(R.id.img_phone_get_goods_star);

		new OrderGetReceipt(getActivity(), this).execute((String) null);

		btnUserInfoSave = (Button) v.findViewById(R.id.btn_save);
		btnUserInfoChange = (Button) v.findViewById(R.id.btn_change);
		btnUserInfoChange.setText(Html.fromHtml(getString(R.string.change_user_info)));

		goodsListView = (ListViewForScrollView) v.findViewById(R.id.list_goods);

		goodsListAdapter = new GoodsOrderAdapter(getActivity());
		goodsListAdapter.setList(goodsList);
		goodsListView.setAdapter(goodsListAdapter);

		btnOrderpay = (Button) v.findViewById(R.id.btn_order_pay);

		ll_user_info = (LinearLayout) v.findViewById(R.id.ll_user_info);

		tvPayType = (TextView) v.findViewById(R.id.tv_pay_type);
		switch (payType) {
			case 0:
				tvPayType.setText(getString(R.string.pay_cash));
				break;
			case 1:
				tvPayType.setText(getString(R.string.pay_score));
				break;
			case 2:
				tvPayType.setText(getString(R.string.pay_card));
				break;
			default:
				tvPayType.setText(getString(R.string.pay_cash));
				break;
		}
		tvDeliveryType = (TextView) v.findViewById(R.id.tv_delivery_type);
		tvDeliveryType.setText(getString(deliveryType == 0 ? R.string.delivery_up : R.string.delivery_down));

		btnOrderBack = (Button) v.findViewById(R.id.btn_order_back);
		btnOrderBack.setText(Html.fromHtml(getString(R.string.order_back)));

		tvOrderTotalPrice = (TextView) v.findViewById(R.id.tv_order_total_price);

		double orderTotalPrice = 0.0;
		for (RecordGoodsItem goodsItem : goodsList) {
			orderTotalPrice += goodsItem.goodsBuyCount * goodsItem.goodsPrice;
		}
		tvOrderTotalPrice.setText(Html.fromHtml(getString(R.string.cart_all_price, orderTotalPrice)));

		btnInvoiceInfoSave = (Button) v.findViewById(R.id.btn_save_invoice);
		btnInvoiceInfoSave.setVisibility(View.GONE);
		btnInvoiceInfoChange = (Button) v.findViewById(R.id.btn_change_invoice);
		btnInvoiceInfoChange.setText(Html.fromHtml(getString(R.string.change_invoice_info)));

		rgInvoiceTitleGroup = (RadioGroup) v.findViewById(R.id.invoice_title_group);

		rbInvoicePersonal = (RadioButton) v.findViewById(R.id.title_personal);
		rbInvoiceCompany = (RadioButton) v.findViewById(R.id.title_company);
		
		tvNoneInvoice = (TextView) v.findViewById(R.id.tv_none_invoice);

		tvNormalInvoice = (TextView) v.findViewById(R.id.tv_normal_invoice);
		tvNormalInvoice.setVisibility(View.GONE);

		llInvoiceTitle = (LinearLayout) v.findViewById(R.id.ll_invoice_title);
		llInvoiceTitle.setVisibility(View.GONE);

		llInvoiceContent = (LinearLayout) v.findViewById(R.id.ll_invoice_content);
		llInvoiceContent.setVisibility(View.GONE);

		etCompanyName = (EditText) v.findViewById(R.id.et_company_name);
		etCompanyName.setVisibility(View.GONE);
		imgCompanyNameStar = (ImageView) v.findViewById(R.id.img_company_name_star);
		imgCompanyNameStar.setVisibility(View.GONE);

		tvInvoiceDetail = (TextView) v.findViewById(R.id.tv_invoice_content);
		tvInvoiceTitle = (TextView) v.findViewById(R.id.tv_invoice_title);

		diaLoading.show();
	}

	private void setListener() {

		btnUserInfoSave.setOnClickListener(saveClickListener);
		btnUserInfoChange.setOnClickListener(saveClickListener);
		btnOrderpay.setOnClickListener(orderPayClickListener);
		btnOrderBack.setOnClickListener(orderBackClickListener);
		rgInvoiceTitleGroup.setOnCheckedChangeListener(invoiceTitleGroupClickListener);
		tvNormalInvoice.setOnClickListener(normalInvoiceClickListener);		
		tvNoneInvoice.setOnClickListener(noneInvoiceClickListener);
		btnInvoiceInfoChange.setOnClickListener(invoiceInfoChangeClickListener);
		btnInvoiceInfoSave.setOnClickListener(invoiceInfoSaveClickListener);
	}

	private View.OnClickListener saveClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (receiptSuccess) {
				// 修改收货人信息
				etNameGetGoods.setFocusable(true);
				etNameGetGoods.setFocusableInTouchMode(true);
				etAddressGetGoods.setFocusable(true);
				etAddressGetGoods.setFocusableInTouchMode(true);
				etPhoneGetGoods.setFocusable(true);
				etPhoneGetGoods.setFocusableInTouchMode(true);

				etNameGetGoods.setEnabled(true);
				etNameGetGoods.setBackgroundResource(R.drawable.bg_edit_order);
				imgNameGetGoodsStar.setVisibility(View.VISIBLE);
				etAddressGetGoods.setEnabled(true);
				etAddressGetGoods.setBackgroundResource(R.drawable.bg_edit_order);
				imgAddressGetGoodsStar.setVisibility(View.VISIBLE);
				etPhoneGetGoods.setEnabled(true);
				etPhoneGetGoods.setBackgroundResource(R.drawable.bg_edit_order);
				imgPhoneGetGoodsStar.setVisibility(View.VISIBLE);
				btnUserInfoSave.setVisibility(View.VISIBLE);
				btnUserInfoChange.setVisibility(View.GONE);
				receiptSuccess = false;
			} else {
				if (ComUtil.isEmpty(etNameGetGoods.getText().toString())) {
					DialogCom.DiaCom(getActivity(), "收件人姓名不能为空");
					return;
				}
				if (ComUtil.isEmpty(etAddressGetGoods.getText().toString())) {
					DialogCom.DiaCom(getActivity(), "收件地址不能为空");
					return;
				}

				if (ComUtil.isEmpty(etPhoneGetGoods.getText().toString())) {
					DialogCom.DiaCom(getActivity(), "联系电话不能为空");
					return;
				}

				if (!ComUtil.isMobileNO(etPhoneGetGoods.getText().toString())) {
					DialogCom.DiaCom(getActivity(), "联系电话格式不正确");
					return;
				}

				OrderReceipt(etAddressGetGoods.getText().toString(), etPhoneGetGoods.getText().toString(),
						etNameGetGoods.getText().toString());
			}

		}
	};

	private View.OnClickListener orderPayClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			if (!receiptSuccess) {
				DialogCom.DiaCom(getActivity(), "请先填写并保存收货人信息");
				return;
			}

			if (!invoiceSuccess) {
				DialogCom.DiaCom(getActivity(), "请先填写并保存发票信息");
				return;
			}

			StringBuilder goodsIds = new StringBuilder();
			float totalPrice = 0;
			for (RecordGoodsItem  goods : goodsList) {
				goodsIds.append(goods.goodsId);
				goodsIds.append(',');
				goodsIds.append(goods.goodsBuyCount);
				goodsIds.append(',');
				totalPrice += goods.goodsPrice * goods.goodsBuyCount;
			}

			int type = 0;
			String title;
			switch(invoiceType) {
				case NONE:
					type = 0;
					title = null;
					break;
				case PERSONAL:
					type = 1;
					title = new String(tvInvoiceTitle.getText().toString());
					break;
				case COMPANY:
					type = 2;
					title = new String(tvInvoiceTitle.getText().toString());
					break;
				default:
					type = 0;
					title = null;
					break;
			}
			new OrderRequest(getActivity(), OrderConfirmFragment.this, goodsIds.deleteCharAt(goodsIds.length() - 1)
					.toString(), mall, payType, totalPrice, "", type, title).execute((String) null);

			diaLoading.show();
		}
	};

	@Override
	public void onGetResult(int requestType, Object result) {

		if (requestType == ORDER_RECEIPT_API) {

			diaLoading.hide();

			if (result == null) {
				return;
			}

			int code = (Integer) result;

			if (0 == code) {
				DialogCom.DiaCom(getActivity(), "保存收货人信息成功");
				receiptSuccess = true;
				etNameGetGoods.setFocusable(false);
				etAddressGetGoods.setFocusable(false);
				etPhoneGetGoods.setFocusable(false);

//				etNameGetGoods.setEnabled(false);
				etNameGetGoods.setBackgroundColor(Color.rgb(255, 255, 255));
				imgNameGetGoodsStar.setVisibility(View.GONE);
//				etAddressGetGoods.setEnabled(false);
				etAddressGetGoods.setBackgroundColor(Color.rgb(255, 255, 255));
				imgAddressGetGoodsStar.setVisibility(View.GONE);
//				etPhoneGetGoods.setEnabled(false);
				etPhoneGetGoods.setBackgroundColor(Color.rgb(255, 255, 255));
				imgPhoneGetGoodsStar.setVisibility(View.GONE);
				btnUserInfoSave.setVisibility(View.GONE);
				btnUserInfoChange.setVisibility(View.VISIBLE);
			} else {
				DialogCom.DiaCom(getActivity(), "保存收货人信息失败");
			}
		} else if (requestType == ORDER_GET_RECEIPT_API) {

			diaLoading.hide();

			if (result == null) {
				return;
			}

			Recipient r = (Recipient) result;

			etNameGetGoods.setFocusable(false);
			etAddressGetGoods.setFocusable(false);
			etPhoneGetGoods.setFocusable(false);

//			etNameGetGoods.setEnabled(false);
			etNameGetGoods.setBackgroundColor(Color.WHITE);
			etNameGetGoods.setText(r.userName);
			imgNameGetGoodsStar.setVisibility(View.GONE);

//			etAddressGetGoods.setEnabled(false);
			etAddressGetGoods.setBackgroundColor(Color.WHITE);
			etAddressGetGoods.setText(r.userAddress);
			imgAddressGetGoodsStar.setVisibility(View.GONE);

//			etPhoneGetGoods.setEnabled(false);
			etPhoneGetGoods.setBackgroundColor(Color.WHITE);
			etPhoneGetGoods.setText(r.userPhone);
			imgPhoneGetGoodsStar.setVisibility(View.GONE);

			btnUserInfoSave.setVisibility(View.GONE);
			btnUserInfoChange.setVisibility(View.VISIBLE);
			receiptSuccess = true;

		} else if (requestType == ORDER_SAVE_API) {

			if (result == null) {
				DialogCom.DiaCom(getActivity(), "提交订单失败");
				diaLoading.hide();
				return;
			}

			OrderSave item = (OrderSave) result;

			if (0 == item.code) {
				createdOrderId = item.orderId;
				if (fromCart) {
					removeGoodsBuyFromCart();
					return;
				}

				diaLoading.hide();

				finish();
			} else {
				diaLoading.hide();
				DialogCom.DiaCom(getActivity(), "提交订单失败");
			}
		} else if (requestType == CART_REMOVE_GOODS_API) {
			diaLoading.hide();
			if (result == null) {
				return;
			}
			int code = (Integer) result;

			if (code != 0) {
				ComUtil.toastText(getActivity(), "同步购物车失败", Toast.LENGTH_SHORT);
				return;
			}

			Iterator<GoodsCartItem> itFLC = IFEApplication.getInstance().getDataFLC().iterator();
			while (itFLC.hasNext()) {
				if (itFLC.next().isChecked()) {
					itFLC.remove();
				}
			}
			finish();
		}

	}

	private void finish() {
		if (payType == Constant.PAYTYPE_CASH) {
			ShopUtil.showOrderSuccess(getActivity());
		} else {
			ShopUtil.showOrderPay(getActivity(), createdOrderId);
		}

		getActivity().finish();
	}

	@Override
	public void onError(int requestType) {
		diaLoading.hide();
		if (requestType != REDIRECT_API) {
			ComUtil.toastText(getActivity(), "连接服务器出错", Toast.LENGTH_SHORT);
		}
	}

	/**
	 * 购买成功后删除购买的商品
	 */
	private void removeGoodsBuyFromCart() {

		StringBuilder goodsIds = new StringBuilder();
		for (RecordGoodsItem  goods : goodsList) {
			goodsIds.append(goods.goodsId);
			goodsIds.append(',');
		}

		new CartRemoveGoodsRequest(getActivity(), OrderConfirmFragment.this, goodsIds.deleteCharAt(
				goodsIds.length() - 1).toString()).execute((String) null);
	}

	private View.OnClickListener orderBackClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			getActivity().finish();
		}
	};

	private RadioGroup.OnCheckedChangeListener invoiceTitleGroupClickListener = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int radioButtonId = group.getCheckedRadioButtonId();
			RadioButton rb = (RadioButton) getActivity().findViewById(radioButtonId);
			if (rb.getText().equals("公司")){
				etCompanyName.setVisibility(View.VISIBLE);
				imgCompanyNameStar.setVisibility(View.VISIBLE);
				invoiceType = InvoiceType.COMPANY;
			} else {
				etCompanyName.setVisibility(View.GONE);
				imgCompanyNameStar.setVisibility(View.GONE);
				invoiceType = InvoiceType.PERSONAL;
			}
			
		}
	};

	private View.OnClickListener normalInvoiceClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (invoiceSuccess == false && invoiceType == InvoiceType.NONE) {
				tvNormalInvoice.setBackgroundResource(R.drawable.bg_edit_blue);
				tvNoneInvoice.setBackgroundResource(R.drawable.bg_black);

				llInvoiceTitle.setVisibility(View.VISIBLE);
				llInvoiceContent.setVisibility(View.VISIBLE);

				rbInvoicePersonal.setChecked(true);
				rbInvoiceCompany.setChecked(false);

				etCompanyName.setVisibility(View.GONE);
				imgCompanyNameStar.setVisibility(View.GONE);

				invoiceType = InvoiceType.PERSONAL;
			}
		}
	};

	private View.OnClickListener noneInvoiceClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (invoiceSuccess == false) {
				tvNormalInvoice.setBackgroundResource(R.drawable.bg_black);
				tvNoneInvoice.setBackgroundResource(R.drawable.bg_edit_blue);

				llInvoiceTitle.setVisibility(View.GONE);
				llInvoiceContent.setVisibility(View.GONE);

				invoiceType = InvoiceType.NONE;
			}
		}
	};

	private View.OnClickListener invoiceInfoChangeClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (invoiceType) {
				case NONE:
					tvNoneInvoice.setBackgroundResource(R.drawable.bg_edit_blue);
					tvNormalInvoice.setVisibility(View.VISIBLE);
					tvNormalInvoice.setBackgroundResource(R.drawable.bg_black);
					break;
				case PERSONAL:
					tvNoneInvoice.setVisibility(View.VISIBLE);
					tvNoneInvoice.setBackgroundResource(R.drawable.bg_black);
					tvNormalInvoice.setBackgroundResource(R.drawable.bg_edit_blue);

					rgInvoiceTitleGroup.setVisibility(View.VISIBLE);
					tvInvoiceTitle.setVisibility(View.GONE);

					tvInvoiceDetail.setBackgroundResource(R.drawable.bg_edit_blue);
					break;
				case COMPANY:
					tvNoneInvoice.setVisibility(View.VISIBLE);
					tvNoneInvoice.setBackgroundResource(R.drawable.bg_black);
					tvNormalInvoice.setBackgroundResource(R.drawable.bg_edit_blue);

					rgInvoiceTitleGroup.setVisibility(View.VISIBLE);
					tvInvoiceTitle.setVisibility(View.GONE);

					etCompanyName.setVisibility(View.VISIBLE);
					imgCompanyNameStar.setVisibility(View.VISIBLE);

					tvInvoiceDetail.setBackgroundResource(R.drawable.bg_edit_blue);
					break;
			}
			btnInvoiceInfoChange.setVisibility(View.GONE);
			btnInvoiceInfoSave.setVisibility(View.VISIBLE);
			invoiceSuccess = false;
		}
	};

	private View.OnClickListener invoiceInfoSaveClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (invoiceType) {
				case NONE:
					tvNoneInvoice.setBackgroundColor(getResources().getColor(R.color.white));
					tvNormalInvoice.setVisibility(View.GONE);
					break;
				case PERSONAL:
					tvNormalInvoice.setBackgroundColor(getResources().getColor(R.color.white));
					tvNoneInvoice.setVisibility(View.GONE);

					rgInvoiceTitleGroup.setVisibility(View.GONE);
					tvInvoiceTitle.setVisibility(View.VISIBLE);
					RadioButton rbPersonal = (RadioButton) getActivity().findViewById(rgInvoiceTitleGroup.getCheckedRadioButtonId());
					tvInvoiceTitle.setText(rbPersonal.getText().toString());

					tvInvoiceDetail.setBackgroundColor(getResources().getColor(R.color.white));
					break;
				case COMPANY:
					if (ComUtil.isEmpty(etCompanyName.getText().toString())) {
						DialogCom.DiaCom(getActivity(), "公司名称不能为空");
						return;
					}
					tvNormalInvoice.setBackgroundColor(getResources().getColor(R.color.white));
					tvNoneInvoice.setVisibility(View.GONE);

					rgInvoiceTitleGroup.setVisibility(View.GONE);
					tvInvoiceTitle.setVisibility(View.VISIBLE);
					tvInvoiceTitle.setText(etCompanyName.getText().toString());

					etCompanyName.setVisibility(View.GONE);
					imgCompanyNameStar.setVisibility(View.GONE);

					tvInvoiceDetail.setBackgroundColor(getResources().getColor(R.color.white));
					break;
			}

			btnInvoiceInfoSave.setVisibility(View.GONE);
			btnInvoiceInfoChange.setVisibility(View.VISIBLE);
			invoiceSuccess = true;
		}
	};
	
}
