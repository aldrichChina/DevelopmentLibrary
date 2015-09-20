package com.jia.ywyx;

import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.jia.net.HttpCallbackListener;
import com.jia.net.HttpUtil;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class wdddActivity extends Activity {
	private String goods_id;
	private App appList;
	public String gethttp="http://www.yanwoyinxiang.com/interface.php?action=get_order_info&Order_sn=";
	public String address = gethttp  + ordernumber;
	private WebView web_wddd;
	private Button qr;
	public static String ordernumber;
	private TextView tv_ordernumber;
	private static Boolean isExit = false;
	private static Boolean hasTask = false;
	private Button btn_return;
	public static String CpxqUrl = "http://www.yanwoyinxiang.com/interface.php?action=get_index_xq";
	String CpxqUrl_ddxq = CpxqUrl + "&goods_id=" + goods_id;
	private TextView ddxm;
	private TextView ddsjhm;
	private TextView xxdz;
	private Button ddsptz;
	private TextView moneyyy;
	private TextView tV_number;
	private TextView tv_time;
	private TextView deletetheorder;
	private ImageView ddtp;
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0x111:
				Toast.makeText(wdddActivity.this, appList.getUsername(), Toast.LENGTH_SHORT).show();
				ddxm.setText(appList.getUsername());
				ddsjhm.setText(appList.getMobile());
				xxdz.setText(appList.getAddress());
				ddsptz.setText(appList.getTotal());
				moneyyy.setText(appList.getTotal());
				tV_number.setText(appList.getGoods_number());
				tv_time.setText(appList.getAdd_time());
				goods_id = appList.getGoods_id();
				Log.d("jia", "wdddActivity_goods_id--------------------->"+goods_id);
				//使用Picasso加载图片
		//		Picasso.with(wdddActivity.this).load(CpxqUrl_ddxq).into(ddtp);
			case 0x222:
			//	Toast.makeText(wdddActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wddd);
		ddxm = (TextView) findViewById(R.id.ddxm);
		ddsjhm = (TextView) findViewById(R.id.ddsjhm);
		xxdz = (TextView) findViewById(R.id.confirmorder_xxdz);
		moneyyy = (TextView) findViewById(R.id.confirmorder_tv_moneyyy);
		tV_number = (TextView) findViewById(R.id.ConfirmOrder_TV_number);
		tv_time = (TextView) findViewById(R.id.tv_time);
		ddsptz = (Button) findViewById(R.id.ddsptz);
		qr = (Button) findViewById(R.id.wddd_btn_qr);
		tv_ordernumber = (TextView) findViewById(R.id.wddd_tv_ordernumber);
		tv_ordernumber.setText(ordernumber);
		ddtp = (ImageView) findViewById(R.id.wddd_iv_ddtp);
		
		deletetheorder = (TextView) findViewById(R.id.Deletetheorder);		
		deletetheorder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(wdddActivity.this, "订单删除成功!!!", Toast.LENGTH_LONG).show();
				if (NavUtils.getParentActivityName(wdddActivity.this) != null) {
					NavUtils.navigateUpFromSameTask(wdddActivity.this);
				}
			
			}
		});
		HttpUtil.sendHttpRequest( address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				Log.d("jia", "response--------------------------------->"+response);
				Gson gson = new Gson();
				appList = gson.fromJson(response, App.class);
				Log.d("jia", "appList--------------------------------->"+appList);
				Message message=new Message();
				message.obj=appList;
				message.what=0x111;
				handler.sendMessage(message);
			}
			
			@Override
			public void onError(Exception e) {
				String error="网络出现问题!信息加载失败!!!";
				Message message=new Message();
				message.obj=error;
				message.what=0x222;
				handler.sendMessage(message);
			}
		});
		qr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(wdddActivity.this,
						ConfirmOrder.class);
				startActivity(intent);
			}
		});
		btn_return = (Button) findViewById(R.id.wddd_btn_return);
		btn_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (NavUtils.getParentActivityName(wdddActivity.this) != null) {
					NavUtils.navigateUpFromSameTask(wdddActivity.this);
				}
			}
		});
	}
	

//	Timer tExit = new Timer();
//	TimerTask task = new TimerTask() {
//		@Override
//		public void run() {
//			isExit = false;
//			hasTask = true;
//		}
//	};
//
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (isExit == false) {
//				isExit = true;
//				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//				if (!hasTask) {
//					tExit.schedule(task, 2000);
//				}
//			} else {
//				finish();
//				// 程序正常关闭
//				System.exit(0);
//			}
//		}
//		return true;
//	}
}
