package com.fairlink.passenger.video;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fairlink.passenger.R;

public class TVPopWin {

	public View view;
	Activity mpthis;
	PopupWindow mPopupWindow;
	TextView tv_showText = null;

	public TVPopWin(Activity pthis) {
		mpthis = pthis;
		mPopupWindow = null;
	}

	

	public void dismiss() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
	}

	public void ShowWin(String nu, int x, int y) {
		dismiss();
		View popunwindwow = null;

		LayoutInflater LayoutInflater = (LayoutInflater) mpthis
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		popunwindwow = LayoutInflater.inflate(R.layout.tv_pop_win, null);
		tv_showText = (TextView) popunwindwow.findViewById(R.id.tv_nu_show);
		tv_showText.setText(nu);

		mPopupWindow = new PopupWindow(popunwindwow, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindow.showAtLocation(mpthis.findViewById(R.id.movie_detail),
				Gravity.LEFT | Gravity.TOP, x, y);
		mPopupWindow.update();
	//	handler.postDelayed(runnable, 1000);

	}

}
