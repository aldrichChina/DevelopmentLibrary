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

import com.fairlink.passenger.music.MusicPlayerService;
import com.fairlink.passenger.networkrequest.MusicListRequest.MusicInfo;

import android.os.Environment;

public class MusicLocalCacheManager {
	
	class DownloadTask implements Runnable {
		public DownloadTask(String url) {
			this.downloadurl = url;
		}
		
		String downloadurl;
		
		@Override
		public void run() {
			URL url;
			InputStream is = null;
			FileOutputStream out = null;
			HttpURLConnection connection = null;
			File tmpFile = null;
			try {
				url = new URL(downloadurl);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				int code = connection.getResponseCode();
				
				if (code == HttpURLConnection.HTTP_OK) {
					if(mMusicDir.getFreeSpace() <= FREE_SPACE_SIZE) {
						File[] caches = mMusicDir.listFiles();
						MusicInfo currentMusic;
						MusicPlayerService service = MusicPlayerService.getInstance();
						for(int i=0; i<10&&i<caches.length; i++) {
							if(service != null && ((currentMusic = service.getCurrentMusic()) != null)) {
								int index = currentMusic.location.lastIndexOf("/");
								String name = currentMusic.location.substring(index);
								if(!name.equals(caches[i].getName())) {
									caches[i].delete();
								}
							}
						}
					}
					String fileName = downloadurl.substring(downloadurl.lastIndexOf("/"));
					String tmp = fileName+".tmp";
					
					tmpFile = new File(mMusicDir, tmp);
					tmpFile.delete();
					tmpFile.createNewFile();
					is = connection.getInputStream();
					out = new FileOutputStream(tmpFile);
					byte[] bytes = new byte[1024];
					int size;
					while ((size = is.read(bytes)) != -1) {
						out.write(bytes, 0, size);
					}
					tmpFile.renameTo(new File(mMusicDir, fileName));
					mPendingUrl.remove(downloadurl);
					return;
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(tmpFile != null) {
					tmpFile.delete();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(tmpFile != null) {
					tmpFile.delete();
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
	
	private static MusicLocalCacheManager sInstance;
	private File mMusicDir;
	private ArrayList<String> mPendingUrl = new ArrayList<String>();
	private ExecutorService mPool = Executors.newFixedThreadPool(3);
	private static final int FREE_SPACE_SIZE = 20*1024*1024;//20M
	
	private MusicLocalCacheManager() {
		mMusicDir = new File(Environment.getExternalStorageDirectory(), "musiccache");
		if(!mMusicDir.exists()) {
			mMusicDir.mkdir();
		}
	}
	
	public static synchronized MusicLocalCacheManager getInstance() {
		if(sInstance == null) {
			sInstance = new MusicLocalCacheManager();
		}
		return sInstance;
	}
	
	public void downloadMusic(String url) {
		if(mPendingUrl.contains(url)) {
			return;
		}
		mPendingUrl.add(url);
		mPool.submit(new DownloadTask(url));
	}
	
	public File getMusicFile(String url) {
		int index = url.lastIndexOf("/");
		String fileName = url.substring(index);
		File f = new File(mMusicDir, fileName);
		return f;
	}
	
}
