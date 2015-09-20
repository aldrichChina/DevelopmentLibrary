package com.fairlink.passenger.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.fairlink.passenger.R;
import com.fairlink.passenger.util.ComUtil;

/**
 * @ClassName ： DiaEdit
 * @Description: 商品购买数量弹出框
 */

public class DialogEdit extends Dialog implements
		android.view.View.OnClickListener, TextWatcher {

	private View mDialogView;
	private EditText etNum;
	private Button btnSure;
	private Button btnCancel;
	
	// 默认限购数量
	private int numLimit = 10;

	private volatile static DialogEdit instance;

	public interface OnBuyNumListener {
		public void onBuyNumberChanged(String num);
	}

	private OnBuyNumListener mListener;

	public DialogEdit(Context context) {
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

		this.setCanceledOnTouchOutside(true);

	}

	public static DialogEdit getInstance(Context context) {
		instance = new DialogEdit(context);
		return instance;

	}

	private void init(Context context) {

		mDialogView = View.inflate(context, R.layout.dialog_edit, null);

		etNum = (EditText) mDialogView.findViewById(R.id.et_buy_num);
		btnSure = (Button) mDialogView.findViewById(R.id.btn_sure);
		btnCancel = (Button) mDialogView.findViewById(R.id.btn_cancel);
		setContentView(mDialogView);

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

	}


	/**
	 * 传入限购数量
	 * @param num 限购数量
	 * @return
	 */
	public DialogEdit withNumLimit(int num) {
		numLimit = num;
		return this;
	}
	
	
	/**
	 * 传入购买数量
	 * @param 购买数量
	 * @return
	 */
	public DialogEdit withNumBuy(String num) {
		if(!ComUtil.isEmpty(num)) {
			etNum.setText(num);
			etNum.addTextChangedListener(this);
		}
		return this;
	}
	

	public DialogEdit setmListener(OnBuyNumListener mListener) {
		this.mListener = mListener;
		return this;
	}
	
	
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (etNum.getText().toString().trim().equals(""))
			return;
		
		if (s.length() == 1 && Integer.parseInt(etNum.getText().toString().trim()) <= 0) {
			etNum.setText("1");
			etNum.setSelection(1);
		} else if (Integer.parseInt(etNum.getText().toString().trim()) > numLimit) {
			etNum.setText( Integer.toString(numLimit));
			etNum.setSelection(Integer.toString(numLimit).length());
		} else {
			etNum.setSelection(s.length());
		}
	}
	

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_sure) {
			if (null != mListener) {
				if (ComUtil.isEmpty(etNum.getText().toString())) {
					empty();
				}

				mListener.onBuyNumberChanged(etNum.getText().toString());
			}
		}

		this.dismiss();
	}

	private void empty() {
		etNum.setText("1");
		etNum.setSelection(1);
	}


}
