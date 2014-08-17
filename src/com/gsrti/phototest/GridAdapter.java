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

public class GridAdapter extends BaseAdapter {

	private Context addcontext;
	
	private static LayoutInflater inflater = null;

	private List<Selfie> selfieItems;
	
	private Bitmap[] selfieImages;

	public GridAdapter(Context context, List<Selfie> selfieItems,Bitmap[] selfieImages) {
		addcontext = context;

		this.selfieItems = selfieItems;
		
		this.selfieImages=selfieImages;
		
		inflater = (LayoutInflater) addcontext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return selfieImages.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

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
