package com.fairlink.passenger.view;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.R;
import com.fairlink.passenger.adapter.BaseCategoryAdapter;
import com.fairlink.passenger.networkrequest.ADNewPicRequest;
import com.fairlink.passenger.networkrequest.ADNewPicRequest.ADNEW;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.ShopUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CategoryFragment extends BaseFragment implements OnItemClickListener, HttpTaskCallback {
	public static interface CategorySelectedListener {
		public void onCategorySelected(int index);
	}

	private String adsExternalUrl = "";
	private ImageView adSideBar;
	private int adsRelatedType;
	private BaseCategoryAdapter mAdapter;
	private ListView menuList;
	private CategorySelectedListener mListener;
	private TextView title;
	private ImageView titleImage;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		new ADNewPicRequest(this, ADNewPicRequest.ADS_TYPE_BANNER).execute((String) null);

		mListener = (CategorySelectedListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.category_fragment, container);

		title = (TextView) view.findViewById(R.id.title);
		titleImage = (ImageView) view.findViewById(R.id.title_image);
		menuList = (ListView) view.findViewById(R.id.category_list);
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
			adSideBar.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(pic, adSideBar);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mListener.onCategorySelected(position);
		setItemChecked(position);
	}

	public void setCategoryStringList(List<String> categoryStringList) {
		setCategoryStringListOnly(categoryStringList);
		setItemChecked(0);
	}

	public void setCategoryStringListOnly(List<String> categoryStringList) {
		if (menuList == null) {
			return;
		}

		mAdapter = new BaseCategoryAdapter(getActivity(), categoryStringList);
		menuList.setAdapter(mAdapter);
		menuList.setOnItemClickListener(this);
		menuList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	public void setItemChecked(int position) {
		mAdapter.setmCurrentMenu(position);
		menuList.setItemChecked(position, true);
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

	public void setTitleStringId(int titleStringId) {
		if (title != null) {
			title.setText(titleStringId);
		}
	}

	public void setTitleStringTextSizeId(int titleStringTextSizeId) {
		if (title != null) {
			title.setTextSize(getResources().getDimension(titleStringTextSizeId));
		}
	}
}
