package com.jia.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jia.Model.CustomProgressDialog;
import com.jia.net.HttpCallbackListener;
import com.jia.net.HttpUtil;
import com.jia.ywyx.ConfirmOrder;
import com.jia.ywyx.Cpxq_Activity;
import com.jia.ywyx.MainActivity;
import com.jia.ywyx.R;
import com.jia.ywyx.UILApplication;
import com.jia.ywyx.Url;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * @author 姚佳伟
 * 
 */
public class fragSy extends Fragment implements OnPageChangeListener,
		OnClickListener {
	private CustomProgressDialog dialog;
	public DisplayImageOptions options;
	private ImageView a, b, c, d, e, f, g, h, i, j, k, l;
	/**
	 * ViewPager
	 */
	private ViewPager viewPager;

	/**
	 * װ����ImageView����
	 */
	private ImageView[] tips;

	/**
	 * װImageView����
	 */
	private ImageView[] mImageViews;

	/**
	 * ͼƬ��Դid
	 */
	private int[] imgIdArray;

	private Message ms;
	private int select = 800;
	private ScheduledExecutorService sechExecutorService;
	private static final String address = "http://www.yanwoyinxiang.com/interface.php?action=get_index_ad";
	// private static final String address = "www.yanwoyinxiang.com";
	public static final List<String> imageUrl = new ArrayList<String>();
	public static final List<String> goods_id = new ArrayList<String>();
	public static String getimage, getgoodsid, getimage1, getimage2, getimage3,
			getimage4, getimage5, getimage6, getimage7, getimage8, getimage9,
			getimage10, getgoodsid1, getgoodsid2, getgoodsid3, getgoodsid4,
			getgoodsid5, getgoodsid6, getgoodsid7, getgoodsid8, getgoodsid9,
			getgoodsid10;
	public static String CpxqUrl = "http://www.yanwoyinxiang.com/interface.php?action=get_index_xq";

	private int currentIndex = 0;
	Handler handler;
	Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x111) {
				MainActivity.displayImage(
						imageUrl.get((currentIndex + 0) % imageUrl.size()), a,
						options);
				MainActivity.displayImage(
						imageUrl.get((currentIndex + 1) % imageUrl.size()), b,
						options);
				Log.d("yangxf", "imageUrl测试2------------->" + imageUrl.get(1));
				MainActivity.displayImage(
						imageUrl.get((currentIndex + 2) % imageUrl.size()), c,
						options);
				MainActivity.displayImage(
						imageUrl.get((currentIndex + 3) % imageUrl.size()), d,
						options);
				MainActivity.displayImage(
						imageUrl.get((currentIndex + 4) % imageUrl.size()), e,
						options);
				MainActivity.displayImage(
						imageUrl.get((currentIndex + 5) % imageUrl.size()), f,
						options);
				MainActivity.displayImage(
						imageUrl.get((currentIndex + 6) % imageUrl.size()), g,
						options);
				MainActivity.displayImage(
						imageUrl.get((currentIndex + 7) % imageUrl.size()), h,
						options);
				MainActivity.displayImage(
						imageUrl.get((currentIndex + 8) % imageUrl.size()), i,
						options);
				MainActivity.displayImage(
						imageUrl.get((currentIndex + 9) % imageUrl.size()), j,
						options);
				MainActivity.displayImage(
						imageUrl.get((currentIndex + 10) % imageUrl.size()), k,
						options);
				MainActivity.displayImage(
						imageUrl.get((currentIndex + 11) % imageUrl.size()), l,
						options);
				getimage = imageUrl.get((currentIndex + 0) % imageUrl.size());
				getgoodsid = goods_id.get((currentIndex + 0) % imageUrl.size());
				getimage1 = imageUrl.get((currentIndex + 1) % imageUrl.size());
				getgoodsid1 = goods_id
						.get((currentIndex + 1) % imageUrl.size());
				getimage2 = imageUrl.get((currentIndex + 2) % imageUrl.size());
				getgoodsid2 = goods_id
						.get((currentIndex + 2) % imageUrl.size());
				getimage3 = imageUrl.get((currentIndex + 3) % imageUrl.size());
				getgoodsid3 = goods_id
						.get((currentIndex + 3) % imageUrl.size());
				getimage4 = imageUrl.get((currentIndex + 4) % imageUrl.size());
				getgoodsid4 = goods_id
						.get((currentIndex + 4) % imageUrl.size());
				getimage5 = imageUrl.get((currentIndex + 5) % imageUrl.size());
				getgoodsid5 = goods_id
						.get((currentIndex + 5) % imageUrl.size());
				getimage6 = imageUrl.get((currentIndex + 6) % imageUrl.size());
				getgoodsid6 = goods_id
						.get((currentIndex + 6) % imageUrl.size());
				getimage7 = imageUrl.get((currentIndex + 7) % imageUrl.size());
				getgoodsid7 = goods_id
						.get((currentIndex + 7) % imageUrl.size());
				getimage8 = imageUrl.get((currentIndex + 8) % imageUrl.size());
				getgoodsid8 = goods_id
						.get((currentIndex + 8) % imageUrl.size());
				Log.d("yangxf",
						"imageUrl测试2------------->"
								+ imageUrl.get((currentIndex + 1)
										% imageUrl.size()));
				Log.d("yangxf",
						"imageUrl测试2------------->"
								+ imageUrl.get((currentIndex + 2)
										% imageUrl.size()));

				Log.d("yangxf",
						"imageUrl测试2------------->"
								+ imageUrl.get((currentIndex + 3)
										% imageUrl.size()));
				Log.d("yangxf",
						"imageUrl测试2------------->"
								+ imageUrl.get((currentIndex + 4)
										% imageUrl.size()));
				Log.d("yangxf",
						"imageUrl测试2------------->"
								+ imageUrl.get((currentIndex + 5)
										% imageUrl.size()));
				Log.d("yangxf",
						"imageUrl测试2------------->"
								+ imageUrl.get((currentIndex + 6)
										% imageUrl.size()));
				Log.d("yangxf",
						"imageUrl测试2------------->"
								+ imageUrl.get((currentIndex + 7)
										% imageUrl.size()));
				Log.d("yangxf",
						"imageUrl测试2------------->"
								+ imageUrl.get((currentIndex + 8)
										% imageUrl.size()));
				Log.d("yangxf",
						"imageUrl测试2------------->"
								+ imageUrl.get((currentIndex + 9)
										% imageUrl.size()));
			//	dialog.dismiss(); 
			} else if (msg.what == 0x112) {
				Toast.makeText(getActivity(), "图片加载失败", Toast.LENGTH_SHORT)
						.show();
			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.fragment_all, null);
		dialog = new CustomProgressDialog(getActivity(), "正在玩命加载中....",
				R.anim.frame2);
		dialog.show();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {

				Log.d("yangxf", "response = " + response);
				parseJSONWithGSON(response);
			}

			@Override
			public void onError(Exception e) {
				Message mss = new Message();
				mss.what = 0x112;
				handler2.sendMessage(mss);
			}
		});
		ViewGroup group = (ViewGroup) convertView
				.findViewById(R.id.viewGroup_flag);
		viewPager = (ViewPager) convertView.findViewById(R.id.viewPager_flag);
		a = (ImageView) convertView.findViewById(R.id.a);
		b = (ImageView) convertView.findViewById(R.id.b);
		c = (ImageView) convertView.findViewById(R.id.c);
		d = (ImageView) convertView.findViewById(R.id.d);
		e = (ImageView) convertView.findViewById(R.id.e);
		f = (ImageView) convertView.findViewById(R.id.f);
		g = (ImageView) convertView.findViewById(R.id.g);
		h = (ImageView) convertView.findViewById(R.id.h);
		i = (ImageView) convertView.findViewById(R.id.i);
		j = (ImageView) convertView.findViewById(R.id.j);
		k = (ImageView) convertView.findViewById(R.id.k);
		l = (ImageView) convertView.findViewById(R.id.l);

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic)
				.showImageOnFail(R.drawable.ic).cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).build();

		LinearLayout fragsy = (LinearLayout) convertView
				.findViewById(R.id.fragsy);

		a.setOnClickListener(this);
		a.setTag(1);
		b.setOnClickListener(this);
		b.setTag(2);
		c.setOnClickListener(this);
		c.setTag(3);
		d.setOnClickListener(this);
		d.setTag(4);
		e.setOnClickListener(this);
		e.setTag(5);
		f.setOnClickListener(this);
		f.setTag(6);
		g.setOnClickListener(this);
		g.setTag(7);
		h.setOnClickListener(this);
		h.setTag(8);
		i.setOnClickListener(this);
		i.setTag(9);
		j.setOnClickListener(this);
		j.setTag(10);
		k.setOnClickListener(this);
		k.setTag(11);

		imgIdArray = new int[] { R.drawable.guanggao1, R.drawable.guanggao2,
				R.drawable.guanggao3, R.drawable.guanggao4 };

		tips = new ImageView[imgIdArray.length];

		for (int i = 0; i < tips.length; i++) {
			LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			// 设置每个小圆点距离左边的间距
			margin.setMargins(10, 0, 10, 0);

			ImageView imageView = new ImageView(getActivity()
					.getApplicationContext());
			// 设置每个小圆点的宽高new LayoutParams(15,15)
			imageView.setLayoutParams(margin);

			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			group.addView(imageView);
		}

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				viewPager.setCurrentItem(msg.arg1);
			}

		};

		mImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(getActivity()
					.getApplicationContext());
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imgIdArray[i]);
		}
		viewPager.setAdapter(new MyAdapter());

		viewPager.setOnPageChangeListener(this);

		viewPager.setCurrentItem((mImageViews.length) * 100);

		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		setImage();

		return convertView;
	}

	private List<String> parseJSONWithGSON(String jsonData) {
		Gson gson = new Gson();

		List<Apps> appList = gson.fromJson(jsonData,
				new TypeToken<List<Apps>>() {
				}.getType());
		for (Apps app : appList) {

			imageUrl.add(app.getImage_url());
			goods_id.add(app.getGoods_id());
		}
		Log.d("yangxf", "goods_id = " + goods_id);
		Message mss = new Message();
		mss.what = 0x111;
		mss.obj = imageUrl;
		handler2.sendMessage(mss);
		return imageUrl;

	}

	private void setImage() {
		Log.d("jia", "imageUrl测试------------->" + imageUrl);

	}

	/*
	 * public boolean onTouch(View v, MotionEvent event) { switch
	 * (event.getAction()) { case MotionEvent.ACTION_MOVE:
	 * 
	 * case MotionEvent.ACTION_UP: case MotionEvent.ACTION_CANCEL:
	 * viewPager.requestDisallowInterceptTouchEvent(true); break; } return
	 * false; }
	 */

	@Override
	public void onStart() {
		sechExecutorService = Executors.newSingleThreadScheduledExecutor();
		sechExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,
				TimeUnit.SECONDS);
		super.onStart();
	}

	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				ms = handler.obtainMessage();
				ms.arg1 = select;
				handler.sendMessage(ms);
				select++;
			}
		}
	}

	@Override
	public void onDestroy() {
		sechExecutorService.shutdown();
		super.onDestroy();
	}

	/**
	 * 
	 * @author yaojiawei
	 * 
	 */
	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mImageViews[position
					% mImageViews.length]);

		}

		@Override
		public Object instantiateItem(View container, int position) {

			View pos = mImageViews[position % mImageViews.length];
			((ViewPager) container).addView(pos, 0);

			pos.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d("yangxf", "instantiateItem onClick");
					Intent imgti2 = new Intent();
					imgti2.setClass(getActivity(), Cpxq_Activity.class);
					imgti2.putExtra("gurl", "http://qzone.qq.com/");
					startActivity(imgti2);

				}
			});

			return pos;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		viewPager.getParent().requestDisallowInterceptTouchEvent(true);
	}

	@Override
	public void onPageSelected(int arg0) {

		select = viewPager.getCurrentItem();

		setImageBackground(arg0 % mImageViews.length);
	}

	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

	@Override
	public void onClick(View v) {
		int tag = (Integer) v.getTag();
		switch (tag) {
		case 1:
			Cpxq_Activity.gethttp = CpxqUrl;
			Cpxq_Activity.goodid = getgoodsid;
			ConfirmOrder.ConfirmOrder_goodid = getgoodsid;
			UILApplication.UILApplication_goodsId = getgoodsid;
			Intent intent1 = new Intent(getActivity(), Cpxq_Activity.class);
			startActivity(intent1);
			break;
		case 2:
			Cpxq_Activity.gethttp = CpxqUrl;
			Cpxq_Activity.goodid = getgoodsid1;
			ConfirmOrder.ConfirmOrder_goodid = getgoodsid1;
			UILApplication.UILApplication_goodsId = getgoodsid1;
			Intent intent2 = new Intent(getActivity(), Cpxq_Activity.class);
			startActivity(intent2);
			break;
		case 3:
			Cpxq_Activity.gethttp = CpxqUrl;
			Cpxq_Activity.goodid = getgoodsid2;
			ConfirmOrder.ConfirmOrder_goodid = getgoodsid2;
			UILApplication.UILApplication_goodsId = getgoodsid2;
			Intent intent3 = new Intent(getActivity(), Cpxq_Activity.class);
			startActivity(intent3);
			break;
		case 4:
			Cpxq_Activity.gethttp = CpxqUrl;
			Cpxq_Activity.goodid = getgoodsid3;
			ConfirmOrder.ConfirmOrder_goodid = getgoodsid3;
			UILApplication.UILApplication_goodsId = getgoodsid3;
			Intent intent4 = new Intent(getActivity(), Cpxq_Activity.class);
			startActivity(intent4);
			break;
		case 5:
			Cpxq_Activity.gethttp = CpxqUrl;
			Cpxq_Activity.goodid = getgoodsid4;
			ConfirmOrder.ConfirmOrder_goodid = getgoodsid4;
			UILApplication.UILApplication_goodsId = getgoodsid4;
			Intent intent5 = new Intent(getActivity(), Cpxq_Activity.class);
			startActivity(intent5);
			break;
		case 6:
			Cpxq_Activity.gethttp = CpxqUrl;
			Cpxq_Activity.goodid = getgoodsid5;
			ConfirmOrder.ConfirmOrder_goodid = getgoodsid5;
			UILApplication.UILApplication_goodsId = getgoodsid5;
			Intent intent6 = new Intent(getActivity(), Cpxq_Activity.class);
			startActivity(intent6);
			break;
		case 7:
			Cpxq_Activity.gethttp = CpxqUrl;
			Cpxq_Activity.goodid = getgoodsid6;
			ConfirmOrder.ConfirmOrder_goodid = getgoodsid6;
			UILApplication.UILApplication_goodsId = getgoodsid6;
			Intent intent7 = new Intent(getActivity(), Cpxq_Activity.class);
			startActivity(intent7);
			break;
		case 8:
			Cpxq_Activity.gethttp = CpxqUrl;
			Cpxq_Activity.goodid = getgoodsid7;
			ConfirmOrder.ConfirmOrder_goodid = getgoodsid7;
			UILApplication.UILApplication_goodsId = getgoodsid7;
			Intent intent8 = new Intent(getActivity(), Cpxq_Activity.class);
			startActivity(intent8);
			break;
		case 9:
			Cpxq_Activity.gethttp = CpxqUrl;
			Cpxq_Activity.goodid = getgoodsid8;
			ConfirmOrder.ConfirmOrder_goodid = getgoodsid8;
			UILApplication.UILApplication_goodsId = getgoodsid8;
			Intent intent9 = new Intent(getActivity(), Cpxq_Activity.class);
			startActivity(intent9);
			break;
		case 10:
			Cpxq_Activity.gethttp = CpxqUrl;
			Cpxq_Activity.goodid = getgoodsid9;
			ConfirmOrder.ConfirmOrder_goodid = getgoodsid9;
			UILApplication.UILApplication_goodsId = getgoodsid9;
			Intent intent10 = new Intent(getActivity(), Cpxq_Activity.class);
			startActivity(intent10);
			break;
		case 11:
			Cpxq_Activity.gethttp = CpxqUrl;
			Cpxq_Activity.goodid = getgoodsid10;
			ConfirmOrder.ConfirmOrder_goodid = getgoodsid10;
			UILApplication.UILApplication_goodsId = getgoodsid10;
			Intent intent11 = new Intent(getActivity(), Cpxq_Activity.class);
			startActivity(intent11);
			break;
		}

	}

}
