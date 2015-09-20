package com.fairlink.passenger.cart;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fairlink.passenger.BaseActivity;
import com.fairlink.passenger.R;

/**
 * @ClassName ： CartActivity
 * @Description: 购物车模块主界面
 */

public class CartActivity extends BaseActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_main);

		initView();
	}

	private void initView() {

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		CartFragment frgCart = new CartFragment();
		transaction.add(R.id.cart_main_layout, frgCart);
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
