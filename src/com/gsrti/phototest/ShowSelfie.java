/*
 * rajendra
 */
package com.gsrti.phototest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

// TODO: Auto-generated Javadoc
/**
 * The Class ShowSelfie.
 */
public class ShowSelfie extends Activity implements OnTouchListener {


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_selfie);
		initShowSelfie();

	}

	/**
	 * Initialize the objects.
	 */
	private void initShowSelfie() {
		Intent selfIntent = getIntent();
		int pos = selfIntent.getIntExtra("position", 0);
		ImageView selfieView = (ImageView) findViewById(R.id.selfie_image);
		selfieView.setImageBitmap(MainActivity.selfieImages[pos]);

		selfieView.setOnTouchListener(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@SuppressLint("NewApi")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		ImageView view = (ImageView) v;

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN: 
			break;

		case MotionEvent.ACTION_UP: 
			float x = view.getScaleX();
			float y = view.getScaleY();
			
			view.setScaleX((float) (x+1));
			view.setScaleY((float) (y+1));
			
			break;
		}
		return true;
	}

	
	}



