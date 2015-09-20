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
import android.text.InputType;
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


public class ManagerActivity extends BaseActivity {
	private Button btnReg;
	private EditText edname, edpaw,edMyName,edPhone;
	TextView tvTitle,tvName;
	RelativeLayout layout;

	private void stupView() {
		edname = (EditText) findViewById(R.id.ed_main_userName);
		edpaw = (EditText) findViewById(R.id.ed_main_password);
		edMyName = (EditText) findViewById(R.id.ed_main_myName);
		edPhone = (EditText) findViewById(R.id.ed_main_phone);
		layout = (RelativeLayout) findViewById(R.id.rela_login_main);
		btnReg = (Button)findViewById(R.id.btn_main_RegEdit);
		tvTitle = (TextView)findViewById(R.id.title_fragment_top);
		tvName = (TextView)findViewById(R.id.tv_reg_userName);
		tvName.setVisibility(View.GONE);
		tvTitle.setText("个人信息修改");
		btnReg.setText("提交修改");
		edname.setVisibility(View.GONE);
		edpaw.setInputType(InputType.TYPE_CLASS_NUMBER);
		edMyName.setText(MyApplication.user.getMyName());
		edpaw.setText(MyApplication.user.getPassword());
		edPhone.setText(MyApplication.user.getPhone());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regactivity);
		
		layout = (RelativeLayout) findViewById(R.id.rela_login_main);
		Animation animation = AnimationUtils.loadAnimation(ManagerActivity.this,
				R.anim.logo);
		layout.startAnimation(animation);
		stupView();
		addlistener();
	}

	public void addlistener() {
		btnReg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String paw = edpaw.getText().toString();
				String myName = edMyName.getText().toString();
				String phone = edPhone.getText().toString();
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
				User user = MyApplication.user;
				user.setMyName(myName);
				user.setPassword(paw);
				user.setPhone(phone);
				System.out.println(user.toString());
				PHPRPC_Client client = Tools.getClient();
				client.invoke("upData", new Object[] { Tools.gson.toJson(user)},
						new PHPRPC_Callback() {
							@SuppressWarnings("unused")
							public void handler(final boolean result,
									Object[] args) {
							}
						});
				showToast("修改成功");
				MyApplication.user = user;
				Intent intent = new Intent(ManagerActivity.this, MainPagerActivity.class);
				intent.putExtra("pager", "info");
				startActivity(intent);
				
				finish();
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
