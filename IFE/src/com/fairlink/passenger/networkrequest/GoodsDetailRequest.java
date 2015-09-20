package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import android.content.Context;

import com.fairlink.passenger.bean.Goods;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.JsonUtil;

/**
 * @ClassName ： GoodsDetailRequest
 * @Description: 获取商品详情
 */

public class GoodsDetailRequest extends BaseHttpGetTask {

	public GoodsDetailRequest(Context context, HttpTaskCallback callback, int id) {
		super(GOODS_DETAIL_API, HttpUtil.getGoodDetail(), new HashMap<String, String>(), callback);
		mParam.put("id", id + ""); // 商品主键
	}

	@Override
	protected Object parseJSON(String json) {

		if (json == null) {
			return null;
		}

		return JsonUtil.parseJsonObjcet(json, Goods.class);
	}

}
