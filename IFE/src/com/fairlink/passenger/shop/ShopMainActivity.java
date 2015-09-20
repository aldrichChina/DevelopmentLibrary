package com.fairlink.passenger.shop;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fairlink.analytics.Analytics;
import com.fairlink.analytics.AnalyticsType;
import com.fairlink.passenger.AdvertisementActivity;
import com.fairlink.passenger.R;
import com.fairlink.passenger.adapter.ShopCategoryAdapter.ShopCategorySelectedListener;
import com.fairlink.passenger.adapter.ShopTypeAdapter.ShopTypeSelectedListener;
import com.fairlink.passenger.bean.Goods;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.GoodsListByMallRequest;
import com.fairlink.passenger.networkrequest.GoodsTypeRequest;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.shop.ShopListFragment.ShopGoodsSelectedListener;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.ShopUtil;
import com.fairlink.passenger.view.DialogLoading;

public class ShopMainActivity extends AdvertisementActivity implements ShopGoodsSelectedListener, HttpTaskCallback,
		ShopTypeSelectedListener, ShopCategorySelectedListener, NetworkRequestAPI {

	private ShopCategoryFragment shopCategoryFragment;
	private List<GoodsCategoryItem> goodsTypeList;
	private List<Goods> flcGoodsList;
	private ShopListFragment mShopListFragment;
	private String mall;
	private DialogLoading diaLoading;
	private String invokeExternalUrl;
	private int invokeRelatedType;

	@Override
	public void onBackMainListener(View v) {
		clearAllActivity();
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onTypeSelected(String typeId) {
		if (isFinishing()) {
			return;
		}

		mShopListFragment.updateShopList(typeId, mall);
	}

	@Override
	public void onCategorySelected(String name) {
		if (name.equals(getString(R.string.after_service))) {
			mShopListFragment.showAfterService(mall);
		} else if (ShopUtil.FLC_GoodsMall.equals(mall)) {
			mShopListFragment.showGoodsList(flcGoodsList);
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_main);

		diaLoading = new DialogLoading(this);

		invokeExternalUrl = getIntent().getExtras().getString(ShopUtil.INVOKE_EXTERNAL_URL_KEY_WORD);
		if (invokeExternalUrl == null || invokeExternalUrl.isEmpty()) {
			logger.error("can't get invokeExternalUrl");
			finish();
			return;
		}

		invokeRelatedType = getIntent().getIntExtra(ShopUtil.INVOKE_RELATED_TYPE_KEY_WORD,
				ShopUtil.ADS_RELATED_TYPE_SHOP);

		mall = getIntent().getExtras().getString(ShopUtil.INVOKE_EXTERNAL_URL_KEY_WORD).split("/")[0];

		mShopListFragment = (ShopListFragment) getFragmentManager().findFragmentById(R.id.shop_list);

		shopCategoryFragment = (ShopCategoryFragment) getFragmentManager()
				.findFragmentById(R.id.shop_category_fragment);
		shopCategoryFragment.setTitleString(ShopUtil.getMallName(mall));
		shopCategoryFragment.setTitleStringTextSizeId(R.dimen.category_normal_size);
		shopCategoryFragment.setTitleImageResourceId(R.drawable.category_shopping);

		if (ShopUtil.FLC_GoodsMall.equals(mall)) {
			new GoodsListByMallRequest(this, this, mall).execute((String) null);
		} else if (ShopUtil.Spring_GoodsMall.equals(mall)) {
			new GoodsTypeRequest(this, this, mall).execute((String) null);
		}

		diaLoading.show();
	}

	@Override
	public void onError(int requestType) {
		if (requestType != REDIRECT_API) {
			ComUtil.toastText(this, "连接服务器出错", Toast.LENGTH_LONG);
		}
		diaLoading.hide();
	}

	@Override
	public void onGetResult(int requestType, Object result) {
		diaLoading.hide();
		if (requestType == GOODS_TYPE_API) {

			if (result == null) {
				return;
			}

			setCategory((List<GoodsCategoryItem>)result);

			processParameters(invokeRelatedType, invokeExternalUrl);
		} else if (requestType == GOODS_LIST_BY_MALL_API) {
			if (result == null) {
				return;
			}

			List<GoodsCategoryItem> list = new ArrayList<GoodsCategoryItem>();
			GoodsCategoryItem allFlc = new GoodsCategoryItem();
			allFlc.name = "DEFSS商城";
			list.add(allFlc);
			setCategory(list);
			flcGoodsList = (List<Goods>) result;
			mShopListFragment.showGoodsList(flcGoodsList);
		}
	}

	private void setCategory(List<GoodsCategoryItem> result) {
		goodsTypeList = result;
		GoodsCategoryItem afterServiceType = new GoodsCategoryItem();
		afterServiceType.name = getString(R.string.after_service);
		goodsTypeList.add(afterServiceType);

		shopCategoryFragment.setCategoryList(goodsTypeList);
	}

	public boolean isSameMall(String adsExternalUrl) {
		if (adsExternalUrl == null || adsExternalUrl.isEmpty())
			return false;

		return mall.equals(adsExternalUrl.split("/")[0]);
	}

	public void processParameters(int relatedType, String adsExternalUrl) {

		String[] keyWords = adsExternalUrl.split("/");

		switch (relatedType) {

		case ShopUtil.ADS_RELATED_TYPE_SHOP:
			setGoodsTypeSelected("");
			break;

		case ShopUtil.ADS_RELATED_TYPE_GOODS_TYPE:
			if (2 == keyWords.length) {
				setGoodsTypeSelected(keyWords[1]);
			}
			break;
		}
	}

	private void setGoodsTypeSelected(String type) {
		if (type == null) {
			return;
		}
		
		if (type.isEmpty()) {
			mShopListFragment.updateShopList(goodsTypeList.get(0).goodsType.get(0).getGoodsTypeId(), mall);
		}
	}

	@Override
	public void onShopGoodsSelected(int id) {

		if (isFinishing()) {
			return;
		}

		if (!ComUtil.isEmpty(mall)) {

			Analytics
					.logEvent(this, AnalyticsType.getOperationShop(3),
							mall.equals(ShopUtil.FLC_GoodsMall) ? AnalyticsType.ORIGIN_MALL_FLC
									: AnalyticsType.ORIGIN_MALL_SSS, AnalyticsType.cperationData(null, null));

		}

		ShopUtil.showGoodsDetail(this, id);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Analytics.logEvent(this, AnalyticsType.getOperationShop(1),
				mall.equals(ShopUtil.FLC_GoodsMall) ? AnalyticsType.ORIGIN_MALL_FLC : AnalyticsType.ORIGIN_MALL_SSS,
				null);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Analytics.logEvent(this, AnalyticsType.getOperationShop(2),
				mall.equals(ShopUtil.FLC_GoodsMall) ? AnalyticsType.ORIGIN_MALL_FLC : AnalyticsType.ORIGIN_MALL_SSS,
				null);
	}

}
