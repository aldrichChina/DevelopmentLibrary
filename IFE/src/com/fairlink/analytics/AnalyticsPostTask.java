package com.fairlink.analytics;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.fairlink.passenger.networkrequest.BaseHttpTask;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.Logger;

class AnalyticsPostTask extends BaseHttpTask {

	private String jsonString;

	public AnalyticsPostTask(HttpTaskCallback callback, String jsonString) {
		super(0, HttpUtil.getAnalyticsLogEvent(), callback);
		this.jsonString = jsonString;
		logger = new Logger(this, "AnalyticsHttpPost");
	}

	@Override
	protected Object parseJSON(String json) {
		try {
			return new JSONObject(json == null ? "" : json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	protected HttpRequestBase getRequest() {
		HttpPost httpPost = new HttpPost(mUrl);
		try {
			httpPost.setEntity(new StringEntity(jsonString));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");

		return httpPost;
	}
}