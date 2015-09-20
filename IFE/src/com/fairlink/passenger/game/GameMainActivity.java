package com.fairlink.passenger.game;

import java.util.Arrays;

import android.os.Bundle;

import com.fairlink.passenger.AdvertisementActivity;
import com.fairlink.passenger.R;
import com.fairlink.passenger.view.CategoryFragment;
import com.fairlink.passenger.view.CategoryFragment.CategorySelectedListener;

public class GameMainActivity extends AdvertisementActivity implements CategorySelectedListener {

	GameListFragment mGameListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.game_main_layout);

		CategoryFragment categoryFragment = (CategoryFragment) getFragmentManager().findFragmentById(
				R.id.category_fragment);
		categoryFragment.setTitleStringId(R.string.index_game_str);
		categoryFragment.setTitleStringTextSizeId(R.dimen.category_normal_size);
		categoryFragment.setTitleImageResourceId(R.drawable.category_ebook);
		categoryFragment.setCategoryStringList(Arrays.asList(getResources().getStringArray(R.array.game_category)));

		mGameListFragment = (GameListFragment) getFragmentManager().findFragmentById(R.id.game_list);
	}

	@Override
	public void onCategorySelected(int index) {
	}

}
