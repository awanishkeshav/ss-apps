package com.merchant.handler;

import com.merchant.R;
import android.view.View;
import android.widget.TextView;

public class LoadingHolder {

	private TextView mLabelTv;
	private View rootView;

	public LoadingHolder(View stateView){
		rootView = stateView;
		mLabelTv = (TextView) stateView.findViewById(R.id.labelTV);
		rootView.setVisibility(View.GONE);
	}
	
	public void setVisibile(boolean visible){
		if (visible) {
			rootView.setVisibility(View.VISIBLE);
		}else{
			rootView.setVisibility(View.GONE);
		}
	}
	
	public void setLoadingText(String text){
		mLabelTv.setText(text);
	}
	
}
