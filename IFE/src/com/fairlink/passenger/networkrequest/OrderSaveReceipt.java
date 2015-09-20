package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.fairlink.passenger.util.HttpUtil;

/**
 * @ClassName  ：  OrderSaveReceipt 
 * @Description: 保存收货人信息
 * @author     ：  jiaxue 
 * @date       ：  2014-12-25 下午5:06:55 

 */

public class OrderSaveReceipt extends BaseHttpPostTask {

	public OrderSaveReceipt(Context context, HttpTaskCallback callback,
			String userAddress, String userPhone,
			String userName) {
		super(ORDER_RECEIPT_API, HttpUtil.saveOrderReceipt(),
				new HashMap<String, String>(), callback);
		mParam.put("userAddress", userAddress); // 收货人地址
		mParam.put("userPhone", userPhone); // 收货人联系电话
		mParam.put("userName", userName); // 0收货人姓名
	}

	@Override
	protected Object parseJSON(String json) {
		if (json == null) {
			return null;
		}
		
		int code = -1;
		
		JSONTokener parser = new JSONTokener(json);
		try {
			JSONObject menu = (JSONObject) parser.nextValue();

			code =  menu.getInt("code");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return code;
	}

}
