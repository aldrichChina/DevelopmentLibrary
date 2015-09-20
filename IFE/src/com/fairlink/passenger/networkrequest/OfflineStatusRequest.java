package com.fairlink.passenger.networkrequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fairlink.passenger.util.HttpUtil;

public class OfflineStatusRequest extends BaseHttpGetTask {

	public static class OfflineStatus {
		public int code;
		public boolean hasPassengerInfo;
	}

	public OfflineStatusRequest(HttpTaskCallback callback) {
		super(OFFLINE_STATUS_API, HttpUtil.getOfflineStatus(), null, callback);

	}

	@Override
	protected Object parseJSON(String json) {
		if (json == null) {
			return null ;
		}
		
		OfflineStatus status = new OfflineStatus();
		status.hasPassengerInfo = true;
		JSONTokener parser = new JSONTokener(json);
		
		try {
			JSONObject menu = (JSONObject) parser.nextValue();

			int code  = menu.getInt("code");
			status.code = code;
			
			if (0 == code) {
				JSONObject item = menu.getJSONObject("data");
				
				if(!item.isNull("hasPassengerInfo")) {
					status.hasPassengerInfo = item.getBoolean("hasPassengerInfo");
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return status;
	}

}
