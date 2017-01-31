package com.merchant.handler;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.merchant.R;

public class DateRangeSelector implements OnClickListener {

	public View rootLayout;
	public TextView titleTv;
	public ImageButton previousIv, nextIv;
	public DateRangeSelectorListener listener;
	public ArrayList<String> mTitles;
	public int selection = 0;

	public interface DateRangeSelectorListener {
		public abstract void onSelectionChange(int pos, String title);
	}

	public DateRangeSelector(View paramRootLayout,
			ArrayList<String> titles) {
		mTitles = titles;
		rootLayout = paramRootLayout;
		titleTv = (TextView) paramRootLayout.findViewById(R.id.titleTV);
		titleTv.setText("");
		nextIv = (ImageButton) paramRootLayout.findViewById(R.id.nextIv);
		previousIv = (ImageButton) paramRootLayout.findViewById(R.id.previousIv);
		nextIv.setOnClickListener(this);
		previousIv.setOnClickListener(this);
		setDisabled(nextIv);
		setDisabled(previousIv);
		configureForPosition(selection);

	}
	
	private void setEnabled(ImageView v){
		v.setAlpha((float)1.0);
		v.setClickable(true);
	}
	
	private void setDisabled(ImageView v){
		v.setAlpha((float)0.3);
		v.setClickable(false);
	}

	public void setTitles(ArrayList<String> titles){
		mTitles = titles;
		configureForPosition(selection);
	}
	public void setSelection(int pos) {
		selection = pos;
		configureForPosition(pos);

	}
	
	private void configureForPosition(int pos){
		if (mTitles != null && !mTitles.isEmpty()) {
			if (pos >= 0 && pos < mTitles.size()) {
				
				String title = mTitles.get(pos);
				titleTv.setText(title);
				if (pos == 0) {
					setDisabled(previousIv);
				}else{
					setEnabled(previousIv);
				}
				if (pos == mTitles.size()-1) {
					setDisabled(nextIv);
				}else{
					setEnabled(nextIv);
				}
			}
			
		}
	}

	public int getSelection() {
		return selection;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.nextIv:
			if (mTitles != null && selection+1 <= mTitles.size()-1) {
				selection ++;
				configureForPosition(selection);
				listener.onSelectionChange(selection, mTitles.get(selection));
			}
			
			break;
		case R.id.previousIv:
			if (mTitles != null && selection-1 >= 0) {
				selection --;
				configureForPosition(selection);
				listener.onSelectionChange(selection, mTitles.get(selection));
			}
			break;

		default:
			break;
		}
		
	}
}
