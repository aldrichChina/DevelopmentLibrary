package com.fairlink.passenger.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.R;

/**
 * @ClassName ： PayCompleteFragment
 * @Description: 支付完成界面
 */

public class OrderPayCompleteFragment extends BaseFragment implements OnClickListener {

	private Button btnOrders;

	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pay_com_fragment, null);

		btnOrders = (Button) view.findViewById(R.id.btn_order_detial);
		btnOrders.setOnClickListener(this);
		return view;
	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		showOrders();
	}

	private void showOrders() {
		startActivity(new Intent(getActivity(), OrderRecordActivity.class));
		getActivity().finish();
	}

}
