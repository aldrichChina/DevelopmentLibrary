package com.fairlink.passenger.shop;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.R;
import com.fairlink.passenger.adapter.ShopCategoryAdapter;
import com.fairlink.passenger.networkrequest.ADNewPicRequest.ADNEW;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.Logger;
import com.fairlink.passenger.util.ShopUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShopCategoryFragment extends BaseFragment implements HttpTaskCallback {
	public static interface CategorySelectedListener {
		public void onCategorySelected(String typeId);
	}

	private String adsExternalUrl = "";
	private ImageView adSideBar;
	private int adsRelatedType;
	private ShopCategoryAdapter mAdapter;
	private ExpandableListView menuList;
	private TextView title;
	private ImageView titleImage;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.shop_category_fragment, container);

		title = (TextView) view.findViewById(R.id.title);
		titleImage = (ImageView) view.findViewById(R.id.title_image);
		menuList = (ExpandableListView) view.findViewById(R.id.shop_category_list);
		adSideBar = (ImageView) view.findViewById(R.id.img_ad_side_bar);
		adSideBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!ComUtil.isEmpty(adsExternalUrl)) {
					ShopUtil.showShop(getActivity(), adsRelatedType, adsExternalUrl);
				}
			}
		});
		return view;
	}

	@Override
	public void onError(int requestType) {
	}

	@Override
	public void onGetResult(int requestType, Object result) {
		if (result == null) {
			return;
		}

		ADNEW data = (ADNEW) result;
		adsExternalUrl = data.adsExternalUrl;
		adsRelatedType = data.adsRelatedType;
		String pic = data.adsPath;
		if (pic != null) {
			ImageLoader.getInstance().displayImage(pic, adSideBar);
		}
	}

	public void setCategoryList(List<GoodsCategoryItem> categoryList) {
		if (menuList == null) {
			return;
		}
		
		mAdapter = new ShopCategoryAdapter(getActivity(), categoryList);
		menuList.setGroupIndicator(null);
		menuList.setAdapter(mAdapter);
		menuList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	public void setTitleImageResourceId(int titleImageResourceId) {
		if (titleImage != null) {
			titleImage.setImageResource(titleImageResourceId);
		}
	}

	public void setTitleString(String titleString) {
		if (title != null) {
			title.setText(titleString);
		}
	}

	public void setTitleStringTextSizeId(int titleStringTextSizeId) {
		if (title != null) {
			title.setTextSize(getResources().getDimension(titleStringTextSizeId));
		}
	}
}
