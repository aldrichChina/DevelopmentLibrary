package com.fairlink.passenger.networkrequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.BitmapFactory;


public class bookManager {
	
	public static interface BookDownloadCallback {
		public void onBookDownload(String url, String path, boolean success);
	}
	
	class DownloadTask implements Runnable {
		public DownloadTask(Context context,String url, String fileType, String fileName, BookDownloadCallback callback) {
			this.fileType = fileType;
			this.downloadurl = url;
			this.fileName = fileName;
			this.context = context;
			this.callback = callback;
		}
		
		String downloadurl;
		String fileType;
		String fileName;
		Context context;
		BookDownloadCallback callback;
		
		@Override
		public void run() {
			URL url;
			InputStream is = null;
			FileOutputStream out = null;
			HttpURLConnection connection = null;
			File book = null;
			String file = null;
			boolean success = false;
			try {
				url = new URL(downloadurl);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				int code = connection.getResponseCode();
				
				if (code == HttpURLConnection.HTTP_OK) {
					file = "/sdcard/book/"+fileName +fileType;
					String tmp = file+".tmp";
					book = new File(tmp);
					book.delete();
					book.createNewFile();
					is = connection.getInputStream();
					out = new FileOutputStream(book);
					byte[] bytes = new byte[1024];
					int size;
					while ((size = is.read(bytes)) != -1) {
						out.write(bytes, 0, size);
					}
					success = true;
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(connection != null) {
					connection.disconnect();
				}
				if(success) {
					book.renameTo(new File(file));
				}
				mPendingUrl.remove(downloadurl);
				callback.onBookDownload(downloadurl, file, success);
			}
			
		}
	}
	
	private static bookManager sInstance;
	private ArrayList<String> mPendingUrl = new ArrayList<String>();
	private ExecutorService mPool = Executors.newFixedThreadPool(2);
//	private HashMap<String, String> mBookCache = new HashMap<String, String>();
	
	
	private bookManager() {}
	
	public static synchronized bookManager getInstance() {
		if(sInstance == null) {
			sInstance = new bookManager();
		}
		return sInstance;
	}
	
	public void downloadbook(Context context, String url, String fileType, String fileName, BookDownloadCallback callback) {
		if(mPendingUrl.contains(url)) {
			return;
		}
		mPendingUrl.add(url);
		mPool.submit(new DownloadTask(context, url, fileType, fileName, callback));
	}
	
	public boolean isBookDownloading(String url) {
		return mPendingUrl.contains(url);
	}
	
//	public String getBookFile(String url) {
//		return mBookCache.get(url);
//	}
	
	/*public static Bitmap decodePhoto(String imagePath, int reqwidth,
			int reqheight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);
		options.inSampleSize = calculateInSampleSize(options, reqwidth,
				reqheight);

		options.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
		return bitmap;
	}*/

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

			final float totalPixels = width * height;

			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

}
