package com.fairlink.passenger.video;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.analytics.Analytics;
import com.fairlink.analytics.AnalyticsType;
import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.R;
import com.fairlink.passenger.bean.RecVideo;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.MovieDetailRequest;
import com.fairlink.passenger.networkrequest.MovieDetailRequest.MovieDetail;
import com.fairlink.passenger.networkrequest.MovieDetailRequest.MoviePlayerItem;
import com.fairlink.passenger.networkrequest.MovieListRequest;
import com.fairlink.passenger.networkrequest.MovieListRequest.MovieInfo;
import com.fairlink.passenger.networkrequest.MovieListRequest.MovieList;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.networkrequest.PhotoManager;
import com.fairlink.passenger.networkrequest.PhotoManager.PhotoDownloadCallback;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.Constant;
import com.fairlink.passenger.util.ImageUtil;
import com.fairlink.passenger.view.DialogLoading;
import com.fairlink.passenger.view.HorizontalListView;

public class BaseVideoDetailFragment extends BaseFragment implements OnClickListener, HttpTaskCallback, PhotoDownloadCallback, OnItemClickListener,
		OnPageChangeListener, NetworkRequestAPI {

	public static interface MovieDetailQuitListener {
		public void onMovieDetailQuit(int current);
	}

	private RelativeLayout relInfo;
	private ImageView mMovieImg;
	// private TextView mTitle;
	private TextView mMovieBasic, mTVBasic;
	private TextView mMovieContent;
	private ViewPager mTVPlayList;
	private LinearLayout mPages, mMoviePlayList;
	private HorizontalListView mMovieList;

	private ArrayList<MovieInfo> mAll;
	private MovieDetail mMovieDetail;
	private MovieListAdapter mAdapter;

	private LayoutInflater mInflater;
	private static final int ITEM_COUNT = 6;
	private static final int PAGE_ITEMS_COUNT = 40;
	private int mCurrentPage = 1;
	private int mType = 0;
	private TVPopWin pw;

	private DialogLoading diaLoading;

	// private ArrayList<View> mTVPagesView = new ArrayList<View>();
	private TVPlayListAdapter mTVAdapter;
	private boolean mStartPlay = false;
	private TextView movie_top_name;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		diaLoading = new DialogLoading(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.movie_detail, null);
		pw = new TVPopWin(getActivity());

		relInfo = (RelativeLayout) view.findViewById(R.id.movie_info);
		relInfo.setVisibility(View.INVISIBLE);

		mMovieImg = (ImageView) view.findViewById(R.id.movie_img);

		// mTitle = (TextView) view.findViewById(R.id.title);
		mMovieBasic = (TextView) view.findViewById(R.id.movie_basic_info);
		mTVBasic = (TextView) view.findViewById(R.id.tv_basic_info);
		mMovieContent = (TextView) view.findViewById(R.id.movie_content);
		mTVPlayList = (ViewPager) view.findViewById(R.id.tv_play_list);
		mTVAdapter = new TVPlayListAdapter();
		mTVPlayList.setAdapter(mTVAdapter);
		mPages = (LinearLayout) view.findViewById(R.id.pages);
		mMoviePlayList = (LinearLayout) view.findViewById(R.id.movie_play_list);
		mMovieList = (HorizontalListView) view.findViewById(R.id.movie_list);
		movie_top_name = (TextView) view.findViewById(R.id.movie_top_name);
		
		mAdapter = new MovieListAdapter();
		mMovieList.setAdapter(mAdapter);
		mMovieList.setOnItemClickListener(this);
		mTVPlayList.setOnPageChangeListener(this);
		mInflater = inflater;

		return view;
	}

	public void onResume() {
		super.onResume();
		mStartPlay = false;
	}

	public void onDestroy() {

		if (diaLoading != null) {
			diaLoading.dismiss();
		}
		super.onDestroy();
	}

	public void setPlayId(int playId) {
		mMovieList.setSelection(playId);
	}

	public void setMovieInfo(String id) {
		if (mMovieDetail == null || !id.equals(mMovieDetail.id)) {

			relInfo.setVisibility(View.INVISIBLE);
			new MovieDetailRequest(getActivity(), this, id).execute((String) null);
			diaLoading.show();
		}
	}

	public void setMenu(int type) {
		mCurrentPage = 1;

		mType = type;
		loadPage(type);
		mAll = null;

	}

	private void loadPage(int type) {
		new MovieListRequest(getActivity(), this, type, mCurrentPage, PAGE_ITEMS_COUNT).execute((String) null);
	}

	@Override
	public void onClick(View v) {
		if (!mStartPlay) {
			getActivity().sendBroadcast(new Intent(Constant.Action.ACTION_PLAY_VIDEO));
			String location = (String) v.getTag();
			String imgVideo = PhotoManager.getInstance().getImageFile(mMovieDetail.image);
			Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
			intent.putExtra("name", mMovieDetail.name);
			intent.putExtra("id", mMovieDetail.id);
			intent.putExtra("type", mType);
			intent.putExtra("url", location);
			intent.putExtra("img", imgVideo);

			RecVideo video = IFEApplication.getInstance().getVideo();
			if ((null != video) && (mMovieDetail.id).equals(video.getId())) {
				intent.putExtra("position", video.getPosition());
			} else {
				intent.putExtra("position", 0);
			}

			startActivity(intent);

			mStartPlay = true;
			// 记录视频播放
			Analytics.logEvent(getActivity(), AnalyticsType.getOperationVideoMus(4), AnalyticsType.ORIGIN_VIDEO,
					AnalyticsType.cperationData(ComUtil.getVideoType(mType), mMovieDetail.name));

		}
	}

	@Override
	public void onGetResult(int requestType, Object result) {

		diaLoading.hide();
		if (result != null) {
			if (requestType == NetworkRequestAPI.MOVIE_TV_DETAIL_API) {
				mMovieDetail = (MovieDetail) result;
				initView();
			} else if (requestType == NetworkRequestAPI.MOVIE_TV_LIST_API) {
				MovieList list = (MovieList) result;
				if (mAll != null) {
					mAll.addAll(list.items);
				} else {
					mAll = list.items;
				}
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onError(int requestType) {
		diaLoading.hide();
		if (requestType != REDIRECT_API) {
			ComUtil.toastText(getActivity(), "连接服务器出错", Toast.LENGTH_SHORT);
		}
	}

	private void initView() {

		relInfo.setVisibility(View.VISIBLE);

		String path = PhotoManager.getInstance().getImageFile(mMovieDetail.image);
		if (path != null) {
			Bitmap bmp = PhotoManager.getInstance().decodePhoto(path, mMovieImg.getWidth(), mMovieImg.getHeight());
			if (bmp != null) {
				mMovieImg.setScaleType(ScaleType.FIT_XY);
				mMovieImg.setImageBitmap(bmp);
			} else {
				mMovieImg.setScaleType(ScaleType.FIT_CENTER);
				mMovieImg.setImageResource(R.drawable.default_400);
			}

		} else {
			mMovieImg.setScaleType(ScaleType.FIT_CENTER);
			mMovieImg.setImageResource(R.drawable.default_400);
			PhotoManager.getInstance().downloadImage(mMovieDetail.image, this);
		}
		// mTitle.setText(mMovieDetail.name);
		StringBuilder sb = new StringBuilder();
		if (!TextUtils.isEmpty(mMovieDetail.name)) {
			sb.append(getString(R.string.movie_name) + mMovieDetail.name + "\n");
		}
		if (!TextUtils.isEmpty(mMovieDetail.director)) {
			sb.append(getString(R.string.director) + mMovieDetail.director + "\n");
		}
		if (!TextUtils.isEmpty(mMovieDetail.area)) {
			sb.append(getString(R.string.region) + mMovieDetail.area + "\n");
		}
		if (!TextUtils.isEmpty(mMovieDetail.actor)) {
			sb.append(getString(R.string.actor) + mMovieDetail.actor + "\n");
		}

		TextView v;
		if (mMovieDetail.isMovice.equals("1")) {

			mTVBasic.setVisibility(View.GONE);
			mMovieBasic.setVisibility(View.VISIBLE);
			v = mMovieBasic;

		} else {

			mMovieBasic.setVisibility(View.GONE);
			mTVBasic.setVisibility(View.VISIBLE);
			v = mTVBasic;
		}
		v.setText(sb.toString());
		movie_top_name.setText(mMovieDetail.name.toString());
		mMovieContent.setText(mMovieDetail.content);
		setPlayList();
	}

	private void setPlayList() {
		// mPlayList1.removeAllViews();
		// mPlayList2.removeAllViews();
		// mTVPagesView.clear();
		// mTVPlayList.removeAllViews();
		mPages.removeAllViews();
		mMoviePlayList.removeAllViews();

		MoviePlayerItem item;
		View view = null;
		TextView button;
		LayoutInflater inflater = getActivity().getLayoutInflater();

		int len = mMovieDetail.items.size();
		// if(mMovieDetail.isMovice.equals("0")) {
		if (len > 1) {
			// mPlayList1.setVisibility(View.VISIBLE);
			// mPlayList2.setVisibility(View.VISIBLE);
			mTVPlayList.setVisibility(View.VISIBLE);
			mPages.setVisibility(View.VISIBLE);
			mMoviePlayList.setVisibility(View.INVISIBLE);

			int pagenumber = len / (2 * ITEM_COUNT);
			if (len % (2 * ITEM_COUNT) != 0) {
				pagenumber++;
			}
			ImageView page;
			LinearLayout playlist1, playlist2;
			int start;
			View playlayout;
			for (int i = 0; i < pagenumber; i++) {
				view = inflater.inflate(R.layout.tv_page_item, null);
				page = (ImageView) view.findViewById(R.id.page);
				mPages.addView(view);
				if (i == 0) {
					page.setImageResource(R.drawable.login_circle_enter);
				} else {
					page.setImageResource(R.drawable.login_circle);
				}

				// view = inflater.inflate(R.layout.tv_playlist_page, null);
				// playlist1 = (LinearLayout)
				// view.findViewById(R.id.play_list1);
				// playlist2 = (LinearLayout)
				// view.findViewById(R.id.play_list2);
				// start = i*(2*ITEM_COUNT);
				// for(int j=start; j<start+ITEM_COUNT&&j<len; j++) {
				// item = mMovieDetail.items.get(j);
				//
				// playlayout = inflater.inflate(R.layout.video_play_layout,
				// null);
				// button = (TextView) playlayout.findViewById(R.id.item);
				// button.setOnClickListener(this);
				// button.setTag(item.location);
				// button.setText(String.valueOf(j+1));
				// playlist1.addView(playlayout);
				// }
				// if(start+ITEM_COUNT < len) {
				// start = start+ITEM_COUNT;
				// for(int j=start; j<start+ITEM_COUNT&&j<len; j++) {
				// item = mMovieDetail.items.get(j);
				//
				// playlayout = inflater.inflate(R.layout.video_play_layout,
				// null);
				// button = (TextView) playlayout.findViewById(R.id.item);
				// button.setOnClickListener(this);
				// button.setTag(item.location);
				// button.setText(String.valueOf(j+1));
				// playlist2.addView(playlayout);
				// }
				// }
				// mTVPagesView.add(view);
			}

			if (len <= ITEM_COUNT) {
				// mPlayList2.setVisibility(View.GONE);
				mPages.setVisibility(View.GONE);
			} else if (len <= 2 * ITEM_COUNT) {
				mPages.setVisibility(View.GONE);
			}

			// mTVPlayList.setAdapter(new TVPlayListAdapter());
			mTVPlayList.setCurrentItem(0);
			mTVAdapter.notifyDataSetChanged();
		} else {
			mMoviePlayList.setVisibility(View.VISIBLE);
			// mPlayList1.setVisibility(View.INVISIBLE);
			// mPlayList2.setVisibility(View.INVISIBLE);
			mTVPlayList.setVisibility(View.INVISIBLE);
			mPages.setVisibility(View.INVISIBLE);

			for (int i = 0; i < ITEM_COUNT && i < len; i++) {
				item = mMovieDetail.items.get(i);

				view = inflater.inflate(R.layout.movie_play_item, null);
				view.setOnClickListener(this);
				view.setTag(item.location);

				mMoviePlayList.addView(view);
			}
		}
	}

	@Override
	public void onPhotoDownload(final String url, final String path) {
		if (mMovieDetail != null && url.equals(mMovieDetail.image)) {
			mMovieImg.setScaleType(ScaleType.FIT_XY);
			mMovieImg.setImageBitmap(PhotoManager.getInstance().decodePhoto(path, mMovieImg.getWidth(), mMovieImg.getHeight()));
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onPhotoDownloadError(String url, String path) {
		// TODO Auto-generated method stub

	}

	class MovieListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mAll == null ? 0 : mAll.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mAll.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView image;
			TextView name;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.movie_detail_item1, null);
			}
			image = (ImageView) convertView.findViewById(R.id.image);
			name = (TextView) convertView.findViewById(R.id.name);
			MovieInfo info = mAll.get(position);
			convertView.setTag(info);

			ImageUtil.setImage(info.image, ImageUtil.SMALL, image, null, null);

			name.setText(info.name);
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		MovieInfo info = (MovieInfo) arg1.getTag();
		setMovieInfo(info.id);
	}

	class TVPlayListAdapter extends PagerAdapter {

		private LayoutInflater inflater;

		public TVPlayListAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public void destroyItem(View v, int position, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) v).removeView((View) arg2);

		}

		@Override
		public int getCount() {
			if (mMovieDetail == null) {
				return 0;
			}
			int len = mMovieDetail.items.size();
			// TODO Auto-generated method stub
			int pagenumber = len / (2 * ITEM_COUNT);
			if (len % (2 * ITEM_COUNT) != 0) {
				pagenumber++;
			}
			return pagenumber;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View v, int position) {
			View view;
			LinearLayout playlist1, playlist2;
			view = inflater.inflate(R.layout.tv_playlist_page, null);
			playlist1 = (LinearLayout) view.findViewById(R.id.play_list1);
			playlist2 = (LinearLayout) view.findViewById(R.id.play_list2);
			int start = position * (2 * ITEM_COUNT);
			MoviePlayerItem item;
			// final TVPopWin pw = new TVPopWin(getActivity());
			int len = mMovieDetail.items.size();
			TextView button;
			View playlayout;

			for (int j = start; j < start + ITEM_COUNT && j < len; j++) {
				item = mMovieDetail.items.get(j);
				playlayout = inflater.inflate(R.layout.video_play_layout, null);
				button = (TextView) playlayout.findViewById(R.id.item);
				button.setOnClickListener(BaseVideoDetailFragment.this);
				button.setTag(item.location);
				button.setText(String.valueOf(j + 1));
				final String num = button.getText().toString();
				button.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						int[] location = new int[2];
						v.getLocationOnScreen(location);
						int x = location[0];
						int y = location[1];
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							pw.ShowWin(num, x - 5, y - v.getHeight() - 10);
						}
						if (event.getAction() == MotionEvent.ACTION_UP) {
							pw.dismiss();
						}
						return false;
					}

				});
				playlist1.addView(playlayout);
			}
			if (start + ITEM_COUNT < len) {
				start = start + ITEM_COUNT;
				for (int j = start; j < start + ITEM_COUNT && j < len; j++) {
					item = mMovieDetail.items.get(j);

					playlayout = inflater.inflate(R.layout.video_play_layout, null);
					button = (TextView) playlayout.findViewById(R.id.item);
					button.setOnClickListener(BaseVideoDetailFragment.this);
					button.setTag(item.location);
					button.setText(String.valueOf(j + 1));
					playlist2.addView(playlayout);
					final String num = button.getText().toString();
					button.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							int[] location = new int[2];
							v.getLocationOnScreen(location);
							int x = location[0];
							int y = location[1];
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								pw.ShowWin(num, x - 5, y - v.getHeight() - 15);
							}
							if (event.getAction() == MotionEvent.ACTION_UP) {
								pw.dismiss();
							}
							return false;
						}

					});
				}
			}
			((ViewPager) v).addView(view);
			return view;
		}

		@Override
		public int getItemPosition(Object object) {

			return POSITION_NONE;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		pw.dismiss();
	}

	@Override
	public void onPageSelected(int arg0) {
		ImageView view;
		for (int i = 0; i < mPages.getChildCount(); i++) {
			view = (ImageView) mPages.getChildAt(i).findViewById(R.id.page);
			if (i == arg0) {
				view.setImageResource(R.drawable.login_circle_enter);
			} else {
				view.setImageResource(R.drawable.login_circle);
			}
		}

	}
}
