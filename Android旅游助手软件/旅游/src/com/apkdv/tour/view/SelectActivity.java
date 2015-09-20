package com.apkdv.tour.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apkdv.tour.adapter.SelectAdapter;
import com.apkdv.tour.entity.Suites;
import com.apkdv.tour.utils.DataUtils;
import com.apkdv.tour.utils.Tools;
import com.apldv.tour.R;


public class SelectActivity extends BaseActivity {
	RelativeLayout layout;
	ListView selectList;
	TextView title;
	SelectAdapter adapter;
	ArrayList<Suites> suites;
	String titleString;
	private void stupView() {
		layout = (RelativeLayout) findViewById(R.id.rela_login_main);
		selectList = (ListView)findViewById(R.id.lv_suites);
		title = (TextView)findViewById(R.id.tv_select_title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suites_activity);
		
		layout = (RelativeLayout) findViewById(R.id.rela_login_main);
		Animation animation = AnimationUtils.loadAnimation(SelectActivity.this,
				R.anim.logo);
		layout.startAnimation(animation);
		stupView();
		setData();
		addlistener();
	}

	private void setData() {
		selectList.setDivider(null);
		titleString =  getIntent().getStringExtra("title");
		if (titleString.equals("酒店查看")) {
			title.setText(titleString);
			suites = DataUtils.getSuites();
		}else {
			title.setText(titleString);
			suites = DataUtils.gettour();
		}
		adapter = new SelectAdapter(this,suites);
		selectList.setAdapter(adapter);
	}

	public void addlistener() {
		selectList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SelectActivity.this, ShowActivity.class);
				intent.putExtra("entity", Tools.gson.toJson(suites.get(position)));
				if (titleString.equals("酒店查看")) {
					intent.putExtra("title", "酒店详情");
				}else {
					intent.putExtra("title", "景点详情");
				}
				startActivity(intent);
			}
		});
	}

	public void showToast(String String) {
		Toast.makeText(getApplicationContext(), String, Toast.LENGTH_SHORT)
				.show();
	}

}
