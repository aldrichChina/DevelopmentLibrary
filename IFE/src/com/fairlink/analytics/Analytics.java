package com.fairlink.analytics;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.util.DeviceUtil;
import com.fairlink.passenger.util.Logger;

public class Analytics {
	private static Logger logger = new Logger(null, "Analytics");

	static public void logEvent(Context ctx, String type, String origin, Map<String, Object> data) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("origin", origin);
		map.put("platform", "android" + android.os.Build.VERSION.RELEASE + " " + android.os.Build.MANUFACTURER + " "
				+ android.os.Build.MODEL);
		map.put("deviceId", DeviceUtil.getUID(ctx));
		if (data != null) {
			map.put("data", new JSONObject(data).toString().replace("\\", "\\\\").replace("\"", "\\\""));
		}

		final String jsonString = new JSONObject(map).toString();
		HttpTaskCallback callback = new HttpTaskCallback() {

			@Override
			public void onGetResult(int requestType, Object result) {
			}

			@Override
			public void onError(int requestType) {
				logger.error("Analytics result:" + "服务器出错");
			}
		};

		logger.info("send json : " + jsonString);
		new AnalyticsPostTask(callback, jsonString).execute((String) null);
	}
}
