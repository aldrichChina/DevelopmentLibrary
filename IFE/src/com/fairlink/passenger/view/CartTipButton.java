package com.fairlink.passenger.view;

import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.R;
import com.fairlink.passenger.util.ShopUtil;

public class CartTipButton extends TipButton {

	@Override
	protected int getBackgroundResource() {
		return R.drawable.icon_com_cart;
	}

	@Override
	protected String getNumber() {
		return "" + IFEApplication.getInstance().getGoodsAllNum();
	}

	@Override
	protected void showActivity() {
		ShopUtil.showCart(getActivity());
	}
}