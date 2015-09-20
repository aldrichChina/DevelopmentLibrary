package com.jia.ywyx;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class QueryOrder extends Activity{

	private Button begantoquery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.queryorder);
		begantoquery = (Button) findViewById(R.id.Begantoquery);
		begantoquery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(QueryOrder.this, "订单号码输入有误!请重新输入!", Toast.LENGTH_LONG).show();
				
			}
		});
	}
}
