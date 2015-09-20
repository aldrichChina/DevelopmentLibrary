package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.util.HttpUtil;

/**
 * @ClassName ： OrderSaveRequest
 * @Description: 立即购买或者结算操作
 */

public class OrderRequest extends BaseHttpPostTask {

	public static class OrderSave {
		public int code;
		public String orderId;
	}

	public OrderRequest(Context context, HttpTaskCallback callback, String orderGoodsList, String mall, int payWay,
			float totalPrice, String remark, int invoiceType, String invoiceTitle) {
		super(ORDER_SAVE_API, HttpUtil.getOrderSave(), new HashMap<String, String>(), callback);
		mParam.put("orderGoodsList", orderGoodsList); // 座位号
		mParam.put("payWay", String.valueOf(payWay)); // 购物车数据(json格式)
		mParam.put("totalPrice", String.valueOf(totalPrice)); // 航班号
		mParam.put("remark", remark); // 0:护照,1:身份证
		mParam.put("mall", mall);
		mParam.put("invoiceType", String.valueOf(invoiceType));
		mParam.put("invoiceTitle", invoiceTitle);
	}

	@Override
	protected Object parseJSON(String json) {
		if (json == null) {
			return null;
		}

		JSONTokener parser = new JSONTokener(json);
		OrderSave item = new OrderSave();

		try {
			JSONObject menu = (JSONObject) parser.nextValue();

			item.code = menu.getInt("code");
			if (0 == item.code) {
				item.orderId = menu.getString("data");

				IFEApplication.getInstance().addOrdersNum();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return item;
	}

}
