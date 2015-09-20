package com.apkdv.tour.view;

import org.phprpc.PHPRPC_Callback;
import org.phprpc.PHPRPC_Client;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apkdv.tour.entity.Suites;
import com.apkdv.tour.entity.User;
import com.apkdv.tour.model.MyApplication;
import com.apkdv.tour.utils.Tools;
import com.apldv.tour.R;

public class ShowActivity extends BaseActivity {
	TextView title, tv_name, tv_pri;
	RelativeLayout addMessage;
	Button btnlay;
	ImageView imageShow;
	Suites suites;

	private void stupView() {
		title = (TextView) findViewById(R.id.tv_select_title);
		tv_name = (TextView) findViewById(R.id.tv_show_name);
		tv_pri = (TextView) findViewById(R.id.tv_show_q);
		addMessage = (RelativeLayout) findViewById(R.id.relat_add_message);
		btnlay = (Button) findViewById(R.id.btn_show_play);
		imageShow = (ImageView) findViewById(R.id.image_show);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_activity);
		stupView();
		setData();
		addlistener();
	}

	private void setData() {
		String titleString = getIntent().getStringExtra("title");
		suites = Tools.gson.fromJson(getIntent().getStringExtra("entity"),
				Suites.class);
		if (titleString.equals("酒店详情")) {
			title.setText(titleString);
			addMessage.setVisibility(View.INVISIBLE);
			tv_pri.setText("是否预定此酒店，需支付" + suites.getMoney() + "元");
		} else {
			title.setText(titleString);
			tv_pri.setText("是否购买此门票，需支付" + suites.getMoney() + "元");
			addMessage.setVisibility(View.VISIBLE);
		}
		imageShow.setImageResource(suites.getPidId());
		tv_name.setText(suites.getName());
	}

	public void addlistener() {
		btnlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				User user = MyApplication.user;
				double d;
				if (MyApplication.user.getMoney() == null) {
					noteng();
					return;
				} else {
					d = Double.parseDouble(MyApplication.user.getMoney())
							- Double.parseDouble(suites.getMoney());
					if (d < 0) {
						noteng();
						return;
					}
				user.setMoney(d + "");
				PHPRPC_Client client = Tools.getClient();
				client.invoke("upData",
						new Object[] { Tools.gson.toJson(user) },
						new PHPRPC_Callback() {
							@SuppressWarnings("unused")
							public void handler(final boolean result,
									Object[] args) {
							}
						});
				MyApplication.user = user;
				dialog();
				// finish();
			}
			}
		});
		addMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ShowActivity.this, MessageActivity.class));
				finish();
			}
		});
	}

	public void showToast(String String) {
		Toast.makeText(getApplicationContext(), String, Toast.LENGTH_SHORT)
				.show();
	}

	public void dialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示信息：")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(ShowActivity.this,
								MainPagerActivity.class);
						intent.putExtra("pager", "main");
						startActivity(intent);
						finish();

					}
				})
				.setMessage("支付成功，剩余余额为：" + MyApplication.user.getMoney() + "元");
		builder.create().show();
	}

	public void noteng() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示信息：")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).setMessage("余额不足，请充值！");
		builder.create().show();
	}
}
