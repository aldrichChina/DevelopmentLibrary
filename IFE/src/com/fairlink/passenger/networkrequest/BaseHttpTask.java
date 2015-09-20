package com.fairlink.passenger.networkrequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.LoginMain;
import com.fairlink.passenger.util.Logger;

public abstract class BaseHttpTask implements NetworkRequestAPI {

	private static final String REDIRECT_KEY_WORD = "redirect";
	protected HashMap<String, String> mParam;
	protected String mUrl;
	protected Logger logger;

	private HttpTaskCallback mCallback;
	private int mRequestType;
	private static final Handler sHandler = new Handler();

	public static interface HttpTaskCallback {
		public void onGetResult(int requestType, Object result);

		public void onError(int requestType);
	}

	public BaseHttpTask(int requestType, String url, HttpTaskCallback callback) {
		mRequestType = requestType;
		mUrl = url;
		mCallback = callback;
	}

	protected abstract HttpRequestBase getRequest();

	protected String doInBackground(String... params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpRequestBase httpRequest = getRequest();
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 500000);
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 500000);

		String sessionId = IFEApplication.getInstance().getSessionId();
		if (sessionId != null) {
			httpRequest.setHeader("Cookie", "JSESSIONID=" + sessionId);
		}

		try {
			HttpResponse response = httpclient.execute(httpRequest);
			logger.debug("send http request:" + httpRequest.getURI() + "[" + mParam + "]");
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				String getString = EntityUtils.toString(entity, "utf-8");
				logger.debug("receive http repond:" + getString);

				CookieStore mCookieStore = httpclient.getCookieStore();
				List<Cookie> cookies = mCookieStore.getCookies();
				for (int i = 0; i < cookies.size(); i++) {
					// 这里是读取Cookie['SESSIONID']的值存在静态变量中，保证每次都是同一个值
					if ("JSESSIONID".equals(cookies.get(i).getName())) {
						IFEApplication.getInstance().setSessionId(cookies.get(i).getValue());
						break;
					}
				}

				return getString;
			} else {
				logger.error("get error status:" + statusCode + "when request url:" + mUrl);
				return "";
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("receive UnsupportedEncodingException [" + e.getMessage() + "] when request url:" + this.mUrl);
			return "";
		} catch (ClientProtocolException e) {
			logger.error("receive ClientProtocolException [" + e.getMessage() + "] when request url:" + mUrl);
			return "";
		} catch (IOException e) {
			logger.error("receive IOException [" + e.getMessage() + "] when request url:" + mUrl);
			return "";
		}
	}

	protected abstract Object parseJSON(String json);

	public final void execute(final String... params) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				final String result = doInBackground(params);

				sHandler.post(new Runnable() {

					@Override
					public void run() {
						onPostExecute(result);
					}
				});
			}

		}, "HttpTask").start();
	}

	protected void onPostExecute(String result) {
		if (result == null)
			return;

		if (mCallback != null) {
			if (result.isEmpty()) {
				logger.error("send request failed");
				mCallback.onError(mRequestType);
			} else {
				if(checkRedirect(result)){
					Object taskResult = parseJSON(result);
					mCallback.onGetResult(mRequestType, taskResult);
					
				}

			}
		}
	}

	private boolean checkRedirect(String result) {
		try {
			JSONTokener parser = new JSONTokener(result);
			JSONObject menu = (JSONObject) parser.nextValue();
			int code = menu.optInt("code", -1);
			if (code != 0) {
				return true;
			}

			JSONObject item = menu.optJSONObject("data");
			if (item == null)
				return true;

			if (item.optBoolean(REDIRECT_KEY_WORD, false)) {
				logger.error("receive redirect, offline is " + item.getBoolean("offline"));

				mCallback.onError(REDIRECT_API);

				backToLogin(item.getBoolean("offline"));
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}

	private void backToLogin(boolean isOffline) throws JSONException {
		Context context = IFEApplication.getInstance().getBaseContext();
		if (context != null) {
			IFEApplication.getInstance().setSessionId(null);
			Intent intent = new Intent(context, LoginMain.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(REDIRECT_KEY_WORD, true);
			intent.putExtra("offline", isOffline);
			context.startActivity(intent);
		}
	}
}
