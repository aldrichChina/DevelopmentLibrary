package com.fairlink.passenger;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.fairlink.passenger.util.Logger;

public class BaseFragment extends Fragment {
	protected Logger logger = new Logger(this, "fragment");

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		logger.info("onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.info("onCreate");
	}

	@Override
	public void onStart() {
		super.onStart();
		logger.info("onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		logger.info("onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		logger.info("onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		logger.info("onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		logger.info("onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		logger.info("onDetach");
	}
}
