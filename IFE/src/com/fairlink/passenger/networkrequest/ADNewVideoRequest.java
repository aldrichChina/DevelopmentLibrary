package com.fairlink.passenger.networkrequest;


import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fairlink.passenger.networkrequest.ADNewPicRequest.ADNEW;
import com.fairlink.passenger.util.HttpUtil;

import android.content.Context;

/**
 * @ClassName  ：  ADNewVideoRequest 
 * @Description: 新的视频广告接口	
 * @author     ：  jiaxue 
 * @date       ：  2014-11-4 下午2:18:44 

 */

public class ADNewVideoRequest extends BaseHttpGetTask {

	public ADNewVideoRequest(Context context, HttpTaskCallback callback,int adsType) {
		super(ADS_VIDEO_API, HttpUtil.getADVideo(), new HashMap<String, String>(), callback);
		mParam.put("adsType", ""+adsType);    //广告类型
		
	}

	@Override
	protected Object parseJSON(String json) {
		if(json == null) {
			return null;
		}

		JSONTokener parser = new JSONTokener(json);
		
		ADNEW ad = new ADNEW();
		try {
			JSONObject menu = (JSONObject) parser.nextValue();	
			
			int code = menu.getInt("code");
			if(0 != code) {
				return null;
			}
			
			ad.adsBusiness = menu.getJSONObject("data").getString("adsBusiness");
			ad.adsDesc = menu.getJSONObject("data").getString("adsDesc");
//			ad.adsExternalUrl= menu.getJSONObject("data").getString("adsExternalUrl");
			ad.adsPath = menu.getJSONObject("data").getString("adsPath");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ad;
	}

}
