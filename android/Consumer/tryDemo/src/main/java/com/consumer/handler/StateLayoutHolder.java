package com.consumer.handler;

import com.consumer.R;
import com.consumer.R.id;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class StateLayoutHolder {

	public TextView mCaptionTv, mLabelTv;
	public ImageView mImageView;
	public RatingBar mRatingBar;
	
	public StateLayoutHolder(View stateView, boolean showIcon){
		mCaptionTv = (TextView) stateView.findViewById(R.id.stateCaption);
		mLabelTv = (TextView) stateView.findViewById(R.id.stateLable);
		mImageView = (ImageView) stateView.findViewById(R.id.imageView);
		mRatingBar = (RatingBar) stateView.findViewById(R.id.avgRatingBar);
		mRatingBar.setVisibility(View.GONE);
		if (showIcon) {
			mCaptionTv.setVisibility(View.GONE);
			mImageView.setVisibility(View.VISIBLE);
		}
	}
	
	public void setIconVisibilty(boolean visible){
		if (visible) {
			mCaptionTv.setVisibility(View.GONE);
			mImageView.setVisibility(View.VISIBLE);
		}else{
			mCaptionTv.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.GONE);
		}
	}
	public void setRatingBarVisible(){
		mCaptionTv.setVisibility(View.GONE);
		mImageView.setVisibility(View.GONE);
		mRatingBar.setVisibility(View.VISIBLE);
	}
}
