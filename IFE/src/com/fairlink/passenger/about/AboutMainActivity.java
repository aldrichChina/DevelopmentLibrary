package com.fairlink.passenger.about;

import java.util.Arrays;

import android.os.Bundle;

import com.fairlink.passenger.AdvertisementActivity;
import com.fairlink.passenger.R;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.view.CategoryFragment;
import com.fairlink.passenger.view.CategoryFragment.CategorySelectedListener;

public class AboutMainActivity extends AdvertisementActivity implements CategorySelectedListener {
	public static final String ENCODING = "UTF-8";
	private AboutDetailFragment mDetailFragment;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_main_layout);

		CategoryFragment categoryFragment = (CategoryFragment) getFragmentManager().findFragmentById(
				R.id.category_fragment);
		categoryFragment.setTitleStringId(R.string.index_about_str);
		categoryFragment.setTitleStringTextSizeId(R.dimen.category_normal_size);
		categoryFragment.setTitleImageResourceId(R.drawable.logo_kong1);
		categoryFragment.setCategoryStringList(Arrays.asList(getResources().getStringArray(R.array.about_category)));

		mDetailFragment = (AboutDetailFragment) getFragmentManager().findFragmentById(R.id.about_detail);
	}

	@Override
	public void onCategorySelected(int index) {
		switch (index) {
		case 0:
			mDetailFragment.setText(ComUtil.getFromAssets(this, "about_profile.txt"));
			mDetailFragment.setImage(R.drawable.about_profile2, R.drawable.about_profile1);
			break;

		case 1:
			mDetailFragment.setText(ComUtil.getFromAssets(this, "about_mission.txt"));
			mDetailFragment.setImage(R.drawable.about_management2, R.drawable.about_management1);
			break;

		default:
			break;
		}
	}
}
