package com.fairlink.passenger.util;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;

import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.bean.RecordGoodsItem;
import com.fairlink.passenger.cart.CartActivity;
import com.fairlink.passenger.networkrequest.ADNewPicRequest.ADNEW;
import com.fairlink.passenger.networkrequest.PhotoManager;
import com.fairlink.passenger.order.OrderActivity;
import com.fairlink.passenger.shop.ShopItemDetailActivity;
import com.fairlink.passenger.shop.ShopMainActivity;

public class ShopUtil {
	public static final String MODULAR_EXTERNAL_URL_KEY_WORD = "modularExternalUrl";
	public static final String MODULAR_RELATED_TYPE_KEY_WORD = "modularRelatedType";
	public static final String INVOKE_EXTERNAL_URL_KEY_WORD = "invokeExternalUrl";
	public static final String INVOKE_RELATED_TYPE_KEY_WORD = "invokeRelatedType";
	public static final String FLC_GoodsMall = "FLC";
	public static final String Spring_GoodsMall = IFEApplication.getInstance().getValue("SpringMall");
	public static final int MAX_GOODS_NUMBER = 999;
	public final static int ADS_RELATED_TYPE_COMMON = 0;
	public final static int ADS_RELATED_TYPE_GOODS_TYPE = 1;
	public final static int ADS_RELATED_TYPE_GOODS = 2;
	public final static int ADS_RELATED_TYPE_SHOP = 3;

	public static String getMallName(String mallCode) {
		if (mallCode.equals(Spring_GoodsMall)) {
			return "绿翼商城";
		}
		if (mallCode.equals(FLC_GoodsMall)) {
			return "购物中心";
		}

		return "";
	}

	public static boolean isCurrentShop(String getType, String currentType) {
		if (getType.equals(currentType))
			return true;

		return false;
	}

	public static void showCart(Context context) {
		context.startActivity(new Intent(context, CartActivity.class));
	}

	public static void showGoodsDetail(Context context, int id) {
		Intent intent = new Intent(context, ShopItemDetailActivity.class);
		intent.putExtra("id", id);
		context.startActivity(intent);
	}

	public static void showOrderConfirm(Context context, String goodsMall, int payType,
			ArrayList<RecordGoodsItem> listData, int deliveryType, boolean fromCart) {

		Intent intent = new Intent(context, OrderActivity.class);
		intent.putExtra("page", "confirm");
		intent.putExtra("mall", goodsMall);
		intent.putExtra("payType", payType);
		intent.putExtra("goodsList", listData);
		intent.putExtra("wayDelivery", deliveryType);
		intent.putExtra("fromCart", fromCart);
		context.startActivity(intent);
	}

	public static void showOrderPay(Context context, String orderId) {
		Intent intent = new Intent(context, OrderActivity.class);
		intent.putExtra("page", "pay");
		intent.putExtra("orderId", orderId);
		context.startActivity(intent);
	}

	public static void showOrderSuccess(Context context) {
		Intent intent = new Intent(context, OrderActivity.class);
		intent.putExtra("page", "success");
		context.startActivity(intent);
	}

	public static boolean showShop(Context context, int relatedType, String adsExternalUrl) {
		if (context == null) {
			return false;
		}

		if (context instanceof ShopMainActivity) {
			if (((ShopMainActivity) context).isSameMall(adsExternalUrl)) {
				((ShopMainActivity) context).processParameters(relatedType, adsExternalUrl);
				return true;
			}
		}

		if (relatedType == ADS_RELATED_TYPE_GOODS) {
			showGoodsDetail(context, Integer.parseInt(adsExternalUrl.split("/")[1]));
		} else {
			Intent intent = new Intent(context, ShopMainActivity.class);
			intent.putExtra(INVOKE_RELATED_TYPE_KEY_WORD, relatedType);
			intent.putExtra(INVOKE_EXTERNAL_URL_KEY_WORD, adsExternalUrl);
			context.startActivity(intent);
		}

		return false;
	}

	public static void showShopMallWithAD(Context context, String mall, ADNEW ads) {
		Intent intent = new Intent(context, ShopMainActivity.class);
		intent.putExtra(INVOKE_RELATED_TYPE_KEY_WORD, ADS_RELATED_TYPE_SHOP);
		intent.putExtra(INVOKE_EXTERNAL_URL_KEY_WORD, mall);
		startActivityWithAd(context, intent, ads);
	}

	public static void startActivityWithAd(Context context, Class<?> cls, ADNEW ads) {
		Intent i = new Intent(context, cls);

		startActivityWithAd(context, i, ads);
	}

	public static void startActivityWithAd(Context context, Intent i, ADNEW ads) {

		if (ads != null) {
			String pic = PhotoManager.getInstance().getImageFile(ads.adsPath);
			i.putExtra(MODULAR_RELATED_TYPE_KEY_WORD, ads.adsRelatedType);
			i.putExtra(MODULAR_EXTERNAL_URL_KEY_WORD, ads.adsExternalUrl);
			i.putExtra("pic", pic);
		}

		context.startActivity(i);
	}
}
