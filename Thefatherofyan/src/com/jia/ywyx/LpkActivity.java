package com.jia.ywyx;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LpkActivity extends Activity{
	private WebView web_lpk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lqk_layout);
		web_lpk=(WebView) findViewById(R.id.web_lpk);
		web_lpk.getSettings().setJavaScriptEnabled(true);
		web_lpk.getSettings().setUseWideViewPort(true);
		web_lpk.getSettings().setLoadWithOverviewMode(true);
		web_lpk.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		web_lpk.loadUrl("http://yanwoyinxiang.com/card.php");
	}
}
