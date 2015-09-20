package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import android.content.Context;

import com.fairlink.passenger.bean.Goods;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.JsonUtil;

/**
 * @ClassName ： GoodsListRequest
 * @Description: 获取商品列表
 */

public class GoodsListRequest extends BaseHttpGetTask {

	public GoodsListRequest(Context context, HttpTaskCallback callback, String typeId) {
		super(GOODS_LIST_API, HttpUtil.getGoodsList(), new HashMap<String, String>(), callback);
		mParam.put("typeId", typeId); // 商品标签
	}

	@Override
	protected Object parseJSON(String json) {
		if (json == null) {
			return null;
		}

		return JsonUtil.parseJsonArray(json, Goods.class);
	}
}