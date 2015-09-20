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

import com.apkdv.tour.entity.User;
import com.apkdv.tour.model.MyApplication;
import com.apkdv.tour.utils.Tools;
import com.apldv.tour.R;


public class LoginActivity extends BaseActivity {
	private Button btnLogin, btnloginOut;
	private EditText edname, edpaw;
	RelativeLayout layout;
	boolean tager;

	private void stupView() {
		btnloginOut = (Button) findViewById(R.id.btn_main_reg);
		btnLogin = (Button) findViewById(R.id.btn_main_login);
		edname = (EditText) findViewById(R.id.ed_main_userName);
		edpaw = (EditText) findViewById(R.id.ed_main_password);
		layout = (RelativeLayout) findViewById(R.id.rela_login_main);
		anim();
	}
	public void anim(){
		Animation animation = AnimationUtils.loadAnimation(LoginActivity.this,
				R.anim.act_in);
		layout.startAnimation(animation);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginactivity);
		stupView();
		addlistener();
	}

	public void addlistener() {
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String name = edname.getText().toString();
				String paw = edpaw.getText().toString();

				if (Tools.isNull(name)) {
					edname.setError("�û�������Ϊ��");
					return;
				}
				if (Tools.isNull(paw)) {
					edpaw.setError("���벻��Ϊ��");
					return;
				}
				PHPRPC_Client client = Tools.getClient();
				client.invoke("login", new Object[] { name, paw },
						new PHPRPC_Callback() {
							@SuppressWarnings("unused")
							public void handler(final String result,
									Object[] args) {
								runOnUiThread(new Runnable() {
									public void run() {
										if (!Tools.isNull(result)) {
											MyApplication.user = Tools.gson.fromJson(result, User.class);
											showToast("��½�ɹ�");
											MyApplication.USER_NAME = name;
											startActivity(new Intent(LoginActivity.this, MainPagerActivity.class));
											finish();
										} else {
											showDialog("ϵͳ��ʾ", "��½ʧ�ܣ����������Ƿ���ȷ",
													LoginActivity.this, "���µ�½");
										}
									}
								});
							}
						});
			}
		});
		btnloginOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, RegActivity.class));
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
