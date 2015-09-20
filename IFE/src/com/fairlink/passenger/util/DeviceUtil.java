package com.fairlink.passenger.util;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

/**
 * @ClassName ： DeviceUtil
 * @Description: 公共工具类
 * @author ： John
 * @date ： 2015-02-03 下午11:53:00
 */

public class DeviceUtil {

	public static String getUID(Context ctx) {

		String imei = ((TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if (imei == null) {
			return Secure
					.getString(ctx.getContentResolver(), Secure.ANDROID_ID);

		}
		return imei;
	}

}
