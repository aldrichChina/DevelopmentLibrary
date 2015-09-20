package com.fairlink.passenger.view;

import com.fairlink.passenger.R;
import com.fairlink.passenger.util.ComUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * @ClassName ： DiaSSSClause
 * @Description: 春秋航空会员条约
 * @author ： jiaxue
 * @date ： 2014-12-15 下午1:26:11
 */

public class DiaSSSClause extends Dialog implements
		android.view.View.OnClickListener {

	private View mDialogView;

	private TextView tvDetail;
	private Button btnClose;
	
	
	private  static DiaSSSClause instance;

	public DiaSSSClause(Context context) {
		super(context, R.style.dialog);
		init(context);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

		this.setCanceledOnTouchOutside(true);

	}

	
	public static DiaSSSClause getInstance(Context context) {

		instance = new DiaSSSClause(context);
		return instance;

	}
	
	private void init(Context context) {

		mDialogView = View.inflate(context, R.layout.dialog_caluse, null);

		tvDetail = (TextView)mDialogView. findViewById(R.id.tv_detail);

		tvDetail.setText(ComUtil.getFromAssets(context, "login_user_conditions.txt"));

		btnClose = (Button) mDialogView.findViewById(R.id.btn_close);
		
		setContentView(mDialogView);

		btnClose.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		this.dismiss();

	}

}
