package com.fairlink.passenger.networkrequest;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.fairlink.passenger.util.HttpUtil;

public class FlightinfoRequest extends BaseHttpGetTask{

	public static class FlightInfo {
		public String fligLaunchCity;
		public String fligLandingCity;
		public String fligNo;
		public WeatherInfo weather;
		public long fligLaunchTime;
		public long fligLandingTime;
		public String timeZoneOrig;
		public String timeZoneDest;
	}

	public static class WeatherInfo {
		public String temperature;
		public String weather;
		public String dayPictureUrl;
	}

	public FlightinfoRequest(HttpTaskCallback callback) {
		super(FlIGHT_INFO_API, HttpUtil.getFlightinfo(), null, callback);
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
			FlightInfo flightInfo = new FlightInfo();
			flightInfo.fligLaunchCity = data.getString("fligLaunchCity");
			flightInfo.fligLandingCity = data.getString("fligLandingCity");
			flightInfo.fligNo = data.getString("fligNo");
			flightInfo.fligLaunchTime = data.getLong("fligLaunchTime");
			flightInfo.fligLandingTime = data.getLong("fligLandingTime");
			if(!data.isNull("timeZoneOrig"))
				flightInfo.timeZoneOrig = data.getString("timeZoneOrig");
			if(!data.isNull("timeZoneDest"))
				flightInfo.timeZoneDest = data.getString("timeZoneDest");
			
			if(data.has("fligWeather")){
				String weather = data.getString("fligWeather");
				if(weather != null && weather.length() > 0){
					JSONTokener jsonParser = new JSONTokener(weather);  
					JSONObject wj = (JSONObject) jsonParser.nextValue();
					WeatherInfo weatherInfo = new WeatherInfo();
					weatherInfo.temperature = wj.getString("temperature");
					weatherInfo.weather = wj.getString("weather");
					weatherInfo.dayPictureUrl = wj.getString("dayPictureUrl");
					flightInfo.weather = weatherInfo;
				}
				
			}

			return flightInfo;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
