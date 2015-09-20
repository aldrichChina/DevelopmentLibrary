package com.fairlink.passenger.video;

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
import com.fairlink.passenger.adapter.MovieListAdapter;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.MovieListRequest;
import com.fairlink.passenger.networkrequest.MovieListRequest.MovieInfo;
import com.fairlink.passenger.networkrequest.MovieListRequest.MovieList;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.util.ComUtil;

public class BaseVideoListFragment extends BaseFragment implements HttpTaskCallback, NetworkRequestAPI, OnItemClickListener {
	
	public static interface MovieSelectedListener {
		public void onMovieSelected(String name,String id,int type, int playId);
	}

	private GridView 				movie_list;
	private MovieListAdapter		movieAdapter;
	
	private MovieSelectedListener 	mListener;
	private ArrayList<MovieInfo> mData;
	private static final int PAGE_ITEMS_COUNT = 40;
	private int mCurrentPage = 1;
	private int mType = 0;
	public BaseVideoListFragment() {
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (MovieSelectedListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.base_list_layout, null);
		movie_list = (GridView) view.findViewById(R.id.base_list);
		mData = new ArrayList<MovieInfo>();
		movieAdapter = new MovieListAdapter(getActivity(), mData);
		movie_list.setAdapter(movieAdapter);
		movie_list.setOnItemClickListener(this);

		loadPage(mType);
		return view;
	}
	
	private void loadPage(int current) {
		new MovieListRequest(getActivity(), this, current, mCurrentPage,
				PAGE_ITEMS_COUNT).execute((String) null);

	}
	
	public void setMovieMenu(int  current) {
		mType = current;
		mData = new ArrayList<MovieInfo>();
		loadPage(current);
		movie_list.setAdapter(movieAdapter);
	} 

	private void processData(List<MovieInfo> listGet) {
		
		movieAdapter = new MovieListAdapter(getActivity(),mData);
		movie_list.setAdapter(movieAdapter);
		movieAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onGetResult(int requestType, Object result) {
		if(result == null) {
			return;
		}
		if(getActivity() == null){
			return;
		}
		MovieList list = (MovieList) result;
		
		//if(mData != null) {
		//	mData.addAll(list.items);
		//} else {
		mData = list.items;
		//}
		processData(mData);
	}

	@Override
	public void onError(int requestType) {
		//in some case, Activity is null
		if(getActivity() == null){
			return;
		}
		if (requestType != REDIRECT_API) {
			ComUtil.toastText(getActivity(), "连接服务器出错", Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(mData != null && mData.size() > arg2){
			MovieInfo item = mData.get(arg2);
			mListener.onMovieSelected(item.name, item.id, mType, arg2);
		}
	}
}
