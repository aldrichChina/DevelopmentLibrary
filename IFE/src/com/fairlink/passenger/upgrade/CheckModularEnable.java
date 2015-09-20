package com.fairlink.passenger.upgrade;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.fairlink.passenger.util.HttpUtil;

public class CheckModularEnable {

	public static String MODULAR_ENABLE_FILE = "modular_enable.json";
	
	private static CheckModularEnable instance;

	boolean mIsFinished = true;
	String mJsonStr = "{\"result\": false }";
	
	
	public interface CheckModularEnableListener {
		public void onFinish();
	}
	
	List<CheckModularEnableListener> mListeners = new ArrayList<CheckModularEnableListener>();

	
	public static CheckModularEnable getInstance() {
		if (instance == null) {
			instance = new CheckModularEnable();
		}
		return instance;
	}
	
	private CheckModularEnable() {
	}



	public void start() {
		if(mIsFinished){
			new DownloadModularConfig(HttpUtil.getMainAppEnableModular()).execute((String) null);
			mIsFinished = false;
			
		}
	}

	public void stop() {
	}

	public void addListener(CheckModularEnableListener l){
		mListeners.add(l);
	}

	public void removeListener(CheckModularEnableListener l){
		mListeners.remove(l);
	}
	
	
	public boolean isFinished(){
		return mIsFinished;
	}
	
	public String jsonString(){
		return mJsonStr;
	}
	
	class DownloadModularConfig extends AsyncTask<String, Integer, String> {

		protected String mUrl;

		public DownloadModularConfig(String url) {
			mUrl = url;
		}

		@Override
		protected String doInBackground(String... params) {
			DefaultHttpClient httpclient = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet(mUrl);

			try {
				HttpResponse response = httpclient.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();

					mJsonStr = EntityUtils.toString(entity, "utf-8");
					
				}else{
					mJsonStr = "{\"result\": false }";
				}
				
				return mJsonStr;
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void onProgressUpdate(Integer... values) {
		}

		@Override
		protected void onPostExecute(String result) {
			mIsFinished = true;
			
			CheckModularEnableListener[] listeners = mListeners.toArray(new CheckModularEnableListener[0]);
			for(CheckModularEnableListener l : listeners){
				l.onFinish();
			}
		}
	}
}
