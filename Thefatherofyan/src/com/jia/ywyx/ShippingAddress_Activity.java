package com.jia.ywyx;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ShippingAddress_Activity extends Activity {
	private Button btn_fk;
	private String consignee_str, phonenumber_str, detailedAddress_str,
			province_str, city_str, county_str, zipcode_str;
	private ImageView returnbtn;
	private EditText consignee;
	private EditText phonenumber;
	private EditText zipcode;
	private EditText province, city, county;
	private EditText detailedAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("yangxf", "ShippingAddress_Activity");
		setContentView(R.layout.shippingaddress_activity);
		consignee = (EditText) findViewById(R.id.shippingaddress_activity_consignee);
		phonenumber = (EditText) findViewById(R.id.ShippingAddress_Activity_phonenumber);
		zipcode = (EditText) findViewById(R.id.ShippingAddress_Activity_zipcode);
		province = (EditText) findViewById(R.id.ShippingAddress_Activity_province);
		city = (EditText) findViewById(R.id.ShippingAddress_Activity_city);
		county = (EditText) findViewById(R.id.ShippingAddress_Activity_county);
		detailedAddress = (EditText) findViewById(R.id.ShippingAddress_Activity_DetailedAddress);
		btn_fk = (Button) findViewById(R.id.btn_fk);
		btn_fk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				consignee_str = consignee.getText().toString();
				phonenumber_str = phonenumber.getText().toString();
				zipcode_str = zipcode.getText().toString();
				province_str = province.getText().toString();
				city_str = city.getText().toString();
				county_str = county.getText().toString();
				detailedAddress_str = detailedAddress.getText().toString();
				Log.d("jia", "consignee_str-------------->" + consignee_str);
				SharedPreferences.Editor editor = getSharedPreferences("data",
						MODE_PRIVATE).edit();
				editor.putString("consignee_str", consignee_str);
				editor.putString("phonenumber_str", phonenumber_str);
				editor.putString("zipcode_str", zipcode_str);
				editor.putString("province_str", province_str + " ");
				editor.putString("city_str", city_str + " ");
				editor.putString("county_str", county_str);
				editor.putString("detailedAddress_str", detailedAddress_str);

				editor.commit();
				Intent intent = new Intent(ShippingAddress_Activity.this,
						ConfirmOrder.class);
				startActivity(intent);
			}
		});
		returnbtn = (ImageView) findViewById(R.id.shippingaddress_activity_returnbtn);
		returnbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShippingAddress_Activity.this,
						ConfirmOrder.class);
				startActivity(intent);

			}
		});
	}
}
