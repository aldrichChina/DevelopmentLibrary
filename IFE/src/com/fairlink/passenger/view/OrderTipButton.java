package com.fairlink.passenger.view;

import android.content.Intent;

import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.R;
import com.fairlink.passenger.order.OrderRecordActivity;

public class OrderTipButton extends TipButton {

	@Override
	protected int getBackgroundResource() {
		return R.drawable.icon_order_list;
	}

	@Override
	protected String getNumber() {
		return IFEApplication.getInstance().getOrdersAllNum();
	}

	@Override
	protected void showActivity() {
		startActivityForResult(new Intent(getActivity(), OrderRecordActivity.class), 20);
	}
}