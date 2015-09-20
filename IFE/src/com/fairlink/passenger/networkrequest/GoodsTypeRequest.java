package com.fairlink.passenger.networkrequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.fairlink.passenger.bean.GoodsTypeWithRelation;
import com.fairlink.passenger.shop.GoodsCategoryItem;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.JsonUtil;

/**
 * @ClassName ： GoodTypeRequest
 * @Description: 获取商品类型
 */

public class GoodsTypeRequest extends BaseHttpGetTask {

	public GoodsTypeRequest(Context context, HttpTaskCallback callback, String mall) {
		super(GOODS_TYPE_API, HttpUtil.getGoodsType(), new HashMap<String, String>(), callback);
		mParam.put("mall", mall);
	}

	@Override
	protected Object parseJSON(String json) {
		List<GoodsTypeWithRelation> list = JsonUtil.parseJsonArray(json, GoodsTypeWithRelation.class);
		if (list == null || list.isEmpty())
			return null;

		List<GoodsCategoryItem> ret = new ArrayList<GoodsCategoryItem>();
		for (GoodsTypeWithRelation type : list) {
			if (type.getParent() != 0)
				continue;

			GoodsCategoryItem firstLayer = new GoodsCategoryItem();
			firstLayer.name = type.getGoodsTypeName();
			firstLayer.goodsType = new ArrayList<GoodsTypeWithRelation>();
			firstLayer.id = type.getId();
			ret.add(firstLayer);
		}
		
		for (GoodsCategoryItem category : ret) {
			for (GoodsTypeWithRelation type : list) {
				if (type.getParent() != category.id)
					continue;

				category.goodsType.add(type);
			}
		}

		return ret;

	}

}
