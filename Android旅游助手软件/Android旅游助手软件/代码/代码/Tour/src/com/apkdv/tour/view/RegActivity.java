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
import android.widget.Toast;

import com.apkdv.tour.utils.Tools;
import com.apldv.tour.R;


public class RegActivity extends BaseActivity {
	private Button btnReg;
	private EditText edname, edpaw,edMyName,edPhone;
	RelativeLayout layout;
	boolean tager;

	private void stupView() {
		edname = (EditText) findViewById(R.id.ed_main_userName);
		edpaw = (EditText) findViewById(R.id.ed_main_password);
		edMyName = (EditText) findViewById(R.id.ed_main_myName);
		edPhone = (EditText) findViewById(R.id.ed_main_phone);
		layout = (RelativeLayout) findViewById(R.id.rela_login_main);
		btnReg = (Button)findViewById(R.id.btn_main_RegEdit);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regactivity);
		
		layout = (RelativeLayout) findViewById(R.id.rela_login_main);
		Animation animation = AnimationUtils.loadAnimation(RegActivity.this,
				R.anim.logo);
		layout.startAnimation(animation);
		stupView();
		addlistener();
	}

	public void addlistener() {
		btnReg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String name = edname.getText().toString();
				String paw = edpaw.getText().toString();
				String myName = edMyName.getText().toString();
				String phone = edPhone.getText().toString();

				if (Tools.isNull(name)) {
					edname.setError("用户名不能为空");
					return;
				}
				if (Tools.isNull(paw)) {
					edpaw.setError("密码不能为空");
					return;
				}
				if (Tools.isNull(phone)) {
					edpaw.setError("姓名不能为空");
					return;
				}
				if (Tools.isNull(myName)) {
					edpaw.setError("手机号码不能为空");
					return;
				}
				PHPRPC_Client client = Tools.getClient();
				client.invoke("addUser", new Object[] { name, paw,myName,phone },
						new PHPRPC_Callback() {
							@SuppressWarnings("unused")
							public void handler(final boolean result,
									Object[] args) {
								runOnUiThread(new Runnable() {
									public void run() {
										if (result) {
										showToast("注册成功，请登录");
										startActivity(new Intent(RegActivity.this, LoginActivity.class));
										finish();
										} else {
											showDialog("系统提示",
													"注册失败，用户明已经被使用，或网络连接失败！",
													RegActivity.this, "重新注册");
										}
									}
								});
							}
						});
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

	

}
