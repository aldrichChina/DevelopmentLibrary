package com.fairlink.passenger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import android.app.Application;
import android.os.Environment;
import android.os.Handler;

import com.fairlink.passenger.bean.RecVideo;
import com.fairlink.passenger.cart.GoodsCartItem;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.HeartRequest;
import com.fairlink.passenger.networkrequest.PhotoManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * @ClassName ： IFEApplication
 * @Description: 全局应用程序类：用于保存和调用全局应用配置
 */

public class IFEApplication extends Application implements HttpTaskCallback {

	private static IFEApplication instance;

	public synchronized static IFEApplication getInstance() {

		return instance;
	}

	private RecVideo video; // 首页推荐 影视信息
	private List<GoodsCartItem> dataFLC; // 空中商城购物车列表
	private HashMap<String, Long> videoPosition = new HashMap<String, Long>();
	private boolean offlineStatus = false;
	private Properties prop;
	private int ordersCount;
	private String JSESSIONID;
	public String saytext;

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;

		InputStream in = null;
		try {
			prop = new Properties();
			File f = new File(Environment.getExternalStorageDirectory().getPath() + "/config.properties");
			if (f.exists()) {
				in = new FileInputStream(f);
			} else {
				in = getResources().getAssets().open("config.properties");

			}
			prop.load(in);
		} catch (IOException e) {
			throw new RuntimeException("A error happens when reading config.properties", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

		dataFLC = new ArrayList<GoodsCartItem>();

		initImageLoader();
		PhotoManager.getInstance().init();

		handler.postDelayed(runnable, 1000);
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			new HeartRequest(IFEApplication.this).execute((String) null);
			handler.postDelayed(this, 3 * 60 * 1000);
		}
	};

	public String getValue(String key) {
		return prop.getProperty(key);
	}

	private void initImageLoader() {

		File cacheDir = StorageUtils.getOwnCacheDirectory(this, "IFEIMG/Cache/");// 获取到缓存的目录地址

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(10)
				// 线程池内加载的数量
				.tasksProcessingOrder(QueueProcessingType.LIFO).denyCacheImageMultipleSizesInMemory()
				.memoryCacheSize(2 * 1024 * 1024).diskCache(new UnlimitedDiscCache(cacheDir))
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).build();

		ImageLoader.getInstance().init(config);
		ImageLoader.getInstance().clearDiskCache();
	}

	public RecVideo getVideo() {
		return video;
	}

	public void setVideo(RecVideo video) {
		this.video = video;
	}

	// 获取所有商品数量
	public int getGoodsAllNum() {
		int num = 0;

		if (dataFLC != null) {
			for (GoodsCartItem item : dataFLC) {
				num += item.getGoodsItem().getCount();
			}
		}

		return num;
	}

	/***********************************************
	 * 
	 * 空中商城购物车相关操作
	 * 
	 **********************************************/

	public List<GoodsCartItem> getDataFLC() {
		return dataFLC;
	}

	public void setDataFLC(List<GoodsCartItem> dataFLC) {
		if (dataFLC == null)
			this.dataFLC = new ArrayList<GoodsCartItem>();
		else
			this.dataFLC = dataFLC;
	}

	/** 增加单个商品到购物车 */
	public void setSingleDataFLC(GoodsCartItem newItem) {
		boolean exist = false;
		for (GoodsCartItem goodsCartItem : dataFLC) {
			if (goodsCartItem.getGoodsItem().getGoods().getId() == newItem.getGoodsItem().getGoods().getId()) {
				goodsCartItem.getGoodsItem().addCount(newItem.getGoodsItem().getCount());
				exist = true;
				break;
			}
		}

		if (!exist)
			dataFLC.add(newItem);
	}

	public boolean getOfflineStatus() {
		return offlineStatus;
	}

	public void setOfflineStatus(boolean offline) {
		offlineStatus = offline;
	}

	public String getOrdersAllNum() {
		return Integer.valueOf(ordersCount).toString();
	}

	public void setOrdersAllNum(int ordersCount) {
		this.ordersCount = ordersCount;
	}

	public void addOrdersNum() {
		ordersCount++;
	}

	public void decreaseOrdersNum() {
		if (ordersCount == 0) {
			return;
		}

		ordersCount--;
	}

	public synchronized void setSessionId(String id) {
		JSESSIONID = id;
	}

	public synchronized String getSessionId() {
		return JSESSIONID;
	}

	@Override
	public void onGetResult(int requestType, Object result) {

	}

	@Override
	public void onError(int requestType) {
		// TODO Auto-generated method stub

	}
	
	//电影进度
	public void setVideoPosition(String url, long positin) {
		videoPosition.put(url, positin);
	}
	
	public long getVideoPosition(String url) {
		long position = 0;
		if (videoPosition.containsKey(url)) {
			position = videoPosition.get(url);
		}
		return position;
	}	
	
}

