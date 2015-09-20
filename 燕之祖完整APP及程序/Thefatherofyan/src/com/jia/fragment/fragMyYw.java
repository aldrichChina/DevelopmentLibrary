package com.jia.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jia.ywyx.Cpxq_Activity;
import com.jia.ywyx.GldzActivity;
import com.jia.ywyx.LpkActivity;
import com.jia.ywyx.QueryOrder;
import com.jia.ywyx.R;
import com.jia.ywyx.ScjActivity;
import com.jia.ywyx.UserLogin;
import com.jia.ywyx.UserRegister;
import com.jia.ywyx.dfkActivity;
import com.jia.ywyx.dplActivity;
import com.jia.ywyx.dshActivity;
import com.jia.ywyx.wdddActivity;

public class fragMyYw extends Fragment {
	private View layout_scj;
	private View layout_gwc;
	private View layout_lpk;
	private View layout_dzgl;
	private ImageView dfk;
	private ImageView dsh;
	private ImageView dpl;
	private ImageView wddd;
	private Button dl;
	private Button zc;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.myyw, null);
		layout_scj = convertView.findViewById(R.id.layout_scj);
		layout_gwc = convertView.findViewById(R.id.layout_gwc);
		layout_lpk = convertView.findViewById(R.id.layout_lpk);
		layout_dzgl = convertView.findViewById(R.id.layout_dzgl);
		dfk = (ImageView) convertView.findViewById(R.id.dfk);
		dsh = (ImageView) convertView.findViewById(R.id.dsh);
		dpl = (ImageView) convertView.findViewById(R.id.dpl);
		wddd = (ImageView) convertView.findViewById(R.id.wddd);
		dl = (Button) convertView.findViewById(R.id.dl);
		zc = (Button) convertView.findViewById(R.id.zc);
		wddd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i=new Intent(getActivity(), QueryOrder.class);
				startActivity(i);
			}
		});
		dpl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ComponentName comp = new ComponentName(getActivity()
						.getApplicationContext(), dplActivity.class);
				Intent intent = new Intent();
				intent.setComponent(comp);
				startActivity(intent);
			}
		});
		dsh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ComponentName comp = new ComponentName(getActivity()
						.getApplicationContext(), dshActivity.class);
				Intent intent = new Intent();
				intent.setComponent(comp);
				startActivity(intent);
			}
		});
		dfk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ComponentName comp = new ComponentName(getActivity()
						.getApplicationContext(), dfkActivity.class);
				Intent intent = new Intent();
				intent.setComponent(comp);
				startActivity(intent);

			}
		});
		layout_dzgl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ComponentName comp = new ComponentName(getActivity()
						.getApplicationContext(), GldzActivity.class);
				Intent intent = new Intent();
				intent.setComponent(comp);
				startActivity(intent);
			}
		});
		layout_lpk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ComponentName comp = new ComponentName(getActivity()
						.getApplicationContext(), LpkActivity.class);
				Intent intent = new Intent();
				intent.setComponent(comp);
				startActivity(intent);
			}
		});
		layout_gwc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ComponentName comp = new ComponentName(getActivity()
						.getApplicationContext(), Cpxq_Activity.class);
				Intent intent = new Intent();
				intent.setComponent(comp);
				startActivity(intent);
			}
		});
		layout_scj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ComponentName comp = new ComponentName(getActivity()
						.getApplicationContext(), ScjActivity.class);
				Intent intent = new Intent();
				intent.setComponent(comp);
				startActivity(intent);

			}
		});
		dl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ComponentName comp = new ComponentName(getActivity()
						.getApplicationContext(), UserLogin.class);
				Intent intent = new Intent();
				intent.setComponent(comp);
				startActivity(intent);
			}
		});
		zc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity()
						.getApplicationContext(), UserRegister.class);
				startActivity(intent);
			}
		});
		return convertView;
	}
}
