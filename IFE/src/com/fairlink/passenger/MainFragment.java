package com.fairlink.passenger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.analytics.Analytics;
import com.fairlink.analytics.AnalyticsType;
import com.fairlink.passenger.about.AboutMainActivity;
import com.fairlink.passenger.adapter.ModuleListAdapter;
import com.fairlink.passenger.application.Application;
import com.fairlink.passenger.application.ApplicationManager;
import com.fairlink.passenger.application.InstalledApplication;
import com.fairlink.passenger.bean.DynamicType;
import com.fairlink.passenger.bean.RecVideo;
import com.fairlink.passenger.book.BookDetailActivity;
import com.fairlink.passenger.book.BookMainViewActivity;
import com.fairlink.passenger.customer.CustomerMainActivity;
import com.fairlink.passenger.game.GameMainActivity;
import com.fairlink.passenger.music.MusicCategoryActivity;
import com.fairlink.passenger.networkrequest.ADNewPicRequest;
import com.fairlink.passenger.networkrequest.ADNewPicRequest.ADNEW;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.DynamicTypeByParentIdRequest;
import com.fairlink.passenger.networkrequest.FlightinfoRequest;
import com.fairlink.passenger.networkrequest.FlightinfoRequest.FlightInfo;
import com.fairlink.passenger.networkrequest.GetCartRequest;
import com.fairlink.passenger.networkrequest.MovieDetailRequest;
import com.fairlink.passenger.networkrequest.MovieDetailRequest.MovieDetail;
import com.fairlink.passenger.networkrequest.MovieListRequest.MovieList;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.networkrequest.OrderRecordRequest;
import com.fairlink.passenger.networkrequest.PhotoManager;
import com.fairlink.passenger.plane.PlaneStatusActivity;
import com.fairlink.passenger.setting.SettingMainActivity;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.Constant;
import com.fairlink.passenger.util.PackageUtils;
import com.fairlink.passenger.util.ShopUtil;
import com.fairlink.passenger.video.MovieCategoryActivity;
import com.fairlink.passenger.view.BatteryView;
import com.fairlink.passenger.view.CustomViewSwitcher;
import com.fairlink.passenger.view.DialogCom;
import com.fairlink.passenger.view.DialogLoading;
import com.fairlink.passenger.view.TipButton;

public class MainFragment extends BaseFragment implements HttpTaskCallback, NetworkRequestAPI, Runnable,
		LaunchModuleListener {

	private static final int ROOLING_TIME_SPAN = 5000;
	private static final String BASE_URL_KEY = "baseUrl";
	private View mWarningView;
	private TextView mTime;
	private int mCurrentTime;

	private TextView tvFlight;

	private List<CustomViewSwitcher> rollingSlots = new ArrayList<CustomViewSwitcher>();
	private GridView moduleSlots;

	private ImageView imgWeather; // 天气
	private TextView txtWeather;

	private DialogLoading diaLoading;

	private BatteryView battery_view;
	private ADNEW ads;

	Context mContext;

	private boolean isDestroying = false;

	private Handler rollHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			for (CustomViewSwitcher slot : rollingSlots) {
				slot.showNext();
			}
			sendEmptyMessageDelayed(1, ROOLING_TIME_SPAN);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mContext = inflater.getContext();

		View view = inflater.inflate(R.layout.main_fragment, container, false);

		diaLoading = new DialogLoading(mContext);

		initView(view);

		if (!IFEApplication.getInstance().getOfflineStatus()) {
			new FlightinfoRequest(this).execute((String) null);
			new GetCartRequest(this).execute((String) null);
			new OrderRecordRequest(this).execute((String) null);
		}

		getIndexDynamicTypes();

		return view;
	}

	private void initView(View parentView) {
		battery_view = (BatteryView) parentView.findViewById(R.id.battery_view);
		tvFlight = (TextView) parentView.findViewById(R.id.tv_flight);

		rollingSlots.add(new CustomViewSwitcher(this, (ImageSwitcher) parentView.findViewById(R.id.index_slot1)));
		rollingSlots.add(new CustomViewSwitcher(this, (ImageSwitcher) parentView.findViewById(R.id.index_slot2)));
		rollingSlots.add(new CustomViewSwitcher(this, (ImageSwitcher) parentView.findViewById(R.id.index_slot3)));
		rollingSlots.add(new CustomViewSwitcher(this, (ImageSwitcher) parentView.findViewById(R.id.index_slot4)));
		moduleSlots = (GridView) parentView.findViewById(R.id.module_layout);

		txtWeather = (TextView) parentView.findViewById(R.id.txt_weather);
		imgWeather = (ImageView) parentView.findViewById(R.id.img_weather);
		mWarningView = parentView.findViewById(R.id.pic_ad);
		mWarningView.setVisibility(View.VISIBLE);

		mTime = (TextView) mWarningView.findViewById(R.id.time);

		View warninPic = mWarningView.findViewById(R.id.warning_pic);
		warninPic.setOnTouchListener(warningListener);
		warninPic.setFocusable(true);
		warninPic.setClickable(true);
		warninPic.setLongClickable(true);
		warninPic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCurrentTime <= 0)
					mWarningView.setVisibility(View.GONE);
			}
		});

		mCurrentTime = Constant.AD_TIME;
		setSpannableString();
		mTime.postDelayed(this, 1000);

		parentView.findViewById(R.id.img_set).setOnClickListener(settingListener);
	}

	private void initTypes(List<List<DynamicType>> types) {

		if (types == null || types.isEmpty()) {
			getIndexDynamicTypes();
			return;
		}

		for (int index = 0; index < rollingSlots.size() && index < types.size(); index++) {
			if ((types.get(index).size() > 0) && (types.get(index).get(0).getSlot() <= 4)) {
				boolean isBig = index == 0 || index == 1;
				rollingSlots.get(index).setTypes(types.get(index), isBig);
			}
		}

		List<DynamicType> moduleList = new ArrayList<DynamicType>();
		for (int index = 0; index < types.size(); index++) {
			if ((types.get(index).size() > 0) && (types.get(index).get(0).getSlot() > 4)) {
				moduleList.add(types.get(index).get(0));
			}
		}

		ModuleListAdapter adapter = new ModuleListAdapter(getActivity(), moduleList, this);
		moduleSlots.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void getIndexDynamicTypes() {
		new DynamicTypeByParentIdRequest(0, this).execute((String) null);
	}

	private void setSpannableString() {
		String time = String.format(getString(R.string.djs_time), mCurrentTime);
		SpannableString ss = new SpannableString(time);
		int index = time.indexOf(String.valueOf(mCurrentTime));
		ss.setSpan(new ForegroundColorSpan(Color.YELLOW), index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		mTime.setText(ss);
	}

	@Override
	public void launchModule(DynamicType type) {

		if (type == null) {
			logger.error("receive invalid type");
			return;
		}

		if (ComUtil.isFastDoubleClick()) {
			return;
		}

		if (type.isLaunchByResouceId()) {

			Intent i = null;

			if (type.isModule(DynamicType.MODULE_MOVIE)) {
				i = new Intent(getActivity(), MovieCategoryActivity.class);
			} else if (type.isModule(DynamicType.MODULE_MUSIC)) {
				i = new Intent(getActivity(), MusicCategoryActivity.class);
			} else if (type.isModule(DynamicType.MODULE_EBOOK)) {
				i = new Intent(getActivity(), BookDetailActivity.class);
			}

			if (null != i) {
				i.putExtra("resourceId", type.getResourceId());
				getActivity().startActivity(i);
				return;
			}

			if (ShopUtil.FLC_GoodsMall.equals(type.getType()) || ShopUtil.Spring_GoodsMall.equals(type.getType())) {
				ShopUtil.showGoodsDetail(getActivity(), type.getResourceId());
				return;
			}
		} else {
			if (type.isModule(DynamicType.MODULE_MOVIE)) {
				launchMovie();
				return;
			} else if (type.isModule(DynamicType.MODULE_MUSIC)) {
				launchMusic();
				return;
			} else if (type.isModule(DynamicType.MODULE_EBOOK)) {
				launchEbook();
				return;
			} else if (type.isModule(ShopUtil.FLC_GoodsMall)) {
				launchFLC();
				return;
			} else if (type.isModule(ShopUtil.Spring_GoodsMall)) {
				launchSSS();
				return;
			} else if (type.isModule(DynamicType.MODULE_AIRENV)) {
				launchAirenv();
				return;
			} else if (type.isModule(DynamicType.MODULE_ABOUT)) {
				launchAbout();
				return;
			} else if (type.isModule(DynamicType.MODULE_GAME)) {
				launchGame();
				return;
			} else if (type.isModule(DynamicType.MODULE_CUSTOMER)) {
				launchCustomer();
				return;
			} else if (type.isModule(DynamicType.MODULE_NEWS)) {
				launchNews();
				return;
			}
		}
		return;
	}

	private void launchMovie() {
		ShopUtil.startActivityWithAd(mContext, MovieCategoryActivity.class, ads);

		Analytics.logEvent(getActivity(), AnalyticsType.getOperationVideoMus(1), AnalyticsType.ORIGIN_VIDEO,
				AnalyticsType.cperationData(null, null));
	}

	private void launchMusic() {
		ShopUtil.startActivityWithAd(mContext, MusicCategoryActivity.class, ads);
	}

	private void launchEbook() {
		ShopUtil.startActivityWithAd(mContext, BookMainViewActivity.class, ads);

		Analytics.logEvent(getActivity(), AnalyticsType.getOperationEbook(1, 0), AnalyticsType.ORIGIN_EBOOK,
				AnalyticsType.cperationData(null, null));
	}

	private void launchFLC() {
		ShopUtil.showShopMallWithAD(mContext, ShopUtil.FLC_GoodsMall, ads);
		Analytics.logEvent(getActivity(), AnalyticsType.getOperationShop(1), AnalyticsType.ORIGIN_MALL_FLC,
				AnalyticsType.cperationData(null, null));
	}

	private void launchSSS() {
		ShopUtil.showShopMallWithAD(mContext, ShopUtil.Spring_GoodsMall, ads);
		Analytics.logEvent(getActivity(), AnalyticsType.getOperationShop(1), AnalyticsType.ORIGIN_MALL_SSS,
				AnalyticsType.cperationData(null, null));
	}

	private void launchAirenv() {
		startActivity(new Intent(mContext, PlaneStatusActivity.class));
	}

	private void launchAbout() {
		ShopUtil.startActivityWithAd(mContext, AboutMainActivity.class, ads);
	}

	private void launchGame() {
		ShopUtil.startActivityWithAd(mContext, GameMainActivity.class, ads);

		Analytics.logEvent(getActivity(), AnalyticsType.getOperationGame(1), AnalyticsType.ORIGIN_GAME,
				AnalyticsType.cperationData(null, null));
	}

	private void launchCustomer() {
		ShopUtil.startActivityWithAd(mContext, CustomerMainActivity.class, ads);
	}

	private void launchNews() {
		Intent i;
		String newsPackage = "";
		List<InstalledApplication> applicaitonList = ApplicationManager.getInstence().applicationList();
		for (Application application : applicaitonList) {
			if (application.getType().equals("news") && application.getCategory().equals("news")) {
				newsPackage = application.getComponentName();
				break;
			}
		}

		if (newsPackage == null || newsPackage.isEmpty()) {
			DialogCom.DiaCom(mContext, "新闻不存在");
			return;
		}

		if (PackageUtils.checkApkExist(mContext, newsPackage)) {
			i = new Intent();
			i = mContext.getPackageManager().getLaunchIntentForPackage(newsPackage);
			i.putExtra(BASE_URL_KEY, IFEApplication.getInstance().getValue("BASE_IP"));
			startActivity(i);
		} else {
			DialogCom.DiaCom(mContext, "新闻不存在");
		}
	}

	public void onResume() {
		super.onResume();
		battery_register();
		new ADNewPicRequest(MainFragment.this, ADNewPicRequest.ADS_TYPE_MODULAR).execute((String) null);

		rollHandler.sendEmptyMessageDelayed(1, ROOLING_TIME_SPAN);
	}

	public void onPause() {
		super.onPause();
		battery_unregister();

		rollHandler.removeMessages(1);
	}

	@Override
	public void onDestroyView() {
		isDestroying = true;

		if (diaLoading != null) {
			diaLoading.dismiss();
		}
		super.onDestroyView();

		FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
		ft.remove(getFragmentManager().findFragmentById(R.id.cart_tip_button));
		ft.remove(getFragmentManager().findFragmentById(R.id.order_tip_button));
		ft.commit();
	}

	private View.OnClickListener settingListener = new View.OnClickListener() {
		public void onClick(View v) {
			if (ComUtil.isFastDoubleClick()) {
				return;
			}

			Intent i = new Intent(mContext, SettingMainActivity.class);
			startActivity(i);

		}
	};

	private View.OnTouchListener warningListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setAlpha(0.5f);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setAlpha(1.0f);
			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				v.setAlpha(1.0f);
			}
			Long l = event.getEventTime() - event.getDownTime();
			if (l <= 500)
				return false;
			else
				return true;
		}
	};

	@Override
	public void onGetResult(int requestType, Object result) {

		if (isDestroying) {
			return;
		}

		if (requestType == ADS_PIC_API) {

			if (result == null) {
				return;
			} else {
				final ADNEW tempData = (ADNEW) result;
				String pic = PhotoManager.getInstance().getImageFile(tempData.adsPath);
				if (pic == null) {
					PhotoManager.getInstance().downloadImage(tempData.adsPath, new PhotoManager.PhotoDownloadCallback() {

						@Override
						public void onPhotoDownload(String url, String path) {
							ads = tempData;
						}
						
						@Override
						public void onPhotoDownloadError(String url, String path) {
							// TODO Auto-generated method stub
							
						}
						
					});
				} else {
					ads = tempData;
				}
			}

		} else if (requestType == WEATHER_API) {
			if (result == null) {
				return;
			}
			String js = (String) result;
			String[] jl = js.split(";");
			txtWeather.setText(jl[0]);
			int r = getWeatherResByName(jl[1]);
			if (r > 0)
				imgWeather.setImageResource(r);
		} else if (requestType == FlIGHT_INFO_API) {
			if (result == null) {
				return;
			}
			FlightInfo flightInfo = (FlightInfo) result;
			tvFlight.setText(String.format(getResources().getString(R.string.flight_mian), flightInfo.fligNo, flightInfo.fligLaunchCity + "~"
					+ flightInfo.fligLandingCity));
			if (flightInfo.weather != null) {
				txtWeather.setText(flightInfo.fligLandingCity + "\r\n" + flightInfo.weather.temperature + "\r\n" + flightInfo.weather.weather);
			    AssetManager am = mContext.getAssets();  
				try {
				    InputStream is = am.open("weather/"+flightInfo.weather.dayPictureUrl+".png");
				    imgWeather.setImageBitmap(BitmapFactory.decodeStream(is));
				} catch (IOException e) {
					e.printStackTrace();
				}  
			}
		} else if (requestType == MOVIE_TV_DETAIL_API) {

			final MovieDetail mMovieDetail = (MovieDetail) result;
			if (mMovieDetail != null) {
				RecVideo recVideo = new RecVideo(mMovieDetail.name, mMovieDetail.id, 0, mMovieDetail.items.get(0).location, mMovieDetail.image, 0);
				IFEApplication.getInstance().setVideo(recVideo);

				PhotoManager.getInstance().downloadImage(mMovieDetail.image, new PhotoManager.PhotoDownloadCallback() {

					@Override
					public void onPhotoDownload(String url, String path) {
						IFEApplication.getInstance().getVideo().setImg(path);
					}
					
					@Override
					public void onPhotoDownloadError(String url, String path) {
						// TODO Auto-generated method stub
						
					}

				});
			}
		} else if (requestType == MOVIE_TV_LIST_API) {
			MovieList list = (MovieList) result;
			if (list.items.size() > 0) {
				new MovieDetailRequest(getActivity(), this, list.items.get(0).id).execute((String) null);
			}
		} else if (requestType == CART_GET_GOODS_API) {
			((TipButton) getFragmentManager().findFragmentById(R.id.cart_tip_button)).update();
		} else if (requestType == ORDER_RECORD_API) {
			((TipButton) getFragmentManager().findFragmentById(R.id.order_tip_button)).update();
		} else if (requestType == DYNAMIC_TYPE_BY_PARENT_ID_API) {
			initTypes((List<List<DynamicType>>) result);
		}
	}

	private int getWeatherResByName(String name) {
		int r = -1;
		if (name.contains("daxue")) {
			r = R.drawable.daxue;
		} else if (name.contains("dayu")) {
			r = R.drawable.dayu;
		} else if (name.contains("duoyun")) {
			r = R.drawable.duoyun;
		} else if (name.contains("qing")) {
			r = R.drawable.qing;
		} else if (name.contains("wu")) {
			r = R.drawable.wu;
		} else if (name.contains("xiaoxue")) {
			r = R.drawable.xiaoxue;
		} else if (name.contains("yin")) {
			r = R.drawable.yin;
		}
		return r;
	}

	@Override
	public void onError(int requestType) {
		if (isDestroying) {
			return;
		}
		diaLoading.hide();
		if (requestType != REDIRECT_API) {
			ComUtil.toastText(getActivity(), "连接服务器出错", Toast.LENGTH_SHORT);
		}
	}

	private BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
				int level = intent.getIntExtra("level", 0);
				int scale = intent.getIntExtra("scale", 100);
				int power = level * 100 / scale;
				battery_view.setPower(power);
			}
		}
	};

	private void battery_register() {
		getActivity().registerReceiver(batteryChangedReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}

	private void battery_unregister() {
		getActivity().unregisterReceiver(batteryChangedReceiver);
	}

	@Override
	public void run() {
		mCurrentTime--;
		setSpannableString();
		if (mCurrentTime > 0)
			mTime.postDelayed(this, 1000);
	}
}
