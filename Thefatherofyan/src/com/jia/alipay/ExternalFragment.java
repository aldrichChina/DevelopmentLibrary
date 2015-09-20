package com.jia.alipay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.StaticLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jia.ywyx.ConfirmOrder;
import com.jia.ywyx.R;
import com.jia.ywyx.UILApplication;
import com.jia.ywyx.wdddActivity;

public class ExternalFragment extends Fragment {
	// String
	// CpxqUrl="http://www.yanwoyinxiang.com/interface.php?action=get_index_xq&goods_id=107";
	// Cpxq_Activity.address;
	UILApplication application;
	public ConfirmOrder confirmOrder;
	// Handler handler=new Handler(){
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 0x111:
	// product_subject.setText(appList.getGoods_name());
	// pay_external_detail.setText(appList.getGoods_name());
	// product_price.setText("0.01");
	//
	// }
	// };
	// };
	public TextView product_subject;
	
	public TextView product_price;
	private Button toviewtheorder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		confirmOrder=new ConfirmOrder();
		String hjprice_str=confirmOrder.hjprice;
		String name_str = confirmOrder.name_str;
		Log.d("jia", "hjprice_str------------->"+ConfirmOrder.hjprice);
		View convertView = inflater.inflate(R.layout.pay_external, null);
		application = (UILApplication) getActivity().getApplication();
		product_subject = (TextView) convertView.findViewById(R.id.product_subject);
		product_price = (TextView) convertView.findViewById(R.id.product_price);
		toviewtheorder = (Button) convertView.findViewById(R.id.pay_external_btn_Toviewtheorder);
		toviewtheorder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(), wdddActivity.class);
				startActivity(intent);
				
			}
		});
		product_subject.setText(name_str);
		product_price.setText(hjprice_str);
		Log.d("jia", "hjprice_str------------->"+hjprice_str);
		return convertView;
		
	}
}
