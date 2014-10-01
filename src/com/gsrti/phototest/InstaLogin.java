/*
 * rajendra
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class InstaLogin.
 */
public class InstaLogin extends Dialog {
	
	/** The Constant TAG. */
	private static final String TAG = InstaLogin.class.getSimpleName();


	/** The Constant FILL. */
	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT,
			ViewGroup.LayoutParams.WRAP_CONTENT);
	
	/** The Constant MARGIN. */
	static final int MARGIN = 4;
	
	/** The Constant PADDING. */
	static final int PADDING = 2;

	/** The mUrl to store authentication url. */
	private String mUrl;
	
	/** The interface object to call callback methods. */
	private InstaDialogListener mListener;

	/** The webview object to display login in webview. */
	private WebView mWebView;
	
	/** The layout to display login page. */
	private LinearLayout mContent;


	/**
	 * Instantiates a new instalogin.
	 *
	 * @param context the context
	 * @param url the authentication url
	 * @param listener the InstaDialogListener
	 */
	public InstaLogin(Context context, String url, InstaDialogListener listener) {
		super(context);

		mUrl = url;
		mListener = listener;
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
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

	/**
	 * Sets the up web view.
	 */
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

	/**
	 * The Class OAuthWebViewClient.
	 */
	private class OAuthWebViewClient extends WebViewClient {

		/* (non-Javadoc)
		 * @see android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
		 */
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

		/* (non-Javadoc)
		 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
		 */
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		/* (non-Javadoc)
		 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
		 */
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

	}

	/**
	 * The listener interface for receiving instaDialog events.
	 * The class that is interested in processing a instaDialog
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addInstaDialogListener<code> method. When
	 * the instaDialog event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see InstaDialogEvent
	 */
	public interface InstaDialogListener {
		
		/**
		 * On complete.
		 *
		 * @param accessToken the access token
		 */
		public abstract void onComplete(String accessToken);
	}

}
