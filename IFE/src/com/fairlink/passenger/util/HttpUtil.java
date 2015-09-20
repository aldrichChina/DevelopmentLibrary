package com.fairlink.passenger.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.fairlink.passenger.IFEApplication;

/**
 * @ClassName ： HttpUtil
 * @Description: 网络请求地址
 */

public class HttpUtil {

	private static final String LOGIN = "passenger/login";
	private static final String LOGIN_MEMBER = "passenger/memberLogin";
	private static final String REGISTER = "passenger/appRegister";

	private static final String VIDEO_DETAIL = "video/";
	private static final String VIDEO_LIST = "video/list";

	private static final String BOOK_LIST_ALL = "ebook/listAll";
	private static final String BOOK_LIST_PIC = "ebook/listPics";
	private static final String BOOK_BY_ID = "ebook/getEBookById";

	private static final String MUS_LIST = "music/getListOfType/";

	private static final String ADS_PIC = "adsResource/getImgAds";
	private static final String ADS_VIDEO = "adsResource/getVideoAds";

	private static final String ANALYTICS_LOG_EVENT = "analytics/logEvent";

	private static final String LOG_EVENT = "logResource/logEvent";
	private static final String HEART = "passenger/heart";

	private static final String GOODS_DETAIL = "goodsResource/getGoodsInforById";
	private static final String GOODS_LABEL = "goodsResource/getGoodsListByProductId";
	private static final String GOODS_LIST = "goodsResource/getGoodsListByTypeId";
	private static final String GOODS_ALL_MALL = "goodsResource/getGoodsListByMall";
	private static final String GOODS_TYPE = "goodsTypeResource/getGoodsTypeTree";
	

	private static final String UPGRADE_MAIN_APP = "application/getMainApplication";
	private static final String UPGRADE_MAIN_APP_ENABLE_MODULAR = "upgradeResource/getMainAppEnableModular";
	private static final String UPGRADE_APPLICATION_LIST = "application/list";

	private static final String ORDER_SAVE = "orderResource/createOrder";
	private static final String ORDER_FIND = "orderResource/findOrderByOrderId/";
	private static final String ORDER_PAY = "orderResource/orderPay";
	private static final String ORDER_RECORD = "orderResource/orderRecord";

	private static final String ORDER_GET_RECEIPT = "AddresseeResource/get";
	private static final String ORDER_UPDATE_RECEIPT = "AddresseeResource/update";

	private static final String PASSENGER_FEEDBACK = "/messageboard/save";

	private static final String BROADCAST = "flight/flightPAStatus";

	private static final String OFFLINE_STATUS = "offline/getPassengerStatus";

	private static final String BOOT_ANIMATION = "application/getBootAnimation";
	private static final String SHUTDOWN_ANIMATION = "application/getShutdownAnimation";

	private static final String CART_ADD_GOODS = "cartResource/addToCart";
	private static final String CART_REMOVE_GOODS = "cartResource/removeGoods";
	private static final String CART_UPDATE_GOODS = "cartResource/updateGoods";
	private static final String CART_GET_GOODS = "cartResource/getCartGoodsDetailList";
	private static final String TEST = "dynamicTypeRelation/getAllByParentId";

	public static String getFlightinfo() {
		return IFEApplication.getInstance().getValue("BASE_URL") + "flight/localFlightInfo";
	}

	public static String getPlaneinfo() {
		return IFEApplication.getInstance().getValue("BASE_URL") + "planeinfo/getplaneinfo";
	}

	public static String getWeather() {
		return IFEApplication.getInstance().getValue("BASE_URL") + "flight/weather";
	}

	// 获取普通乘客登录信息
	public static String getLogin() {
		return IFEApplication.getInstance().getValue("BASE_URL") + LOGIN;
	}

	// 获取会员登录信息
	public static String getLoginMember() {
		return IFEApplication.getInstance().getValue("BASE_URL") + LOGIN_MEMBER;
	}

	// 获取注册信息
	public static String getRegister() {
		return IFEApplication.getInstance().getValue("BASE_URL") + REGISTER;
	}

	// 获取所有电子书
	public static String getBookListAll() {
		return IFEApplication.getInstance().getValue("BASE_URL") + BOOK_LIST_ALL;
	}

	// 获取电子书详情图片列表
	public static String getBookListPic() {
		return IFEApplication.getInstance().getValue("BASE_URL") + BOOK_LIST_PIC;
	}

	// 获取电子书详情图片列表
	public static String getBookById() {
		return IFEApplication.getInstance().getValue("BASE_URL") + BOOK_BY_ID;
	}

	// 获取音乐列表
	public static String getMusList() {
		return IFEApplication.getInstance().getValue("BASE_URL") + MUS_LIST;
	}

	// 获取影视列表
	public static String getVideoList() {
		return IFEApplication.getInstance().getValue("BASE_URL") + VIDEO_LIST;
	}

	// 获取影视详情
	public static String getVideoDetail() {
		return IFEApplication.getInstance().getValue("BASE_URL") + VIDEO_DETAIL;
	}

	// 获取图片广告
	public static String getADPic() {
		return IFEApplication.getInstance().getValue("BASE_URL") + ADS_PIC;
	}

	// 获取视频广告
	public static String getADVideo() {
		return IFEApplication.getInstance().getValue("BASE_URL") + ADS_VIDEO;
	}

	// 获取商品类型
	public static String getGoodsType() {
		return IFEApplication.getInstance().getValue("BASE_URL") + GOODS_TYPE;
	}

	// 获取商品列表
	public static String getGoodsList() {
		return IFEApplication.getInstance().getValue("BASE_URL") + GOODS_LIST;
	}

	// 获取商品列表
	public static String getAllGoodsListByMall() {
		return IFEApplication.getInstance().getValue("BASE_URL") + GOODS_ALL_MALL;
	}
	

	// 获取商品详情
	public static String getGoodDetail() {
		return IFEApplication.getInstance().getValue("BASE_URL") + GOODS_DETAIL;
	}

	// 获取商品标签
	public static String getGoodsLabel() {
		return IFEApplication.getInstance().getValue("BASE_URL") + GOODS_LABEL;
	}

	// 立即购买、结算生成订单组
	public static String getOrderSave() {
		return IFEApplication.getInstance().getValue("BASE_URL") + ORDER_SAVE;
	}

	// 通过订单组标识号 获取生成订单数据
	public static String getOrderFind() {
		return IFEApplication.getInstance().getValue("BASE_URL") + ORDER_FIND;
	}

	// 保存收货人信息
	public static String saveOrderReceipt() {
		return IFEApplication.getInstance().getValue("BASE_URL") + ORDER_UPDATE_RECEIPT;
	}

	public static String getOrderReceipt() {
		return IFEApplication.getInstance().getValue("BASE_URL") + ORDER_GET_RECEIPT;
	}

	// 支付订单
	public static String getOrderPay() {
		return IFEApplication.getInstance().getValue("BASE_URL") + ORDER_PAY;
	}

	// 订单记录
	public static String getOrderRecord() {
		return IFEApplication.getInstance().getValue("BASE_URL") + ORDER_RECORD;
	}

	public static String getAnalyticsLogEvent() {
		return IFEApplication.getInstance().getValue("BASE_URL") + ANALYTICS_LOG_EVENT;
	}

	public static String getUpgradeMainApp() {
		return IFEApplication.getInstance().getValue("BASE_URL") + UPGRADE_MAIN_APP;
	}

	public static String getMainAppEnableModular() {
		return IFEApplication.getInstance().getValue("BASE_URL") + UPGRADE_MAIN_APP_ENABLE_MODULAR;
	}

	public static String getApplications() {
		return IFEApplication.getInstance().getValue("BASE_URL") + UPGRADE_APPLICATION_LIST;
	}

	public static String getBroadcast() {
		return IFEApplication.getInstance().getValue("BASE_URL") + BROADCAST;
	}

	public static String getOfflineStatus() {
		return IFEApplication.getInstance().getValue("BASE_URL") + OFFLINE_STATUS;
	}

	public static String getPassengerFeedback() {
		return IFEApplication.getInstance().getValue("BASE_URL") + PASSENGER_FEEDBACK;
	}

	public static String getBootAnimation() {
		return IFEApplication.getInstance().getValue("BASE_URL") + BOOT_ANIMATION;
	}

	public static String getShutdownAnimation() {
		return IFEApplication.getInstance().getValue("BASE_URL") + SHUTDOWN_ANIMATION;
	}

	public static String getCartAddGoods() {
		return IFEApplication.getInstance().getValue("BASE_URL") + CART_ADD_GOODS;
	}

	public static String getCartRemoveGoods() {
		return IFEApplication.getInstance().getValue("BASE_URL") + CART_REMOVE_GOODS;
	}

	public static String getCartUpdateGoods() {
		return IFEApplication.getInstance().getValue("BASE_URL") + CART_UPDATE_GOODS;
	}

	public static String getCart() {
		return IFEApplication.getInstance().getValue("BASE_URL") + CART_GET_GOODS;
	}

	public static String getLogEvent() {
		return IFEApplication.getInstance().getValue("BASE_URL") + LOG_EVENT;
	}

	public static String getHeart() {
		return IFEApplication.getInstance().getValue("BASE_URL") + HEART;
	}

	public static String getDynamicType(int id) {
		return IFEApplication.getInstance().getValue("BASE_URL") + TEST + "/" + id;
	}

	public static boolean isUrlExist(String urlStr) {
		URL url = null;
		URLConnection urlCon;
		InputStream inStream;// 你只需判断这个变量值 .
		try {
			url = new URL(urlStr);
			urlCon = url.openConnection();
			inStream = urlCon.getInputStream();
			return (inStream != null);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

}
