package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.content.Context;

import com.fairlink.passenger.util.HttpUtil;

/**
 * @ClassName  ：  LoginMemberRequest 
 * @Description: 会员登录接口
 * @author     ：  jiaxue 
 * @date       ：  2014-12-11 下午2:35:13 

 */

public class LoginMemberRequest extends BaseHttpGetTask {

	
	public LoginMemberRequest(Context context, HttpTaskCallback callback,
			String seatNo,String password) {
		super(LOGIN_MEMBER_API, HttpUtil.getLoginMember(), new HashMap<String, String>(), callback);
		mParam.put("seatNo", seatNo);      // 座位号
		mParam.put("password", password);  // 会员密码
		
	}


	@Override
	protected Object parseJSON(String json) {
		if (json == null) {
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
