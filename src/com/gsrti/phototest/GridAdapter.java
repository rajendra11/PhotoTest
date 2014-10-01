/*
 * rajendra
 */
package com.gsrti.phototest;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

// TODO: Auto-generated Javadoc
/**
 * The Class GridAdapter.
 */
public class GridAdapter extends BaseAdapter {

	/** The addcontext. */
	private Context addcontext;
	
	/** The inflater. */
	private static LayoutInflater inflater = null;

	/** The selfie items list. */
	private List<Selfie> selfieItems;
	
	/** The selfie images. */
	private Bitmap[] selfieImages;

	/**
	 * Instantiates a new grid adapter.
	 *
	 * @param context the context
	 * @param selfieItems the selfie items
	 * @param selfieImages the selfie images
	 */
	public GridAdapter(Context context, List<Selfie> selfieItems,Bitmap[] selfieImages) {
		addcontext = context;

		this.selfieItems = selfieItems;
		
		this.selfieImages=selfieImages;
		
		inflater = (LayoutInflater) addcontext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return selfieImages.length;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View gridItemView;
		if (arg1 == null)
			gridItemView = new View(addcontext);

		gridItemView = inflater.inflate(R.layout.row_grid, null);

		ImageView thumbNail = (ImageView) gridItemView
				.findViewById(R.id.item_image);
		
		if(position%3==0)
		{
			thumbNail.getLayoutParams().width=190;
			thumbNail.getLayoutParams().height=190;
			thumbNail.setScaleType(ScaleType.FIT_XY);
		}
		
		if(selfieImages[position]!=null)
		thumbNail.setImageBitmap(selfieImages[position]);

		return gridItemView;
	}

	
}
