package com.apkdv.tour.view;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkdv.tour.adapter.MainFragmentPagerAdapter;
import com.apkdv.tour.model.MyApplication;
import com.apldv.tour.R;



public class MainPagerActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {
	ViewPager viewPager;
	ImageView[] btnArray = new ImageView[2];
	TextView tvTitle;
	MainFragmentPagerAdapter adapter;
	public static MainPagerActivity instance;
	int currentPageIndex = 0;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		try {
			setContentView(R.layout.main);
			instance = this;
			setupView();
			addListener();
			setPinc(currentPageIndex);
			getPager();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getPager(){
		try {
			String pager = getIntent().getStringExtra("pager");
			if (pager.equals("info")) {
				viewPager.setCurrentItem(1);
			}else {
				viewPager.setCurrentItem(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new Builder(MainPagerActivity.this);
			builder.setTitle("提示信息：")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									MyApplication.exit();
									dialog.dismiss();
									finish();
									
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).setMessage("确定退出程序 ？").show();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void addListener() {
		// TODO Auto-generated method stub
		viewPager.setOnPageChangeListener(this);

		for (ImageView btn : btnArray) {
			btn.setOnClickListener(this);
		}
		
	}

	private void setupView() {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		btnArray[0] = (ImageView) findViewById(R.id.image_main_fre);
		btnArray[1] = (ImageView) findViewById(R.id.image_main_setting);

		ArrayList<Fragment> list = new ArrayList<Fragment>();
		list.add(new MainFragment());
		list.add(new ManagerFragment());
		adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),
				list);
		viewPager.setAdapter(adapter);
		tvTitle = (TextView)findViewById(R.id.tv_main_Text);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int pageIndex) {
		this.currentPageIndex = pageIndex;
		setPinc(currentPageIndex);
	}
	public void setTitle(int id){
		switch (id) {
		case 0:
			tvTitle.setText("欢迎　"+MyApplication.user.getMyName()+"　登录");
			break;
		case 1:
			tvTitle.setText("个人中心");
			break;
		}
	}
	public void setPinc(int cuurindex){
		System.out.println(cuurindex);
		if (cuurindex == 0) {
			btnArray[0].setImageResource(R.drawable.tab_find_frd_pressed);
			btnArray[1].setImageResource(R.drawable.tab_settings_normal);
		}else {
			btnArray[0].setImageResource(R.drawable.tab_find_frd_normal);
			btnArray[1].setImageResource(R.drawable.tab_settings_pressed);
		}
		setTitle(cuurindex);
	}

	@Override
	public void onClick(View v) {
		setTitle(currentPageIndex);
		try {
			switch (v.getId()) {
			case R.id.image_main_fre:
				this.currentPageIndex = 0;
				setPinc(currentPageIndex);
				break;
			case R.id.image_main_setting:
				this.currentPageIndex = 1;
				setPinc(currentPageIndex);
				break;
			
			}
			// 显示另外一个fragment
			viewPager.setCurrentItem(currentPageIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
