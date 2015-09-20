package com.jia.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jia.ywyx.ConfirmOrder;
import com.jia.ywyx.MainActivity;
import com.jia.ywyx.R;
import com.jia.ywyx.ShippingAddress_Activity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class fragGwc extends Fragment {
	public DisplayImageOptions options;
	private Button gs;
	private TextView gwc_tV_spm1, tV_rl, tV_jg, tv_pp;
	
	private Button gwcReturn;
	private ImageView btn_balance;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.gwc, null);
		gwcReturn = (Button) convertView.findViewById(R.id.gwcReturn);
		gwcReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);

			}
		});

		gs = (Button) convertView.findViewById(R.id.gs);
		tv_pp = (TextView) convertView.findViewById(R.id.tV_pp);
		gwc_tV_spm1 = (TextView) convertView.findViewById(R.id.gwc_tV_spm1);
		
		tV_rl = (TextView) convertView.findViewById(R.id.tV_rl);
		tV_jg = (TextView) convertView.findViewById(R.id.gwc_tV_price1);
		btn_balance = (ImageView) convertView.findViewById(R.id.btn_balance);
		btn_balance.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ConfirmOrder.class);
				startActivity(intent);
			}
		});
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic)
				.showImageOnFail(R.drawable.ic).cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).build();
		SharedPreferences preferences = getActivity().getSharedPreferences("jia", 0);
		String tv_ppsp = preferences.getString("Goods_name", "");
		String tv_buiden = preferences.getString("Goods_burden", "");
		String iv_Goods_img = preferences.getString("Goods_img", "");
		String tv_Goods_one_price = preferences.getString("Goods_one_price", "");
		String tv_Goods_start_weight = preferences.getString("Goods_start_weight", "");

		gwc_tV_spm1.setText(tv_ppsp);
		
		tV_rl.setText(tv_Goods_start_weight);
		tV_jg.setText(tv_Goods_one_price);
		gs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragSy fragSy = new fragSy();
				FragmentManager fragmentManager = getFragmentManager();
				Fragment fragment = fragmentManager
						.findFragmentById(R.id.frament);
				if (fragment == null) {
					fragment = new fragSy();
					fragmentManager.beginTransaction()
							.add(R.id.frament, fragment).commit();

				}

				
			}
		});
		return convertView;
	}
}
