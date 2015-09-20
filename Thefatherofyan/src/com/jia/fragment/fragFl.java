package com.jia.fragment;

import com.jia.ywyx.R;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class fragFl extends Fragment{
	private WebView fl_wv_fl;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView=inflater.inflate(R.layout.fl, null);
		fl_wv_fl = (WebView) convertView.findViewById(R.id.fl_wv_fl);
		fl_wv_fl.getSettings().setJavaScriptEnabled(true);
		fl_wv_fl.getSettings().setUseWideViewPort(true);
		fl_wv_fl.getSettings().setLoadWithOverviewMode(true);
		fl_wv_fl.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		fl_wv_fl.loadUrl("http://www.yanwoyinxiang.com/goods.php?id=129");
		return convertView;
	}
}
