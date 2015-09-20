package com.fairlink.passenger.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.fairlink.passenger.IFEApplication;

import android.util.Log;

public class Logger {
	private String tag;

	static final int DEBUG = 1;
	static final int VERBOSE = 2;
	static final int INFO = 3;
	static final int WARNING = 4;
	static final int ERROR = 5;

	static class LogPostRequest{

		protected Map<String, Object> mParam;
//		protected static String JSESSIONID = null;


		private static final int CORE_POOL_SIZE = 1;
		private static final int MAXIMUM_POOL_SIZE = 1;
		private static final int KEEP_ALIVE = 1;

		private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(1024);
		private static final ThreadFactory sThreadFactory = new ThreadFactory() {
			private final AtomicInteger mCount = new AtomicInteger(1);

			public Thread newThread(Runnable r) {
				return new Thread(r, "LogTask #" + mCount.getAndIncrement());
			}
		};
		
		
		private static volatile Executor sDefaultExecutor = new ThreadPoolExecutor(
				CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
				sPoolWorkQueue, sThreadFactory, new ThreadPoolExecutor.DiscardPolicy());

		public LogPostRequest(int priority, String tag, String message) {
			mParam = new HashMap<String, Object>();
			mParam.put("priority", priority);
			mParam.put("tag", tag);
			mParam.put("message", message);
		}

		protected HttpRequestBase getRequest() {
			HttpPost httpPost = new HttpPost(HttpUtil.getLogEvent());

			try {
				httpPost.setEntity(new StringEntity(new JSONObject(mParam).toString())); 
				httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			return httpPost;
		}
		

		String doInBackground() {
			DefaultHttpClient httpclient = new DefaultHttpClient();

			HttpRequestBase httpRequest = getRequest();
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);

			String sessionId = IFEApplication.getInstance().getSessionId();
			if (sessionId != null) {
				httpRequest.setHeader("Cookie", "JSESSIONID=" + sessionId);
			}

			try {
				HttpResponse response = httpclient.execute(httpRequest);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					String getString = EntityUtils.toString(entity, "utf-8");

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
					return "";
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return "";
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
		}


		public final void execute() {
			sDefaultExecutor.execute(new Runnable() {

				@Override
				public void run() {
					doInBackground();

				}

			});
		}

	}
	
	

	public Logger(Object o, String tag) {
		String className = "";
		if (o != null) {
			String[] split = o.getClass().toString().split("\\.");
			if (split.length > 0) {
				className = split[split.length - 1];
			}
		}

		if (tag == null)
			tag = "";

		if (className.isEmpty())
			this.tag = "FL_" + tag;
		else
			this.tag = "FL_" + tag + "_" + className;
	}

	public void verbose(String msg) {
		Log.v(tag, msg);
		new LogPostRequest(VERBOSE, tag, msg).execute();
	}

	public void debug(String msg) {
		Log.d(tag, msg);
		new LogPostRequest(DEBUG, tag, msg).execute();
	}

	public void info(String msg) {
		Log.i(tag, msg);
		new LogPostRequest(INFO, tag, msg).execute();
	}

	public void warn(String msg) {
		Log.w(tag, msg);
		new LogPostRequest(WARNING, tag, msg).execute();
	}

	public void error(String msg) {
		Log.e(tag, msg);
		new LogPostRequest(ERROR, tag, msg).execute();
	}
}
