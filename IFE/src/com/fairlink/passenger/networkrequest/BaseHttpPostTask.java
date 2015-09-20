package com.fairlink.passenger.networkrequest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.fairlink.passenger.GetLanguage;
import com.fairlink.passenger.util.Logger;

public abstract class BaseHttpPostTask extends BaseHttpTask {

	public BaseHttpPostTask(int requestType, String url, HashMap<String, String> param, HttpTaskCallback callback) {
		super(requestType, url, callback);
		mParam = param;
		logger = new Logger(this, "HttpPost");
	}

	@Override
	protected HttpRequestBase getRequest() {
		HttpPost httpPost = new HttpPost(mUrl);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (mParam != null) {
			Set<Entry<String, String>> set = mParam.entrySet();
			Iterator<Entry<String, String>> iterator = set.iterator();
			Entry<String, String> entry;
			while (iterator.hasNext()) {
				entry = iterator.next();
				if (entry.getKey().equals("language")) {
					nvps.add(new BasicNameValuePair(entry.getKey(), GetLanguage.ChooseLan()));
				} else {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return httpPost;
	}
}
