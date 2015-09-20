package com.fairlink.passenger.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.R;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.PassengerFeedbackRequest;
import com.fairlink.passenger.networkrequest.PassengerFeedbackRequest.FeedbackMessage;
import com.fairlink.passenger.util.ComUtil;

public class CustomerBBSFragment extends BaseFragment {

	private EditText etSay;
	private Button btnSu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.customer_bbs, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		etSay = (EditText) view.findViewById(R.id.et_tosay);
		btnSu = (Button) view.findViewById(R.id.btn_submit);
		String txt = IFEApplication.getInstance().saytext;
		if(txt != null)
			etSay.setText(txt);
		btnSu.setOnClickListener(mListener);
	}

	private View.OnClickListener mListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			bbsSubmit();

		}
	};

	private HttpTaskCallback mFeedbackListener = new HttpTaskCallback() {

		@Override
		public void onGetResult(int requestType, Object result) {
			
		}

		@Override
		public void onError(int requestType) {
			
		}
		
	};
	
	public void onPause() {
		super.onPause();
		IFEApplication.getInstance().saytext = etSay.getText().toString();
	}
	
	private void bbsSubmit() {

		if (ComUtil.isEmpty(etSay.getText().toString())) {
			ComUtil.toastText(getActivity(), "您未填写信息", Toast.LENGTH_SHORT);
			return;
		}else{
			FeedbackMessage msg = new FeedbackMessage();
			msg.mbContent = etSay.getText().toString();
			new PassengerFeedbackRequest(getActivity(), msg, mFeedbackListener).execute((String)null);			
		}

	}

}
