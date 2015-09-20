package com.apkdv.tour.view;

import org.phprpc.PHPRPC_Callback;
import org.phprpc.PHPRPC_Client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apkdv.tour.entity.User;
import com.apkdv.tour.model.MyApplication;
import com.apkdv.tour.utils.Tools;
import com.apldv.tour.R;


public class MoneyActivity extends BaseActivity {
	private Button btnReg;
	private EditText edNumber, edCard;
	TextView money;
	RelativeLayout layout;

	private void stupView() {
		edNumber = (EditText) findViewById(R.id.ed_money_number);
		edCard = (EditText) findViewById(R.id.ed_money_card);
		layout = (RelativeLayout) findViewById(R.id.rela_login_main);
		btnReg = (Button)findViewById(R.id.btn_money_add);
		money = (TextView)findViewById(R.id.tv_money);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmoney_activity);
		
		layout = (RelativeLayout) findViewById(R.id.rela_login_main);
		Animation animation = AnimationUtils.loadAnimation(MoneyActivity.this,
				R.anim.logo);
		layout.startAnimation(animation);
		stupView();
		setData();
		addlistener();
	}

	private void setData() {
		System.out.println(MyApplication.user.toString());
		if (MyApplication.user.getMoney() == null) {
			money.setText(0.0+"");
		}else {
			money.setText(MyApplication.user.getMoney());
		}
		
	}

	public void addlistener() {
		btnReg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String paw = edNumber.getText().toString();
				if (Tools.isNull(paw)) {
					edNumber.setError("充值金额不能为空");
					return;
				}
				User user = MyApplication.user;
				double d;
				if (MyApplication.user.getMoney() == null) {
					 d = Double.parseDouble(paw);
				}else {
					 d = Double.parseDouble(MyApplication.user.getMoney())+Double.parseDouble(paw);
				}
				user.setMoney(d+"");
				System.out.println(user.toString());
				PHPRPC_Client client = Tools.getClient();
				client.invoke("upData", new Object[] { Tools.gson.toJson(user)},
						new PHPRPC_Callback() {
							@SuppressWarnings("unused")
							public void handler(final boolean result,
									Object[] args) {
							}
						});
				MyApplication.user = user;
				dialog();
//				finish();
			}
		});

	}

	public void showToast(String String) {
		Toast.makeText(getApplicationContext(), String, Toast.LENGTH_SHORT)
				.show();
	}

	public void showDialog(String title, String msg, Context contacts,
			String buttopn) {
		AlertDialog.Builder builder = new Builder(contacts);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setNegativeButton(buttopn,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	public  void dialog(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示信息：").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(MoneyActivity.this, MainPagerActivity.class);
				intent.putExtra("pager", "info");
				startActivity(intent);
				finish();
				
			}
		}).setMessage("充值成功,当前账户余额为："+MyApplication.user.getMoney()+"元");
		builder.create().show();
	}

}
