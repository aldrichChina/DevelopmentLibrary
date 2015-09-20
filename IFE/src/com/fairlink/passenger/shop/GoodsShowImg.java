package com.fairlink.passenger.shop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.fairlink.passenger.BaseActivity;
import com.fairlink.passenger.R;
import com.fairlink.passenger.networkrequest.PhotoManager;
import com.fairlink.passenger.util.ImageUtil;

/**
 * @ClassName  ：  GoodsShowImg 
 * @Description: 商品显示大图
 * @author     ：  jiaxue 
 * @date       ：  2014-11-27 下午5:09:26 

 */

public class GoodsShowImg extends BaseActivity {

	private ViewPager mViewPager;
	private LinearLayout linBottom;
	Button mPreSelectedBt;   
	
	private SamplePagerAdapter adapter;
	
	private static String[] strDrawables;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_goods_big_img);
		
		initView();
		initData();
		setListeners();
		
	}
	
	private void initData() {
		String pics = getIntent().getExtras().getString("pics");
		strDrawables = pics.split(",");
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.goods_detail_circle);
		for (int i = 0; i < strDrawables.length; i++) {
			Button bt = new Button(this);
			bt.setLayoutParams(new ViewGroup.LayoutParams(bitmap.getWidth(),
					bitmap.getHeight()));
			bt.setBackgroundResource(R.drawable.goods_detail_circle);
			linBottom.addView(bt);
		}
		
		 
		if (strDrawables != null && strDrawables.length > 0) {

			if (mPreSelectedBt != null) {
				mPreSelectedBt.setBackgroundResource(R.drawable.goods_detail_circle);
			}

			Button currentBt = (Button) linBottom.getChildAt(0);
			currentBt.setBackgroundResource(R.drawable.goods_detail_circle_choose);
			mPreSelectedBt = currentBt;
		}
		  
	}
	
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.pager_goods_img);
		linBottom = (LinearLayout) findViewById(R.id.lin_bottom);
		
	}
	
	private void setListeners(){
		adapter = new SamplePagerAdapter();
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				
				 if(mPreSelectedBt != null){   
	                    mPreSelectedBt.setBackgroundResource(R.drawable.goods_detail_circle);   
	                }   
	                   
	                Button currentBt = (Button)linBottom.getChildAt(position);   
	                currentBt.setBackgroundResource(R.drawable.goods_detail_circle_choose);   
	                mPreSelectedBt = currentBt;   
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}
		});
	}
	
	
	
	private class SamplePagerAdapter extends PagerAdapter implements OnClickListener {
		private LayoutInflater inflater;

		SamplePagerAdapter() {
			inflater = getLayoutInflater();
		}

		@Override
		public int getCount() {
			return strDrawables.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View imageLayout = inflater.inflate(R.layout.goods_show_big_item, container,
					false);
			imageLayout.setOnClickListener(this);
			final ImageView photoView = (ImageView) imageLayout
					.findViewById(R.id.image);
			photoView.setOnClickListener(this);
			
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);
			
			String imgUrl = strDrawables[position];
			
			ImageUtil.setImage(imgUrl, ImageUtil.MID, photoView, null, spinner);
			
			((ViewPager) container).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void onClick(View v) {
			if(v.getId() != R.id.image){
				finish();
			}
		}

	}
	
	public void backListener(View v) {
		finish();
	}
	
	
	
}
