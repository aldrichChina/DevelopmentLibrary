package com.apkdv.tour.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.apldv.tour.R;

public class MainFragment extends Fragment{
	RelativeLayout suitedLayout,selectLayout,messageLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_activity, null);
		init(view);
		addListener();
		return view;
	}
	private void addListener() {
		selectLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SelectActivity.class);
				intent.putExtra("title", "景点查询");
				startActivity(intent);
				
			}
		});
		suitedLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SelectActivity.class);
				intent.putExtra("title", "酒店查看");
				startActivity(intent);
				
			}
		});
		messageLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), MessageActivity.class));
				
			}
		});
	}
	private void init(View view) {
		selectLayout = (RelativeLayout)view.findViewById(R.id.relat_select);
		suitedLayout = (RelativeLayout)view.findViewById(R.id.relat_suites);
		messageLayout = (RelativeLayout)view.findViewById(R.id.relat_main_message);
	}
}
