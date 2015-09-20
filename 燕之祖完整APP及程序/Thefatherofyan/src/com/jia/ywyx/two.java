package com.jia.ywyx;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

public class two extends Activity implements OnClickListener {

	private Button scanButton;
	private TextView text;
	private EditText inputEditText;
	private Button genButton;
	private ImageView img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.two);
		scanButton = (Button) findViewById(R.id.scan);
		text = (TextView) findViewById(R.id.text);
		genButton = (Button) findViewById(R.id.gen);
		img=(ImageView) findViewById(R.id.img);
		inputEditText = (EditText) findViewById(R.id.input);
		genButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String in = inputEditText.getText().toString();
				if (in.equals("")) {
					Toast.makeText(two.this, "请输入文本",
							Toast.LENGTH_LONG).show();
				} else {
					try {
						Bitmap qrcode = EncodingHandler.createQRCode(in, 400);
						img.setImageBitmap(qrcode);
					} catch (WriterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		scanButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(two.this, "我爱买电子商务有限公司 你可以扫描二维码啦！！！！", Toast.LENGTH_LONG).show();
		Intent startScan = new Intent(two.this, CaptureActivity.class);
		// startActivity(startScan);
		startActivityForResult(startScan, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			String result = data.getExtras().getString("result");
			text.setText(result);
		}
	}
}
