package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {
	public static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
	public static final String EXTRA_ANSWER_SHOWN="com.bignerdranch.android.geoquiz.answer_show";
	private static final String KEY_IS_CHEAT="com.bignerdranch.android.geoquiz.answer_shown";
	private boolean mIsCheat;
	private boolean mAnswerIsTrue;
	private TextView mAnswerTextView;
	private Button mShowAnswer;
	private TextView mTV_jb;
	private void setAnswerShownResult(boolean isAnswerShown) {
		Intent intent=new Intent();
		intent.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
		setResult(RESULT_OK,intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);
		mAnswerIsTrue = getIntent()
				.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
		mAnswerTextView = (TextView) findViewById(R.id.answerTextView);
		mShowAnswer = (Button) findViewById(R.id.showAnswerButton);
		mTV_jb = (TextView) findViewById(R.id.tV_jb);
		mTV_jb.setText("API level "+Build.VERSION.SDK_INT);
		
		mShowAnswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mAnswerIsTrue) {
					mAnswerTextView.setText(R.string.true_button);
				} else {
					mAnswerTextView.setText(R.string.false_button);
				}
				setAnswerShownResult(true);
			}
		});
		if(savedInstanceState!=null&&savedInstanceState.getBoolean(KEY_IS_CHEAT)){
			if(mAnswerIsTrue){
				mAnswerTextView.setText(R.string.true_button);
			}else{
				mAnswerTextView.setText(R.string.false_button);
			}
			setAnswerShownResult(true);
			mIsCheat=true;
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(KEY_IS_CHEAT, mIsCheat);
		super.onSaveInstanceState(outState);
	}
	
}
