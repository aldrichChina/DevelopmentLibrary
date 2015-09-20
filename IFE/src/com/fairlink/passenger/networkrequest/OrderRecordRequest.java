package com.fairlink.passenger.networkrequest;

import java.util.HashMap;
import java.util.List;

import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.bean.Order;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.JsonUtil;

/**
 * @ClassName ： OrderRecord
 * @Description: 订单记录
 */

public class OrderRecordRequest extends BaseHttpGetTask {

	public OrderRecordRequest(HttpTaskCallback callback) {
		super(ORDER_RECORD_API, HttpUtil.getOrderRecord(), new HashMap<String, String>(), callback);
	}

	@Override
	protected Object parseJSON(String json) {
		if (json == null) {
			return null;
		}

		int sum = 0;
		List<Order> list = (List<Order>) JsonUtil.parseJsonArray(json, Order.class);
		if (list != null) {
			for (Order item : list) {
				if (item.isUnhandled())
					sum++;
			}
		}

		IFEApplication.getInstance().setOrdersAllNum(sum);

		return list;
	}

}
