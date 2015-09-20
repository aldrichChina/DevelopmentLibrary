package com.fairlink.passenger.customer;

import java.util.Arrays;

import android.os.Bundle;

import com.fairlink.passenger.AdvertisementActivity;
import com.fairlink.passenger.R;
import com.fairlink.passenger.view.CategoryFragment;
import com.fairlink.passenger.view.CategoryFragment.CategorySelectedListener;

public class CustomerMainActivity extends AdvertisementActivity implements CategorySelectedListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_main_layout);

		CategoryFragment categoryFragment = (CategoryFragment) getFragmentManager().findFragmentById(
				R.id.category_fragment);
		categoryFragment.setTitleStringId(R.string.index_customer_str);
		categoryFragment.setTitleStringTextSizeId(R.dimen.category_normal_size);
		categoryFragment.setTitleImageResourceId(R.drawable.category_voice);
		categoryFragment.setCategoryStringList(Arrays.asList(getResources().getStringArray(R.array.customer_category)));
	}

	@Override
	public void onCategorySelected(int index) {

	}
}
