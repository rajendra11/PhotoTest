package com.gsrti.phototest;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class InstaLogin extends Dialog {
	
	private static final String TAG = InstaLogin.class.getSimpleName();


	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT,
			ViewGroup.LayoutParams.WRAP_CONTENT);
	static final int MARGIN = 4;
	static final int PADDING = 2;

	private String mUrl;
	private InstaDialogListener mListener;

	private WebView mWebView;
	private LinearLayout mContent;


	public InstaLogin(Context context, String url, InstaDialogListener listener) {
		super(context);

		mUrl = url;
		mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.HORIZONTAL);
		setUpWebView();

		final float scale = getContext().getResources().getDisplayMetrics().density;
		float[] dimensions = { 360, 260 };

		addContentView(mContent, new FrameLayout.LayoutParams(
				(int) (dimensions[0] * scale + 0.1f), (int) (dimensions[1]
						* scale + 0.1f)));
	}

	private void setUpWebView() {
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(true);
		mWebView.setHorizontalScrollBarEnabled(true);
		mWebView.setWebViewClient(new OAuthWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(mUrl);
		mWebView.setLayoutParams(FILL);
		mContent.addView(mWebView);

		if (Build.VERSION.SDK_INT <= 18) {
			mWebView.getSettings().setSavePassword(false);
		} 
	}

	private class OAuthWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (url.startsWith(InstaFetch.CALLBACK_URL)) {
				String urls[] = url.split("=");
				mListener.onComplete(urls[1]);
				InstaLogin.this.dismiss();
				return true;
			}
			return false;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

	}

	public interface InstaDialogListener {
		public abstract void onComplete(String accessToken);
	}

}
