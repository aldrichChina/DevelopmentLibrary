/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.jia.ywyx;

import android.R.integer;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.jia.net.HttpCallbackListener;
import com.jia.net.HttpUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class UILApplication extends Application {
	public int i;
	public static String UILApplication_goodsId;
	Cpxq_Activity cpxq_Activity;
//	String CpxqUrl = cpxq_Activity.address;  
//	public static String CpxqUrl_ddxq="http://www.yanwoyinxiang.com/interface.php?action=get_index_xq";
	String CpxqUrl ;// = CpxqUrl_ddxq+"&goods_id="+UILApplication_goodsId;
//	String CpxqUrl = "http://www.yanwoyinxiang.com/interface.php?action=get_index_xq&goods_id=107";
	private App appList;
	public String product_subject;
	public String pay_external_detail;
	public String product_price;
	public String price;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x123:
				product_subject = appList.getGoods_name();
				pay_external_detail = appList.getGoods_name();
				product_price = appList.getGoods_one_price();
	//			int a=Integer.parseInt(price);
				
	//			product_price=String.valueOf(a*i);
				
			}
		};
	};

	

	public String getProduct_subject() {
		return product_subject;
	}

	public void setProduct_subject(String product_subject) {
		this.product_subject = product_subject;
	}

	public String getPay_external_detail() {
		return pay_external_detail;
	}

	public void setPay_external_detail(String pay_external_detail) {
		this.pay_external_detail = pay_external_detail;
	}

	public String getProduct_price() {
		return product_price;
	}

	public void setProduct_price(String product_price) {
		this.product_price = product_price;
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		cpxq_Activity=new Cpxq_Activity();
		i = cpxq_Activity.getI();
		
		if (Config.DEVELOPER_MODE
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyDeath().build());
		}

		super.onCreate();
		Log.d("jia", "UILApplication------CpxqUrl-------->" + CpxqUrl);
		HttpUtil.sendHttpRequest(CpxqUrl, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				Gson gson = new Gson();
				appList = gson.fromJson(response, App.class);
				Log.d("jia", "appList----------------->" + appList);
				Log.d("jia", "商品名称---------->" + appList.getGoods_name());
				Log.d("jia", "price is----------->" + appList.getPrice());
				Log.d("jia",
						"Goods_burden is----------->"
								+ appList.getGoods_burden());
				Log.d("jia",
						"Goods_degree is----------->"
								+ appList.getGoods_degree());
				Log.d("jia",
						"Goods_img is----------->" + appList.getGoods_img());
				Log.d("jia",
						"Goods_name is----------->" + appList.getGoods_name());
				Log.d("jia",
						"Goods_one_price is----------->"
								+ appList.getGoods_one_price());
				Log.d("jia",
						"Goods_place is----------->" + appList.getGoods_place());
				Log.d("jia",
						"Goods_start_weight is----------->"
								+ appList.getGoods_start_weight());

				Message message = new Message();
				message.what = 0x123;
				message.obj = appList;
				handler.sendMessage(message);

			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub

			}
		});
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.threadPoolSize(3).memoryCache(new WeakMemoryCache()).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}