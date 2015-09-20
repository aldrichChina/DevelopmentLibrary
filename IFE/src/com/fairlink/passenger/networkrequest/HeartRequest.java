package com.fairlink.passenger.networkrequest;


import com.fairlink.passenger.util.HttpUtil;

public class HeartRequest extends BaseHttpGetTask{

	public HeartRequest(HttpTaskCallback callback) {
		super(HEART_API, HttpUtil.getHeart(), null, callback);
	}

	@Override
	protected Object parseJSON(String json) {
		return null;
	}

}
