package com.jia.intenttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IntentActivity2 extends Activity {

	private Button activity2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_2);
		Intent intent=getIntent();
		String data = intent.getStringExtra("extra_data");
		Log.d("jia", data);
		activity2 = (Button) findViewById(R.id.activity_2);
		activity2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("jia", "¾ÍÊÇÅ£±Æ!!!!!!!!!!!!!!!!!!!!!!!!!!");
				setResult(RESULT_OK, intent);
				finish();

			}
		});
	}

	

}
