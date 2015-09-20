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

public class ManagerFragment extends Fragment{
	RelativeLayout reMyInfo,addmonet;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.manager_activity, null);
		init(view);
		addListener();
		return view;
	}
	private void init(View view) {
		addmonet = (RelativeLayout)view.findViewById(R.id.relat_add_money);
		reMyInfo = (RelativeLayout)view.findViewById(R.id.relat_my_info);
		
		
	}
	private void addListener() {
		reMyInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), ManagerActivity.class));
			}
		});
		addmonet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), MoneyActivity.class));
			}
		});
		
	}
}
