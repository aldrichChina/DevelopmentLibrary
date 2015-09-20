package com.fairlink.passenger.networkrequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fairlink.passenger.util.HttpUtil;


public class WeatherRequest extends BaseHttpGetTask{

	public WeatherRequest(
			HttpTaskCallback callback) {
		super(WEATHER_API, HttpUtil.getWeather(), null, callback);

	}

	@Override
	protected Object parseJSON(String json) {
		if (json == null || json.isEmpty()) {
			return null;
		}
		
		JSONTokener parser = new JSONTokener(json);
		String data = "";
		try {
			JSONObject menu = (JSONObject) parser.nextValue();
			String a = menu.getString("temperature");
			String b = menu.getString("weather");
			String p = menu.getString("dayPictureUrl");
			data = "张家界\r\n" + a +"\r\n"+ b + ";" + p;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} 
		return data;
	}
}
