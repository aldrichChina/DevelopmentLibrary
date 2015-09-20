package com.fairlink.passenger.cart;

import com.fairlink.passenger.bean.GoodsItem;

public class GoodsCartItem {

	private GoodsItem goodsItem;
	private boolean isChecked;

	public GoodsItem getGoodsItem() {
		return goodsItem;
	}

	public void setGoodsItem(GoodsItem goodsItem) {
		this.goodsItem = goodsItem;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}
