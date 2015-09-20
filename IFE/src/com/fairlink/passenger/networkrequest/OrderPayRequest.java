package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.util.HttpUtil;

/**
 * @ClassName  ：  OrderPayRequest 
 * @Description: 支付订单 

 */

public class OrderPayRequest extends BaseHttpPostTask {

	public OrderPayRequest(Context context, HttpTaskCallback callback,
			String orderId,
			String cardOwnerName,
			String creditNumber,
			String cardOwnerPhoneNumber,
			String idCardno,
			String payCvv2,
			String payCardOverdue
			) {
		super(ORDER_PAY_API, HttpUtil.getOrderPay(), new HashMap<String, String>(), callback);
		
		mParam.put("orderId", orderId); // 订单组标示号
		mParam.put("cardOwnerName", cardOwnerName); // 持卡人姓名
		mParam.put("cardOwnerPhoneNumber", cardOwnerPhoneNumber); // 持卡人手机号码
		mParam.put("creditNumber", creditNumber); // 信用卡号
		mParam.put("payCvv2", payCvv2); // Cvv2号
		mParam.put("payCardOverdue", payCardOverdue); // 有效期
		mParam.put("idCardno", idCardno); 
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
			if (code == 0)
				IFEApplication.getInstance().decreaseOrdersNum();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return code;
	}

}
