package com.apkdv.tour.view;

import java.util.ArrayList;
import java.util.List;

import org.phprpc.PHPRPC_Callback;
import org.phprpc.PHPRPC_Client;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.apkdv.tour.adapter.MessageAdapter;
import com.apkdv.tour.entity.Message;
import com.apkdv.tour.model.MyApplication;
import com.apkdv.tour.utils.Tools;
import com.apldv.tour.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MessageActivity extends BaseActivity {
	ArrayList<Message> messages;
	ListView mesListView;
	Button btn_submit;
	TextView tveg;
	EditText editText;
	MessageAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_activity);
		init();
		setData();
		addListener();
	}

	private void setData() {
		tveg.setText("限制枢瑞140字");
		getMessage();
	}
	private void addListener() {
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				int content = editText.getText().toString().length();
				tveg.setText("还可输入" + (140 - content) + "字");
			}
		});
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String strMessage = editText.getText().toString();
				Message message = new Message();
				message.setMessage(strMessage);
				Gson gson = new Gson();
				PHPRPC_Client client = Tools.getClient();
				client.invoke("addMessage",
						new Object[] { MyApplication.user.getUserName(),strMessage },
						new PHPRPC_Callback() {
							@SuppressWarnings("unused")
							public void handler(final String result,
									Object[] args) {
								getMessage();
								MessageActivity.this.editText.getText().clear();

							}
						});

			}
		});

	}

	private void init() {
		mesListView = (ListView) findViewById(R.id.lv_message);
		btn_submit = (Button) findViewById(R.id.btn_message_submit);
		tveg = (TextView) findViewById(R.id.tv_message_input);
		editText = (EditText) findViewById(R.id.ed_message_input);
	}

	public void getMessage() {

		PHPRPC_Client client = Tools.getClient();
		client.invoke("findMessage", new Object[] {MyApplication.user.getUserName()},
				new PHPRPC_Callback() {
					@SuppressWarnings("unused")
					public void handler(final String result, Object[] args) {
						Gson gson = new Gson();
						final ArrayList<Message> mArrayList = gson.fromJson(result,
								new TypeToken<List<Message>>() {
								}.getType());
						
						if (mArrayList != null) {
							runOnUiThread(new Runnable() {
								public void run() {
									adapter = new MessageAdapter(mArrayList,
											MessageActivity.this);
									mesListView.setDivider(null);
									mesListView.setAdapter(adapter);
								}
							});
						}

					}
				});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			}
		return super.onKeyDown(keyCode, event);
	}
}
