package com.jia.pullscrollview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class MainActivity extends Activity {
	private PullToRefreshScrollView mPullToRefreshScrollView;
	private ArrayList<View> arr=new ArrayList<View>();
	private TextView mTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTextView=(TextView) findViewById(R.id.text);
		mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pullscro);
		mPullToRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						new AsyncTask<Void, Void, Void>() {

							@Override
							protected Void doInBackground(Void... params) {
								try {
									new Thread().sleep(3000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								return null;
							}

							protected void onPostExecute(Void result) {
								mTextView.setText("Ò¦¼ÑÎ°");
								mPullToRefreshScrollView.onRefreshComplete();
							};
						}.execute();

					}
				});
	}

}
