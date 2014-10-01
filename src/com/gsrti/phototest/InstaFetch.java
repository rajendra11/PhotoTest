/*
 * rajendra
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class InstaFetch.
 */
public class InstaFetch {

	/** The Constant TAG. */
	private static final String TAG = InstaFetch.class.getSimpleName();

	/** The session object to store current user data. */
	private StoreInstaData mSession;
	
	/** The dialog object to display login dialog screen. */
	private InstaLogin mDialog;
	
	/** The listener object for OAuth. */
	private OAuthListener mListener;
	
	/** The progressdialog object to display progress bar. */
	private ProgressDialog mProgress;
	
	/** The authentication url. */
	private String mAuthUrl;

	/** The access token . */
	private String mAccessToken;
	
	/** The context object. */
	private Context mContext;

	/** The instagram client id . */
	private String mClientId;
	
	/** The insntagram client secret key. */
	private String mClientSecret;

	/** The callback url after authentication. */
	public static String CALLBACK_URL = "";
	
	/** The Constant AUTH_URL. */
	private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
	
	/** The Constant TOKEN_URL. */
	private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";


	/**
	 * Instantiates a new insta fetch.
	 *
	 * @param context the context
	 */
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

	/**
	 * Gets the access token.
	 *
	 * @param code the code
	 * @return the access token
	 */
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

	/** create handler. */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			 if (msg.what == 1) {
				mProgress.dismiss();
				mListener.onSuccess();
			} 
		}
	};

	/**
	 * Checks for access token.
	 *
	 * @return true, if successful
	 */
	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}

	/**
	 * Sets the listener.
	 *
	 * @param listener the new listener
	 */
	public void setListener(OAuthListener listener) {
		mListener = listener;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return mSession.getUsername();
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return mSession.getId();
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return mSession.getName();
	}

	/**
	 * Gets the access token.
	 *
	 * @return the access token
	 */
	public String getAccessToken() {
		return mSession.getAccessToken();
	}

	/**
	 * Authorize.
	 */
	public void authorize() {

		mDialog.show();
	}

	/**
	 * Stream to string method to create string from URL.
	 *
	 * @param is the is
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Reset access token.
	 */
	public void resetAccessToken() {
		if (mAccessToken != null) {
			mSession.resetAccessToken();
			mAccessToken = null;
		}
	}

	/**
	 * The listener interface for receiving OAuth events.
	 * The class that is interested in processing a OAuth
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOAuthListener<code> method. When
	 * the OAuth event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OAuthEvent
	 */
	public interface OAuthListener {
		
		/**
		 * On success.
		 */
		public abstract void onSuccess();
	}
}