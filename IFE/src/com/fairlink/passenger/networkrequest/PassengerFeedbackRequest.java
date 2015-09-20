package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import android.content.Context;

import com.fairlink.passenger.util.HttpUtil;

public class PassengerFeedbackRequest extends BaseHttpPostTask {

	public static class FeedbackMessage {
		public String mbContent;
		public String passengerEmail;
		public String passengerPhone;
	}
	
	public PassengerFeedbackRequest(Context context, FeedbackMessage msg, HttpTaskCallback callback) {
		super(0, HttpUtil.getPassengerFeedback(), new HashMap<String, String>(), callback);
		mParam.put("mbContent", msg.mbContent);
		mParam.put("passengerEmail", msg.passengerEmail);
		mParam.put("passengerPhone", msg.passengerPhone);
	}

	@Override
	protected Object parseJSON(String json) {
		return null;
	}
}
