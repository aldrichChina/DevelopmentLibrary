package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.fairlink.passenger.util.HttpUtil;

/**
 * @ClassName ： OrderSaveReceipt
 * @Description: 保存收货人信息
 */

public class OrderGetReceipt extends BaseHttpGetTask {
	public static class Recipient {
		public String userAddress;
		public String userPhone;
		public String userName;
	}

	public OrderGetReceipt(Context context, HttpTaskCallback callback) {
		super(ORDER_GET_RECEIPT_API, HttpUtil.getOrderReceipt(),
				new HashMap<String, String>(), callback);
	}

	@Override
	protected Object parseJSON(String json) {
		if (json == null) {
			return null;
		}

		Recipient r = null ;

		JSONTokener parser = new JSONTokener(json);
		try {
			JSONObject menu = (JSONObject) parser.nextValue();

			if(menu.getInt("code") == 0){
				JSONObject data = menu.getJSONObject("data");
				
				r = new Recipient();
				r.userAddress = data.getString("userAddress");
				r.userName = data.getString("receiveMan");
				r.userPhone = data.getString("receivePhone");
				
			}
			

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return r;
	}

}
