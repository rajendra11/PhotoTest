/*
 * rajendra
 */
package com.gsrti.phototest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.gsrti.phototest.InstaFetch.OAuthListener;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity {

	/** The m inst obj for Instafetch class. */
	private InstaFetch mInstObj;

	/** The Constant TAG for debugging. */
	private static final String TAG = MainActivity.class.getSimpleName();

	/** The list of selfie items. */
	private List<Selfie> selfieItems = new ArrayList<Selfie>();
	
	/** The grid view. */
	private GridView gridView;;

	/** The selfie bitmpap images array. */
	public static Bitmap[] selfieImages;

	/** The slefie tag url to fetch images from instagram with hashtag selfie. */
	private String SLEFIE_TAG_URL = "https://api.instagram.com/v1/tags/selfie/media/recent?access_token=";
	
	/** The x,y values to save the current touch positions. */
	int x,y;
	
	/** The parms. */
	private RelativeLayout.LayoutParams parms;
	
	/** The boolean to check item is dragging or not. */
	private boolean onTheMove=false;


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// authenticateUser();

		init();
		callInstagram();
	}

	/**
	 * Initialize the variables.
	 */
	public void init() {
		gridView = (GridView) findViewById(R.id.gridView1);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(MainActivity.this,
						ShowSelfie.class);
				mainIntent.putExtra("position", position);
				startActivity(mainIntent);
			}
		});

		/*gridView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					GridView parent = (GridView) v;

					int gx = (int) event.getX();
					int gy = (int) event.getY();

					int position = parent.pointToPosition(gx, gy);
					if (position > AdapterView.INVALID_POSITION) {

						int count = parent.getChildCount();
						for (int i = 0; i < count; i++) {
							final View curr = parent.getChildAt(i);
							

							curr.setOnTouchListener(new View.OnTouchListener() {

								@Override
								public boolean onTouch(View v, MotionEvent event) {
									
									 x = (int) event.getX();
									 y = (int) event.getY();
									parms = (RelativeLayout.LayoutParams) curr
											.getLayoutParams();
									
									switch (event.getAction()) {
									case MotionEvent.ACTION_DOWN: {
										onTheMove = true;
										x = (int) event.getRawX();
										y = (int) event.getRawY();
										
									}
										break;
									case MotionEvent.ACTION_MOVE: {
										if (onTheMove) {
											// if the user press down then lets start moving!
											int x_cord = (int) event.getRawX();
											int y_cord = (int) event.getRawY();

											parms.rightMargin = x - x_cord;
											parms.topMargin = y_cord - y;
											curr.setLayoutParams(parms);
										}
									}
										break;
									case MotionEvent.ACTION_UP: {
										onTheMove = false;

										// set button back to original location
										parms.rightMargin = 0;
										parms.topMargin = 0;
										x = 0;
										y = 0;
										curr.setLayoutParams(parms);
									}
									}
									return true;
								}
							});

						}

						
						
					}
				}
				}
				return true;
			}
		});*/

	}

	/**
	 * Call instagram to authenticate and fetch images.
	 */
	public void callInstagram() {
		mInstObj = new InstaFetch(this);

		OAuthListener listener = new OAuthListener() {

			@Override
			public void onSuccess() {

				AsyncTaskRunner runner = new AsyncTaskRunner();
				String sleepTime = "1000";
				runner.execute(sleepTime);
			}

		};

		mInstObj.setListener(listener);

		mInstObj.authorize();

	}

	/**
	 * Display images on grid view.
	 */
	public void displayImages() {
		gridView.setAdapter(new GridAdapter(this, selfieItems, selfieImages));
	}

	/**
	 * The Class AsyncTaskRunner to download images in back end thread.
	 */
	private class AsyncTaskRunner extends AsyncTask<String, String, String> {

		/** The resp. */
		private String resp;

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected String doInBackground(String... params) {
			try {

				getSelfiePhotos(mInstObj.getAccessToken());

				selfieImages = new Bitmap[selfieItems.size()];

				for (int i = 0; i < selfieItems.size(); i++) {
					Selfie m = selfieItems.get(i);
					selfieImages[i] = getBitmapFromURL(m.getThumbnailUrl());
				}
				int time = Integer.parseInt(params[0]);

				Thread.sleep(time);
				resp = "Slept for " + time + " milliseconds";
			} catch (InterruptedException e) {
				e.printStackTrace();
				resp = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				resp = e.getMessage();
			}
			return resp;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {

			displayImages();
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {

		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
		 */
		@Override
		protected void onProgressUpdate(String... text) {

		}
	}

	/**
	 * Gets the bitmap from url.
	 *
	 * @param src the src
	 * @return the bitmap from url
	 */
	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the selfie photos using the url.
	 *
	 * @param accessToken the access token
	 * @return the selfie photos
	 */
	public void getSelfiePhotos(String accessToken) {
		try {

			String mURl = SLEFIE_TAG_URL + accessToken;

			URL example = new URL(mURl);

			URLConnection tc = example.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					tc.getInputStream()));

			String line;

			while ((line = in.readLine()) != null) {
				JSONObject ob = new JSONObject(line);

				JSONArray object = ob.getJSONArray("data");

				Log.e(TAG, "SelfiePhotos total" + object.length());

				for (int i = 0; i < object.length(); i++) {

					JSONObject jo = (JSONObject) object.get(i);
					JSONObject nJImages = (JSONObject) jo
							.getJSONObject("images");

					JSONObject thumbJSON = (JSONObject) nJImages
							.getJSONObject("thumbnail");


					Selfie selfie = new Selfie();
					selfie.setThumbnailUrl(thumbJSON.getString("url"));
					selfieItems.add(selfie);
				}

			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (JSONException e) {

			e.printStackTrace();
		}

	}

}
