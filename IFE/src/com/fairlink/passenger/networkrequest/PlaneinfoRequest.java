package com.fairlink.passenger.networkrequest;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.fairlink.passenger.bean.PlaneInfo;
import com.fairlink.passenger.util.HttpUtil;

public class PlaneinfoRequest extends BaseHttpGetTask{

	public PlaneinfoRequest(HttpTaskCallback callback) {
		super(PLANE_INFO_API, HttpUtil.getPlaneinfo(), null, callback);
	}

	@Override
	protected Object parseJSON(String json) {
		if(json == null)
			return null;
		try
		{
			JSONTokener parser = new JSONTokener(json);
			JSONObject menu = (JSONObject) parser.nextValue();
			int code = menu.getInt("code");
			if(code != 0)
				return null;
			JSONObject data = menu.getJSONObject("data");
			PlaneInfo plane = new PlaneInfo();
			plane.setAltitude(data.getString("altitude"));
			plane.setAirtemp(data.getString("airtemp"));
			plane.setLongitude(data.getString("longitude"));
			plane.setLatitude(data.getString("latitude"));
			plane.setFlyspeed(data.getString("flyspeed"));
			plane.setGroundspeed(data.getString("groundspeed"));
			
			return plane;
		}
		catch(Exception ex)
		{
			
		}
		return null;
	}

}
