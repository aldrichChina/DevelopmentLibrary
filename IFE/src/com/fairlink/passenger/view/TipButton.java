package com.fairlink.passenger.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fairlink.passenger.R;

public abstract class TipButton extends Fragment {

	protected Button button;
	protected TextView number;

	protected abstract int getBackgroundResource();

	protected abstract String getNumber();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.tip_button, container);

		number = (TextView) view.findViewById(R.id.tip_num);

		button = (Button) view.findViewById(R.id.tip_button);
		button.setBackgroundResource(getBackgroundResource());
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showActivity();
			}
		});
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		update();
	}

	protected abstract void showActivity();

	public void update() {
		number.setText(getNumber());
	}
}
