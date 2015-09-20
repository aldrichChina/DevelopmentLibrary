package com.fairlink.passenger.inform;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.passenger.BaseActivity;
import com.fairlink.passenger.R;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.BroadcastGetRequest;
import com.fairlink.passenger.networkrequest.BroadcastGetRequest.BroadcastStatus;
import com.fairlink.passenger.util.ComUtil;

public class InformDialog extends BaseActivity implements HttpTaskCallback {
//	 private TextView      mButton;
	 private TextView      mText;
	 private InformDialog  that;
	 private BroadcastStatus retVal = new BroadcastStatus();
	 private static final int command = 1;
	 
	 private String type;
	 private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
	 public static boolean showInformDialog = true;
	 
	 protected void onCreate(Bundle savedInstanceState) {
         Intent controlHomeButton = new Intent();
         controlHomeButton .setAction("customer.control.homeButton");
         controlHomeButton .putExtra("lock", true);
         sendBroadcast(controlHomeButton);

		 super.onCreate(savedInstanceState);
		 that = this;
		 setContentView(R.layout.inform_dialog);
		 
		 type = getIntent().getStringExtra("type_notice");
		 
//		 mButton = (TextView)findViewById(R.id.inform_button);
		 mText   = (TextView)findViewById(R.id.inform_txt);
//		 mButton.setOnClickListener(this);
		 getBroadData(type);
		 
		 this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);

		 handler.postDelayed(runnable, 1000);
	 }

	@Override
	public void onGetResult(int requestType, Object result) {
		if(null == result) {
			return;
		}
		
		retVal = (BroadcastStatus) result;
		
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// 要做的事情
				handler.postDelayed(this, 1000);		
			    getBroadData(type);
			if(retVal.bcStatus == null){
//				setTextContent("Can't get result from server.");
			} else {
				if(retVal.bcStatus.equals("0")) {
//					setTextContent("Please wait for a while.");
				} else {
//					sendBroadcast(new Intent("com.fairlink.passenger.inform") ); 
					showInformDialog = true;
					that.finish();					
				}
			}
		}
	};
	
	
	@Override
	protected void onPause() {
		super.onPause();
		if(!showInformDialog){
			startActivity(new Intent(this, InformDialog.class));
		}
	}

	protected void onDestroy() {
        Intent controlHomeButton = new Intent();
        controlHomeButton .setAction("customer.control.homeButton");
        controlHomeButton .putExtra("lock", false);
        sendBroadcast(controlHomeButton);

		showInformDialog = true;
		super.onDestroy();
		handler.removeCallbacks(runnable);
	};

	public void getBroadData(String type){
		new BroadcastGetRequest(that, Integer.toString(command), this).execute((String)null);
	}
	
	public void setTextContent(String str) {
		mText.setText(str);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU){
			return true;
		} else if(keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onError(int requestType) {
		ComUtil.toastText(this, "连接服务器出错", Toast.LENGTH_SHORT);
	}
}