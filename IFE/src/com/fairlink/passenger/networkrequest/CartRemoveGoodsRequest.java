package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.fairlink.passenger.util.HttpUtil;

public class CartRemoveGoodsRequest extends BaseHttpPostTask {

	public CartRemoveGoodsRequest(Context context, HttpTaskCallback callback, int id) {
		super(CART_REMOVE_GOODS_API, HttpUtil.getCartRemoveGoods(), new HashMap<String, String>(), callback);
		mParam.put("goodsArray", id + "");
	}

	public CartRemoveGoodsRequest(Context context, HttpTaskCallback callback, String id) {
		super(CART_REMOVE_GOODS_API, HttpUtil.getCartRemoveGoods(), new HashMap<String, String>(), callback);
		mParam.put("goodsArray", id);	}

	@Override
	protected Object parseJSON(String json) {
		if (json == null) {
			return null;
		}

		int code = -1;

		JSONTokener parser = new JSONTokener(json);
		try {
			JSONObject menu = (JSONObject) parser.nextValue();
			code = menu.getInt("code");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return code;
	}

}
