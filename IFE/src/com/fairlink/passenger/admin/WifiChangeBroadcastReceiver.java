package com.fairlink.passenger.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.fairlink.analytics.Analytics;
import com.fairlink.analytics.AnalyticsType;
import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.util.Logger;

public class WifiChangeBroadcastReceiver extends BroadcastReceiver {

	private Logger logger = new Logger(this, "wifi");

	static enum WifiCipherType {
		NONE, IEEE8021XEAP, WEP, WPA, WPA2, WPAWPA2;
	}
	
    private static String SSID = "Fairlink_9527";
    private static String PASSWORD = "8046FLC0146";
	private static final WifiCipherType WIFI_CIPHER_TYPE = WifiCipherType.WPA2;
	
	private Context mContext;

	static final int MAX_ACCUMULATOR = 10;
	private int accumulator;

	private static WifiChangeBroadcastReceiver instance;
	private OnWifiChangedListener mListner;
	private boolean isConnected = false;
	private String ssid = "";
	private int signalLevel = 0;
	private int speed = 0;

	public static WifiChangeBroadcastReceiver getInstance(Context context) {
		if (instance == null) {
			instance = new WifiChangeBroadcastReceiver(context);
		}
		return instance;
	}

	WifiChangeBroadcastReceiver(Context context) {
		mContext = context;

		String ssid = IFEApplication.getInstance().getValue("SSID");
		if (ssid != null && !ssid.isEmpty()) {
			SSID = ssid;
			logger.info("overwrite ssid from " + SSID + " to " + ssid);
		}

		String password = IFEApplication.getInstance().getValue("PASSWORD");
		if (password != null && !password.isEmpty()) {
			PASSWORD = password;
			logger.info("overwrite password from " + PASSWORD + " to " + password);
		}
	}

	public void start() {
		addNewWifi(createWifiConfiguration(SSID, PASSWORD, WIFI_CIPHER_TYPE));

		new Timer().schedule(new TimerTask() {
			public void run() {
				getWifiInfo();
			}
		}, 0, 6 * 1000);
	}

	public void stop() {
		mContext.unregisterReceiver(this);
	}

	private void logWifiChange()
	{
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo.getBSSID() != null) {
			// wifi名称
			String ssid = wifiInfo.getSSID();
			// wifi信号强度
			int signalLevel = WifiManager.calculateSignalLevel(
					wifiInfo.getRssi(), 5);
			// wifi速度
			int speed = wifiInfo.getLinkSpeed();
			// wifi速度单位
			String units = WifiInfo.LINK_SPEED_UNITS;

			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("ssid", ssid);
			dataMap.put("signalLevel", signalLevel);
			dataMap.put("speed", speed);
			dataMap.put("units", units);
			Analytics.logEvent(mContext, AnalyticsType.getOperationDevice(1),
					AnalyticsType.ORIGIN_DEVICE, dataMap);
		}
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		logger.info("Wifi发生变化");
		getWifiInfo();
		logWifiChange();
	}

	private void getWifiInfo() {
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo.getBSSID() != null) {
			// wifi名称
			String ssid = wifiInfo.getSSID();
			// wifi信号强度
			int signalLevel = WifiManager.calculateSignalLevel(
					wifiInfo.getRssi(), 5);
			// wifi速度
			int speed = wifiInfo.getLinkSpeed();
			
			if (!this.ssid.equals(ssid) || this.signalLevel != signalLevel || this.speed != speed) {
				logger.info("ssid=" + ssid + ",signalLevel=" + signalLevel + ",speed=" + speed + ",units=" + WifiInfo.LINK_SPEED_UNITS);
				this.ssid = ssid;
				this.signalLevel = signalLevel;
				this.speed = speed;
			}

			if (ssid.equals("\"" + SSID + "\"") && (signalLevel > 0)) {
				accumulator = 0;
				if (mListner != null && isConnected == false) {
					mListner.onConnected();
				}
				isConnected = true;
			} else {
				++accumulator;
				if (accumulator > MAX_ACCUMULATOR) {
					if (mListner != null) {
						mListner.onDisConnected();
					}
				}else{
					addNewWifi(createWifiConfiguration(SSID, PASSWORD, WIFI_CIPHER_TYPE));
				}
				isConnected = false;
			}
		}
	}

	private WifiConfiguration createWifiConfiguration(String ssid,
			String password, WifiCipherType type) {
		WifiConfiguration newWifiConfiguration = new WifiConfiguration();
		newWifiConfiguration.allowedAuthAlgorithms.clear();
		newWifiConfiguration.allowedGroupCiphers.clear();
		newWifiConfiguration.allowedKeyManagement.clear();
		newWifiConfiguration.allowedPairwiseCiphers.clear();
		newWifiConfiguration.allowedProtocols.clear();
		newWifiConfiguration.SSID = "\"" + ssid + "\"";
		switch (type) {
		case NONE:
			newWifiConfiguration.allowedKeyManagement
					.set(WifiConfiguration.KeyMgmt.NONE);
			break;
		case IEEE8021XEAP:
			break;
		case WEP:
			break;
		case WPA:
			newWifiConfiguration.preSharedKey = "\"" + password + "\"";
			newWifiConfiguration.hiddenSSID = true;
			newWifiConfiguration.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			newWifiConfiguration.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.TKIP);
			newWifiConfiguration.allowedKeyManagement
					.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			newWifiConfiguration.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			newWifiConfiguration.allowedProtocols
					.set(WifiConfiguration.Protocol.WPA);
			newWifiConfiguration.status = WifiConfiguration.Status.ENABLED;
			break;
		case WPA2:
			newWifiConfiguration.preSharedKey = "\"" + password + "\"";
			newWifiConfiguration.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			newWifiConfiguration.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.TKIP);
			newWifiConfiguration.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.CCMP);
			newWifiConfiguration.allowedKeyManagement
					.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			newWifiConfiguration.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			newWifiConfiguration.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			newWifiConfiguration.allowedProtocols
					.set(WifiConfiguration.Protocol.RSN);
			newWifiConfiguration.status = WifiConfiguration.Status.ENABLED;
			newWifiConfiguration.hiddenSSID = true;
			break;
		default:
			return null;
		}
		return newWifiConfiguration;
	}

	boolean addNewWifi(WifiConfiguration newConfig) {
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
		int netID = wifiManager.addNetwork(newConfig);
		boolean bRet = wifiManager.enableNetwork(netID, true);
		return bRet;
	}

	public void setOnWifiChangedLisner(OnWifiChangedListener listner) {
		mListner = listner;
	}
	
}
