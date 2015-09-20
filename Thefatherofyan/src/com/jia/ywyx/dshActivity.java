package com.jia.ywyx;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class dshActivity extends Activity {
	private WebView web_dsh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dsh_layout);
		web_dsh=(WebView) findViewById(R.id.web_dsh);
		web_dsh.getSettings().setJavaScriptEnabled(true);
		web_dsh.getSettings().setUseWideViewPort(true);
		web_dsh.getSettings().setLoadWithOverviewMode(true);
		web_dsh.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		web_dsh.loadUrl("http://m.kuaidi100.com/index_all.html");
	}
}
