package com.fairlink.passenger.networkrequest;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fairlink.passenger.util.HttpUtil;


public class BootAnimationRequest extends BaseHttpGetTask {

	public static class BootAnimationInfo {
		public boolean code;
		public String version;
		public String path;
	}
	
	public BootAnimationRequest(HttpTaskCallback callback) {
		super(BOOT_ANIMATION_API, HttpUtil.getBootAnimation(), null, callback);
	}

	@Override
	protected Object parseJSON(String jsonString) {
		if (jsonString == null) {
			return null ;
		}

		BootAnimationInfo bootAnimationInfo = new BootAnimationInfo();

		try {
			JSONObject json = (JSONObject) JSONObject.parse(jsonString);
			bootAnimationInfo.code = json.getBoolean("code");
			bootAnimationInfo.version = json.getString("version");
			bootAnimationInfo.path = json.getString("path");
			return bootAnimationInfo;
		} catch(JSONException e){
			logger.error("prase json failed with error:" + e.getMessage());
			bootAnimationInfo.code = false;
			return bootAnimationInfo;
		}

	}
}
