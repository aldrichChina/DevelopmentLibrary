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

public class GoodsListByMallRequest extends BaseHttpGetTask {

	public GoodsListByMallRequest(Context context, HttpTaskCallback callback, String mall) {
		super(GOODS_LIST_BY_MALL_API, HttpUtil.getAllGoodsListByMall(), new HashMap<String, String>(), callback);
		mParam.put("mall", mall); // 商品标签
	}

	@Override
	protected Object parseJSON(String json) {
		if (json == null) {
			return null;
		}

		return JsonUtil.parseJsonArray(json, Goods.class);
	}
}