/*
 * rajendra
 */
package com.gsrti.phototest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

// TODO: Auto-generated Javadoc
/**
 * The Class StoreInstaData.
 */
public class StoreInstaData {

	/** The shared pref. */
	private SharedPreferences sharedPref;
	
	/** The editor. */
	private Editor editor;

	/** The Constant SHARED. */
	private static final String SHARED = "UserPreferences";
	
	/** The Constant API_USERNAME. */
	private static final String API_USERNAME = "username";
	
	/** The Constant API_ID. */
	private static final String API_ID = "id";
	
	/** The Constant API_NAME. */
	private static final String API_NAME = "name";
	
	/** The Constant API_ACCESS_TOKEN. */
	private static final String API_ACCESS_TOKEN = "access_token";

	/**
	 * Instantiates a new sharedpreferences object to store data.
	 *
	 * @param context the context
	 */
	public StoreInstaData(Context context) {
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		editor = sharedPref.edit();
	}

	/**
	 * Store access token.
	 *
	 * @param accessToken the access token
	 * @param id the id
	 * @param username the username
	 * @param name the name
	 * @param image the image
	 */
	public void storeAccessToken(String accessToken, String id,
			String username, String name, String image) {
		editor.putString(API_ID, id);
		editor.putString(API_NAME, name);
		editor.putString(API_ACCESS_TOKEN, accessToken);
		editor.putString(API_USERNAME, username);
		editor.commit();
	}

	/**
	 * Store access token.
	 *
	 * @param accessToken the access token
	 */
	public void storeAccessToken(String accessToken) {
		editor.putString(API_ACCESS_TOKEN, accessToken);
		editor.commit();
	}

	/**
	 * Reset access token.
	 */
	public void resetAccessToken() {
		editor.putString(API_ID, null);
		editor.putString(API_NAME, null);
		editor.putString(API_ACCESS_TOKEN, null);
		editor.putString(API_USERNAME, null);
		editor.commit();
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return sharedPref.getString(API_USERNAME, null);
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return sharedPref.getString(API_ID, null);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return sharedPref.getString(API_NAME, null);
	}

	/**
	 * Gets the access token.
	 *
	 * @return the access token
	 */
	public String getAccessToken() {
		return sharedPref.getString(API_ACCESS_TOKEN, null);
	}
}