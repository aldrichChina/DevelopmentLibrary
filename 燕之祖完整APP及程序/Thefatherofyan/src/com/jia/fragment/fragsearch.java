package com.jia.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jia.ywyx.R;

public class fragsearch extends Fragment{
	private WebView search_wv_search;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView=inflater.inflate(R.layout.search, null);


		search_wv_search = (WebView) convertView.findViewById(R.id.search_wv_search);
		search_wv_search.getSettings().setJavaScriptEnabled(true);
		search_wv_search.getSettings().setUseWideViewPort(true);
		search_wv_search.getSettings().setLoadWithOverviewMode(true);
		search_wv_search.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		search_wv_search.loadUrl("http://www.yanwoyinxiang.com/index.php");
		return convertView;
	
		
	}


}
