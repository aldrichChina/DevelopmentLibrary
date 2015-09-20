package com.fairlink.passenger.book;

import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.analytics.Analytics;
import com.fairlink.analytics.AnalyticsType;
import com.fairlink.passenger.BaseActivity;
import com.fairlink.passenger.R;
import com.fairlink.passenger.adapter.BookImgListAdapter;
import com.fairlink.passenger.bean.BookInfo;
import com.fairlink.passenger.book.MtitlePopupWindow.OnPopupWindowClickListener;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.BookByIdRequest;
import com.fairlink.passenger.networkrequest.BookDetailRequest;
import com.fairlink.passenger.networkrequest.BookDetailRequest.BookDetail;
import com.fairlink.passenger.networkrequest.BookDetailRequest.BookList;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.view.HorizontalListView;
import com.fairlink.passenger.view.HorizontalListView.HorizonalOnScrollListener;

/**
 * @ClassName ： BookDetailActivity
 * @Description: 电子书详情
 */

public class BookDetailActivity extends BaseActivity implements
		HttpTaskCallback, NetworkRequestAPI, OnItemClickListener,
		OnScrollListener, HorizonalOnScrollListener {

	private HorizontalListView horizList;
	private ScaleScrollView horizScroll;
	private ListView pagerList;
	private ScaleHorizontalScrollView pagerScroll;

	private BookImgListAdapter adapter;
	private Button btnDetail;
	private Button btnBack;
	private RelativeLayout rl1;
	private static ArrayList<BookDetail> mData = new ArrayList<BookDetail>();
	private String name;
	private String bookTypeSetting; // 横：landscape
	private String bookDisplaySetting;
	private MtitlePopupWindow mtitlePopupWindow;
	private TextView tv_name;
	private String bookId;

	// private ImageButton rato_btn;
	private boolean isVerticalDisplay;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_detail);

		isVerticalDisplay = true;
		bookId = getIntent().getIntExtra("resourceId", 0) + "";
		// name = getIntent().getStringExtra("bookName");
		bookTypeSetting = getIntent().getStringExtra("bookTypeSetting");
		
		initView();
		setListeners();
		adapter = new BookImgListAdapter(this, mData);
		loadBookInfo(bookId);
		mtitlePopupWindow = new MtitlePopupWindow(this);
		

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (mData != null) {
				mData.clear();

			}
			finish();
			return true;
		} else
			return super.onKeyDown(keyCode, event);

	}

	public void onDestroy() {
		super.onDestroy();
	}

	public void onConfigurationChanged(Configuration newConfig) {
		try {
			super.onConfigurationChanged(newConfig);
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				// land

			} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				// port
			}
		} catch (Exception ex) {
		}
	}

	private void loadBookInfo(String id) {
		new BookByIdRequest(this, id, this).execute((String) null);
	}

	private void loadPage(String id) {
		new BookDetailRequest(this, id, this).execute((String) null);
	}

	private void initView() {
		tv_name = (TextView) findViewById(R.id.tv_name);
		pagerList = (ListView) findViewById(R.id.pagerList);
		pagerScroll = (ScaleHorizontalScrollView) findViewById(R.id.pagerScroll);
		horizList = (HorizontalListView) findViewById(R.id.horizList);
		horizScroll = (ScaleScrollView) findViewById(R.id.horizScroll);
		btnDetail = (Button) findViewById(R.id.btn_detail);
		btnBack = (Button) findViewById(R.id.btn_back);
		pagerList.setOnScrollListener(this);

		
		// tvName.setText(name);
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		rl1.getBackground().setAlpha(200);
	}

	private void setListeners() {

		btnDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mtitlePopupWindow.showAsDropDown(v);

			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mData.clear();
				finish();

			}
		});

		
	}

	@Override
	public void onGetResult(int requestType, Object result) {
		if (result == null) {
			return;
		}

		if (requestType == BOOK_DETAIL_API) {
			BookList list = (BookList) result;
			mData.clear();
			mData.addAll(list.items);
			adapter.notifyDataSetChanged();
			// pagerList.setAdapter(adapter);

			mtitlePopupWindow.changeData(mData);
		} else if (requestType == BOOK_BY_ID_API) {
			BookInfo bookInfo = (BookInfo) result;
			
			this.bookDisplaySetting = bookInfo.getBookDisplaySetting();
			this.name = bookInfo.getBookName();
			this.bookTypeSetting = bookInfo.getBookTypeSetting();
			
			if (bookTypeSetting == null || bookTypeSetting.length() == 0) {
				bookTypeSetting = "portrait";
			}
			bookDisplaySetting = getIntent().getStringExtra("bookDisplaySetting");
			
			if (!bookTypeSetting.equalsIgnoreCase("landscape")) {
				pagerScroll.setVisibility(View.VISIBLE);
				horizScroll.setVisibility(View.GONE);
				pagerList.setOnItemClickListener(this);
			} else {
				pagerScroll.setVisibility(View.GONE);
				horizScroll.setVisibility(View.VISIBLE);
				horizList.setOnItemClickListener(this);
				horizList.setScrollListener(this);
			}
			
			if (bookTypeSetting.equalsIgnoreCase("landscape")) {
				horizList.setAdapter(adapter);
				isVerticalDisplay = false;
			} else {
				pagerList.setAdapter(adapter);
				isVerticalDisplay = true;
			}
			adapter.setDisplaySetting(isVerticalDisplay);
			
			if (mData != null && mData.size() > 0)
				mtitlePopupWindow.changeData(mData);
			mtitlePopupWindow
					.setOnPopupWindowClickListener(new OnPopupWindowClickListener() {

						@Override
						public void onPopupWindowItemClick(int position) {
							if (!bookTypeSetting.equalsIgnoreCase("landscape"))
								pagerList.setSelection(position);
							else
								horizList.setSelection(position);
							Analytics.logEvent(BookDetailActivity.this,
									AnalyticsType.getOperationEbook(4, position),
									AnalyticsType.ORIGIN_EBOOK,
									AnalyticsType.cperationData(null, name));

						}
					});
			if (bookDisplaySetting != null && bookDisplaySetting.length() > 0) {
				if (bookDisplaySetting.equalsIgnoreCase("portrait")) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				} else {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}
			}

			loadPage(bookId);
		}
	}

	@Override
	public void onError(int requestType) {
		if (requestType != REDIRECT_API) {
			ComUtil.toastText(this, "连接服务器出错", Toast.LENGTH_SHORT);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean handled = false;
		try {
			handled = super.dispatchTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return handled;
	}

	private void setRL1Visibe() {
		if (rl1.getVisibility() == View.GONE)
			rl1.setVisibility(View.VISIBLE);
		else {
			rl1.setVisibility(View.GONE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0.getId() == R.id.pagerList || arg0.getId() == R.id.horizList)
			setRL1Visibe();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		String pageNum = String.valueOf(firstVisibleItem + 1);
		if (firstVisibleItem + 1 < 10) {
			pageNum = 0 + pageNum;
		}
		tv_name.setText(pageNum);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onHorizonalScrollL(int index) {
		String pageNum = String.valueOf(index + 1);
		if (index+1 < 10) {
			pageNum = 0 + pageNum;
		}
		tv_name.setText(pageNum);
	}
}
