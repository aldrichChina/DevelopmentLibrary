package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.fairlink.passenger.util.HttpUtil;

public class CartUpdateGoodsRequest extends BaseHttpPostTask {
	
	public CartUpdateGoodsRequest(Context context, HttpTaskCallback callback,
								int id, int count) {
		super(CART_UPDATE_GOODS_API, HttpUtil.getCartUpdateGoods(), new HashMap<String, String>(), callback);
		mParam.put("id", id + "");
		mParam.put("count", String.valueOf(count));
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