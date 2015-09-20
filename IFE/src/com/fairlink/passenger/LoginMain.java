package com.fairlink.passenger;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.fairlink.passenger.application.ApplicationManager;
import com.fairlink.passenger.inform.InformRemoteService;
import com.fairlink.passenger.upgrade.MainAppUpgradeChecker;
import com.fairlink.passenger.util.PackageUtils;

public class LoginMain extends BaseActivity {

	LoginFragment mLoginFragment;
	MainFragment mMainFragment;
	WelcomeFragment mWelcomeFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		PackageUtils.lockStatusbar(this, true);
		PackageUtils.hideNavigationbar(this, true);
		PackageUtils.launchAsHome(this, true);

		Intent intent = new Intent(this, InformRemoteService.class);
		startService(intent);

		super.onCreate(savedInstanceState);
		logger.info("main app startup with version:" + PackageUtils.getAppVersionCode(this));
		ApplicationManager.getInstence().setContext(this);
		setContentView(R.layout.main_login);

		mWelcomeFragment = (WelcomeFragment) getFragmentManager().findFragmentById(R.id.welcome);
	}

	public void onResume() {
		super.onResume();
		clearAllActivity();
//		Intent cleanTasks = new Intent();
//		cleanTasks.setAction("customer.clean.tasks");
//		cleanTasks.putExtra("mtaskid", getTaskId());
//		sendBroadcast(cleanTasks);
	}

	public void onPause() {
		super.onPause();
	}

	public void onDestroy() {
		MainAppUpgradeChecker.getInstance().cancel();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// stop back to home
		// super.onBackPressed();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		if(mLoginFragment != null){
			if(mLoginFragment.isHidden()){
				return super.dispatchTouchEvent(ev);
			}
			if(mLoginFragment.onTouchEvent(ev)){
				return true;			
			}
			
		}
		return super.dispatchTouchEvent(ev);
	}

	void enterMainActivity() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		if (mMainFragment == null) {
			mMainFragment = new MainFragment();
			ft.add(R.id.MainLayout, mMainFragment);
			ft.addToBackStack(null);
		}

		ft.show(mMainFragment);
		ft.hide(mLoginFragment);
		ft.commitAllowingStateLoss();
	}

	/** 直接进入主页 */
	public void onMainListener(View v) {
		enterMainActivity();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		if (getIntent().getExtras() != null) {
			boolean redirect = getIntent().getExtras().getBoolean("redirect");
			
			if (redirect) {
				clearAllActivity();
				boolean offline = getIntent().getExtras().getBoolean("offline");
				IFEApplication.getInstance().setOfflineStatus(offline);
				IFEApplication.getInstance().setDataFLC(null);

				FragmentTransaction ft = getFragmentManager().beginTransaction();

				if (mLoginFragment == null) {
					mLoginFragment = new LoginFragment();
					ft.add(R.id.MainLayout, mLoginFragment);
					ft.addToBackStack(null);
				}
				
				mLoginFragment.showLoginLayout(offline);
	

				if (mMainFragment != null) {
					ft.remove(mMainFragment);
					mMainFragment = null;
				}
	
				ft.show(mLoginFragment);
				
				
				ft.commit();
			}
		}
	}

	public void enterLogin() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.animator.fade_in,
				android.R.animator.fade_out);

		if (mLoginFragment == null) {
			mLoginFragment = new LoginFragment();
			ft.add(R.id.MainLayout, mLoginFragment);
			ft.addToBackStack(null);
		}

		ft.show(mLoginFragment);
		ft.hide(mWelcomeFragment);
		ft.commitAllowingStateLoss ();
	}
}
