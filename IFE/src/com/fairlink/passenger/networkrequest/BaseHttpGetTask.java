package com.fairlink.passenger.networkrequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import com.fairlink.passenger.util.Logger;

public abstract class BaseHttpGetTask extends BaseHttpTask {


	public BaseHttpGetTask(int requestType, String url,
			HashMap<String, String> param, HttpTaskCallback callback) {
		super(requestType, url, callback);
		mParam = param;
		logger = new Logger(this, "HttpGet");
	}

	@Override
	protected HttpRequestBase getRequest() {
		StringBuilder sb = new StringBuilder();
		sb.append(mUrl);

		if (mParam != null && mParam.size() != 0) {
			sb.append("?");

			for (Map.Entry<String, String> entry : mParam.entrySet()) {

				// 如果请求参数中有中文，需要进行URLEncoder编码
				try {
					sb.append(entry.getKey())
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				sb.append("&");
			}

			sb.deleteCharAt(sb.length() - 1);
		}

		return new HttpGet(sb.toString());
	}
}
