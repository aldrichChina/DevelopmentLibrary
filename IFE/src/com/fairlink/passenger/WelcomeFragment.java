package com.fairlink.passenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fairlink.passenger.admin.OnWifiChangedListener;
import com.fairlink.passenger.admin.WifiChangeBroadcastReceiver;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.networkrequest.OfflineStatusRequest;
import com.fairlink.passenger.networkrequest.OfflineStatusRequest.OfflineStatus;
import com.fairlink.passenger.upgrade.AnimationUpgradeChecker;
import com.fairlink.passenger.upgrade.ApplicationUpgradeChecker;
import com.fairlink.passenger.upgrade.MainAppUpgradeChecker;
import com.fairlink.passenger.view.DialogLoading;

public class WelcomeFragment extends BaseFragment implements OnWifiChangedListener, HttpTaskCallback, NetworkRequestAPI {

	Context mContext;
	LoginMain mActivity;
	TextView  welcome;
	private DialogLoading diaLoading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_welcome_fragment, container);	
		welcome = (TextView)view.findViewById(R.id.welcome);
		if(Boolean.valueOf(IFEApplication.getInstance().getValue("wifi_detection"))){
			WifiChangeBroadcastReceiver wifiReceiver = WifiChangeBroadcastReceiver.getInstance(mContext);
			wifiReceiver.start();
			wifiReceiver.setOnWifiChangedLisner(this);
		}
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext = IFEApplication.getInstance().getApplicationContext();
		super.onCreate(savedInstanceState);
		
		

		diaLoading = new DialogLoading(getActivity());
//		diaLoading.show();
//
//		MainAppUpgradeChecker mainAppUpgradeChecker = MainAppUpgradeChecker.getInstance();
//		mainAppUpgradeChecker.setListener(new MainAppUpgradeChecker.MainAppUpgradeListener() {
//			@Override
//			public void onFinish() {
//				diaLoading.hide();
				new OfflineStatusRequest(WelcomeFragment.this).execute((String)null);
				ApplicationUpgradeChecker.getInstance().start();
				AnimationUpgradeChecker.getInstance().start();
				DeviceInfoMonitor.getInstance(mContext).start();
//			}
//		});
//		
//		mainAppUpgradeChecker.checkUpdate();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onAttach(Activity activity) {
		mActivity = (LoginMain)activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
		getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				welcome.setText("欢迎登机  已经连接wifi,正在连接服务器");
			}
		});
		
	}
	
	@Override
	public void onDisConnected() {
		// TODO Auto-generated method stub
	//	mContext.sendBroadcast(new Intent("custom_shutdown"));
	}

	@Override
	public void onGetResult(int requestType, Object result) {
		welcome.setText("欢迎登机  已经联网");
		diaLoading.hide();

		if (result == null) {
			return;
		}

		if (requestType == OFFLINE_STATUS_API) {
			OfflineStatus status = (OfflineStatus) result;
			
			switch (status.code) {
			case 0:
				if (status.hasPassengerInfo == false) {
					//离线版本
					IFEApplication.getInstance().setOfflineStatus(true);
				} else {
					IFEApplication.getInstance().setOfflineStatus(false);
				}
			}

			mActivity.enterLogin();
		}
	}

	@Override
	public void onError(int requestType) {
		if (requestType == OFFLINE_STATUS_API) {
			IFEApplication.getInstance().setOfflineStatus(false);
			mActivity.enterLogin();
		}
	}
}

