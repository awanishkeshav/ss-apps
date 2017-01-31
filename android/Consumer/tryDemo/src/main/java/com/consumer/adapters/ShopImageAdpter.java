package com.consumer.adapters;

import java.util.Random;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.consumer.R;


public class ShopImageAdpter extends BaseAdapter {
	    private Context mContext;
	    private LayoutInflater minflater;
	    private static final String TAG = "GridAdapter";
	    
	 // references to our images
	    private Integer[] mThumbIds = null;
	    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
	    private final Random mRandom;
	    
	    public ShopImageAdpter(Context c) {
	        mContext = c;
	        this.mRandom = new Random();
	        minflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        mThumbIds = new Integer[]{
	            R.drawable.shop1, R.drawable.shop2,
	            R.drawable.shop3, R.drawable.shop4,
	            R.drawable.shop5, R.drawable.shop6,
	        };
	    }

	    public int getCount() {
	        return mThumbIds.length;
	    }

	    public Object getItem(int position) {
	        return mThumbIds[position];
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    
		@SuppressLint("NewApi")
		public View getView(int position, View convertView, ViewGroup parent) {
	    	ViewHolder vh;
			if (convertView == null) {
				convertView = minflater.inflate(R.layout.row_grid_item,parent, false);
				vh = new ViewHolder();
				vh.imgView = (DynamicHeightImageView) convertView.findViewById(R.id.imgView);

				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			
			double positionHeight = getPositionRatio(position);
			vh.imgView.setBackgroundResource(mThumbIds[position]);	
			vh.imgView.setHeightRatio(positionHeight);
					
			//ImageLoader.getInstance().displayImage(getItem(position), vh.imgView);
			return convertView;
		}

		static class ViewHolder {
			DynamicHeightImageView imgView;
		}

		private double getPositionRatio(final int position) {
			double ratio = sPositionHeightRatios.get(position, 0.0);
			// if not yet done generate and stash the columns height
			// in our real world scenario this will be determined by
			// some match based on the known height and width of the image
			// and maybe a helpful way to get the column height!
			if (ratio == 0) {
				
				Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), mThumbIds[position]);
				ratio = (double)bitmap.getHeight()/bitmap.getWidth();
			}
			if (ratio == 0) {
				ratio = getRandomHeightRatio();
			}
			
			sPositionHeightRatios.append(position, ratio);
			Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
			return ratio;
		}

		private double getRandomHeightRatio() {
			return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
														// the width
		}
	}