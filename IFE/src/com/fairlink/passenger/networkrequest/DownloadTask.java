package com.fairlink.passenger.networkrequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.Message;

import com.fairlink.passenger.util.Logger;

public class DownloadTask {

	private String mFullPath;
	private String mSavePath;
	private DownloadListener mListener;
	private Object[] mParams;

	private static final int CORE_POOL_SIZE = 1;
	private static final int MAXIMUM_POOL_SIZE = 1;
	private static final int KEEP_ALIVE = 1;

	private static final int MESSAGE_POST_RESULT = 0x1;
	private static final int MESSAGE_POST_FAILED = 0x2;
	private static final Logger logger = new Logger(null, "Download");
	private static Boolean suspend = false;

	private static final Handler sHandler = new Handler() {
		public void handleMessage(Message msg) {
			DownloadTask task = (DownloadTask) msg.obj;
			switch (msg.what) {
				case MESSAGE_POST_RESULT: {
					task.onPostExecute(true);
					break;
				}

				case MESSAGE_POST_FAILED: {
					task.onPostExecute(false);
					break;
				}
			}
		}
	};


	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "DownloadTask #" + mCount.getAndIncrement());
		}
	};

	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(
			128);

	private static volatile Executor sDefaultExecutor = new ThreadPoolExecutor(
			CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
			sPoolWorkQueue, sThreadFactory);

	public interface DownloadListener {
		void OnFinish(boolean isSuccess, String savePath, Object... params);
	}

	public DownloadTask(String savePath, DownloadListener listener,
			Object... params) {
		mSavePath = savePath;
		mListener = listener;
		mParams = params;
	}

	public void execute(final String url) {
		sDefaultExecutor.execute(new Runnable() {

			@Override
			public void run() {
				Message message = sHandler.obtainMessage(doInBackground(url) == null ? MESSAGE_POST_FAILED : MESSAGE_POST_RESULT,
						DownloadTask.this);
				message.sendToTarget();
			}

		});
	}

	public static void suspend(){
		synchronized(suspend){
			suspend = true;
			
		}
	}
	
	public static void resume(){
		synchronized(suspend){
			suspend.notifyAll(); 
			suspend = false;
		}
	}
	
	String doInBackground(String urlStr) {
		URL url;
		InputStream is = null;
		FileOutputStream out = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			String fileName = new File(url.getFile()).getName();
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(60*1000);
			connection.setReadTimeout(60*1000);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			int code = connection.getResponseCode();

			if (code == HttpURLConnection.HTTP_OK) {
				int contentlen = connection.getContentLength();
				if (contentlen == -1) {
					contentlen = 0;
				}
				File dir = new File(mSavePath);
				if (!dir.exists()) {
					dir.mkdir();
				}
				File temp = new File(dir, fileName);
				mFullPath = temp.getAbsolutePath();
				if (temp.exists()) {
					temp.delete();
				}

				temp.createNewFile();
				is = connection.getInputStream();
				out = new FileOutputStream(temp);
				byte[] bytes = new byte[1024 * 1024];
				int size;
				int count = 0;
				int lastPrintSize = 0;
				while ((size = is.read(bytes)) != -1) {
					synchronized(suspend){
						if(suspend){
							suspend.wait();
						}
					}
					out.write(bytes, 0, size);
					count += size;
					if (count - lastPrintSize > 1024 * 1024) {
						lastPrintSize = count;
						logger.debug("download [" + mFullPath + "] " + lastPrintSize + " finished.");
					}
				}
				return temp.getAbsolutePath();
			}
		} catch (MalformedURLException e) {
			logger.error("download [" + mFullPath + "] failed with MalformedURLException:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("download [" + mFullPath + "] failed with IOException:" + e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			logger.error("download [" + mFullPath + "] failed with InterruptedException:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

	protected void onPostExecute(boolean isSuccess) {
		mListener.OnFinish(isSuccess, mFullPath, mParams);
	}
}
