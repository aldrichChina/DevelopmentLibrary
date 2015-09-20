package com.fairlink.passenger.book;

import java.util.Arrays;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.fairlink.analytics.Analytics;
import com.fairlink.analytics.AnalyticsType;
import com.fairlink.passenger.AdvertisementActivity;
import com.fairlink.passenger.R;
import com.fairlink.passenger.book.BookListFragment.BookInfoListener;
import com.fairlink.passenger.view.CategoryFragment;
import com.fairlink.passenger.view.CategoryFragment.CategorySelectedListener;

public class BookMainViewActivity extends AdvertisementActivity implements CategorySelectedListener, BookInfoListener {

	private BookListFragment bookListFragment;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_mainview_layout);

		CategoryFragment categoryFragment = (CategoryFragment) getFragmentManager().findFragmentById(
				R.id.category_fragment);
		categoryFragment.setTitleStringId(R.string.index_book_str);
		categoryFragment.setTitleStringTextSizeId(R.dimen.category_normal_size);
		categoryFragment.setTitleImageResourceId(R.drawable.category_ebook);
		categoryFragment.setCategoryStringList(Arrays.asList(getResources().getStringArray(R.array.book_category)));

		bookListFragment = (BookListFragment) getFragmentManager().findFragmentById(R.id.booklist_fragment);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.show(bookListFragment);
		transaction.commit();
	}

	@Override
	public void onCategorySelected(int index) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onBookSelected(String name, String id, String type, String displayType) {
		Intent i = new Intent(this, BookDetailActivity.class);
		i.putExtra("resourceId", Integer.parseInt(id));
		onBookSelected(name);
		startActivity(i);
	}

	public void onBookSelected(String name) {
		Analytics.logEvent(this, AnalyticsType.getOperationEbook(3, 0), AnalyticsType.ORIGIN_EBOOK,
				AnalyticsType.cperationData(null, name));
	}
}
