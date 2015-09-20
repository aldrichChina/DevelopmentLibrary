package com.fairlink.passenger.networkrequest;

import java.security.MessageDigest;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.fairlink.passenger.util.HttpUtil;

/**
 * @ClassName ： LoginRequest
 * @Description: 普通乘客登录接口
 * @author ： jiaxue
 * @date ： 2014-12-11 下午2:00:45
 */

public class LoginRequest extends BaseHttpGetTask {

	public static class Passenger {
		public int code;
        public UserInfo userInfo;
	}

	public static class UserInfo {
        public String passengerFlightNo;// 航班号
        public String passengerSeatNo; // 座位号
        public String passengerMemberId; // 会员号
        public int passengerIsRegister;//0：注册，1未注册
        public int passengerIsMember; // 0:不是会员，1：是会员
        public int passengerCardType; // 0:护照,1:身份证
        public int passengerCabinType; // 舱位类型 0:头等舱,1:商务舱,2:经济舱
        public String passengerCardnoMd5; // 加密后的卡号
        public int passengerMemberBalance;// 会员积分
        public String passengerId;//passengerId
	}

	public LoginRequest(Context context, HttpTaskCallback callback,
			String seatNo, String cardNo) {
		super(LOGIN_API, HttpUtil.getLogin(), new HashMap<String, String>(),
				callback);
		String calcedCardNoString = getMD5(cardNo, 32);

		mParam.put("seatNo", seatNo); // 座位号
		mParam.put("cardNo", calcedCardNoString); // 登记证件号后6位

//		JSESSIONID = null;
	}

	@Override
	protected Object parseJSON(String json) {
		if (json == null) {
			return null;
		}

		Passenger passenger = new Passenger();
		JSONTokener parser = new JSONTokener(json);

		try {
			JSONObject menu = (JSONObject) parser.nextValue();

			int code  = menu.getInt("code");
			passenger.code = code;
			
			if (0 == code) {
                UserInfo userInfo = new UserInfo();

                JSONObject item = menu.getJSONObject("data");
				
//				passenger.passengerId = menu.getString("data");
				if(!item.isNull("passengerId")) {
					userInfo.passengerId = item.getString("passengerId");
				}
				
//				if(!item.isNull("passengerFlightNo")) {
//					userInfo.passengerFlightNo = item.getString("passengerFlightNo");
//				}
//				
//
//				if(!item.isNull("passengerSeatNo")) {
//					userInfo.passengerSeatNo = item.getString("passengerSeatNo");
//				}
//				
//				if(!item.isNull("passengerMemberId")) {
//					userInfo.passengerMemberId = item.getString("passengerMemberId");
//				}
//				
				
				if(!item.isNull("passengerIsMember")) {
					userInfo.passengerIsMember = item.getInt("passengerIsMember");
				}
				if(!item.isNull("passengerIsRegister"))
					userInfo.passengerIsRegister = item.getInt("passengerIsRegister");
				
//				
//				if(!item.isNull("passengerCardType")) {
//					userInfo.passengerCardType = item.getInt("passengerCardType");
//				}
//				
//				if(!item.isNull("passengerCabinType")) {
//					userInfo.passengerCabinType = item.getInt("passengerCabinType");
//				}
//				
//				if(!item.isNull("passengerCardnoMd5")) {
//					userInfo.passengerCardnoMd5 = item.getString("passengerCardnoMd5");
//				}
//				
//				if(!item.isNull("passengerMemberBalance")) {
//					userInfo.passengerMemberBalance = item.getInt("passengerMemberBalance");
//				}
//				
//				
//				if(!item.isNull("passengerLandAirport")) {
//					userInfo.passengerLandAirport = item.getString("passengerLandAirport");
//				}
//				
//				if(!item.isNull("passengerLaunchAirport")) {
//					userInfo.passengerLaunchAirport = item.getString("passengerLaunchAirport");
//				}
//				
//				if(!item.isNull("passengerLevel")) {
//					userInfo.passengerLevel = item.getInt("passengerLevel");
//				}
//				
//				if(!item.isNull("passengerName")) {
//					userInfo.passengerName = item.getString("passengerName");
//				}
//				
//				if(!item.isNull("passengerCountry")) {
//					userInfo.passengerCountry = item.getString("passengerCountry");
//				}
//				
//				
//				if(!item.isNull("passengerSex")) {
//					userInfo.passengerSex = item.getString("passengerSex");
//				}
//				
//				if(!item.isNull("passengerAge")) {
//					userInfo.passengerSex = item.getString("passengerAge");
//				}
//				
//				
//				if(!item.isNull("passengerMemberState")) {
//					userInfo.passengerMemberState = item.getInt("passengerMemberState");
//				}
				
                passenger.userInfo = userInfo;


			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return passenger;

	}

	private String getMD5(String args, int len) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(args.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			if (len <= 16)
				result = buf.toString().substring(8, 24);
			else
				result = buf.toString();

			return result;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
