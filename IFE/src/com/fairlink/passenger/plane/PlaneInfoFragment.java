package com.fairlink.passenger.plane;

import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.R;
import com.fairlink.passenger.bean.PlaneInfo;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.FlightinfoRequest;
import com.fairlink.passenger.networkrequest.FlightinfoRequest.FlightInfo;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.networkrequest.PlaneinfoRequest;
import com.fairlink.passenger.util.DateUtils;

public class PlaneInfoFragment extends BaseFragment implements HttpTaskCallback, NetworkRequestAPI {

//	private String toDesTimeTxt;
	
	private TextView altitude;
	private TextView groundSpeed;
	private TextView flySpeed;
	private TextView airTemp;
//	private TextView latitude;
//	private TextView longitude;
	private TextView timeFrom;
	private TextView timeTo;

	private PlaneInfo planeInfo;
	private FlightInfo flightInfo;
	private PlaneinfoRequest planeinfoRequest = new PlaneinfoRequest(this);


	void showInfo() {
		if (planeInfo != null) {
			
			if (planeInfo.getAltitude() == null) {
				altitude.setText("--");
			} else {
				String alti = String.format("%.2f", Float.parseFloat(planeInfo.getAltitude()));
				altitude.setText(alti+"米");
			}
			
			if (planeInfo.getGroundspeed() == null) {
				groundSpeed.setText("--");
			} else {
				String gs = String.format("%.2f", Float.parseFloat(planeInfo.getGroundspeed()));
				groundSpeed.setText(gs+"节");
			}
			
			if (planeInfo.getFlyspeed() == null) {
				flySpeed.setText("--");
			} else {
				String fs = String.format("%.2f", Float.parseFloat(planeInfo.getFlyspeed()));
				flySpeed.setText(fs+"Km/h");
			}
			
			if (planeInfo.getAirtemp() == null) {
				airTemp.setText("--");
			} else {
				String at = String.format("%.2f", Float.parseFloat(planeInfo.getAirtemp()));
				airTemp.setText(at+"°C");
			}
			
//			if (planeInfo.getLatitude() == null) {
//				latitude.setText("--");
//			} else {
//				String lati = String.format("%.2f", Float.parseFloat(planeInfo.getLatitude()));
//				latitude.setText(lati);
//			}
//			
//			if (planeInfo.getLongitude() == null) {
//				longitude.setText("--");
//			} else {
//				String longi = String.format("%.2f", Float.parseFloat(planeInfo.getLongitude()));
//				longitude.setText(longi);
//			}
			
		}
		if (flightInfo != null) {
			if (flightInfo.timeZoneOrig == null) {
				timeFrom.setText("--");
			} else {
				timeFrom.setText(DateUtils.dateToString("yyyy-MM-dd HH:mm", 
						new Date(System.currentTimeMillis() + Integer.parseInt(flightInfo.timeZoneOrig) * 60 * 60 * 1000)));
			}
			
			if (flightInfo.timeZoneDest == null) {
				timeTo.setText("--");
			} else {
				timeTo.setText(DateUtils.dateToString("yyyy-MM-dd HH:mm", 
						new Date(System.currentTimeMillis() + Integer.parseInt(flightInfo.timeZoneDest) * 60 * 60 * 1000)));
			}
			
//			if(toDesTimeTxt != null){
//				info.append("距降落时间：");
//				info.append(toDesTimeTxt);
//			}

		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mH = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(flightInfo != null){

				long interval = flightInfo.fligLandingTime - System.currentTimeMillis();
				if (interval < 0) {
					interval = 0;
				}

//				toDesTimeTxt = FormatTime((int) interval);
				showInfo();
				
			}
			mH.sendEmptyMessageDelayed(1, 30 * 1000);
			planeinfoRequest.execute((String) null);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.plane_info, null);
		altitude = (TextView)view.findViewById(R.id.altitude);
		groundSpeed = (TextView)view.findViewById(R.id.ground_speed);
		flySpeed = (TextView)view.findViewById(R.id.fly_speed);
		airTemp = (TextView)view.findViewById(R.id.air_temp);
//		latitude = (TextView)view.findViewById(R.id.latitude);
//		longitude = (TextView)view.findViewById(R.id.longitude);
		timeFrom = (TextView)view.findViewById(R.id.time_from);
		timeTo = (TextView)view.findViewById(R.id.time_to);
		
		new FlightinfoRequest(this).execute((String) null);
		planeinfoRequest.execute((String) null);

		return view;
	}

	@Override
	public void onGetResult(int requestType, Object result) {
		if (requestType == PLANE_INFO_API) {
			if (result != null) {
				planeInfo = (PlaneInfo) result;
				showInfo();
			}
		} else if (requestType == FlIGHT_INFO_API) {
			if (result == null) {
				return;
			}
			flightInfo = (FlightInfo) result;

			 mH.sendEmptyMessage(1);
			showInfo();
		}
	}

	@Override
	public void onError(int requestType) {

	}

	private String FormatTime(int sec) {

		sec /= 1000;
		StringBuilder text = new StringBuilder();
		int hour = sec / 3600;
		int minute = sec / 60 - hour * 60;
		if (hour > 0) {
			if (hour < 10) {
				text.append("0" + hour).append(":");
			} else {
				text.append(hour).append(":");
			}
		} else {
			text.append("00").append(":");
		}

		if (minute > 0) {
			if (minute < 10) {
				text.append("0" + minute);
			} else {
				text.append(minute);
			}
		} else {
			text.append("00");
		}

		return text.toString();
	}
}
