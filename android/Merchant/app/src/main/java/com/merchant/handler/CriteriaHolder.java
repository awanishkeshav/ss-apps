package com.merchant.handler;

import com.merchant.R;

import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class CriteriaHolder {

	public LinearLayout rootLayout;
	public TextView titleTv;
	public RatingBar ratingBar;
	
	public CriteriaHolder(LinearLayout layout){
		rootLayout = layout;
		titleTv = (TextView) layout.findViewById(R.id.criteriaTv);
		ratingBar = (RatingBar)layout.findViewById(R.id.ratingBar);
		ratingBar.setRating(1);
	}
}
