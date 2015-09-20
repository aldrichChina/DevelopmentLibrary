package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.fairlink.passenger.util.HttpUtil;

public class BroadcastGetRequest extends BaseHttpGetTask {
	public static class BroadcastStatus {
		public String bcStatus;
		public String bcTime;
		public String bcType;
	}

	public BroadcastGetRequest(Context context, String bcType, HttpTaskCallback callback) {
		super(BROADCAST_API, HttpUtil.getBroadcast(), new HashMap<String, String>(), callback);
		mParam.put("bctype", bcType);
	}

	@Override
	protected Object parseJSON(String json) {
		// TODO Auto-generated method stub
		if(null == json) {
			return null;
		}
		
		BroadcastStatus retValue = new BroadcastStatus();
		JSONTokener parser = new JSONTokener(json);
		
		try {
			JSONObject item = (JSONObject) parser.nextValue();
			retValue.bcStatus = item.getString("bcstatus");
			retValue.bcTime   = item.getString("bctime");
			retValue.bcType   = item.getString("bctype");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return retValue;
	}
}
