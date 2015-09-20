package com.fairlink.passenger.shop;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.R;
import com.fairlink.passenger.adapter.ShopListAdapter;
import com.fairlink.passenger.bean.Goods;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.GoodsListRequest;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.view.DialogLoading;

public class ShopListFragment extends BaseFragment implements HttpTaskCallback, NetworkRequestAPI {

	private TextView mInformation;
	private GridView listShop;
	private ShopListAdapter mAdapter;
	private DialogLoading diaLoading;

	private boolean noNet;

	public static interface ShopGoodsSelectedListener {
		public void onShopGoodsSelected(int id);
	}

	private ShopGoodsSelectedListener mListener;

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		diaLoading = new DialogLoading(activity);
		mListener = (ShopGoodsSelectedListener) activity;
	}

	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shop_list, null);

		mInformation = (TextView) view.findViewById(R.id.information);
		listShop = (GridView) view.findViewById(R.id.goods_list);
		showEmpty(true);

		return view;
	}

	public void onDestroy() {
		if (diaLoading != null) {
			diaLoading.dismiss();
		}
		super.onDestroy();
	}
	
	public void showAfterService(String mall) {
		mInformation.setText(ComUtil.getFromAssets(getActivity(), mall + "_after_service.txt"));
		mInformation.setVisibility(View.VISIBLE);
		listShop.setVisibility(View.INVISIBLE);
	}

	private void showEmpty(boolean show) {
		if (show) {
			if (noNet)
				mInformation.setText(getString(R.string.no_net));
			else
				mInformation.setText(getString(R.string.empty_list));

			mInformation.setVisibility(View.VISIBLE);
			listShop.setVisibility(View.INVISIBLE);
		} else {
			mInformation.setVisibility(View.INVISIBLE);
			listShop.setVisibility(View.VISIBLE);
		}
	}

	public void showGoodsList(List<Goods> listGet) {

		diaLoading.show();
		mAdapter = new ShopListAdapter(getActivity(), listGet, mListener);
		listShop.setAdapter(mAdapter);
		diaLoading.hide();
		mAdapter.notifyDataSetChanged();
		showEmpty(false);
	}

	// 更新 列表
	public void updateShopList(String typeId, String mall) {

		if (typeId != null) {
			diaLoading.show();
			new GoodsListRequest(getActivity(), this, typeId).execute((String) null);
		} else {
			showAfterService(mall);
		}
	}

	@Override
	public void onGetResult(int requestType, Object result) {

		diaLoading.hide();
		if (requestType == GOODS_LIST_API) {

			if (result == null) {
				noNet = true;
				showEmpty(true);
				return;
			}

			List<Goods> list = (List<Goods>) result;

			if (list.size() > 0) {
				showGoodsList(list);
			} else {
				noNet = false;
				showEmpty(true);
			}
		}
	}

	@Override
	public void onError(int requestType) {
		diaLoading.hide();
		if (requestType != REDIRECT_API) {
			ComUtil.toastText(getActivity(), "连接服务器出错", Toast.LENGTH_SHORT);
		}
	}

}
