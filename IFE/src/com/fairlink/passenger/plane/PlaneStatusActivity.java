package com.fairlink.passenger.plane;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.fairlink.passenger.BaseActivity;
import com.fairlink.passenger.R;

public class PlaneStatusActivity extends BaseActivity {


	private PlaneInfoFragment mInfoFragment;
	private PlaneMapFragment mMapFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plane_status);

		mInfoFragment = (PlaneInfoFragment) getFragmentManager().findFragmentById(R.id.info);
		mMapFragment = (PlaneMapFragment) getFragmentManager().findFragmentById(R.id.map);

	}

	void showInfo() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.show(mInfoFragment);
		transaction.hide(mMapFragment);
		transaction.commit();

	}

	void showMap() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.hide(mInfoFragment);
		transaction.show(mMapFragment);
		transaction.commit();

	}
}
