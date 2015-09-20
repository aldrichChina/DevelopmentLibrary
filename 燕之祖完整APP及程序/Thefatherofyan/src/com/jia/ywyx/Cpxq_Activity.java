package com.jia.ywyx;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.jia.Model.CustomProgressDialog;
import com.jia.net.HttpCallbackListener;
import com.jia.net.HttpUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class Cpxq_Activity extends Activity implements OnClickListener {
	private CustomProgressDialog dialog;
	private App appList;
	public static final int SHOW_RESPONSE = 1;
	// String address =
	// "http://www.yanwoyinxiang.com/interface.php?action=get_index_xq&goods_id=107";
	public static int i = 1;
	public static String gethttp, goodid;
	public String address = gethttp + "&goods_id=" + goodid;
	private TextView pq, sqmc, gg;
	private ImageView cpxqtq;
	public DisplayImageOptions options;
	private TextView cd;
	private Button btn_Return;
	private Button btn_gwc;
	private boolean dismiss;
	private FragmentManager fragmentManager;
	private TextView Nameofcommodity;
	private TextView price;
	private TextView favorablerate;
	private Button btn_gm;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	private Button minus;
	private Button append;
	private TextView purchasequantity;

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cpxq);

		sendMessage(i);
		minus = (Button) findViewById(R.id.minus);
		minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (i > 1) {
					i = i - 1;
					sendMessage(i);
				}

			}
		});
		append = (Button) findViewById(R.id.append);
		append.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				i = i + 1;
				sendMessage(i);
			}
		});

		purchasequantity = (TextView) findViewById(R.id.purchasequantity);
		// purchasequantity.setText("1");

		btn_gwc = (Button) findViewById(R.id.btn_gwc);
		btn_gwc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = getSharedPreferences("jia",MODE_PRIVATE).edit();
				editor.putString("Goods_name", appList.getGoods_name());
				editor.putString("Goods_burden", appList.getGoods_burden());
				editor.putString("Goods_img", appList.getGoods_img());
				editor.putString("Goods_one_price",appList.getPrice());
				editor.putString("Goods_start_weight",appList.getGoods_start_weight());
				editor.commit();
				Toast.makeText(Cpxq_Activity.this, "加入购物车成功!!!",
						Toast.LENGTH_LONG).show();
			}
		});
		btn_gm = (Button) findViewById(R.id.btn_gm);
		btn_gm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Cpxq_Activity.this,
						ConfirmOrder.class);
				startActivity(intent);

			}
		});
		Nameofcommodity = (TextView) findViewById(R.id.Nameofcommodity);
		cpxqtq = (ImageView) findViewById(R.id.cpxqtq);
		price = (TextView) findViewById(R.id.price);
		btn_Return = (Button) findViewById(R.id.btn_Return);
		favorablerate = (TextView) findViewById(R.id.favorablerate);
		btn_Return.setOnClickListener(this);
		// btn_gwc.setOnClickListener(this);
		Log.d("yangxf", "Cpxq_Activity address = " + address);
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic)
				.showImageOnFail(R.drawable.ic).cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).build();
		
		dialog = new CustomProgressDialog(this, "正在加载中",
					R.anim.frame2);
			dialog.show();
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {

				Log.d("yangxf", "Cpxq_Activity address = " + address);
				Gson gson = new Gson();
				appList = gson.fromJson(response, App.class);
				Log.d("jia", "appList----------------->" + appList);
				Log.d("jia", "商品名称---------->" + appList.getGoods_name());
				Log.d("jia", "price is----------->" + appList.getPrice());
				Log.d("jia",
						"Goods_burden is----------->"
								+ appList.getGoods_burden());
				Log.d("jia",
						"Goods_degree is----------->"
								+ appList.getGoods_degree());
				Log.d("jia",
						"Goods_img is----------->" + appList.getGoods_img());
				Log.d("jia",
						"Goods_name is----------->" + appList.getGoods_name());
				Log.d("jia",
						"Goods_one_price is----------->"
								+ appList.getGoods_one_price());
				Log.d("jia",
						"Goods_place is----------->" + appList.getGoods_place());
				Log.d("jia","Goods_start_weight is----------->"
								+ appList.getGoods_start_weight());
				Message message = new Message();
				message.what = SHOW_RESPONSE;

				message.obj = appList;
				handler.sendMessage(message);
			}

			@Override
			public void onError(Exception e) {

				Log.d("jia", "onError------------->" + e);
			}
		});
	}
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case SHOW_RESPONSE:
				Nameofcommodity.setText(appList.getGoods_name());
				price.setText(appList.getPrice());
				favorablerate.setText(appList.getComment_haopinglv());
				MainActivity.displayImage(appList.getGoods_img(), cpxqtq,
						options);
				dialog.dismiss(); 
				break;
			case 0x333:

				purchasequantity.setText(msg.obj.toString());
			}

		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_gwc:
			SharedPreferences.Editor editor = getSharedPreferences("jia",
					MODE_PRIVATE).edit();
			editor.putString("Brand_name", appList.getBrand_name());
			editor.putString("Goods_burden", appList.getGoods_burden());
			editor.putString("Goods_img", appList.getGoods_img());
			editor.putString("Goods_one_price", appList.getGoods_one_price());
			editor.putString("Goods_start_weight",
					appList.getGoods_start_weight());
			editor.commit();
			Toast.makeText(Cpxq_Activity.this, "加入购物车成功!!!", Toast.LENGTH_LONG)
					.show();
			break;
		case R.id.btn_gm:
			Log.d("yangxf", "onClick btn_gm");
			Intent intent = new Intent(Cpxq_Activity.this,
					ShippingAddress_Activity.class);
			startActivity(intent);

			/*
			 * Intent intent = new Intent(Cpxq_Activity.this, qrddtest.class);
			 * startActivity(intent);
			 */
			break;
		case R.id.btn_Return:
			Intent intent2 = new Intent(Cpxq_Activity.this, MainActivity.class);
			startActivity(intent2);
			/*
			 * fragSy sy=new fragSy(); FragmentTransaction transaction =
			 * fragmentManager.beginTransaction(); transaction.add(R.id.frament,
			 * sy); transaction.commit();
			 */
			break;
		}

	}

	public void sendMessage(int i) {
		Message message = new Message();
		message.what = 0x333;
		message.obj = i;
		handler.sendMessage(message);
	}
}
