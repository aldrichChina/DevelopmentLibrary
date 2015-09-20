package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import android.content.Context;

import com.fairlink.passenger.bean.Order;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.JsonUtil;

/**
 * @ClassName ： OrderFindRequest
 * @Description: 支付订单列表
 */

public class OrderFindRequest extends BaseHttpGetTask {


	public OrderFindRequest(Context context, HttpTaskCallback callback, String orderId) {
		super(ORDER_FIND_API, HttpUtil.getOrderFind() + orderId, new HashMap<String, String>(), callback);
	}

	@Override
	protected Object parseJSON(String json) {
		return JsonUtil.parseJsonObjcet(json, Order.class);
	}
}