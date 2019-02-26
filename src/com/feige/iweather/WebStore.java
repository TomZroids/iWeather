package com.feige.iweather;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feige.sweather.R;

public class WebStore extends Activity {
	Handler handler;
	WebView wv;
	TextView textView;
	ProgressBar progressBar;

	String web;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.webview);

		
		textView = (TextView) findViewById(R.id.textView1);
		progressBar = (ProgressBar) findViewById(R.id.pb);
		wv = (WebView) findViewById(R.id.webview);
		wv.getSettings().setAllowFileAccess(true);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl("http://m.jd.com/ware/search.action?sid=56a36c1a343a56f60f43feef73845f89&keyword=雾霾&catelogyList=");
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if (wv.getContentHeight() != 0) {
				}
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}

		});

		wv.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				if (newProgress == 0) {
					textView.setText("亲，商城还没有开业，敬请期待～");
					progressBar.setVisibility(View.VISIBLE);
				}
				progressBar.setProgress(newProgress);
				progressBar.postInvalidate();
				if (newProgress == 100) {
					textView.setText("亲，商城还没有开业，敬请期待～");
					progressBar.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
			wv.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
