package com.jia.intenttest;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class IntentActivity extends Activity {

	private Button activity1;
	private TextView bj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent);
		activity1 = (Button) findViewById(R.id.activity_1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��hhʱmm��ss��");
		Date    curDate    =   new    Date(System.currentTimeMillis());//��ȡ��ǰʱ��       
		String    ordernumberstr    =    sdf.format(curDate);    
		int i = Integer.valueOf(ordernumberstr);
		bj = (TextView) findViewById(R.id.bj);
		bj.setText(ordernumberstr);
		activity1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//String data = ;
				Intent intent = new Intent(IntentActivity.this,
						IntentActivity2.class);
				intent.putExtra("extra_data", "�ܲ���ţ����");
				startActivityForResult(intent, 1);
				Log.d("jia", "��ʼ");
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				String returnedData = data.getStringExtra("jia");
				Log.d("jia", returnedData);
			}
			break;
		default:

		}
	}

}
