package com.fairlink.passenger.networkrequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fairlink.passenger.bean.DynamicType;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.JsonUtil;

public class DynamicTypeByParentIdRequest extends BaseHttpGetTask {

	public DynamicTypeByParentIdRequest(int parentId, HttpTaskCallback callback) {
		super(DYNAMIC_TYPE_BY_PARENT_ID_API, HttpUtil.getDynamicType(parentId), new HashMap<String, String>(), callback);
	}

	@Override
	protected Object parseJSON(String json) {
		int lastSlot = -1;
		List<DynamicType> slot = null;
		List<DynamicType> list = JsonUtil.parseJsonArray(json, DynamicType.class);
		List<List<DynamicType>> ret = new ArrayList<List<DynamicType>>();

		for (DynamicType type : list) {
			if (lastSlot != type.getSlot()) {
				slot = new ArrayList<DynamicType>();
				ret.add(slot);
				lastSlot = type.getSlot();
			}

			slot.add(type);
		}

		return ret;
	}
}
