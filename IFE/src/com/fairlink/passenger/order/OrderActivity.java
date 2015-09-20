package com.fairlink.passenger.order;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fairlink.passenger.BaseActivity;
import com.fairlink.passenger.R;

/**
 * @ClassName ： OrderActivity
 * @Description: 订单模块主界面
 */

public class OrderActivity extends BaseActivity {

	private String page;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_main_activity);
		initView();
	}

	private void initView() {
		Fragment orderFragment = null;

		page = getIntent().getExtras().getString("page");
		if (page == null || page.isEmpty()) {
			finish();
			return;
		}

		if (page.equals("confirm")) {
			orderFragment = new OrderConfirmFragment();
		} else if (page.equals("pay")) {
			orderFragment = new OrderPayFragment();
		} else if (page.equals("success")) {
			orderFragment = new OrderPayCompleteFragment();
		} else {
			finish();
			return;
		}

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(R.id.order_main_layout, orderFragment);
		transaction.show(orderFragment);
		transaction.hide(getFragmentManager().findFragmentById(R.id.cart_tip_button));
		transaction.hide(getFragmentManager().findFragmentById(R.id.order_tip_button));
		transaction.commitAllowingStateLoss();
		
		Button btnBack = (Button) findViewById(R.id.com_back_main).findViewById(R.id.btn_back);
		btnBack.setBackgroundResource(R.drawable.icon_back_pre);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onBackMainListener(View v) {
		finish();
	}
}
