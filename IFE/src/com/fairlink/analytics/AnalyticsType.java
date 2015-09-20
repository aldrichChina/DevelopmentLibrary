package com.fairlink.analytics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

/**
 * @ClassName ： AnalyticsType
 * @Description: 操作日志类型规范
 * @author ： jiaxue
 * @date ： 2014-12-30 下午2:12:16
 */

public class AnalyticsType {

	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	// 记录所在的页面
	public final static String ORIGIN_CART = "cart";
	public final static String ORIGIN_VIDEO = "video";
	public final static String ORIGIN_NEWS = "news";
	public final static String ORIGIN_CLUB = "club";
	public final static String ORIGIN_MALL_SSS = "mall_sss";
	public final static String ORIGIN_MUSIC = "music";
	public final static String ORIGIN_KID = "children";
	public final static String ORIGIN_VOICE_CUSTOMER = "voice_customer";
	public final static String ORIGIN_GAME = "game";
	public final static String ORIGIN_EBOOK = "ebook";
	public final static String ORIGIN_MALL_FLC = "mall_flc";
	public final static String ORIGIN_ABOUT_US = "about_us";
	public final static String ORIGIN_DEVICE = "device";

	public static String getOperationShop(int type) {

		switch (type) {
		case 1:
			return "enter";
		case 2:
			return "out";
		case 3:
			return "browse";
		case 4:
			return "cart_add";
		case 5:
			return "cart_delete";
		default:
			return "unkown";
		}

	}

	public static String getOperationVideoMus(int type) {

		switch (type) {
		case 1:
			return "enter";
		case 2:
			return "out";
		case 3:
			return "open";
		case 4:
			return "play";
		case 5:
			return "pause";
		default:
			return "unkown";
		}

	}

	public static String getOperationEbook(int type,int page) {
		
		switch (type) {
		case 1:
			return "enter";
		case 2:
			return "out";
		case 3:
			return "open";
		case 4:
			return "jump to page:"+page;
		default:
			return "unkown";
		}
	}

	public static String getOperationAD(int type) {
		switch (type) {
		case 1:
			return "open";
		case 2:
			return "jump";
		default:
			return "unkown";
		}
	}

	public static String getOperationNews(int type) {
		switch (type) {
		case 1:
			return "enter";
		case 2:
			return "out";
		default:
			return "unkown";
		}
	}

	public static String getOperationGame(int type) {
		switch (type) {
		case 1:
			return "enter";
		case 2:
			return "out";
		case 3:
			return "open";
		case 4:
			return "exit";
		default:
			return "unkown";
		}
	}

	public static String getOperationOrder(int type) {
		switch (type) {
		case 1:
			return "create";
		case 2:
			return "cancel";
		case 3:
			return "pay";
		case 4:
			return "pay_confirm";
		case 5:
			return "return_goods";
		default:
			return "unkown";
		}
	}

	public static String getOperationDevice(int type) {

		switch (type) {
		case 1:
			return "battery_change";
		case 2:
			return "wifi_change";
		case 3:
			return "device_space";
		default:
			return "unkown";
		}

	}
	
	/**
	 * 操作资源数据
	 * 
	 * @param category
	 *            栏目
	 * @param resourceName
	 *            资源名称
	 * @return
	 */
	public static Map<String, Object> cperationData(String category,
			String resourceName) {
//		UserInfo userInfo = IFEApplication.getInstance().getUserInfo();
		Map<String, Object> dataMap = new HashMap<String, Object>();
//		dataMap.put("userID", userInfo.passengerId);
		dataMap.put("operationTime", sdf.format(new Date()));
		dataMap.put("category", category);
		dataMap.put("resourceName", resourceName);
		return dataMap;
	};

	

}
