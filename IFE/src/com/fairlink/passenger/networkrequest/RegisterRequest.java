package com.fairlink.passenger.networkrequest;

import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.HttpUtil;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

public class RegisterRequest extends BaseHttpPostTask {

	public RegisterRequest(Context context, HttpTaskCallback callback,
			String passengerId,String passengerPhone,String passengerEmail) {
		super(REGISTER_API, HttpUtil.getRegister(), new HashMap<String, String>(), callback);
		mParam.put("passengerId", passengerId);
		mParam.put("passengerPhone", passengerPhone);
		if(!ComUtil.isEmpty(passengerEmail))
		   mParam.put("passengerEmail", passengerEmail);
	}

	@Override
	protected Object parseJSON(String json) {
		if(json == null) {
			return null;
		}
		
		int code = -1;
		
		JSONTokener parser = new JSONTokener(json);
		try {
			JSONObject menu = (JSONObject) parser.nextValue();
			code = menu.getInt("code");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		return code;
	}
}
