package com.jia.ywyx;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GldzActivity extends Activity {
	private WebView web_dzgl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dzgl_layout);
		web_dzgl = (WebView) findViewById(R.id.web_dzgl);
		web_dzgl.getSettings().setJavaScriptEnabled(true);
		web_dzgl.getSettings().setUseWideViewPort(true);
		web_dzgl.getSettings().setLoadWithOverviewMode(true);
		web_dzgl.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		web_dzgl.loadUrl("http://yanwoyinxiang.com/article.php?id=44");
	}
}
