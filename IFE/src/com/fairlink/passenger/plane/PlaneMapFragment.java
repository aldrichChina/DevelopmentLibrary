package com.fairlink.passenger.plane;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.R;

public class PlaneMapFragment extends BaseFragment{
	
	private WebView mWeb;
	private LinearLayout linLoad;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.plane_map, container);
		
		
		linLoad = (LinearLayout) view.findViewById(R.id.lin_load);
		mWeb = (WebView) view.findViewById(R.id.map);
		mWeb.setHorizontalScrollBarEnabled(false);
		mWeb.setVerticalScrollBarEnabled(false);
	
		
		
		WebSettings settings = mWeb.getSettings();  
	    settings.setSupportZoom(true);  
	    settings.setJavaScriptEnabled(true);  
	    settings.setDomStorageEnabled(true);  
	    settings.setCacheMode(WebSettings.LOAD_NO_CACHE); 
	    
	    webLoad();

	    return view; 
	}
	
	
	private void webLoad() {
		
		
		mWeb.loadUrl(IFEApplication.getInstance().getValue("MAP_URL"));
		
		
		mWeb.setOnTouchListener(new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == (MotionEvent.EDGE_LEFT | MotionEvent.EDGE_RIGHT | MotionEvent.EDGE_TOP | MotionEvent.EDGE_BOTTOM)){
                            return false;
                    }else{
                            return true;
                    }
            }
		});
		mWeb.setWebViewClient(new WebViewClient() {
			
			
			  @Override  
	            public void onReceivedError(WebView view, int errorCode,  
	                    String description, String failingUrl) {  
	            }  

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url != null && url.startsWith("mailto:")
						|| url.startsWith("geo:") || url.startsWith("tel:")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(url));
					startActivity(intent);
					return true;
				}
				view.loadUrl(url);
				return true;
			}
			
			@Override  
            public void onPageStarted(WebView view, String url,  
                    Bitmap favicon) {  
                super.onPageStarted(view, url, favicon); 
                
            }  
  
            @Override  
            public void onPageFinished(WebView view, String url) {  
                super.onPageFinished(view, url);  
                linLoad.setVisibility(View.GONE);  
            }  

		});
		
	}
	
}
