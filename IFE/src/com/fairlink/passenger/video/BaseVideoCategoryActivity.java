package com.fairlink.passenger.video;

import java.util.Arrays;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fairlink.analytics.Analytics;
import com.fairlink.analytics.AnalyticsType;
import com.fairlink.passenger.AdvertisementActivity;
import com.fairlink.passenger.R;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.video.BaseVideoDetailFragment.MovieDetailQuitListener;
import com.fairlink.passenger.video.BaseVideoListFragment.MovieSelectedListener;
import com.fairlink.passenger.view.CategoryFragment;
import com.fairlink.passenger.view.CategoryFragment.CategorySelectedListener;

public abstract class BaseVideoCategoryActivity extends AdvertisementActivity implements MovieSelectedListener,
		CategorySelectedListener, MovieDetailQuitListener {
	private Button btnBack;
	private BaseVideoListFragment mListFragment;
	private BaseVideoDetailFragment mDetailFragment;
	private int mCurrentMenu = -1;
	private boolean detailView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_category_layout);
		btnBack = (Button) findViewById(R.id.com_back_main).findViewById(R.id.btn_back);
		mListFragment = (BaseVideoListFragment) getFragmentManager().findFragmentById(R.id.movie_list);
		mDetailFragment = (BaseVideoDetailFragment) getFragmentManager().findFragmentById(R.id.movie_detail);

		CategoryFragment categoryFragment = (CategoryFragment) getFragmentManager().findFragmentById(
				R.id.category_fragment);
		categoryFragment.setTitleStringId(R.string.index_movie_str);
		categoryFragment.setTitleStringTextSizeId(R.dimen.category_little_size);
		categoryFragment.setTitleImageResourceId(R.drawable.category_movie);
		categoryFragment.setCategoryStringListOnly(Arrays.asList(getResources().getStringArray(R.array.type_movie)));

		int movieId = getIntent().getIntExtra("resourceId", 0);
		if (movieId == 0) {
			onCategorySelected(0);
			categoryFragment.setItemChecked(0);
		} else {
			onMovieSelected("", movieId + "", 0, 0);
			detailView = false;
			btnBack.setBackgroundResource(R.drawable.icon_back_main);
		}
	}

	@Override
	public void onMovieSelected(String name, String id, int type, int playId) {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		mDetailFragment.setMovieInfo(id);
		mDetailFragment.setMenu(type);
		mDetailFragment.setPlayId(playId);
		transaction.hide(mListFragment);
		transaction.show(mDetailFragment);
		transaction.commitAllowingStateLoss();

		detailView = true;
		btnBack.setBackgroundResource(R.drawable.icon_back_pre);

		Analytics(type, name);
	}

	private void Analytics(int type, String name) {
		Analytics.logEvent(this, AnalyticsType.getOperationVideoMus(3), AnalyticsType.ORIGIN_VIDEO,
				AnalyticsType.cperationData(ComUtil.getVideoType(type), name));
	}

	@Override
	public void onCategorySelected(int index) {

		if (isFinishing()) {
			return;
		}

		if (index != mCurrentMenu) {
			mCurrentMenu = index;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.hide(mDetailFragment);
			mListFragment.setMovieMenu(index);
			transaction.show(mListFragment);
			transaction.commit();
		}
	}

	@Override
	public void onMovieDetailQuit(int current) {

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.hide(mDetailFragment);
		transaction.show(mListFragment);
		transaction.commit();
	}

	@Override
	public void onBackPressed() {

		if (detailView) {

			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.hide(mDetailFragment);
			transaction.show(mListFragment);
			transaction.commit();
			detailView = false;
			btnBack.setBackgroundResource(R.drawable.icon_back_main);

		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onBackMainListener(View v) {
		if (detailView) {

			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.hide(mDetailFragment);
			transaction.show(mListFragment);
			transaction.commit();
			detailView = false;
			btnBack.setBackgroundResource(R.drawable.icon_back_main);

		} else {
			finish();
		}
	}
}
