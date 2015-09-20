package com.fairlink.passenger.book;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.R;
import com.fairlink.passenger.adapter.BookListAdapter;
import com.fairlink.passenger.bean.BookInfo;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.BookRequest;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.util.ComUtil;

public class BookListFragment extends BaseFragment implements HttpTaskCallback, NetworkRequestAPI, OnItemClickListener {

	public static interface BookInfoListener {
		public void onBookSelected(String name, String id, String type, String displayType);
	}

	private GridView book_List;
	private BookListAdapter bookAdapter;
	private BookInfoListener mListener;
	private List<BookInfo> mData;

	public BookListFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (BookInfoListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.base_list_layout, null);
		book_List = (GridView) view.findViewById(R.id.base_list);
		mData = new ArrayList<BookInfo>();
		bookAdapter = new BookListAdapter(getActivity(), mData);
		book_List.setAdapter(bookAdapter);
		book_List.setOnItemClickListener(this);

		loadPage();
		return view;
	}

	private void loadPage() {
		new BookRequest(getActivity(), this).execute((String) null);
	}

	@Override
	public void onGetResult(int requestType, Object result) {
		if (result == null) {
			return;
		}

		// add fault tolerance in some case, Activity has been destroy
		if (getActivity() == null) {
			return;
		}

		List<BookInfo> list = (List<BookInfo>) result;
		if (mData != null) {
			mData.addAll(list);
		} else {
			mData = list;
		}
		bookAdapter.notifyDataSetChanged();
	}

	@Override
	public void onError(int requestType) {

		if (requestType != REDIRECT_API) {
			ComUtil.toastText(getActivity(), "连接服务器出错", Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		BookInfo item = mData.get(arg2);
		mListener.onBookSelected(item.getBookName(), item.getBookId(), item.getBookTypeSetting(),
				item.getBookDisplaySetting());
	}
}
