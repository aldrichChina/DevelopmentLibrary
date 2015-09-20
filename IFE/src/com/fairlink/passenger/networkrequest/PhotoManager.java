package com.fairlink.passenger.networkrequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;

import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.application.dbmanager.DBManager;
import com.fairlink.passenger.util.ComUtil;

public class PhotoManager {
	
	public interface PhotoDownloadCallback {
		public void onPhotoDownload(String url, String path);
		public void onPhotoDownloadError(String url, String path);
	}
	
	public static interface PhotoDecodedCallback {
		public void onPhotoDecoded(String path, Bitmap bitmap);
	}
	
	class DownloadTask implements Runnable {
		public DownloadTask(String url, PhotoDownloadCallback callback) {
			this.callback = callback;
			this.downloadurl = url;
		}
		
		String downloadurl;
		String path;
		PhotoDownloadCallback callback;
		
		@Override
		public void run() {
			URL url;
			InputStream is = null;
			FileOutputStream out = null;
			HttpURLConnection connection = null;
			try {
				url = new URL(downloadurl);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				int code = connection.getResponseCode();
				
				if (code == HttpURLConnection.HTTP_OK) {
					String fileName = downloadurl.hashCode()+".png";
					File image = new File(mPhotoDir, fileName);
					path = image.getAbsolutePath();
					image.delete();
					image.createNewFile();
					is = connection.getInputStream();
					out = new FileOutputStream(image);
					byte[] bytes = new byte[1024];
					int size;
					while ((size = is.read(bytes)) != -1) {
						out.write(bytes, 0, size);
					}
					mPendingUrl.remove(downloadurl);
					if(mCurrentCacheSize + image.length() > MAX_CACHE_SIZE || mPhotoDir.getFreeSpace() <= FREE_SPACE_SIZE) {
						cleanSomeCache();
					} 
					saveCacheFile(downloadurl, path);
					if(callback != null) {
						mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								callback.onPhotoDownload(downloadurl, path);
							}
						});
					}
					return;
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				if(callback != null) {
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							callback.onPhotoDownloadError(downloadurl, path);
						}
					});
				}
			} catch (IOException e) {
				e.printStackTrace();
				if(callback != null) {
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							callback.onPhotoDownloadError(downloadurl, path);
						}
					});
				}
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
			}
			mPendingUrl.remove(downloadurl);
		}
	}
	
	private static final int MAX_CACHE_SIZE = 50*1024*1024;//50M
	private static final int FREE_SPACE_SIZE = 20*1024*1024;//20M
	private static PhotoManager sInstance;
	private File mPhotoDir;
	private ArrayList<String> mPendingUrl = new ArrayList<String>();
	private ExecutorService mPool = Executors.newFixedThreadPool(4);
	private HashMap<String, String> mPhotoCache = new HashMap<String, String>();
	private HashMap<String, SoftReference<Bitmap>> mPhotoBitmapCache = new HashMap<String, SoftReference<Bitmap>>();
	private DBManager manager;
	private int mCurrentCacheSize = 0;
	private static final Handler mHandler = new Handler();

	private PhotoManager() {

	}

	public void init() {
		mPhotoDir = new File(Environment.getExternalStorageDirectory(), "IFE/photocache");
		ComUtil.cleanFolder(mPhotoDir.getAbsolutePath());
		if(!mPhotoDir.exists()) {
			mPhotoDir.mkdirs();
		}
		manager = new DBManager(IFEApplication.getInstance());
		loadCachePhoto();
	}

	private synchronized void saveCacheFile(String key, String value) {
		mPhotoCache.put(key, value);
		manager.addPictureItem(key, value);

	}
	
	private synchronized void cleanSomeCache() {
		Set<Entry<String, String>> entrySet = mPhotoCache.entrySet();
		Iterator<Entry<String, String>> it = entrySet.iterator();
		int size = entrySet.size();
		int i=0;
		String path;
		File file;
		Entry<String, String> entry;
		while(it.hasNext() && i < size/2) {
			entry = it.next();
			path = entry.getValue();
			it.remove();
			manager.deletePictureItem(entry.getKey());
			file = new File(path);
			mCurrentCacheSize -= file.length();
			file.delete();
			i++;
		}
	}
	
	private void loadCachePhoto() {
		HashMap<String, String> cachefile = manager.queryCacheFile();
		Set<Entry<String, String>> entrySet = cachefile.entrySet();
		Iterator<Entry<String, String>> it = entrySet.iterator();
		while(it.hasNext()) {
			Entry<String, String> entry = it.next();
			mPhotoCache.put(entry.getKey(), entry.getValue());
			File file = new File(entry.getValue());
			mCurrentCacheSize += file.length();
			file.delete();
		}
	}
	
	public static synchronized PhotoManager getInstance() {
		if(sInstance == null) {
			sInstance = new PhotoManager();
		}
		return sInstance;
	}
	
	public synchronized void downloadImage(String url, PhotoDownloadCallback callback) {
		if(mPendingUrl.contains(url)) {
			return;
		}
		mPendingUrl.add(url);
		mPool.submit(new DownloadTask(url, callback));
	}
	
	public String getImageFile(String url) {
		String path = mPhotoCache.get(url);
		if (path != null) {
			File image = new File(path);
			if (image.exists()) {
				return path;
			} else {
				mPhotoCache.remove(path);
				manager.deletePictureItem(path);
			}
		}
		return null;
	}
	
	public static Bitmap decodePhotoAsync(String imagePath, int reqwidth, int reqheight, PhotoDecodedCallback callback) {
		Bitmap bitmap;
		SoftReference<Bitmap> ref = sInstance.mPhotoBitmapCache.get(imagePath);
		if(ref != null) {
			bitmap = ref.get();
			if(bitmap != null) {
				return bitmap;
			}
		}
		
		new DecodeTask(imagePath, reqwidth, reqheight, callback).execute((String)null);
		return null;
	}
	
	private static Bitmap decodePhotoFromFile(String imagePath, int reqwidth, int reqheight) {
		if(reqwidth > 1280)
			reqwidth = 1280;
		if(reqheight > 800)
			reqheight = 800;
		
		Bitmap bitmap;
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);
		options.inSampleSize = calculateInSampleSize(options, reqwidth,
				reqheight);

		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;

		bitmap = BitmapFactory.decodeFile(imagePath, options);
		return bitmap;
	}
	
	public Bitmap decodePhoto(String imagePath, int reqwidth,
			int reqheight) {
		Bitmap bitmap;
		SoftReference<Bitmap> ref = mPhotoBitmapCache.get(imagePath);
		if(ref != null) {
			bitmap = ref.get();
			if(bitmap != null) {
				return bitmap;
			}
		}
		
		
		bitmap = decodePhotoFromFile(imagePath, reqwidth, reqheight);
		if(bitmap != null) {
			ref = new SoftReference<Bitmap>(bitmap);
			sInstance.mPhotoBitmapCache.put(imagePath, ref);
		}
		return bitmap;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) height / (float) reqHeight);
			if (widthRatio > heightRatio) {
				inSampleSize = widthRatio;
			} else {
				inSampleSize = heightRatio;
			}
			if (reqWidth != -1 && reqHeight != -1 ){
				final float totalPixels = width * height;

				final float totalReqPixelsCap = reqWidth * reqHeight * 2;

				while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
					inSampleSize++;
			    }
			}
		}
		return inSampleSize;
	}

	static class DecodeTask extends AsyncTask<String, Integer, Bitmap> {

		String path;
		PhotoDecodedCallback callback;
		int width;
		int height;
		
		public DecodeTask(String path, int width, int height, PhotoDecodedCallback callback) {
			this.path = path;
			this.callback = callback;
			this.width = width;
			this.height = height;
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			return decodePhotoFromFile(path, width, height);
		}
		
		protected void onPostExecute(Bitmap bitmap) {
			if(bitmap != null) {
				SoftReference<Bitmap> ref = new SoftReference<Bitmap>(bitmap);
				sInstance.mPhotoBitmapCache.put(path, ref);
			}
			if(callback != null) {
				callback.onPhotoDecoded(path, bitmap);
			}
		}
	}
}
