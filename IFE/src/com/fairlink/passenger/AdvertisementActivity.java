package com.fairlink.passenger;

import java.io.File;

import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairlink.passenger.util.Constant;
import com.fairlink.passenger.util.ShopUtil;

public class AdvertisementActivity extends BaseActivity implements OnTouchListener, Runnable {

	public class AdvertisementGestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float x, float y) {
			mTime.removeCallbacks(AdvertisementActivity.this);
			hidePicAD();
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float x, float y) {
			float dis = e2.getRawX() - e1.getRawX();
			mAD.setTranslationX(dis);
			return true;
		}
	}

	protected String adsExternalUrl;
	protected boolean isClicked = false;
	protected boolean isShown = false;
	protected View mAD;
	protected ImageView mADPic;
	protected int mCurrentTime;
	protected GestureDetector mGestureDetector;
	protected TextView mTime;
	private boolean pa = false;

	protected void hidePicAD() {
		if (mAD == null)
			return;

		mAD.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (!isShown) {
			showAd();
		}

		isShown = true;

		if (isClicked) {
			hidePicAD();
		}
		pa = false;
	}

	@Override
	public void onPause() {
		pa = true;
		super.onPause();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public void run() {
		if (!pa) {
			if (isClicked)
				return;
			mCurrentTime--;
			setSpannableString();
		}
		if (mCurrentTime <= 0) {
			hidePicAD();
		} else {
			mTime.postDelayed(this, 1000);
		}

	}

	protected void setSpannableString() {
		String time = String.format(getString(R.string.ad_time), mCurrentTime);
		SpannableString ss = new SpannableString(time);
		int index = time.indexOf(String.valueOf(mCurrentTime));
		ss.setSpan(new ForegroundColorSpan(Color.YELLOW), index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		mTime.setText(ss);
	}

	private void showAd() {

		mAD = findViewById(R.id.modular_ad_layout);
		if (mAD == null)
			return;

		mADPic = (ImageView) mAD.findViewById(R.id.pic);
		mADPic.setOnTouchListener(this);
		mADPic.setFocusable(true);
		mADPic.setClickable(true);
		mADPic.setLongClickable(true);
		mTime = (TextView) mAD.findViewById(R.id.time);
		mGestureDetector = new GestureDetector(this, new AdvertisementGestureListener());
		mTime.removeCallbacks(this);
		mADPic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isClicked)
					return;

				boolean isSameActivity = ShopUtil.showShop(
						AdvertisementActivity.this,
						getIntent().getIntExtra(ShopUtil.MODULAR_RELATED_TYPE_KEY_WORD, ShopUtil.ADS_RELATED_TYPE_SHOP),
						adsExternalUrl);

				logger.info("user click ad");
				
				if (isSameActivity) {
					hidePicAD();
				}

				isClicked = true;
			}
		});

		adsExternalUrl = getIntent().getStringExtra(ShopUtil.MODULAR_EXTERNAL_URL_KEY_WORD);
		if (adsExternalUrl == null || adsExternalUrl.isEmpty())
			return;

		showAdPic();
	}

	protected void showAdPic() {
		String pic = getIntent().getStringExtra("pic");
		if (pic != null) {
			mAD.setVisibility(View.VISIBLE);
			mADPic.setImageURI(Uri.fromFile(new File(pic)));
			mCurrentTime = Constant.AD_TIME;
			setSpannableString();
			mTime.postDelayed(this, 1000);
		} else {
			hidePicAD();
		}
	}
}
