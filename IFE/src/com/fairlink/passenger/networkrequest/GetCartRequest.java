package com.fairlink.passenger.networkrequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.bean.GoodsItem;
import com.fairlink.passenger.cart.GoodsCartItem;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.JsonUtil;

public class GetCartRequest extends BaseHttpGetTask {

	public GetCartRequest(HttpTaskCallback callback) {
		super(CART_GET_GOODS_API, HttpUtil.getCart(), new HashMap<String, String>(), callback);
	}

	@Override
	protected Object parseJSON(String json) {
		if (json == null) {
			return null;
		}

		List<GoodsCartItem> list = new ArrayList<GoodsCartItem>();
		List<GoodsItem> src = JsonUtil.parseJsonArray(json, GoodsItem.class);
		if (src != null) {

			for (GoodsItem item : src) {
				GoodsCartItem cartItem = new GoodsCartItem();
				cartItem.setChecked(false);
				cartItem.setGoodsItem(item);
				list.add(cartItem);
			}
		}

		IFEApplication.getInstance().setDataFLC(list);

		return null;

	}

}
