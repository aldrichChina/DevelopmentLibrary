package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fairlink.passenger.util.HttpUtil;

/**
 * @ClassName ： ADNewRequest
 * @Description: 新的图片广告接口
 */

public class ADNewPicRequest extends BaseHttpGetTask {

	public final static int ADS_TYPE_BANNER = 1;
	public final static int ADS_TYPE_VIDEO_START = 2;
	public final static int ADS_TYPE_VIDEO_PAUSE = 3;
	public final static int ADS_TYPE_VIDEO_SWITCH = 4;
	public final static int ADS_TYPE_MODULAR = 5;

	public static class ADNEW {
		public int adsRelatedType;
		public String adsBusiness;
		public String adsDesc;
		public String adsExternalUrl;
		public String adsPath;
	}

	public ADNewPicRequest(HttpTaskCallback callback, int adsType) {

		super(ADS_PIC_API, HttpUtil.getADPic(), new HashMap<String, String>(), callback);
		mParam.put("adsType", "" + adsType); // 1:模块跳转广告 2：侧栏广告 3：视频暂停广告

	}

	@Override
	protected Object parseJSON(String json) {

		if (json == null) {
			return null;
		}

		JSONTokener parser = new JSONTokener(json);

		ADNEW ad = new ADNEW();
		try {
			JSONObject menu = (JSONObject) parser.nextValue();

			int code = menu.getInt("code");
			if (0 != code) {
				return null;
			}

			ad.adsRelatedType = menu.getJSONObject("data").getInt("adsRelateType");
			ad.adsBusiness = menu.getJSONObject("data").getString("adsBusiness");
			ad.adsDesc = menu.getJSONObject("data").getString("adsDesc");
			if (!menu.getJSONObject("data").isNull("adsExternalUrl"))
				ad.adsExternalUrl = menu.getJSONObject("data").getString("adsExternalUrl");
			ad.adsPath = menu.getJSONObject("data").getString("adsPath");

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return ad;
	}
}
