package com.gsrti.phototest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gsrti.phototest.InstaLogin.InstaDialogListener;

public class InstaFetch {

	private static final String TAG = InstaFetch.class.getSimpleName();

	private StoreInstaData mSession;
	private InstaLogin mDialog;
	private OAuthListener mListener;
	private ProgressDialog mProgress;
	private String mAuthUrl;

	private String mAccessToken;
	private Context mContext;

	private String mClientId;
	private String mClientSecret;

	public static String CALLBACK_URL = "";
	private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
	private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";


	public InstaFetch(Context context) {

		mContext = context;

		mClientId = mContext.getString(R.string.CLIENT_ID);
		mClientSecret = mContext.getString(R.string.CLIENT_SECRET);

		mSession = new StoreInstaData(context);
		mAccessToken = mSession.getAccessToken();
		CALLBACK_URL = mContext.getString(R.string.CALLBACK_URL);

		mAuthUrl = AUTH_URL
				+ "?client_id="
				+ mClientId
				+ "&redirect_uri="
				+ CALLBACK_URL
				+ "&response_type=code&display=touch&scope=likes+comments+relationships";

		InstaDialogListener listener = new InstaDialogListener() {
			@Override
			public void onComplete(String code) {
				getAccessToken(code);
			}

		};

		mDialog = new InstaLogin(context, mAuthUrl, listener);
		mProgress = new ProgressDialog(context);
		mProgress.setCancelable(false);
	}

	private void getAccessToken(final String code) {
		mProgress.setMessage("Getting access token ...");
		mProgress.show();

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Getting access token");
				int error = 1;
				try {
					URL url = new URL(TOKEN_URL);
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					urlConnection.setRequestMethod("POST");
					urlConnection.setDoInput(true);
					urlConnection.setDoOutput(true);

					OutputStreamWriter writer = new OutputStreamWriter(
							urlConnection.getOutputStream());
					writer.write("client_id=" + mClientId + "&client_secret="
							+ mClientSecret + "&grant_type=authorization_code"
							+ "&redirect_uri=" + CALLBACK_URL + "&code=" + code);
					writer.flush();
					String response = streamToString(urlConnection
							.getInputStream());

					JSONObject jsonObj = (JSONObject) new JSONTokener(response)
							.nextValue();

					mAccessToken = jsonObj.getString("access_token");

					String id = jsonObj.getJSONObject("user").getString("id");
					String user = jsonObj.getJSONObject("user").getString(
							"username");
					String name = jsonObj.getJSONObject("user").getString(
							"full_name");
					String userImage = jsonObj.getJSONObject("user").getString(
							"profile_picture");
					mSession.storeAccessToken(mAccessToken, id, user, name,
							userImage);

				} catch (Exception ex) {
					error = 0;
					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(error, 1, 0));
			}
		}.start();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			 if (msg.what == 1) {
				mProgress.dismiss();
				mListener.onSuccess();
			} 
		}
	};

	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}

	public void setListener(OAuthListener listener) {
		mListener = listener;
	}

	public String getUserName() {
		return mSession.getUsername();
	}

	public String getId() {
		return mSession.getId();
	}

	public String getName() {
		return mSession.getName();
	}

	public String getAccessToken() {
		return mSession.getAccessToken();
	}

	public void authorize() {

		mDialog.show();
	}

	private String streamToString(InputStream is) throws IOException {
		String str = "";

		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			str = sb.toString();
		}

		return str;
	}

	public void resetAccessToken() {
		if (mAccessToken != null) {
			mSession.resetAccessToken();
			mAccessToken = null;
		}
	}

	public interface OAuthListener {
		public abstract void onSuccess();
	}
}