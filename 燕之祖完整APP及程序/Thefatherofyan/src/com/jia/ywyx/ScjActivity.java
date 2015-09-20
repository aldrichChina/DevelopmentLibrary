package com.jia.ywyx;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ScjActivity extends Activity{

	private WebView web_scj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scj_layout);
		web_scj=(WebView) findViewById(R.id.web_scj);
		web_scj.getSettings().setJavaScriptEnabled(true);
		web_scj.getSettings().setUseWideViewPort(true);
		web_scj.getSettings().setLoadWithOverviewMode(true);
		web_scj.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		web_scj.loadUrl("http://yanwoyinxiang.com/flow.php");
	}
}
