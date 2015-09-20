package com.fairlink.passenger.order;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.R;
import com.fairlink.passenger.adapter.GoodsOrderAdapter;
import com.fairlink.passenger.bean.GoodsItem;
import com.fairlink.passenger.bean.Order;
import com.fairlink.passenger.bean.RecordGoodsItem;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.networkrequest.OrderFindRequest;
import com.fairlink.passenger.networkrequest.OrderPayRequest;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.MD5Util;
import com.fairlink.passenger.util.ShopUtil;
import com.fairlink.passenger.view.ComDialog;
import com.fairlink.passenger.view.ComDialog.OnCommitListener;
import com.fairlink.passenger.view.DialogCom;
import com.fairlink.passenger.view.DialogLoading;
import com.fairlink.passenger.view.ListViewForScrollView;

/**
 * @ClassName ： OrderPayFragment
 * @Description: 订单支付界面
 */

public class OrderPayFragment extends BaseFragment implements HttpTaskCallback, NetworkRequestAPI, OnFocusChangeListener {

	private ScrollView svOrderPay;
	private EditText etNamePayGoods;
	private EditText etCardPayGoods;
	private EditText etPhonePayGoods;
	private EditText etNOPayGoods;
	private EditText etCvv2;
	private EditText etYear;
	private EditText etMonth;
	private ListViewForScrollView listCardLine;
	private TextView tvCardPlaneOrder;
	private TextView tvCardPlaneMall;
	private TextView tvCardPlaneDelivery;
	private TextView tvCardPlanePrice;
	private Button btnOrderpay;
	private GoodsOrderAdapter adapterGoodsList;
	private DialogLoading diaLoading;
	private String orderId; // 订单组标示号

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		diaLoading = new DialogLoading(activity);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.order_pay_fragment, null);
		Bundle bundle = getActivity().getIntent().getExtras();
		orderId = bundle.getString("orderId");
		initView(view);
		OrderFind(orderId);
		return view;
	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	public void onDestroy() {
		super.onDestroy();

		if (diaLoading != null) {
			diaLoading.dismiss();
		}
	}

	public void OrderFind(String orderId) {
		diaLoading.show();
		new OrderFindRequest(getActivity(), this, orderId).execute((String) null);
	}

	// 支付订单
	private void OrderPay() {
		diaLoading.show();

		new OrderPayRequest(getActivity(), this, orderId, etNamePayGoods.getText().toString(), etCardPayGoods.getText()
				.toString(), etPhonePayGoods.getText().toString(), etNOPayGoods.getText().toString(), etCvv2.getText()
				.toString(), etMonth.getText().toString() + etYear.getText().toString()).execute((String) null);
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus)
			return;
		
		completeValue((EditText)v);
	}

	private void completeValue(EditText et) {
		String value = et.getText().toString();
		if (value.length() == 1) {
			et.setText("0" + value);
		}
	}

	private void initView(View v) {

		svOrderPay = (ScrollView) v.findViewById(R.id.sv_order_pay);
		tvCardPlaneOrder = (TextView) v.findViewById(R.id.tv_card_plane_order);
		tvCardPlaneMall = (TextView) v.findViewById(R.id.tv_card_plane_mall);
		tvCardPlaneDelivery = (TextView) v.findViewById(R.id.tv_card_plane_delivery);
		tvCardPlanePrice = (TextView) v.findViewById(R.id.tv_card_plane_price);
		etNamePayGoods = (EditText) v.findViewById(R.id.et_name_pay_goods);
		etCardPayGoods = (EditText) v.findViewById(R.id.et_card_pay_goods);
		etPhonePayGoods = (EditText) v.findViewById(R.id.et_phone_pay_goods);
		etNOPayGoods = (EditText) v.findViewById(R.id.et_no_pay_goods);
		etCvv2 = (EditText) v.findViewById(R.id.et_cvv2_pay_goods);
		etYear = (EditText) v.findViewById(R.id.et_year);
		etYear.setOnFocusChangeListener(this);
		etMonth = (EditText) v.findViewById(R.id.et_month);
		etMonth.setOnFocusChangeListener(this);
		btnOrderpay = (Button) v.findViewById(R.id.btn_order_pay);
		listCardLine = (ListViewForScrollView) v.findViewById(R.id.list_card_plane);
		adapterGoodsList = new GoodsOrderAdapter(getActivity());
		listCardLine.setAdapter(adapterGoodsList);
		btnOrderpay.setOnClickListener(orderPayClickListener);
	}

	private View.OnClickListener orderPayClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			if (processEditVerify()) {

				// listener.Analytics(3,orderId);
				ComDialog dialogBuilder = ComDialog.getInstance(getActivity());
				dialogBuilder.withMessage(true, "信息提交后不能修改！点击取消可以返回核对信息").withBtnSureText("确定支付")
						.setmListener(onSuerlistener).show();
			}
		}
	};

	private OnCommitListener onSuerlistener = new OnCommitListener() {

		@Override
		public void doSomeThings() {

			// listener.Analytics(4,orderId);
			OrderPay();

		}
	};

	/** 处理验证输入信息 */
	private boolean processEditVerify() {
		
		completeValue(etMonth);
		completeValue(etYear);

		String md5 = MD5Util.GetMD5Code((etNOPayGoods.getText().toString()));
		md5 = MD5Util.GetMD5Code(md5);

		if (ComUtil.isEmpty(etNamePayGoods.getText().toString())) {
			DialogCom.DiaCom(getActivity(), "支付人姓名不能为空");
		} else if (ComUtil.isEmpty(etCardPayGoods.getText().toString())) {

			DialogCom.DiaCom(getActivity(), "信用卡号不能为空");
		} else if (etCardPayGoods.getText().length()<8) {

			DialogCom.DiaCom(getActivity(), "信用卡号格式不正确");
		} else if (ComUtil.isEmpty(etPhonePayGoods.getText().toString())) {

			DialogCom.DiaCom(getActivity(), "手机号码不能为空");
		} else if (!ComUtil.isMobileNO(etPhonePayGoods.getText().toString())) {

			DialogCom.DiaCom(getActivity(), "手机号码格式不正确");
		} else if (ComUtil.isEmpty(etNOPayGoods.getText().toString())) {

			DialogCom.DiaCom(getActivity(), "身份证不能为空");

		} else if (ComUtil.isEmpty(etCvv2.getText().toString())) {

			DialogCom.DiaCom(getActivity(), "Cvv2不能为空");
		} else if (etCvv2.getText().length()<3) {

			DialogCom.DiaCom(getActivity(), "Cvv2格式不正确");
		} else if (ComUtil.isEmpty(etYear.getText().toString()) || (ComUtil.isEmpty(etMonth.getText().toString()))) {

			DialogCom.DiaCom(getActivity(), "有效期不能为空");
		} else if (getMonthValue() > 12 || getMonthValue() == 0) {
			DialogCom.DiaCom(getActivity(), "有效期(月)格式不正确");
		} else {
			return true;
		}

		return false;
	}

	private int getMonthValue() {
		try {
			return Integer.parseInt(etMonth.getText().toString());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/** 处理信用卡订单 */
	private void processCardList(Order item) {
		tvCardPlaneOrder.setText("订单号:" + item.getOrderNoOnflight());
		tvCardPlaneMall.setText(ShopUtil.getMallName(item.getGoodsMall()));
		tvCardPlaneDelivery
				.setText(getString(item.getDistributionWay() == 0 ? R.string.delivery_up : R.string.delivery_down));
		tvCardPlanePrice.setText("金额:￥" + String.format("%.2f", item.getOrderGoodsPrice()));
		List<RecordGoodsItem> list = new ArrayList<RecordGoodsItem> ();
		for (GoodsItem tmp : item.getGoodsList()) {
			list.add(tmp.getRecordGoodsItem());
		}
		adapterGoodsList.setList(list);
	}

	@Override
	public void onGetResult(int requestType, Object result) {

		if (requestType == ORDER_FIND_API) {

			diaLoading.hide();

			if (result == null) {
				DialogCom.DiaCom(getActivity(), "订单获取失败");
				return;
			}

			Order item = (Order) result;

			svOrderPay.setVisibility(View.VISIBLE);
			svOrderPay.smoothScrollTo(0, 0);
			processCardList(item);

		} else if (requestType == ORDER_PAY_API) {
			diaLoading.hide();

			if (result == null) {
				return;
			}

			int code = (Integer) result;

			if (0 == code) {
				ShopUtil.showOrderSuccess(getActivity());
				getActivity().finish();
			} else if (1 == code) {
				DialogCom.DiaCom(getActivity(), "身份证填写错误");
			} else if (2 == code) {
				DialogCom.DiaCom(getActivity(), "填写姓名错误");
			} else {
				DialogCom.DiaCom(getActivity(), "支付订单失败");
			}
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
