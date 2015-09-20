package com.jia.ywyx;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class dplActivity extends Activity {
	private WebView web_View;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dpl_layout);
		web_View=(WebView) findViewById(R.id.web_View);
		web_View.getSettings().setJavaScriptEnabled(true);
		web_View.getSettings().setUseWideViewPort(true);
		web_View.getSettings().setLoadWithOverviewMode(true);
		web_View.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		web_View.loadUrl("http://yanwoyinxiang.com/article.php?id=44");
	}
}
