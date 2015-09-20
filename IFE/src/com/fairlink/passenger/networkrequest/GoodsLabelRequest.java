package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import android.content.Context;

import com.fairlink.passenger.bean.Goods;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.JsonUtil;

/**
 * @ClassName ： GoodsLabelRequest
 * @Description: 获取商品标签列表
 */

public class GoodsLabelRequest extends BaseHttpGetTask {

	public GoodsLabelRequest(Context context, String productId, String mall, HttpTaskCallback callback) {
		super(GOODS_LABEL_API, HttpUtil.getGoodsLabel(), new HashMap<String, String>(), callback);
		mParam.put("productId", productId); // 货品Id
		mParam.put("mall", mall);
	}

	@Override
	protected Object parseJSON(String json) {
		if (json == null) {
			return null;
		}

		return JsonUtil.parseJsonArray(json, Goods.class);
	}

}
