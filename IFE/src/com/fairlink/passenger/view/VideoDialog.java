package com.fairlink.passenger.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fairlink.passenger.R;

public class VideoDialog extends Dialog implements
		android.view.View.OnClickListener {

	private View mDialogView;

	private LinearLayout linBtnsLayout;
	private TextView tvTitle;
	private Button btnSure;
	private Button btnCancel;
	private Button btnDismiss;


	private volatile static VideoDialog instance;
	
	 public interface OnSureListener {
			public void doSomeThings();
		}

	 private OnSureListener mListener;


	

	public VideoDialog(Context context) {
		super(context, R.style.dialog);
		init(context);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

		this.setCanceledOnTouchOutside(false);

	}

	public static VideoDialog getInstance(Context context) {

		instance = new VideoDialog(context);
		return instance;

	}

	private void init(Context context) {

		mDialogView = View.inflate(context, R.layout.dialog, null);

		linBtnsLayout = (LinearLayout) mDialogView.findViewById(R.id.lin_btns);
		tvTitle = (TextView) mDialogView.findViewById(R.id.tv_title);
		btnSure = (Button) mDialogView.findViewById(R.id.btn_sure);
		btnCancel = (Button) mDialogView.findViewById(R.id.btn_cancel);
		btnDismiss = (Button) mDialogView.findViewById(R.id.btn_dismiss);
		setContentView(mDialogView);

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnDismiss.setOnClickListener(this);

	}

	/** 切换视图 */
	private void toggleView(boolean showBtns) {
		if (showBtns) {
			linBtnsLayout.setVisibility(View.VISIBLE);
			btnDismiss.setVisibility(View.GONE);
		} else {
			linBtnsLayout.setVisibility(View.GONE);
			btnDismiss.setVisibility(View.VISIBLE);
		}

	}

	public VideoDialog withMessage(boolean showBtns, CharSequence msg) {
		toggleView(showBtns);
		tvTitle.setText(msg);
		return this;
	}
	
	public VideoDialog setmListener(OnSureListener mListener) {
		this.mListener = mListener;
		return this;
	}
	

	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.btn_dismiss) {
			if(null != mListener)
			mListener.doSomeThings();
		}
		
		this.dismiss();
	}

}
