package com.jia.ywyx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.jia.fragment.fragFl;
import com.jia.fragment.fragGwc;
import com.jia.fragment.fragMyYw;
import com.jia.fragment.fragSy;
import com.jia.fragment.fragsearch;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.L;

/**
 * @author yaojiawei
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	public ImageLoader imageLoader = ImageLoader.getInstance();
	private static final String TEST_FILE_NAME = "yaojiawei.png";
	private ImageView image_whh, imagea, imageb, imagec, imaged, imagesc,
			shouyeda, shouyedba, shouyedbb, shouyeea, shouyeeb, shouyeebb;

	/**
	 * ViewPager
	 */
	private ViewPager viewPager;

	private ImageView[] tips;

	private ImageView[] mImageViews;

	private int[] imgIdArray;

	private Handler handler;
	private Message ms;
	private int select = 800;

	private ScheduledExecutorService sechExecutorService;

	private List<View> list;

	private fragSy fragSy;
	private fragGwc fragGwc;
	private fragsearch fragsearch;
	private fragFl fragFl;
	private fragMyYw fragMyYw;
	private FragmentManager fragmentManager;

	private ImageView image1;
	private ImageView dbgwc;
	private ImageView image3;
	private ImageView image4;
	private ImageView image5;

	private View sy_layout;
	private View gwc_layout;
	private View ss_layout;
	private View fl_layout;
	private View mywy_layout;
	private ImageView two;
	public static String getUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		initViews();
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic)
				.showImageOnFail(R.drawable.ic).cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).build();
		fragmentManager = getFragmentManager();

		setTabSelection(0);
		image_whh = (ImageView) findViewById(R.id.image_whh);
		imagea = (ImageView) findViewById(R.id.imagea);
		imageb = (ImageView) findViewById(R.id.imageb);
		imagec = (ImageView) findViewById(R.id.imagec);
		imaged = (ImageView) findViewById(R.id.imaged);
		imagesc = (ImageView) findViewById(R.id.imagesc);
		shouyeda = (ImageView) findViewById(R.id.shouyeda);
		shouyedba = (ImageView) findViewById(R.id.shouyedba);
		shouyedbb = (ImageView) findViewById(R.id.shouyedbb);
		shouyeea = (ImageView) findViewById(R.id.shouyeea);
		shouyeeb = (ImageView) findViewById(R.id.shouyeeb);
		shouyeebb = (ImageView) findViewById(R.id.shouyeebb);
		/*
		 * displayImage(Url.wUrl, image_whh, options);
		 * Log.d("yangxf","Url.wUrl = "+Url.wUrl);
		 * Log.d("yangxf","Url.bUrl = "+Url.bUrl);
		 * Log.d("yangxf","Url.cUrl = "+Url.cUrl);
		 * Log.d("yangxf","Url.dUrl = "+Url.dUrl);
		 * Log.d("yangxf","Url.sUrl = "+Url.sUrl);
		 * Log.d("yangxf","Url.dbbUrl = "+Url.dbbUrl);
		 * Log.d("yangxf","Url.eaUrl = "+Url.eaUrl);
		 * Log.d("yangxf","Url.bbUrl = "+Url.bbUrl);
		 * 
		 * displayImage(Url.aUrl, imagea, options); displayImage(Url.bUrl,
		 * imageb, options); displayImage(Url.cUrl, imagec, options);
		 * displayImage(Url.dUrl, imaged, options); displayImage(Url.sUrl,
		 * imagesc, options); displayImage(Url.daUrl, shouyeda, options);
		 * displayImage(Url.baUrl, shouyedba, options); displayImage(Url.dbbUrl,
		 * shouyedbb, options); displayImage(Url.eaUrl, shouyeea, options);
		 * displayImage(Url.ebUrl, shouyeeb, options); displayImage(Url.bbUrl,
		 * shouyeebb, options);
		 */
		/*
		 * LinearLayout activity_main = (LinearLayout)
		 * findViewById(R.id.activity_main);
		 * activity_main.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(MainActivity.this,ImagePagerActivity.class);
		 * 
		 * startActivity(intent); } });
		 */

		File testImageOnSdCard = new File("/mnt/sdcard", TEST_FILE_NAME);
		if (!testImageOnSdCard.exists()) {

			copyTestImageToSdCard(testImageOnSdCard);
		}
		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		two = (ImageView) findViewById(R.id.two);
		two.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, two.class);
				startActivity(intent);
			}
		});

		imgIdArray = new int[] { R.drawable.guanggao1, R.drawable.guanggao2,
				R.drawable.guanggao3, R.drawable.guanggao4, };

		tips = new ImageView[imgIdArray.length];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(10, 10));
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
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(msg.arg1);
			}

		};

		// 锟斤拷图片装锟截碉拷锟斤拷锟斤拷锟斤拷
		mImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imgIdArray[i]);
		}

		// 锟斤拷锟斤拷Adapter
		viewPager.setAdapter(new MyAdapter());
		// 锟斤拷锟矫硷拷锟斤拷锟斤拷锟斤拷要锟斤拷锟斤拷锟矫碉拷锟侥憋拷锟斤拷
		viewPager.setOnPageChangeListener(new PageChange());
		// 锟斤拷锟斤拷ViewPager锟斤拷默锟斤拷锟斤拷,
		// 锟斤拷锟斤拷为锟斤拷锟饺碉拷100锟斤拷锟斤拷锟斤拷锟斤拷锟接匡拷始锟斤拷锟斤拷锟斤拷锟襟滑讹拷
		viewPager.setCurrentItem((mImageViews.length) * 100);
		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

	}

	public static void displayImage(String uri, ImageView imageView,
			DisplayImageOptions options) {

		getUri = uri;
		ImageLoader.getInstance().displayImage(uri, imageView, options);

	}

	@Override
	public void onBackPressed() {
		imageLoader.stop(); // 停止锟斤拷锟斤拷图片
		super.onBackPressed();
		// onBackPressed_local(this);
	}

	/**
	 * 锟斤拷一锟斤拷锟竭程帮拷assert目录锟铰碉拷图片锟斤拷锟狡碉拷SD锟斤拷目录锟斤拷
	 * 
	 * @param testImageOnSdCard
	 */
	private void copyTestImageToSdCard(final File testImageOnSdCard) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream is = getAssets().open(TEST_FILE_NAME);
					FileOutputStream fos = new FileOutputStream(
							testImageOnSdCard);
					byte[] buffer = new byte[8192];
					int read;
					try {
						while ((read = is.read(buffer)) != -1) {
							fos.write(buffer, 0, read); // 写锟斤拷锟斤拷锟斤拷锟�
						}
					} finally {
						fos.flush(); // 写锟斤拷SD锟斤拷
						fos.close(); // 锟截憋拷锟斤拷锟斤拷锟�
						is.close(); // 锟截憋拷锟斤拷锟斤拷锟斤拷
					}
				} catch (IOException e) {
					L.w("Can't copy test image onto SD card");
				}
			}
		}).start(); // 锟斤拷锟斤拷锟竭筹拷
	}

	@Override
	protected void onStart() {
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
				// 锟斤拷锟斤拷
				handler.sendMessage(ms);
				select++;
			}
		}
	}

	@Override
	protected void onDestroy() {
		// 锟斤拷Activity锟斤拷锟缴硷拷锟斤拷时锟斤拷停止锟叫伙拷
		sechExecutorService.shutdown();
		super.onDestroy();
	}

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
			// System.out.println("装锟斤拷"+position);
			((ViewPager) container).addView(mImageViews[position
					% mImageViews.length], 0);
			return mImageViews[position % mImageViews.length];
		}
	}

	public class PageChange implements OnPageChangeListener {

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

	private void initViews() {
		sy_layout = findViewById(R.id.sy_layout);
		gwc_layout = findViewById(R.id.gwc_layout);
		ss_layout = findViewById(R.id.ss_layout);
		fl_layout = findViewById(R.id.fl_layout);
		mywy_layout = findViewById(R.id.mywy_layout);
		image1 = (ImageView) findViewById(R.id.image1);
		dbgwc = (ImageView) findViewById(R.id.dbgwc);
		image3 = (ImageView) findViewById(R.id.image3);
		image4 = (ImageView) findViewById(R.id.image4);
		image5 = (ImageView) findViewById(R.id.image5);
		sy_layout.setOnClickListener(this);
		gwc_layout.setOnClickListener(this);
		ss_layout.setOnClickListener(this);
		fl_layout.setOnClickListener(this);
		mywy_layout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sy_layout:
			setTabSelection(0);
			break;
		case R.id.gwc_layout:
			setTabSelection(1);
			break;
		case R.id.ss_layout:
			setTabSelection(2);
			break;
		case R.id.fl_layout:
			setTabSelection(3);
			break;
		case R.id.mywy_layout:
			setTabSelection(4);
			break;
		default:
			break;
		}

	}

	private void setTabSelection(int index) {
		clearSelection();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 锟斤拷锟斤拷锟截碉拷锟斤拷锟叫碉拷Fragment锟斤拷锟皆凤拷止锟叫讹拷锟紽ragment锟斤拷示锟节斤拷锟斤拷锟较碉拷锟斤拷锟�
		hideFragments(transaction);
		switch (index) {
		case 0:
			// 锟斤拷锟斤拷锟斤拷锟斤拷锟较ab时锟斤拷锟侥憋拷丶锟斤拷锟酵计拷锟斤拷锟斤拷锟斤拷锟缴�
			image1.setImageResource(R.drawable.menu_1_down);
			if (fragSy == null) {
				// 锟斤拷锟組essageFragment为锟秸ｏ拷锟津创斤拷一锟斤拷锟斤拷锟斤拷拥锟斤拷锟斤拷锟斤拷锟�
				fragSy = new fragSy();
				transaction.add(R.id.frament, fragSy);
			} else {
				// 锟斤拷锟組essageFragment锟斤拷为锟秸ｏ拷锟斤拷直锟接斤拷锟斤拷锟斤拷示锟斤拷锟斤拷
				transaction.show(fragSy);
			}
			break;
		case 1:
			// 锟斤拷锟斤拷锟斤拷锟斤拷锟较ab时锟斤拷锟侥憋拷丶锟斤拷锟酵计拷锟斤拷锟斤拷锟斤拷锟缴�
			dbgwc.setImageResource(R.drawable.menu_2_down);
			if (fragGwc == null) {
				// 锟斤拷锟組essageFragment为锟秸ｏ拷锟津创斤拷一锟斤拷锟斤拷锟斤拷拥锟斤拷锟斤拷锟斤拷锟�
				fragGwc = new fragGwc();
				transaction.add(R.id.frament, fragGwc);
			} else {
				// 锟斤拷锟組essageFragment锟斤拷为锟秸ｏ拷锟斤拷直锟接斤拷锟斤拷锟斤拷示锟斤拷锟斤拷
				transaction.show(fragGwc);
			}
			break;
		case 2:
			// 锟斤拷锟斤拷锟斤拷锟斤拷锟较ab时锟斤拷锟侥憋拷丶锟斤拷锟酵计拷锟斤拷锟斤拷锟斤拷锟缴�
			image3.setImageResource(R.drawable.menu_3_down);
			if (fragsearch == null) {
				// 锟斤拷锟組essageFragment为锟秸ｏ拷锟津创斤拷一锟斤拷锟斤拷锟斤拷拥锟斤拷锟斤拷锟斤拷锟�
				fragsearch = new fragsearch();
				transaction.add(R.id.frament, fragsearch);
			} else {
				// 锟斤拷锟組essageFragment锟斤拷为锟秸ｏ拷锟斤拷直锟接斤拷锟斤拷锟斤拷示锟斤拷锟斤拷
				transaction.show(fragsearch);
			}
			break;
		case 3:
			// 锟斤拷锟斤拷锟斤拷锟斤拷锟较ab时锟斤拷锟侥憋拷丶锟斤拷锟酵计拷锟斤拷锟斤拷锟斤拷锟缴�
			image4.setImageResource(R.drawable.menu_4_down);
			if (fragFl == null) {
				// 锟斤拷锟組essageFragment为锟秸ｏ拷锟津创斤拷一锟斤拷锟斤拷锟斤拷拥锟斤拷锟斤拷锟斤拷锟�
				fragFl = new fragFl();
				transaction.add(R.id.frament, fragFl);
			} else {
				// 锟斤拷锟組essageFragment锟斤拷为锟秸ｏ拷锟斤拷直锟接斤拷锟斤拷锟斤拷示锟斤拷锟斤拷
				transaction.show(fragFl);
			}
			break;
		case 4:
			// 锟斤拷锟斤拷锟斤拷锟斤拷锟较ab时锟斤拷锟侥憋拷丶锟斤拷锟酵计拷锟斤拷锟斤拷锟斤拷锟缴�
			image5.setImageResource(R.drawable.menu_5_down);
			if (fragMyYw == null) {
				// 锟斤拷锟組essageFragment为锟秸ｏ拷锟津创斤拷一锟斤拷锟斤拷锟斤拷拥锟斤拷锟斤拷锟斤拷锟�
				fragMyYw = new fragMyYw();
				transaction.add(R.id.frament, fragMyYw);
			} else {
				// 锟斤拷锟組essageFragment锟斤拷为锟秸ｏ拷锟斤拷直锟接斤拷锟斤拷锟斤拷示锟斤拷锟斤拷
				transaction.show(fragMyYw);
			}
			break;
		}
		transaction.commit();
	}
	private void clearSelection() {
		image1.setImageResource(R.drawable.menu_1);

		dbgwc.setImageResource(R.drawable.menu_2);

		image3.setImageResource(R.drawable.menu_3);

		image4.setImageResource(R.drawable.menu_4);

		image5.setImageResource(R.drawable.menu_5);
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (fragSy != null) {
			transaction.hide(fragSy);
		}
		if (fragGwc != null) {
			transaction.hide(fragGwc);
		}
		if (fragsearch != null) {
			transaction.hide(fragsearch);
		}
		if (fragFl != null) {
			transaction.hide(fragFl);
		}
		if (fragMyYw != null) {
			transaction.hide(fragMyYw);
		}
	}

	public void onBackPressed_local(final Activity context) {
		Dialog dialog = new AlertDialog.Builder(context)
				.setTitle("您确定要退出系统吗?")
				// 设置标题
				.setMessage("系统提示")
				// 设置内容
				.setPositiveButton("确定", // 设置确定按钮
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								finish();
							}
						})
				.setNeutralButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) { // 点击"退出"按钮之后推出程序
						return;
					}
				}).create(); // 创建按钮
		dialog.show(); // 显示对话框
	}
	private static Boolean isExit = false;
	 private static Boolean hasTask = false;
	 Timer tExit = new Timer();
	 TimerTask task = new TimerTask() {
	  @Override
	  public void run() {
	   isExit = false;
	   hasTask = true;
	  }
	 };
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  if (keyCode == KeyEvent.KEYCODE_BACK) {
	   if (isExit == false) {
	    isExit = true;
	    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
	    if (!hasTask) {
	     tExit.schedule(task, 2000);
	    }
	   } else {
	    finish();
	    // 程序正常关闭
	    System.exit(0);
	   }
	  }
	  return true;
	 }
}