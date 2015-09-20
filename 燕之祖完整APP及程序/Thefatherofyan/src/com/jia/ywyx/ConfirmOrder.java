package com.jia.ywyx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jia.alipay.PayDemoActivity;
import com.jia.net.HttpCallbackListener;
import com.jia.net.HttpUtil;

public class ConfirmOrder extends Activity {
	private TextView tradename;
	private String finalprice_str;

	public int i;

	private TextView number;

	private String consignee_str, phonenumber_str, province_str, city_str,
			county_str, detailedAddress_str, zipcode_str;
	public List<NameValuePair> params;
	Cpxq_Activity activity = new Cpxq_Activity();
	public static String ConfirmOrder_goodid;
	public static String CpxqUrl = "http://www.yanwoyinxiang.com/interface.php?action=get_index_xq";
	String CpxqUrl_ddxq = CpxqUrl + "&goods_id=" + ConfirmOrder_goodid;
	// 生成订单json
	Map<String, Object> map;
	List<Map<String, Object>> list;
	// 随机数
	int max = 99999;
	int min = 10000;
	Random random = new Random();
	int s = random.nextInt(max) % (max - min + 1) + min;
	// 当前日期
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd" + s);
	// long currentTime = System.currentTimeMillis();// 时间戳
	long currentTime = new Date().getTime();// 时间戳

	Date curDate = new Date(currentTime);// 获取当前时间
	// 订单号
	public String ordernumberstr = formatter.format(curDate);
	
	// "http://www.yanwoyinxiang.com/interface.php?action=get_index_xq&goods_id=107"
	private App appList;
	// String CpxqUrl_ddxq
	// ="http://www.yanwoyinxiang.com/interface.php?action=get_index_xq&goods_id=107";
	UILApplication application;
	private Button btn_return;
	private View ddjbxx;
	private Button confirmorder_btn_qr;
	PayDemoActivity payDemoActivity = new PayDemoActivity();
	private Button ddsptz;
	private TextView confirmorder_hjprice;
	private TextView moneyyy;
	private TextView ddxm, ddsjhm, confirmorder_xxdz;
	public static String hjprice;
	public static String name_str;
	HttpClient client;

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x323:
				finalprice_str = appList.getPrice();
				int finalprice_int = Integer.parseInt(finalprice_str);

				Log.d("jia", "finalprice_int" + finalprice_int);

				hjprice = String.valueOf(finalprice_int * i);
				Log.d("jia", "i------------>" + i);
				confirmorder_hjprice.setText(hjprice);
				ddsptz.setText(appList.getPrice());
				moneyyy.setText(appList.getPrice());
				name_str = appList.getGoods_name();

				tradename.setText(appList.getGoods_name());
				break;
			case 0x123:
				Toast.makeText(ConfirmOrder.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
			case 0x111:
	//			Toast.makeText(ConfirmOrder.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
				Log.d("jia", msg.obj.toString());

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.confirmorder);
		wdddActivity.ordernumber=ordernumberstr;
		client = new DefaultHttpClient();
		i = activity.getI();
		Log.d("jia", ordernumberstr);
		Log.d("jia", "时间戳---------------------->" + currentTime / 1000);
		Log.d("jia", "时间戳---------------------->" + curDate);
		application = (UILApplication) getApplication();
		SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
		consignee_str = pref.getString("consignee_str", "");
		phonenumber_str = pref.getString("phonenumber_str", "");
		zipcode_str = pref.getString("zipcode_str", "");
		province_str = pref.getString("province_str", "");
		city_str = pref.getString("city_str", "");
		county_str = pref.getString("county_str", "");
		detailedAddress_str = pref.getString("detailedAddress_str", "");
		ddxm = (TextView) findViewById(R.id.ddxm);
		ddsjhm = (TextView) findViewById(R.id.ddsjhm);
		confirmorder_xxdz = (TextView) findViewById(R.id.confirmorder_xxdz);

		ddxm.setText(consignee_str);
		ddsjhm.setText(phonenumber_str);
		confirmorder_xxdz.setText(province_str + city_str + county_str
				+ detailedAddress_str);
		Log.d("jia", ordernumberstr);
		btn_return = (Button) findViewById(R.id.btn_return);
		btn_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConfirmOrder.this,
						Cpxq_Activity.class);
				startActivity(intent);

			}
		});
		ddjbxx = findViewById(R.id.confirmorder_ddjbxx);
		ddjbxx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("yangxf", "onClick1111");
				Intent intent = new Intent(ConfirmOrder.this,
						ShippingAddress_Activity.class);
				startActivity(intent);

			}
		});
		confirmorder_btn_qr = (Button) findViewById(R.id.confirmorder_btn_qr);
		confirmorder_btn_qr.setOnClickListener(new OnClickListener() {

			// 发送订单
			@Override
			public void onClick(View v) {
				final String name = consignee_str;
				final String Area = province_str + city_str + county_str;
				final String Address = detailedAddress_str;
				final String phonenumber = phonenumber_str;
				final String Zip_code = zipcode_str;
				final String Ordenumber = ordernumberstr;
				final String goods_id = ConfirmOrder_goodid;
				final String goods_name = name_str;
				final String shuliang = String.valueOf(i);
				final String time = String.valueOf(currentTime);
				Log.d("jia", "time--------------->" + time);
				final String total = hjprice;
				new Thread() {

					public void run() {
						try {
							HttpPost post = new HttpPost(
									"http://www.yanwoyinxiang.com/interface.php?action=add_user_address");
							params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("name", name));
							params.add(new BasicNameValuePair("Area", Area));
							params.add(new BasicNameValuePair("Address",
									Address));
							params.add(new BasicNameValuePair("phonenumber",
									phonenumber));
							params.add(new BasicNameValuePair("Zip_code",
									Zip_code));
							params.add(new BasicNameValuePair("Ordenumber",
									Ordenumber));
							params.add(new BasicNameValuePair("goods_id",
									goods_id));
							params.add(new BasicNameValuePair("goods_name",
									goods_name));
							params.add(new BasicNameValuePair("shuliang",
									shuliang));
							params.add(new BasicNameValuePair("time", time));
							params.add(new BasicNameValuePair("total", total));
							// 设置请求参数
							post.setEntity(new UrlEncodedFormEntity(params,
									HTTP.UTF_8));
							// 发送post请求
							HttpResponse response = client.execute(post);
							// 如果服务器成功地返回响应
							if (response.getStatusLine().getStatusCode() == 200) {
								String msg = EntityUtils.toString(response
										.getEntity());
								Log.d("jia", "msg----------------->" + msg);
								Message message = new Message();
								message.what = 0x111;
								message.obj = msg;
								handler.sendMessage(message);
							}
						} catch (Exception e) {
							/*
							 * Toast.makeText(ConfirmOrder.this, "订单提交失败",
							 * Toast.LENGTH_SHORT).show();
							 */
							e.printStackTrace();
						}
					};
				}.start();

				Intent intent = new Intent(ConfirmOrder.this,
						PayDemoActivity.class);
				startActivity(intent);
			}
		});
		ddsptz = (Button) findViewById(R.id.ddsptz);
		number = (TextView) findViewById(R.id.ConfirmOrder_TV_number);
		number.setText(String.valueOf(i));
		confirmorder_hjprice = (TextView) findViewById(R.id.confirmorder_hjprice);

		moneyyy = (TextView) findViewById(R.id.confirmorder_tv_moneyyy);
		tradename = (TextView) findViewById(R.id.confirmorder_tradename);
		Log.d("jia", "ConfirmOrder.java   CpxqUrl_ddxq--------->"
				+ CpxqUrl_ddxq);
		HttpUtil.sendHttpRequest(CpxqUrl_ddxq, new HttpCallbackListener() {

			private Message message;

			@Override
			public void onFinish(String response) {
				Gson gson = new Gson();
				appList = gson.fromJson(response, App.class);
				message = new Message();
				message.what = 0x323;
				message.obj = appList;
				handler.sendMessage(message);

			}

			@Override
			public void onError(Exception e) {
				String gson_str = "网络有问题!加载错误";
				message = new Message();
				message.what = 0x123;
				message.obj = gson_str + e;
				handler.sendMessage(message);

			}
		});

	}
}
