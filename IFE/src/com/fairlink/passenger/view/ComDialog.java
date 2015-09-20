package com.fairlink.passenger.view;

import com.fairlink.passenger.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ComDialog extends Dialog implements android.view.View.OnClickListener {

	private View mDialogView;

	private LinearLayout linBtnsLayout;
	private TextView tvTitle;
	private Button btnSure;
	private Button btnCancel;
	private Button btnDismiss;

	private volatile static ComDialog instance;

	public interface OnCommitListener {
		public void doSomeThings();
	}

	private OnCommitListener mListener;

	public ComDialog(Context context) {
		super(context, R.style.dialog);
		init(context);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

		this.setCanceledOnTouchOutside(true);

	}

	public static ComDialog getInstance(Context context) {

		instance = new ComDialog(context);
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

	public ComDialog withMessage(boolean showBtns, CharSequence msg) {
		toggleView(showBtns);
		tvTitle.setText(msg);
		return this;
	}

	public ComDialog withBtnSureText(CharSequence text) {
		btnSure.setText(text);
		return this;
	}

	public ComDialog setmListener(OnCommitListener mListener) {
		this.mListener = mListener;
		return this;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_sure) {
			if (null != mListener)
				mListener.doSomeThings();
		}

		this.dismiss();
	}

}
