package com.merchant.handler;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.merchant.R;
import com.merchant.common.Constants;


public class HeaderLayout {
	public static final int RES_NONE = 0;

	public ImageView mLeftIb, mLeftExtraIb, mRightExtraIb, mRightIb;
	public TextView mTitleTv, mSubtitleTv;
	public TextView mNotificationTv;


	public HeaderLayout(Context context) {
		mLeftIb 	 = (ImageView) ((Activity) context).findViewById(R.id.left_ib);
		switch (Constants.bank){
			case SBI:
				mLeftIb.setImageResource(R.drawable.logo_sbi_100x100_white);
				break;
			case IndusInd:
				mLeftIb.setImageResource(R.drawable.logo_indusind_100x100);
				break;
		}
		mLeftExtraIb = (ImageButton) ((Activity) context).findViewById(R.id.left_extra_ib);
		mRightExtraIb= (ImageView) ((Activity) context).findViewById(R.id.right_extra_ib);
		mRightIb 	 = (ImageView) ((Activity) context).findViewById(R.id.right_ib);
		
		mTitleTv = (TextView) ((Activity) context).findViewById(R.id.title_tv);
		mSubtitleTv = (TextView) ((Activity) context).findViewById(R.id.subtitle_tv);
		mTitleTv.setVisibility(View.GONE);
		mSubtitleTv.setVisibility(View.GONE);
		
	}

	public void setHeaderITT(int leftResId, int titleResId, int rightResId) {
		mLeftIb.setImageResource(leftResId);
		if (titleResId > 0) {
			mTitleTv.setText(titleResId);
			mTitleTv.setVisibility(View.VISIBLE);
		}

		mLeftExtraIb.setVisibility(View.GONE);
		mRightExtraIb.setVisibility(View.GONE);
		mRightIb.setVisibility(View.INVISIBLE);
	}


	public void setHeaderIITII(int leftResId, int leftExtraResId, int titleResId, int rightExtraResId, int rightResId) {
		mLeftIb.setImageResource(leftResId);
		mLeftExtraIb.setImageResource(leftExtraResId);
		//mTitleTv.setText(titleResId);
		if(leftResId!= HeaderLayout.RES_NONE)
			mLeftIb.setImageResource(leftResId);
		else
			mLeftIb.setVisibility(View.INVISIBLE);
		//leftResId!= HeaderLayout.RES_NONE ? mRightIb.setImageResource(rightResId) : mRightIb.setImageResource(rightResId);
		
		if(rightResId!= HeaderLayout.RES_NONE){
			mRightIb.setImageResource(rightResId);
			mRightIb.setVisibility(View.VISIBLE);
		}else
			mRightIb.setVisibility(View.INVISIBLE);
		
		mLeftExtraIb.setVisibility(leftExtraResId != RES_NONE ? View.VISIBLE : View.GONE);
		mRightExtraIb.setVisibility(rightExtraResId != RES_NONE ? View.VISIBLE : View.GONE);
		//mRightTv.setVisibility(View.GONE);
	}
	
	public void setSubtitle(String subtitle){
		mSubtitleTv.setVisibility(View.VISIBLE);
		mSubtitleTv.setText(subtitle);
	}

	/** To show the dynamic String in the header */
	public void setHeaderString(int leftResId, int leftExtraResId, String titleResId, int rightResId) {
		mLeftIb.setImageResource(leftResId);
		mLeftExtraIb.setImageResource(leftExtraResId);
		if (titleResId != null) {
			mTitleTv.setText(titleResId);
			mTitleTv.setVisibility(View.VISIBLE);
		}
		
		mRightIb.setImageResource(rightResId);
		
		if(rightResId ==0)
			mRightIb.setVisibility(View.INVISIBLE);

		mLeftExtraIb.setVisibility(View.GONE);
		mRightExtraIb.setVisibility(View.GONE);
		
	}


	public void setListener(OnClickListener left, OnClickListener leftExtra, OnClickListener rightExtra, OnClickListener right) {
		mLeftIb.setOnClickListener(left);
		mLeftExtraIb.setOnClickListener(leftExtra);
		mRightExtraIb.setOnClickListener(rightExtra);
		mRightIb.setOnClickListener(right);
	}



	public ImageView getLeftView() {
		return mLeftIb;
	}

	public void setTitle(String title) {
		mTitleTv.setText(title);
		mTitleTv.setVisibility(View.VISIBLE);
	}
	
}
